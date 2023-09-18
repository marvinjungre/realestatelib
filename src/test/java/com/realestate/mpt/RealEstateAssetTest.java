package com.realestate.mpt;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RealEstateAssetTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAnnualRentalIncome() {
        new RealEstateAsset("A123", -1000, 100, 200, 10000, Arrays.asList(0.1, 0.2, 0.3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAnnualAppreciation() {
        new RealEstateAsset("A123", 1000, -100, 200, 10000, Arrays.asList(0.1, 0.2, 0.3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeAnnualExpenses() {
        new RealEstateAsset("A123", 1000, 100, -200, 10000, Arrays.asList(0.1, 0.2, 0.3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialPropertyValue() {
        new RealEstateAsset("A123", 1000, 100, 200, -10000, Arrays.asList(0.1, 0.2, 0.3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyHistoricalReturns() {
        new RealEstateAsset("A123", 1000, 100, 200, 10000, Collections.emptyList());
    }

    @Test
    public void testValidRealEstateAssetConstructor1() {
        RealEstateAsset asset = new RealEstateAsset("A123", 1000, 100, 200, 10000, Arrays.asList(0.1, 0.2, 0.3));
        Assert.assertEquals("A123", asset.getPropertyId());
        Assert.assertEquals(900.0/10000, asset.getExpectedReturn(), 0.0001);
    }

    @Test
    public void testValidRealEstateAssetConstructor2() {
        RealEstateAsset asset = new RealEstateAsset("B456", 0.09, Arrays.asList(0.1, 0.2, 0.3));
        Assert.assertEquals("B456", asset.getPropertyId());
        Assert.assertEquals(0.09, asset.getExpectedReturn(), 0.0001);
    }

    @Test
    public void testValidRealEstateAssetConstructorNoPropertyId1() {
        RealEstateAsset asset = new RealEstateAsset(0.09, Arrays.asList(0.1, 0.2, 0.3));
        Assert.assertEquals("", asset.getPropertyId()); // default value
        Assert.assertEquals(0.09, asset.getExpectedReturn(), 0.0001);
    }

    @Test
    public void testValidRealEstateAssetConstructorNoPropertyId2() {
        RealEstateAsset asset = new RealEstateAsset(1000, 100, 200, 10000, Arrays.asList(0.1, 0.2, 0.3));
        Assert.assertEquals("", asset.getPropertyId()); // default value
        Assert.assertEquals(900.0/10000, asset.getExpectedReturn(), 0.0001);
    }
}

