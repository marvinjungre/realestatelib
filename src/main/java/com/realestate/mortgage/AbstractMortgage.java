package com.realestate.mortgage;

import java.util.List;

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

    // Checks if the input month is valid
    public void checkMonthValidity(int month){
        if (month <= 0) {
            throw new IllegalArgumentException("Month cannot be negative or zero.");
        }
        if (month > this.getTerm()) {
            throw new IllegalArgumentException("Month cannot exceed term time.");
        }
    }

    // Calculates the monthly payment for the mortgage
    public abstract double calculateMonthlyPayment();

    // Calculates the total interest paid over the mortgage term
    public abstract double calculateTotalInterest();

    // Calculates the total cost of the mortgage over its term
    public abstract double calculateTotalCost();

    public abstract double calculateRemainingBalance(int monthsPaid);

    public abstract double calculateRemainingPayments(int monthsPaid);

    public abstract List<AmortizationEntry> generateAmortizationSchedule();

    public static class AmortizationEntry {
        private double beginningBalance;
        private double payment;
        private double interestPaid;
        private double principalPaid;
        private double endingBalance;

        // Default constructor
        public AmortizationEntry() {}

        // All-args constructor
        public AmortizationEntry(double beginningBalance, double payment, double interestPaid, double principalPaid, double endingBalance) {
            this.beginningBalance = beginningBalance;
            this.payment = payment;
            this.interestPaid = interestPaid;
            this.principalPaid = principalPaid;
            this.endingBalance = endingBalance;
        }

        // Getters and Setters
        public double getBeginningBalance() {
            return beginningBalance;
        }

        public void setBeginningBalance(double beginningBalance) {
            this.beginningBalance = beginningBalance;
        }

        public double getPayment() {
            return payment;
        }

        public void setPayment(double payment) {
            this.payment = payment;
        }

        public double getInterestPaid() {
            return interestPaid;
        }

        public void setInterestPaid(double interestPaid) {
            this.interestPaid = interestPaid;
        }

        public double getPrincipalPaid() {
            return principalPaid;
        }

        public void setPrincipalPaid(double principalPaid) {
            this.principalPaid = principalPaid;
        }

        public double getEndingBalance() {
            return endingBalance;
        }

        public void setEndingBalance(double endingBalance) {
            this.endingBalance = endingBalance;
        }
    }

    public void printAmortizationSchedule() {
        List<AmortizationEntry> schedule = generateAmortizationSchedule();

        System.out.println("Month | Beginning Balance | Payment | Interest Paid | Principal Paid | Ending Balance");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int i = 0; i < schedule.size(); i++) {
            AmortizationEntry entry = schedule.get(i);
            System.out.printf("%5d | %17.2f | %7.2f | %13.2f | %14.2f | %13.2f\n",
                    i+1,
                    entry.getBeginningBalance(),
                    entry.getPayment(),
                    entry.getInterestPaid(),
                    entry.getPrincipalPaid(),
                    entry.getEndingBalance()
            );
        }
    }
}
