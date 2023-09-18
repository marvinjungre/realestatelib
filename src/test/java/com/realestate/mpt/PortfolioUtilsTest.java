package com.realestate.mpt;

import org.junit.Assert;
import org.junit.Test;

import javax.sound.sampled.Port;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PortfolioUtilsTest {

    @Test
    public void testHelpComputeWeightsValidInput() {
        List<Double> assetValues = Arrays.asList(10.0, 20.0, 30.0, 40.0);
        List<Double> expectedWeights = Arrays.asList(0.1, 0.2, 0.3, 0.4);

        List<Double> actualWeights = PortfolioUtils.helpComputeWeights(assetValues);

        Assert.assertEquals(expectedWeights, actualWeights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHelpComputeWeightsEmptyInput() {
        List<Double> assetValues = Collections.emptyList();
        PortfolioUtils.helpComputeWeights(assetValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHelpComputeWeightsNullInput() {
        List<Double> assetValues = null;
        PortfolioUtils.helpComputeWeights(assetValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHelpComputeWeightsZeroTotalValue() {
        List<Double> assetValues = Arrays.asList(0.0, 0.0, 0.0);
        PortfolioUtils.helpComputeWeights(assetValues);
    }
}
