package com.realestate.mpt;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.stream.IntStream;

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

    // Performs linear-interpolation-based resampling
    protected static Map<RealEstateAsset, Double> interpolateHistories(Map<RealEstateAsset, Double> assetsWeights) {
        // Extract assets from the provided map
        List<RealEstateAsset> assets = extractAssetsFromMap(assetsWeights);

        // Determine the longest historical returns among the assets
        int maxHistoryLength = assets.stream()
                .mapToInt(asset -> asset.getHistoricalReturns().size())
                .max()
                .orElse(0);

        // Prepare interpolator
        LinearInterpolator interpolator = new LinearInterpolator(); // careful we are not performing "pure" Linear Interpolation here

        for (RealEstateAsset asset : assets) {
            List<Double> history = asset.getHistoricalReturns();

            // If the history length of this asset is less than the maxHistoryLength
            // we need to interpolate its values
            if (history.size() < maxHistoryLength) {
                double[] xvals = IntStream.range(0, history.size()).asDoubleStream().toArray();
                double[] yvals = history.stream().mapToDouble(Double::doubleValue).toArray();

                UnivariateFunction function = interpolator.interpolate(xvals, yvals);

                List<Double> newHistory = IntStream.range(0, maxHistoryLength)
                        .mapToObj(i -> {
                            if (i < history.size()) {
                                return history.get(i); // Return the original data if it exists
                            }
                            double interpolatedIndex = i * (history.size() - 1.0) / (maxHistoryLength - 1.0); // resampling happens here
                            return function.value(interpolatedIndex); // Interpolate for missing data
                        })
                        .collect(Collectors.toList());

                asset.setHistoricalReturns(newHistory);
            }
        }

        return assetsWeights;
    }


    protected static Map<RealEstateAsset, Double> cutHistoriesToShortest(Map<RealEstateAsset, Double> assetsWeights) {
        List<RealEstateAsset> assets = extractAssetsFromMap(assetsWeights);

        // Determine the shortest historical return list
        int minHistoryLength = assets.stream()
                .mapToInt(asset -> asset.getHistoricalReturns().size())
                .min()
                .orElse(0);

        // Cut each asset's historical returns to match the shortest length
        for (RealEstateAsset asset : assets) {
            List<Double> historicalReturns = asset.getHistoricalReturns();
            if (historicalReturns.size() > minHistoryLength) {
                historicalReturns = historicalReturns.subList(0, minHistoryLength); // This does not modify the original list. It provides a view.
                asset.setHistoricalReturns(historicalReturns);
            }
        }
        return assetsWeights; // Return the updated map
    }

    protected static Map<RealEstateAsset, Double> historyCleaning(Map<RealEstateAsset, Double> assetsWeights, String cleanMethod) {
        return switch (cleanMethod) {
            case "i" -> interpolateHistories(assetsWeights);
            case "c" -> cutHistoriesToShortest(assetsWeights);
            default -> throw new IllegalArgumentException("Invalid clean method. Must be 'i' or 'c'.");
        };
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

