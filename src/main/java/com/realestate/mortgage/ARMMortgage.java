package com.realestate.mortgage;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ARMMortgage extends AbstractMortgage {
    private List<RateChange> rateChanges; // List of RateChange objects

    public ARMMortgage(double principal, double interestRate, int term, String termType, List<RateChange> rateChanges) {
        setPrincipal(principal);
        setInterestRate(interestRate);
        setTerm(term, termType);
        this.rateChanges = (rateChanges != null) ? rateChanges : new ArrayList<>();
        this.rateChanges.sort(Comparator.comparingInt(RateChange::getMonth)); // Sort the list by month, otherwise higher changes out of order would be disregarded
        validateRateChanges();
    }
    // Constructor when no rate changes
    public ARMMortgage(double principal, double interestRate, int term, String termType) {
        this(principal, interestRate, term, termType, (List<RateChange>) null);
    }

    // Constructor with varargs for one or more rate changes
    public ARMMortgage(double principal, double interestRate, int term, String termType, RateChange... rateChanges) {
        this(principal, interestRate, term, termType, Arrays.asList(rateChanges));
    }

    // Return the interest rate for a specific month
    public double getRateForMonth(int month) {
        double rate = interestRate;
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
        if(principal <= 0) {
            throw new IllegalArgumentException("Principal cannot be negative or zero.");
        }
        this.principal = principal;
    }

    // Getter and Setter for rateChanges
    public List<RateChange> getRateChanges() {
        return rateChanges;
    }

    public void setRateChanges(List<RateChange> rateChanges) {
        this.rateChanges = rateChanges;
    }

    public double calculateMonthlyPayment(int month) { // As monthly payments change, the month should be specified
        checkMonthValidity(month);
        double monthlyInterestRate = getRateForMonth(month) / 12;
        return principal * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -term));
    }

    public double calculateMonthlyPayment() { // If no month is specified, use the first month
        int month = 1;
        double monthlyInterestRate = getRateForMonth(month) / 12;
        return principal * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -term));
    }

    public double calculateTotalInterest() {
        double totalInterest = 0;
        for (int i = 1; i <= term; i++) {
            totalInterest += calculateMonthlyPayment(i);
        }
        return totalInterest - principal;
    }

    public double calculateTotalCost() {
        return principal + calculateTotalInterest();
    }

    public double calculateRemainingPayments(int monthsPaid) {
        checkMonthValidity(monthsPaid);
        double totalPaid = 0;
        for (int i = 1; i <= monthsPaid; i++) {
            totalPaid += calculateMonthlyPayment(i);
        }
        return calculateTotalCost() - totalPaid;
    }

    public double calculateRemainingBalance(int monthsPaid) {
        checkMonthValidity(monthsPaid);
        double remainingPrincipal = principal;
        for (int i = 1; i <= monthsPaid; i++) {
            double monthlyInterestRate = getRateForMonth(i) / 12;
            double monthlyInterest = remainingPrincipal * monthlyInterestRate;
            double principalPortion = calculateMonthlyPayment(i) - monthlyInterest;
            remainingPrincipal -= principalPortion;
        }
        return remainingPrincipal;
    }

    // Nested RateChange class. Month validation has to happen outside as we have no access to the object's term time
    public static class RateChange {
        private int month;   // The month number when the rate changes
        private double rate; // The new rate starting this month

        public RateChange(int month, double rate) {
            if (rate <= 0) {
                throw new IllegalArgumentException("Rate cannot be negative or zero.");
            }
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

    // Checks if the input month is valid
    private void checkMonthValidity(int month){
        if (month <= 0) {
            throw new IllegalArgumentException("Month cannot be negative or zero.");
        }
        if (month > this.getTerm()) {
            throw new IllegalArgumentException("Month cannot exceed term time.");
        }
    }

    // We use this function to validate month within the RateChange objects, because we do not know the term time
    // within the static method
    private void validateRateChanges() {
        for (RateChange rc : rateChanges) {
            if (rc.getMonth() < 0 || rc.getMonth() > this.term) {
                throw new IllegalArgumentException("RateChange month is outside of the mortgage term range.");
            }
        }
        // Check for duplicates
        for (int i = 1; i < this.rateChanges.size(); i++) {
            if (this.rateChanges.get(i).getMonth() == this.rateChanges.get(i - 1).getMonth()) {
                throw new IllegalArgumentException("Duplicate rate change month detected: " + this.rateChanges.get(i).getMonth());
            }
        }
    }
}