package com.realestate.math.interest;

public class Interest {

    // Compute the Simple Interest on a principal amount given an interest rate and time in years
    public static double simpleInterest(double principal, double interestRate, double time) {
        return principal * interestRate * time;
    }

    // Compute the total value given simple interest
    public static double totalSimpleInterest(double principal, double interestRate, double time) {
        return simpleInterest(principal, interestRate, time) + principal;
    }

    // Compute the total value given compound interest
    public static double totalCompoundInterest(double principal, double interestRate, double time) {
        return principal * Math.pow(1 + interestRate, time);
    }

    // Compute the compound interest on a principal amount given an interest rate and time in years
    public static double compoundInterest(double principal, double interestRate, double time) {
        return totalCompoundInterest(principal, interestRate, time) - principal;
    }

    public static double simpleInterest(double principal, double interestRate, int years, int months) {
        return principal * interestRate * (years + months / 12.0);
    }

    public static double totalSimpleInterest(double principal, double interestRate, int years, int months) {
        return simpleInterest(principal, interestRate, years, months) + principal;
    }

    public static double TotalCompoundInterest(double principal, double interestRate, int years, int months) {
        return principal * Math.pow(1 + interestRate, years + months / 12.0);
    }

    public static double compoundInterest(double principal, double interestRate, int years, int months) {
        return TotalCompoundInterest(principal, interestRate, years, months) - principal;
    }

    public static double simpleInterest(double principal, double interestRate, int years, int months, int days) {
        return principal * interestRate * (years + months / 12.0 + days / 365.0);
    }

    public static double totalSimpleInterest(double principal, double interestRate, int years, int months, int days) {
        return simpleInterest(principal, interestRate, years, months, days) + principal;
    }

    public static double TotalCompoundInterest(double principal, double interestRate, int years, int months, int days) {
        return principal * Math.pow(1 + interestRate, years + months / 12.0 + days / 365.0);
    }

    public static double compoundInterest(double principal, double interestRate, int years, int months, int days) {
        return TotalCompoundInterest(principal, interestRate, years, months, days) - principal;
    }

    // Finds Simple Interest Rate given principal, end value, and time
    public static double findSimpleInterest(double principal, double endValue, double time) {
        return (endValue - principal) / (time * principal);
    }
    // Finds Compound Interest Rate given principal, end value, and time
    public static double findCompoundInterestRate(double principal, double endValue, double time) {
        return Math.pow(endValue / principal, 1.0 / time) - 1;
    }

    // Computes Continuous compound interest (future value)
    public static double continuousCompoundInterest(double principal, double rate, double time) {
        return principal * Math.exp(rate * time);
    }

    // Find out what the continuous compound interest rate is
    public static double findContinuousCompoundRate(double principal, double endValue, double time) {
        return Math.log(endValue / principal) / time;
    }

    // Compute monthly compound interest
    public static double monthlyCompoundInterest(double principal, double annualRate, double years) {
        int n = 12; // Monthly compounding
        double ratePerPeriod = annualRate / n;
        return principal * Math.pow(1 + ratePerPeriod, n * years);
    }
    // compute daily compound interest
    public static double dailyCompoundInterest(double principal, double annualRate, double years) {
        int n = 365; // Daily compounding
        double ratePerPeriod = annualRate / n;
        return principal * Math.pow(1 + ratePerPeriod, n * years);
    }

    // Compute monthly compound rate
    public static double findMonthlyCompoundRate(double principal, double endValue, double years) {
        return Math.pow(endValue / principal, 1.0 / (12 * years)) - 1;
    }
    // Compute Daily Compound Rate
    public static double findDailyCompoundRate(double principal, double endValue, double years) {
        return Math.pow(endValue / principal, 1.0 / (365 * years)) - 1;
    }



}
