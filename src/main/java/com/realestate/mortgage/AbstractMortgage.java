package com.realestate.mortgage;

public abstract class AbstractMortgage {
    protected double principal;
    protected double interestRate;
    protected int term; // in months

    public void setPrincipal(double principal) {
        if(principal <= 0) {
            throw new IllegalArgumentException("Principal cannot be negative or zero.");
        }
        this.principal = principal;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setInterestRate(double interestRate) {
        if(interestRate <= 0) {
            throw new IllegalArgumentException("Initial rate cannot be negative or zero.");
        }
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setTerm(int term, String termType) {
        if(term <= 0) {
            throw new IllegalArgumentException("Term cannot be negative or zero.");
        }
        if ("y".equalsIgnoreCase(termType)) {
            this.term = term * 12;  // Convert years to months
        } else if ("m".equalsIgnoreCase(termType)) {
            this.term = term;       // Term is already in months
        } else {
            throw new IllegalArgumentException("Invalid term type. Use 'y' for years or 'm' for months.");
        }
    }
    public int getTerm() {
        return term;
    }

    // Calculates the monthly payment for the mortgage
    public abstract double calculateMonthlyPayment();

    // Calculates the total interest paid over the mortgage term
    public abstract double calculateTotalInterest();

    // Calculates the total cost of the mortgage over its term
    public abstract double calculateTotalCost();

    public abstract double calculateRemainingBalance(int monthsPaid);

    public abstract double calculateRemainingPayments(int monthsPaid);
}
