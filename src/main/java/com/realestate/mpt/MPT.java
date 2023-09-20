package com.realestate.mpt;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.*;

// MPT - Modern Portfolio Theory
public class MPT {
    private Map<RealEstateAsset, Double> assetsWeights; // Mapping of asset to its weight in the portfolio
    boolean cleanHistory; // Flags whether the historical returns of all assets in the portfolio have the same length
    String cleanMethod;

    // Default constructor, you can use addAsset to add more assets, cleanMethod provided
    public MPT(String cleanMethod) {
        this.assetsWeights = new HashMap<>();
        cleanHistory = false;
        setClean(cleanMethod);
    }
    // Default constructor
    public MPT(){
        this("i");
    }
//-----------
    // Constructor accepting a Map
    public MPT(Map<RealEstateAsset, Double> assetsWeights, String cleanMethod) {
        this.assetsWeights = new HashMap<>(assetsWeights);
        cleanHistory = PortfolioUtils.validateHistoricalReturnsLength(this.assetsWeights);
        setClean(cleanMethod);
    }

    public MPT(Map<RealEstateAsset, Double> assetsWeights){
        this(assetsWeights, "i");
    }
// -----------
    // Constructor accepting two lists
    public MPT(List<RealEstateAsset> assets, List<Double> weights, String cleanMethod) {
        if (assets.size() != weights.size()) {
            throw new IllegalArgumentException("The size of assets list and weights list must be the same.");
        }

        this.assetsWeights = new HashMap<>();
        for (int i = 0; i < assets.size(); i++) {
            this.assetsWeights.put(assets.get(i), weights.get(i));
        }
        cleanHistory = PortfolioUtils.validateHistoricalReturnsLength(assets);
        setClean(cleanMethod);
    }

    public MPT(List<RealEstateAsset> assets, List<Double> weights){
        this(assets, weights, "i");
    }


    //------

    // The weight is the percentage of the portfolio that the asset comprises,
    // i.e. estimated value of the asset / total portfolio value. Use PortfolioUtils' helper function if needed
    public void addAsset(RealEstateAsset asset, double weight) {
        assetsWeights.put(asset, weight);
        cleanHistory = false;
    }

    public double portfolioExpectedReturn() {
        double expectedReturn = 0.0;
        for (Map.Entry<RealEstateAsset, Double> entry : assetsWeights.entrySet()) {
            RealEstateAsset asset = entry.getKey();
            Double weight = entry.getValue();
            expectedReturn += asset.getExpectedReturn() * weight;
        }
        return expectedReturn;
    }

    public void historyCleaner() {
        if (!cleanHistory) {
            boolean validLengths = PortfolioUtils.validateHistoricalReturnsLength(assetsWeights);

            if (!validLengths) {
                this.assetsWeights = PortfolioUtils.historyCleaning(this.assetsWeights, this.cleanMethod);
                this.cleanHistory = true;
            }
        }
    }

    public double portfolioVolatility() {
        // Ensure that the historical returns of all assets in the portfolio have the same length
        historyCleaner();

        // Get covariance matrix
        List<RealEstateAsset> assetsList = new ArrayList<>(assetsWeights.keySet());
        double[][] covarianceMatrix = PortfolioUtils.computeCovarianceMatrix(assetsList);

        // Convert the asset weights to an array
        double[] weightsArray = PortfolioUtils.extractWeightsFromMapAsArray(assetsWeights);

        // Perform matrix multiplication: w^T * (covariance matrix) * w
        RealMatrix covMatrix = new BlockRealMatrix(covarianceMatrix);
        RealVector weights = new ArrayRealVector(weightsArray);

        double volatilitySquared = weights.dotProduct(covMatrix.operate(weights));
        return Math.sqrt(volatilitySquared);
    }

    private double portfolioVolatility(double[] weights, double[][] covarianceMatrix) {
        RealMatrix covMatrix = new BlockRealMatrix(covarianceMatrix);
        RealVector weightVector = new ArrayRealVector(weights);

        double volatilitySquared = weightVector.dotProduct(covMatrix.operate(weightVector));
        return Math.sqrt(volatilitySquared);
    }

    private double[][] getReturnsData() {
        List<RealEstateAsset> assetsList = new ArrayList<>(assetsWeights.keySet());
        int n = assetsList.size();
        int m = assetsList.get(0).getHistoricalReturns().size();  // Assuming all assets have same number of historical returns
        double[][] returns = new double[m][n];

        for (int j = 0; j < n; j++) {
            List<Double> historicalReturns = assetsList.get(j).getHistoricalReturns();
            for (int i = 0; i < m; i++) {
                returns[i][j] = historicalReturns.get(i);
            }
        }

        return returns;
    }

