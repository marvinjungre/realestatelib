package com.realestate.mortgage;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;

public class ARMMortgageTest {

    private static final double DELTA = 0.01; // for floating point comparison

    @Test
    public void testCalculateMonthlyPayment_NoRateChanges() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y");
        double monthlyPayment = mortgage.calculateMonthlyPayment();
        assertEquals(536.82, monthlyPayment, DELTA);  // Assuming standard 30-year fixed rate formula with no rate changes
    }

    @Test
    public void testCalculateMonthlyPayment_SingleRateChange() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06));
        double monthlyPaymentMonth11 = mortgage.calculateMonthlyPayment(11);
        double monthlyPaymentMonth12 = mortgage.calculateMonthlyPayment(12);
        assertEquals(536.82, monthlyPaymentMonth11, DELTA); // Before rate change
        assertNotEquals(536.82, monthlyPaymentMonth12, DELTA); // After rate change (value will be different, so just ensuring it's not the same)
    }

    @Test
    public void testCalculateMonthlyPayment_MultipleRateChanges() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06), new ARMMortgage.RateChange(24, 0.07));
        double monthlyPaymentMonth11 = mortgage.calculateMonthlyPayment(11);
        double monthlyPaymentMonth13 = mortgage.calculateMonthlyPayment(13);
        double monthlyPaymentMonth25 = mortgage.calculateMonthlyPayment(25);
        assertEquals(536.82, monthlyPaymentMonth11, DELTA); // Before any rate changes
        assertNotEquals(536.82, monthlyPaymentMonth13, DELTA); // After first rate change
        assertNotEquals(monthlyPaymentMonth13, monthlyPaymentMonth25, DELTA); // After second rate change
    }

    @Test(expected = IllegalArgumentException.class)
    public void testcalculateRemainingPayments_ExceedingMonthsPaid() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06));
        mortgage.calculateRemainingPayments(361); // Given a 30-year term, this is one month beyond the term
    }

    @Test
    public void testInitialValues() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y");
        assertEquals(100000, mortgage.getPrincipal(), DELTA);
        assertEquals(360, mortgage.getTerm());
    }

    @Test
    public void testRateChangeEffect() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06));
        // Before any rate change
        assertEquals(0.05, mortgage.getRateForMonth(11), DELTA);
        // After the rate change
        assertEquals(0.06, mortgage.getRateForMonth(12), DELTA);
    }

    @Test
    public void testNegativePrincipalException() {
        try {
            ARMMortgage mortgage = new ARMMortgage(-100000, 0.05, 30, "y");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("negative or zero"));
        }
    }

    @Test
    public void testInvalidTermTypeException() {
        try {
            ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "d");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid term type"));
        }
    }

    @Test
    public void testNegativeMonthException() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y");
        try {
            mortgage.calculateMonthlyPayment(-5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("negative or zero"));
        }
    }

    @Test
    public void testExceedingMonthException() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y");
        try {
            mortgage.calculateMonthlyPayment(361);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("exceed term time"));
        }
    }

    @Test
    public void testRateChangeOrderEffect() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(24, 0.07), new ARMMortgage.RateChange(12, 0.06));
        // Before any rate change
        assertEquals(0.05, mortgage.getRateForMonth(11), DELTA);
        // After the first rate change
        assertEquals(0.06, mortgage.getRateForMonth(13), DELTA);
        // After the second rate change
        assertEquals(0.07, mortgage.getRateForMonth(25), DELTA);
    }

    @Test
    public void testSetRateChangesAfterInitialization() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y");
        List<ARMMortgage.RateChange> rateChanges = new ArrayList<>();
        rateChanges.add(new ARMMortgage.RateChange(12, 0.06));
        mortgage.setRateChanges(rateChanges);
        assertEquals(0.06, mortgage.getRateForMonth(12), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRateChangeBeyondTermLimit() {
        List<ARMMortgage.RateChange> rateChanges = new ArrayList<>();
        rateChanges.add(new ARMMortgage.RateChange(361, 0.06));
        new ARMMortgage(100000, 0.05, 30, "y", rateChanges);
    }

    @Test
    public void testCalculateTotalInterestWithNoChanges() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y");
        double totalInterest = mortgage.calculateTotalInterest();
        assertTrue(totalInterest > 0);
    }

    @Test
    public void testCalculateTotalInterestWithRateDecrease() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.04));
        double totalInterest = mortgage.calculateTotalInterest();
        assertTrue(totalInterest > 0);
    }

    @Test
    public void testCalculateTotalCostWithNoChanges() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y");
        double totalCost = mortgage.calculateTotalCost();
        assertEquals(100000 + mortgage.calculateTotalInterest(), totalCost, DELTA);
    }

    @Test
    public void testCalculateTotalCostWithRateIncrease() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06));
        double totalInterest = mortgage.calculateTotalInterest();
        double totalCost = mortgage.calculateTotalCost();
        assertEquals(100000 + totalInterest, totalCost, DELTA);
    }

    @Test
    public void testCalculateMonthlyPayment_ZeroRateChange() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.05));
        double monthlyPaymentMonth11 = mortgage.calculateMonthlyPayment(11);
        double monthlyPaymentMonth12 = mortgage.calculateMonthlyPayment(12);
        assertEquals(536.82, monthlyPaymentMonth11, DELTA);
        assertEquals(536.82, monthlyPaymentMonth12, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRateChangeException() {
        new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, -0.05));
    }

    @Test
    public void testRateChangeFirstMonthEffect() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(1, 0.06));
        assertEquals(0.06, mortgage.getRateForMonth(1), DELTA);
    }

    @Test
    public void testResetRateChanges() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06));
        assertEquals(0.06, mortgage.getRateForMonth(12), DELTA);
        List<ARMMortgage.RateChange> rateChanges = new ArrayList<>();
        rateChanges.add(new ARMMortgage.RateChange(12, 0.07));
        mortgage.setRateChanges(rateChanges);
        assertEquals(0.07, mortgage.getRateForMonth(12), DELTA);
    }

    @Test
    public void testCompoundingEffectWithRateChange() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06));
        double totalInterestWithoutRateChange = mortgage.calculateTotalInterest();
        ARMMortgage mortgageWithRateChange = new ARMMortgage(100000, 0.05, 30, "y");
        double totalInterestWithRateChange = mortgageWithRateChange.calculateTotalInterest();
        assertNotEquals(totalInterestWithoutRateChange, totalInterestWithRateChange, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateRateChangesInitialization() {
        new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06), new ARMMortgage.RateChange(12, 0.07));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRateChange() {
        new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, -0.02));
    }

    @Test
    public void testRateResetAfterCertainPeriod() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new ARMMortgage.RateChange(12, 0.06));
        // Assuming there's an implicit reset back to the initial rate at month 36
        assertEquals(0.06, mortgage.getRateForMonth(35), DELTA);
        assertEquals(0.05, mortgage.getRateForMonth(36), DELTA);
    }

    @Test
    public void testRemainingBalanceWithoutRateChange() {
        ARMMortgage mortgage = new ARMMortgage(200000, 0.04, 30, "y");
        assertEquals(196477.92, mortgage.calculateRemainingBalance(12), DELTA);  // 1 year into the loan without any rate change
    }

    @Test
    public void testRemainingBalanceWithRateChange() {
        ARMMortgage mortgage = new ARMMortgage(200000, 0.04, 30, "y", new ARMMortgage.RateChange(6, 0.05));
        assertEquals(196803.36, mortgage.calculateRemainingBalance(12), DELTA);  // 1 year into the loan with a rate change at 6 months
    }
}
