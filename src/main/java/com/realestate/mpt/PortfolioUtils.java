package com.realestate.mpt;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

public class PortfolioUtils {

    // Extractor function to get the list of RealEstateAsset from the map
    public static List<RealEstateAsset> extractAssetsFromMap(Map<RealEstateAsset, Double> assetsMap) {
        return new ArrayList<>(assetsMap.keySet());
    }

    // Function to validate the historical returns length for assets in the map
    public static boolean validateHistoricalReturnsLength(Map<RealEstateAsset, Double> assetsMap) {
        List<RealEstateAsset> assetsList = extractAssetsFromMap(assetsMap);
        return validateHistoricalReturnsLength(assetsList);
    }

    public static boolean validateHistoricalReturnsLength(List<RealEstateAsset> assets) {
        if (assets == null || assets.isEmpty()) {
            return false;
        }

        int referenceSize = assets.get(0).getHistoricalReturns().size();

        for (RealEstateAsset asset : assets) {
            if (asset.getHistoricalReturns().size() != referenceSize) {
                return false;
            }
        }
        return true;
    }

    public static List<Double> helpComputeWeights(List<Double> assetValues) {
        if (assetValues == null || assetValues.isEmpty()) {
            throw new IllegalArgumentException("Asset values list cannot be null or empty.");
        }

        double totalValue = assetValues.stream().mapToDouble(Double::doubleValue).sum();

        if (totalValue == 0) {
            throw new IllegalArgumentException("Total asset value cannot be zero.");
        }

        return assetValues.stream()
                .map(value -> value / totalValue)
                .collect(Collectors.toList());
    }

    public static double[][] computeCovarianceMatrix(List<RealEstateAsset> assets) {
        // Extract historical returns from assets
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

