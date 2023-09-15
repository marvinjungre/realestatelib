package com.realestate.mortgage;
import java.util.List;

public class ARMMortgage implements Mortgage {
    private double principal;
    private double initialRate;
    private int term;
    private List<RateChange> rateChanges; // List of RateChange objects

    public ARMMortgage(double principal, double initialRate, int term, String termType, List<RateChange> rateChanges) {
        this.principal = principal;
        this.initialRate = initialRate;
        this.rateChanges = rateChanges;

        if ("y".equalsIgnoreCase(termType)) {
            this.term = term * 12;  // Convert years to months
        } else if ("m".equalsIgnoreCase(termType)) {
            this.term = term;       // Term is already in months
        } else {
            throw new IllegalArgumentException("Invalid term type. Use 'y' for years or 'm' for months.");
        }
    }

    // Return the interest rate for a specific month
    public double getRateForMonth(int month) {
        double rate = initialRate;
        for (RateChange rc : rateChanges) {
            if (month >= rc.getMonth()) {
                rate = rc.getRate();
            } else {
                break;
            }
        }
        return rate;
    }

    // Getter and Setter for principal
    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    // Getter and Setter for initialRate
    public double getInitialRate() {
        return initialRate;
    }

    public void setInitialRate(double initialRate) {
        this.initialRate = initialRate;
    }

    // Getter and Setter for term (with term type consideration)
    public int getTerm() {
        return term;
    }

    public void setTerm(int term, String termType) {
        if ("y".equalsIgnoreCase(termType)) {
            this.term = term * 12;  // Convert years to months
        } else if ("m".equalsIgnoreCase(termType)) {
            this.term = term;       // Term is already in months
        } else {
            throw new IllegalArgumentException("Invalid term type. Use 'y' for years or 'm' for months.");
        }
    }


    // Getter and Setter for rateChanges
    public List<RateChange> getRateChanges() {
        return rateChanges;
    }

    public void setRateChanges(List<RateChange> rateChanges) {
        this.rateChanges = rateChanges;
    }

    public double calculateMonthlyPayment(int month) { // As monthly payments change, the month should be specified
        double monthlyInterestRate = getRateForMonth(month) / 12;
        return principal * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -term));
    }

    @Override
    public double calculateMonthlyPayment() { // If no month is specified, use the initial rate
        int month = 0;
        double monthlyInterestRate = getRateForMonth(month) / 12;
        return principal * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -term));
    }

    @Override
    public double calculateTotalInterest() {
        double totalInterest = 0;
        for (int i = 1; i <= term; i++) {
            totalInterest += calculateMonthlyPayment(i);
        }
        return totalInterest - principal;
    }

    @Override
    public double calculateTotalCost() {
        return principal + calculateTotalInterest();
    }

    @Override
    public double calculateRemainingBalance(int monthsPaid) {
        double totalPaid = 0;
        for (int i = 1; i <= monthsPaid; i++) {
            totalPaid += calculateMonthlyPayment(i);
        }
        return calculateTotalCost() - totalPaid;
    }

    // Nested RateChange class
    public static class RateChange {
        private int month;   // The month number when the rate changes
        private double rate; // The new rate starting this month

        public RateChange(int month, double rate) {
            this.month = month;
            this.rate = rate;
        }

        public int getMonth() {
            return month;
        }

        public double getRate() {
            return rate;
        }
    }
}