    private List<RealEstateAsset> getAssetsList() {
        return new ArrayList<>(assetsWeights.keySet());
    }

    public double[] linearizedPortfolioVarianceConstraint(double[] initialWeights) {
        // 1. Compute the Covariance Matrix
        double[][] covarianceArray = PortfolioUtils.computeCovarianceMatrix(getAssetsList());
        RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(covarianceArray);

        // 2. Compute Gradient at Initial Weights
        RealVector initialWeightsVector = new ArrayRealVector(initialWeights, false);
        RealVector gradient = covarianceMatrix.operate(initialWeightsVector).mapMultiply(2.0);

        return gradient.toArray();
    }

    public LinearConstraint getLinearizedRiskConstraint(double[] initialWeights, double desiredRisk) {
        double[] linearizedRiskCoefficients = linearizedPortfolioVarianceConstraint(initialWeights);

        // 3. Construct Linear Constraint for Portfolio Variance using the Delta Method
        // Assuming the desiredRisk has been linearly approximated in line with the delta method
        return new LinearConstraint(linearizedRiskCoefficients, Relationship.LEQ, desiredRisk);
    }

// riskFreeRate could be the 10-year Treasury rate, for example
    public double sharpeRatio(double riskFreeRate) {
        // Ensure that the historical returns of all assets in the portfolio have the same length
        historyCleaner();

        double expectedReturn = portfolioExpectedReturn();
        double volatility = portfolioVolatility();

        return (expectedReturn - riskFreeRate) / volatility;
    }

    public double[] optimizedPortfolio(double desiredRisk) {
        // Ensure that the historical returns of all assets in the portfolio have the same length
        historyCleaner();

        List<RealEstateAsset> assetsList = new ArrayList<>(assetsWeights.keySet());
        int numAssets = assetsList.size();

        // Objective: Maximize Expected Returns
        double[] coeff = new double[numAssets];
        for (int i = 0; i < numAssets; i++) {
            coeff[i] = assetsList.get(i).getExpectedReturn();
        }
        LinearObjectiveFunction f = new LinearObjectiveFunction(coeff, 0);

        // Constraints
        List<LinearConstraint> constraints = new ArrayList<>();

        // Sum of weights = 1
        double[] sumWeights = new double[numAssets];
        Arrays.fill(sumWeights, 1);
        constraints.add(new LinearConstraint(sumWeights, Relationship.EQ, 1.0));

        // Each weight between 0 and 1
        for (int i = 0; i < numAssets; i++) {
            double[] weightArray = new double[numAssets];
            weightArray[i] = 1;
            constraints.add(new LinearConstraint(weightArray, Relationship.GEQ, 0.0));
            constraints.add(new LinearConstraint(weightArray, Relationship.LEQ, 1.0));
        }

        // Risk constraint
        // This is where it gets tricky and is a linear approximation
        // We are adding a constraint that the weighted sum of the volatilities of the assets
        // should be less than or equal to the desiredRisk
        /*double[] riskArray = new double[numAssets];
        for (int i = 0; i < numAssets; i++) {
            riskArray[i] = assetsList.get(i).getHistoricalVolatility();
        }
        constraints.add(new LinearConstraint(riskArray, Relationship.LEQ, desiredRisk));*/
        double[] initialEstimateOfWeights = PortfolioUtils.extractWeightsFromMapAsArray(assetsWeights);
        LinearConstraint linearizedRiskConstraint = getLinearizedRiskConstraint(initialEstimateOfWeights, desiredRisk);

        constraints.add(linearizedRiskConstraint);

        SimplexSolver optimizer = new SimplexSolver(0.2,10000);
        PointValuePair solution = optimizer.optimize(
                new MaxIter(10000),
                f,
                new LinearConstraintSet(constraints),
                GoalType.MAXIMIZE,
                new NonNegativeConstraint(true)
        );

        return solution.getPoint();
    }



    private double[] weightsToArray(int size) {
        double[] arr = new double[size];
        Arrays.fill(arr, 1.0);
        return arr;
    }

    private double[] weightsToArray(int index, double value) {
        double[] arr = new double[assetsWeights.size()];
        Arrays.fill(arr, 0);
        arr[index] = value;
        return arr;
    }


    // Getters and Setters

    public void setClean(String cleanMethod) {
        if(!(Objects.equals(cleanMethod, "i") || Objects.equals(cleanMethod, "c"))){
            throw new IllegalArgumentException("Clean method must be either 'i' for interpolation or 'c' to cut history to the shortest.");
        }
        this.cleanMethod = cleanMethod;
    }


}
