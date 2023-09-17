package com.realestate.mortgage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FixedRateMortgageTest {

    private static final double DELTA = 0.01;  // Tolerance for double comparisons

    @Test
    public void testMonthlyPaymentCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30);
        assertEquals(536.82, mortgage.calculateMonthlyPayment(), DELTA);
    }

    @Test
    public void testTotalInterestCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30);
        assertEquals(93255.78, mortgage.calculateTotalInterest(), DELTA);
    }

    @Test
    public void testTotalCostCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30);
        assertEquals(193255.78, mortgage.calculateTotalCost(), DELTA);
    }

    @Test
    public void testRemainingBalanceCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30);
        assertEquals(91828.73, mortgage.calculateRemainingBalance(60), DELTA);  // 5 years into the loan
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePrincipal() {
        new FixedRateMortgage(-100000, 0.05, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroPrincipal() {
        new FixedRateMortgage(0, 0.05, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInterestRate() {
        new FixedRateMortgage(100000, -0.05, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroInterestRate() {
        new FixedRateMortgage(100000, 0, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTermType() {
        new FixedRateMortgage(100000, 0.05, 30, "cat");
    }

    @Test
    public void testTermConversionToMonths() {
        FixedRateMortgage mortgageYears = new FixedRateMortgage(100000, 0.05, 30, "y");
        FixedRateMortgage mortgageMonths = new FixedRateMortgage(100000, 0.05, 360, "m");
        assertTrue(mortgageYears.getTerm() == mortgageMonths.getTerm());
    }
    @Test
    public void testLargePrincipalSmallInterestRate() {
        FixedRateMortgage mortgage = new FixedRateMortgage(Double.MAX_VALUE, 0.00000001, 30);
        assertTrue(mortgage.calculateMonthlyPayment() > 0);
    }

    @Test
    public void testRemainingBalanceAtEndOfLoan() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30);
        assertEquals(0, mortgage.calculateRemainingBalance(30*12), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTerm() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, -10);
    }



}
