package com.realestate.mpt;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import java.util.List;

class PortfolioUtils {

    public static void validateHistoricalReturnsLength(List<RealEstateAsset> assets) {
        if (assets == null || assets.isEmpty()) {
            throw new IllegalArgumentException("Assets list cannot be null or empty.");
        }

        int referenceSize = assets.get(0).getHistoricalReturns().size();

        for (RealEstateAsset asset : assets) {
            if (asset.getHistoricalReturns().size() != referenceSize) {
                throw new IllegalArgumentException("Mismatch in historical returns length detected. Expected size: " + referenceSize + ", but found size: " + asset.getHistoricalReturns().size() + " for asset ID: " + asset.getPropertyId());
            }
        }
    }

    public static double[][] computeCovarianceMatrix(List<RealEstateAsset> assets) {
        // Extract historical returns from assets
        validateHistoricalReturnsLength(assets);
        int n = assets.size();
        int m = assets.get(0).getHistoricalReturns().size();  // Assuming all assets have same number of historical returns
        double[][] returns = new double[m][n];

        for (int j = 0; j < n; j++) {
            List<Double> historicalReturns = assets.get(j).getHistoricalReturns();
            for (int i = 0; i < m; i++) {
                returns[i][j] = historicalReturns.get(i);
            }
        }

        // Compute covariance matrix using Apache Commons Math
        RealMatrix matrix = new BlockRealMatrix(returns);
        Covariance covariance = new Covariance(matrix);
        RealMatrix covMatrix = covariance.getCovarianceMatrix();

        return covMatrix.getData();
    }
}

