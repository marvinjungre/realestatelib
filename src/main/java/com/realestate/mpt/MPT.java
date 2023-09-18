package com.realestate.mpt;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;


public class MPT {
    private Map<RealEstateAsset, Double> assetsWeights;  // Mapping of asset to its weight in the portfolio

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

    // The weight is the percentage of the portfolio that the asset comprises,
    // i.e. estimated value of the asset / total portfolio value. Use static helper function if needed
    public void addAsset(RealEstateAsset asset, double weight) {
        assetsWeights.put(asset, weight);
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
/*
    public double portfolioVolatility() {
        // Compute portfolio volatility
    }

    public double sharpeRatio(double riskFreeRate) {
        // Compute Sharpe ratio
    }

     */
}
