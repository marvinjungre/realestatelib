package com.realestate.mortgage;

public class FixedRateMortgage extends AbstractMortgage{

    public FixedRateMortgage(double principal, double interestRate, int term, String termType) {
        setPrincipal(principal);
        setInterestRate(interestRate);
        setTerm(term, termType);
    }

    // Constructor without termType, defaults to 'y'
    public FixedRateMortgage(double principal, double interestRate, int term) {
        this(principal, interestRate, term, "y");  // Calls the other constructor with default 'y'
    }

    public double calculateMonthlyPayment() {
        double monthlyInterestRate = interestRate / 12;
        return principal * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -term));
    }

    public double calculateTotalInterest() {
        double monthlyPayment = calculateMonthlyPayment();
        return monthlyPayment * term - principal;
    }

    public double calculateTotalCost() {
        double monthlyPayment = calculateMonthlyPayment();
        return monthlyPayment * term;
    }

    public double calculateRemainingBalance(int monthsPaid) {
        double monthlyInterestRate = interestRate / 12;
        return principal * Math.pow(1 + monthlyInterestRate, monthsPaid) - calculateMonthlyPayment() * (Math.pow(1 + monthlyInterestRate, monthsPaid) - 1) / monthlyInterestRate;
    }

}
