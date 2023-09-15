package com.realestate.mortgage;

public class FixedRateMortgage implements Mortgage{
    private double principal;
    private double interestRate;
    private int term;


    public FixedRateMortgage(double principal, double interestRate, int term, String termType) {
        this.principal = principal;
        this.interestRate = interestRate;

        if ("y".equalsIgnoreCase(termType)) {
            this.term = term * 12;  // Convert years to months
        } else if ("m".equalsIgnoreCase(termType)) {
            this.term = term;       // Term is already in months
        } else {
            throw new IllegalArgumentException("Invalid term type. Use 'y' for years or 'm' for months.");
        }
    }

    // Constructor without termType, defaults to 'y'
    public FixedRateMortgage(double principal, double interestRate, int term) {
        this(principal, interestRate, term, "y");  // Calls the other constructor with default 'y'
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
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
    public int getTerm() {
        return term;
    }

    @Override
    public double calculateMonthlyPayment() {
        double monthlyInterestRate = interestRate / 12;
        return principal * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -term));
    }


    @Override
    public double calculateTotalInterest() {
        double monthlyPayment = calculateMonthlyPayment();
        return monthlyPayment * term - principal;
    }

    @Override
    public double calculateTotalCost() {
        double monthlyPayment = calculateMonthlyPayment();
        return monthlyPayment * term;
    }

    @Override
    public double calculateRemainingBalance(int monthsPaid) {
        double monthlyInterestRate = interestRate / 12;
        return principal * Math.pow(1 + monthlyInterestRate, monthsPaid) - calculateMonthlyPayment() * (Math.pow(1 + monthlyInterestRate, monthsPaid) - 1) / monthlyInterestRate;
    }


}
