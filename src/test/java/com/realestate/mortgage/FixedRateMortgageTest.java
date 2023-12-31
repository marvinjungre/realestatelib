package com.realestate.mortgage;
import com.realestate.mortgage.AbstractMortgage.AmortizationEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.junit.Test;

public class FixedRateMortgageTest {

    private static final double DELTA = 0.01;  // Tolerance for double comparisons

    @Test
    public void testMonthlyPaymentCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30, "y");
        assertEquals(536.82, mortgage.calculateMonthlyPayment(), DELTA);
    }

    @Test
    public void testTotalInterestCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30, "y");
        assertEquals(93255.78, mortgage.calculateTotalInterest(), DELTA);
    }

    @Test
    public void testTotalCostCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30, "y");
        assertEquals(193255.78, mortgage.calculateTotalCost(), DELTA);
    }

    @Test
    public void testRemainingBalanceCalculation() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30, "y");
        assertEquals(91828.73, mortgage.calculateRemainingPayments(60), DELTA);  // 5 years into the loan
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePrincipal() {
        new FixedRateMortgage(-100000, 0.05, 30, "y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroPrincipal() {
        new FixedRateMortgage(0, 0.05, 30, "y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInterestRate() {
        new FixedRateMortgage(100000, -0.05, 30, "y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testZeroInterestRate() {
        new FixedRateMortgage(100000, 0, 30, "y");
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
        FixedRateMortgage mortgage = new FixedRateMortgage(Double.MAX_VALUE, 0.00000001, 30, "y");
        assertTrue(mortgage.calculateMonthlyPayment() > 0);
    }

    @Test
    public void testRemainingBalanceAtEndOfLoan() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 30, "y");
        assertEquals(0, mortgage.calculateRemainingPayments(30*12), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeTerm() {
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, -10, "y");
    }

    @Test
    public void testRemainingBalanceAfter5Years() {
        FixedRateMortgage mortgage = new FixedRateMortgage(200000, 0.04, 30, "y");
        double expectedRemainingBalance = 180895.026; // this value will vary based on your formula and interest calculations
        assertEquals(expectedRemainingBalance, mortgage.calculateRemainingBalance(60), DELTA);  // 5 years into the loan
    }

    @Test
    public void testRemainingBalanceAfter1Year() {
        FixedRateMortgage mortgage = new FixedRateMortgage(200000, 0.04, 30, "y");
        double expectedRemainingBalance = 196477.927; // this value will vary based on your formula and interest calculations
        assertEquals(expectedRemainingBalance, mortgage.calculateRemainingBalance(12), DELTA);  // 1 year into the loan
    }

    @Test
    public void testAmortizationScheduleForThreeYears() {
        // Initialize a 3-year mortgage with some test values
        FixedRateMortgage mortgage = new FixedRateMortgage(100000, 0.05, 3 * 12, "m"); // Using 5% annual interest for simplicity

        // Generate the amortization schedule
        List<AmortizationEntry> schedule = mortgage.generateAmortizationSchedule();

        // Validate the schedule
        assertNotNull(schedule);
        assertEquals(3 * 12, schedule.size()); // 3 years of monthly payments

        // As a basic check, validate the final entry
        AmortizationEntry finalEntry = schedule.get(schedule.size() - 1);
        assertEquals(0, finalEntry.getEndingBalance(), DELTA);
    }

    /* @Test
    public void testPrintAmortizationSchedule() {
        FixedRateMortgage mortgage = new FixedRateMortgage(200000, 0.04, 30, "y");
        mortgage.printAmortizationSchedule();
    } */
}


