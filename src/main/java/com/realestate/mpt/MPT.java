package com.realestate.mpt;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
/*
    public double portfolioVolatility() {
        // Compute portfolio volatility
    }

    public double sharpeRatio(double riskFreeRate) {
        // Compute Sharpe ratio
    }

     */

    // Getters and Setters

    public void setClean(String cleanMethod) {
        if(!(Objects.equals(cleanMethod, "i") || Objects.equals(cleanMethod, "c"))){
            throw new IllegalArgumentException("Clean method must be either 'i' for interpolation or 'c' to cut history to the shortest.");
        }
        this.cleanMethod = cleanMethod;
    }
}
