package com.realestate.mpt;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MPTTest {

    @Test
    public void testHistoryCleanerFlagC() {
        // Define two RealEstateAssets with different historical returns
        RealEstateAsset asset1 = new RealEstateAsset(3680, Arrays.asList(10., 430.06, 590.07, 543.08));
        RealEstateAsset asset2 = new RealEstateAsset(7830, Arrays.asList(4030.03, 2540.02, 4230.01, 4850.04, 1805.20, 4002.03));
        RealEstateAsset asset3 = new RealEstateAsset(7950, Arrays.asList(3230.03, 140.02, 4430.01, 302.04, 230.05, 430.06, 590.07, 543.08, 49.0, 59.0, 43.0,43.0));

        // Initialize an MPT instance with the assets and set cleanMethod to "c"
        Map<RealEstateAsset, Double> testWeights = new HashMap<>();
        testWeights.put(asset1, 0.5);
        testWeights.put(asset2, 0.2);
        testWeights.put(asset3, 0.3);
        MPT mptInstance = new MPT(testWeights, "c");

        // Call historyCleaner
        mptInstance.historyCleaner();

        // The shortest historical return list has a length of 3, so after calling historyCleaner, both assets should have histories of length 3
        int expectedLength = 4;

        // Assert the historical returns lengths of both assets are now equal to the expected length
        Assert.assertEquals(expectedLength, asset1.getHistoricalReturns().size());
        Assert.assertEquals(expectedLength, asset2.getHistoricalReturns().size());
        Assert.assertEquals(expectedLength, asset3.getHistoricalReturns().size());
    }

    @Test
    public void testHistoryCleanerFlagI() {
        // Define three RealEstateAssets with different historical returns
        RealEstateAsset asset1 = new RealEstateAsset(3680, Arrays.asList(10., 100.06, 1000.07, 10000.08));
        RealEstateAsset asset2 = new RealEstateAsset(7830, Arrays.asList(4030.03, 2540.02, 4230.01, 4850.04, 1805.20, 4002.03));
        RealEstateAsset asset3 = new RealEstateAsset(7950, Arrays.asList(3230.03, 140.02, 4430.01, 302.04, 230.05, 430.06, 590.07, 543.08, 49.0, 59.0, 43.0,43.0));

        // Initialize an MPT instance with the assets and set cleanMethod to "i" for interpolation
        Map<RealEstateAsset, Double> testWeights = new HashMap<>();
        testWeights.put(asset1, 0.5);
        testWeights.put(asset2, 0.2);
        testWeights.put(asset3, 0.3);
        MPT mptInstance = new MPT(testWeights, "i");

        // Call historyCleaner
        mptInstance.historyCleaner();

        // After calling historyCleaner with interpolation method, the historical returns of the assets should be interpolated.
        // The exact expected values would depend on the interpolation method used, but for the purposes of this test,
        // we'll check that the historical returns lengths match the longest initial history.
        int expectedLength = 12; // Based on asset3's historical returns length.
        System.out.println(asset1.toString());
        System.out.println(asset2.toString());
        // Assert the historical returns lengths of all assets are now equal to the expected length
        Assert.assertEquals(expectedLength, asset1.getHistoricalReturns().size());
        Assert.assertEquals(expectedLength, asset2.getHistoricalReturns().size());
        Assert.assertEquals(expectedLength, asset3.getHistoricalReturns().size());
    }


}
