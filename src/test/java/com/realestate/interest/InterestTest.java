package com.realestate.interest;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import com.realestate.interest.Interest;

public class InterestTest {

    @Test
    public void testSimpleInterestStandard() {
        assertEquals(554.109589, Interest.simpleInterest(1000, 0.1, 5, 6, 15), 0.0001);
        // Explanation: 5.54109589 years of interest on $1000 at 10% per annum is around $554.109589.
    }

    @Test
    public void testSimpleInterestEdge() {
        assertEquals(0.0, Interest.simpleInterest(0, 0.1, 5, 6, 15), 0.0001);
        // Explanation: If the principal is 0, regardless of the time or interest rate, the interest should be 0.
    }

    @Test
    public void testSimpleInterestFail() {
        assertNotEquals(550.0, Interest.simpleInterest(1000, 0.1, 5, 0, 0), 0.0001);
        // Explanation: This case is supposed to fail because the correct interest is $500.
    }

    @Test
    public void testTotalSimpleInterestStandard() {
        assertEquals(1554.109589, Interest.totalSimpleInterest(1000, 0.1, 5, 6, 15), 0.0001);
        // Explanation: 5.54109589 years of interest on $1000 at 10% per annum is around $554.109589. Add this to the principal to get $1554.109589.
    }

    @Test
    public void testTotalSimpleInterestEdge() {
        assertEquals(0.0, Interest.totalSimpleInterest(0, 0.1, 5, 6, 15), 0.0001);
        // Explanation: If the principal is 0, regardless of the time or interest rate, the total amount (principal + interest) should also be 0.
    }

    @Test
    public void testTotalSimpleInterestFail() {
        assertNotEquals(1550.0, Interest.totalSimpleInterest(1000, 0.1, 5, 0, 0), 0.0001);
        // Explanation: This case is supposed to fail because the correct total amount (principal + interest) is $1500.
    }

    @Test
    public void testFindSimpleInterestStandard() {
        assertEquals(0.1, Interest.findSimpleInterest(1000, 1100, 1), 0.0001);
        // Explanation: $100 interest on $1000 principal over 1 year gives a rate of 10% or 0.1.
    }

    @Test
    public void testFindSimpleInterestEdge() {
        assertEquals(0.0, Interest.findSimpleInterest(1000, 1000, 1), 0.0001);
        // Explanation: No increase in value over time means a rate of 0.
    }

    @Test
    public void testFindSimpleInterestFail() {
        assertNotEquals(0.05, Interest.findSimpleInterest(1000, 1100, 1), 0.0001);
        // Explanation: This case is supposed to fail because the correct rate is 0.1, not 0.05.
    }

    @Test
    public void testFindCompoundInterestRateStandard() {
        assertEquals(0.05094, Interest.findCompoundInterestRate(1000, 1104.486, 2), 0.0001);
        // Explanation: An end value of 1104.486 on a principal of 1000 over 2 years gives a rate close to 5.12% when compounded annually.
    }

    @Test
    public void testFindCompoundInterestRateEdge() {
        assertEquals(0.0, Interest.findCompoundInterestRate(1000, 1000, 2), 0.0001);
        // Explanation: No increase in value over time means a rate of 0.
    }

    @Test
    public void testFindCompoundInterestRateFail() {
        assertNotEquals(0.05, Interest.findCompoundInterestRate(1000, 1104.486, 2), 0.0001);
        // Explanation: This case is supposed to fail because the correct rate is close to 0.1, not 0.05.
    }

    @Test
    public void testContinuousCompoundInterestStandard() {
        assertEquals(1161.8342, Interest.continuousCompoundInterest(1000, 0.05, 3), 0.0001);
        // Explanation: A principal of $1000 with a continuous compound interest rate of 5% over 3 years yields an amount close to $1159.28.
    }

    @Test
    public void testContinuousCompoundInterestEdge() {
        assertEquals(1000, Interest.continuousCompoundInterest(1000, 0.0, 3), 0.0001);
        // Explanation: A principal of $1000 with a continuous compound interest rate of 0% over 3 years should return the principal itself.
    }

    @Test
    public void testContinuousCompoundInterestFail() {
        assertNotEquals(1200.0, Interest.continuousCompoundInterest(1000, 0.05, 3), 0.0001);
        // Explanation: This case is supposed to fail because the correct amount is $1159.28, not $1200.
    }

    @Test
    public void testFindContinuousCompoundRateStandard() {
        assertEquals(0.0492, Interest.findContinuousCompoundRate(1000, 1159.28, 3), 0.0001);
        // Explanation: An end value of $1159.28 on a principal of $1000 over 3 years gives a continuous compound interest rate close to 5%.
    }

    @Test
    public void testFindContinuousCompoundRateEdge() {
        assertEquals(0.0, Interest.findContinuousCompoundRate(1000, 1000, 3), 0.0001);
        // Explanation: If the end value is same as the principal, then the continuous compound interest rate is 0%.
    }

    @Test
    public void testFindContinuousCompoundRateFail() {
        assertNotEquals(0.06, Interest.findContinuousCompoundRate(1000, 1159.28, 3), 0.0001);
        // Explanation: This case is supposed to fail because the correct rate is 5%, not 6%.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindContinuousCompoundRateZeroPrincipal() {
        Interest.findContinuousCompoundRate(0, 1100, 3);
        // Explanation: This test should throw an IllegalArgumentException because the principal is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindContinuousCompoundRateZeroTime() {
        Interest.findContinuousCompoundRate(1000, 1100, 0);
        // Explanation: This test should throw an IllegalArgumentException because the time is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSimpleInterestZeroPrincipal() {
        Interest.findSimpleInterest(0, 1100, 3);
        // Explanation: This test should throw an IllegalArgumentException because the principal is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSimpleInterestZeroTime() {
        Interest.findSimpleInterest(1000, 1100, 0);
        // Explanation: This test should throw an IllegalArgumentException because the time is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCompoundInterestRateZeroPrincipal() {
        Interest.findCompoundInterestRate(0, 1104.486, 2);
        // Explanation: This test should throw an IllegalArgumentException because the principal is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCompoundInterestRateZeroTime() {
        Interest.findCompoundInterestRate(1000, 1104.486, 0);
        // Explanation: This test should throw an IllegalArgumentException because the time is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMonthlyCompoundRateZeroPrincipal() {
        Interest.findMonthlyCompoundRate(0, 1100, 2);
        // Explanation: This test should throw an IllegalArgumentException because the principal is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindMonthlyCompoundRateZeroYears() {
        Interest.findMonthlyCompoundRate(1000, 1100, 0);
        // Explanation: This test should throw an IllegalArgumentException because the years is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDailyCompoundRateZeroPrincipal() {
        Interest.findDailyCompoundRate(0, 1100, 2);
        // Explanation: This test should throw an IllegalArgumentException because the principal is zero.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindDailyCompoundRateZeroYears() {
        Interest.findDailyCompoundRate(1000, 1100, 0);
        // Explanation: This test should throw an IllegalArgumentException because the years is zero.
    }



}
