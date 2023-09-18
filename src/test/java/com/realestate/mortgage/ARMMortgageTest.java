package com.realestate.mortgage;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.realestate.mortgage.ARMMortgage.RateChange;

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
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06));
        double monthlyPaymentMonth11 = mortgage.calculateMonthlyPayment(11);
        double monthlyPaymentMonth12 = mortgage.calculateMonthlyPayment(12);
        assertEquals(536.82, monthlyPaymentMonth11, DELTA); // Before rate change
        assertNotEquals(536.82, monthlyPaymentMonth12, DELTA); // After rate change (value will be different, so just ensuring it's not the same)
    }

    @Test
    public void testCalculateMonthlyPayment_MultipleRateChanges() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06), new RateChange(24, 0.07));
        double monthlyPaymentMonth11 = mortgage.calculateMonthlyPayment(11);
        double monthlyPaymentMonth13 = mortgage.calculateMonthlyPayment(13);
        double monthlyPaymentMonth25 = mortgage.calculateMonthlyPayment(25);
        assertEquals(536.82, monthlyPaymentMonth11, DELTA); // Before any rate changes
        assertNotEquals(536.82, monthlyPaymentMonth13, DELTA); // After first rate change
        assertNotEquals(monthlyPaymentMonth13, monthlyPaymentMonth25, DELTA); // After second rate change
    }

    @Test(expected = IllegalArgumentException.class)
    public void testcalculateRemainingPayments_ExceedingMonthsPaid() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06));
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
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06));
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
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(24, 0.07), new RateChange(12, 0.06));
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
        List<RateChange> rateChanges = new ArrayList<>();
        rateChanges.add(new RateChange(12, 0.06));
        mortgage.setRateChanges(rateChanges);
        assertEquals(0.06, mortgage.getRateForMonth(12), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRateChangeBeyondTermLimit() {
        List<RateChange> rateChanges = new ArrayList<>();
        rateChanges.add(new RateChange(361, 0.06));
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
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.04));
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
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06));
        double totalInterest = mortgage.calculateTotalInterest();
        double totalCost = mortgage.calculateTotalCost();
        assertEquals(100000 + totalInterest, totalCost, DELTA);
    }

    @Test
    public void testCalculateMonthlyPayment_ZeroRateChange() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.05));
        double monthlyPaymentMonth11 = mortgage.calculateMonthlyPayment(11);
        double monthlyPaymentMonth12 = mortgage.calculateMonthlyPayment(12);
        assertEquals(536.82, monthlyPaymentMonth11, DELTA);
        assertEquals(536.82, monthlyPaymentMonth12, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRateChangeException() {
        new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, -0.05));
    }

    @Test
    public void testRateChangeFirstMonthEffect() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(1, 0.06));
        assertEquals(0.06, mortgage.getRateForMonth(1), DELTA);
    }

    @Test
    public void testResetRateChanges() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06));
        assertEquals(0.06, mortgage.getRateForMonth(12), DELTA);
        List<RateChange> rateChanges = new ArrayList<>();
        rateChanges.add(new RateChange(12, 0.07));
        mortgage.setRateChanges(rateChanges);
        assertEquals(0.07, mortgage.getRateForMonth(12), DELTA);
    }

    @Test
    public void testCompoundingEffectWithRateChange() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06));
        double totalInterestWithoutRateChange = mortgage.calculateTotalInterest();
        ARMMortgage mortgageWithRateChange = new ARMMortgage(100000, 0.05, 30, "y");
        double totalInterestWithRateChange = mortgageWithRateChange.calculateTotalInterest();
        assertNotEquals(totalInterestWithoutRateChange, totalInterestWithRateChange, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateRateChangesInitialization() {
        new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06), new RateChange(12, 0.07));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRateChange() {
        new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, -0.02));
    }

    @Test
    public void testRateResetAfterCertainPeriod() {
        ARMMortgage mortgage = new ARMMortgage(100000, 0.05, 30, "y", new RateChange(12, 0.06));
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
        RateChange rateChange = new RateChange(6, 0.05); // Rate change at 6 months
        ARMMortgage mortgage = new ARMMortgage(200000, 0.04, 30, "y", Arrays.asList(rateChange));
        assertEquals(196812.506, mortgage.calculateRemainingBalance(12), DELTA);  // 1 year into the loan with a rate change at 6 months
    }

    @Test
    public void testRemainingBalanceWithMultipleRateChanges() {
        RateChange rateChange1 = new RateChange(6, 0.05);   // Rate change at 6 months
        RateChange rateChange2 = new RateChange(12, 0.06);  // Rate change at 1 year
        RateChange rateChange3 = new RateChange(18, 0.04);  // Rate change at 1.5 years
        RateChange rateChange4 = new RateChange(24, 0.05);  // Rate change at 2 years
        RateChange rateChange5 = new RateChange(30, 0.06);  // Rate change at 2.5 years
        List<RateChange> rateChanges = Arrays.asList(rateChange1, rateChange2, rateChange3, rateChange4, rateChange5);
        ARMMortgage mortgage = new ARMMortgage(200000, 0.04, 3, "y", rateChanges);
        assertEquals(5971.986 , mortgage.calculateRemainingBalance(35), DELTA);  // 3 years into the loan with rate changes at the specified intervals
    }

    @Test
    public void testGenerateAmortizationScheduleWithRateChanges() {
        RateChange rateChange1 = new RateChange(12, 0.05);  // Change to 5% after 1 year
        RateChange rateChange2 = new RateChange(24, 0.06);  // Change to 6% after 2 years
        RateChange rateChange3 = new RateChange(36, 0.04);  // Change back to 4% after 3 years

        ARMMortgage mortgage = new ARMMortgage(200000, 0.04, 48, "m", rateChange1, rateChange2, rateChange3);
        List<ARMMortgage.AmortizationEntry> schedule = mortgage.generateAmortizationSchedule();
        
        // Check the first month's values
        assertEquals(200000, schedule.get(0).getBeginningBalance(), DELTA);
        // You can continue to add more assertions here for other fields of the first entry

        // Loop through each entry in the schedule and validate
        for (int i = 0; i < schedule.size(); i++) {
            ARMMortgage.AmortizationEntry entry = schedule.get(i);

            double expectedInterest = entry.getBeginningBalance() * mortgage.getRateForMonth(i + 1) / 12;
            assertEquals(expectedInterest, entry.getInterestPaid(), DELTA);

            double expectedPrincipal = entry.getPayment() - entry.getInterestPaid();
            assertEquals(expectedPrincipal, entry.getPrincipalPaid(), DELTA);

            double expectedEndingBalance = entry.getBeginningBalance() - entry.getPrincipalPaid();
            assertEquals(expectedEndingBalance, entry.getEndingBalance(), DELTA);

            // Check if the ending balance of this month is the starting balance of next month
            if (i < schedule.size() - 1) { // Skip for the last entry
                assertEquals(entry.getEndingBalance(), schedule.get(i + 1).getBeginningBalance(), DELTA);
            }
        }

        // Check that the mortgage balance is zero at the end of the term
        assertEquals(0, schedule.get(schedule.size() - 1).getEndingBalance(), DELTA);
        // mortgage.printAmortizationSchedule();
        // System.out.println(mortgage.calculateTotalCost());
        // System.out.println(mortgage.calculateTotalInterest());
    }
}
