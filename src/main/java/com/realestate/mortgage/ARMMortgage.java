package com.realestate.mortgage;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ARMMortgage extends AbstractMortgage {
    private List<RateChange> rateChanges; // List of RateChange objects
    private List<AmortizationEntry> cachedSchedule = null;

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

    public double calculateMonthlyPayment(int month) {
        checkMonthValidity(month);
        List<AmortizationEntry> schedule = generateAmortizationSchedule();
        // Since the list is 0-indexed, we access the entry using month-1.
        return schedule.get(month - 1).getPayment();
    }

    public double calculateMonthlyPayment() { // If no month is specified, use the first month
        int month = 1;
        double monthlyInterestRate = getRateForMonth(month) / 12;
        return principal * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -term));
    }

    public double calculateTotalInterest() {
        List<AmortizationEntry> schedule = generateAmortizationSchedule();
        double totalInterest = 0;
        for (AmortizationEntry entry : schedule) {
            totalInterest += entry.getInterestPaid();
        }
        return totalInterest;
    }

    public double calculateTotalCost() {
        return principal + calculateTotalInterest();
    }

    public double calculateRemainingPayments(int monthsPaid) {
        checkMonthValidity(monthsPaid);
        List<AmortizationEntry> schedule = generateAmortizationSchedule();
        double totalPaid = 0;
        for (int i = 0; i < monthsPaid; i++) {
            totalPaid += schedule.get(i).getPayment();
        }
        return calculateTotalCost() - totalPaid;
    }

    public double calculateRemainingBalance(int monthsPaid) {
        checkMonthValidity(monthsPaid);
        List<AmortizationEntry> schedule = generateAmortizationSchedule();

        return schedule.get(monthsPaid - 1).getEndingBalance();
    }

    public List<AmortizationEntry> generateAmortizationSchedule() {
        if (cachedSchedule != null) {
            return cachedSchedule;
        }
        List<AmortizationEntry> schedule = new ArrayList<>();

        double currentBalance = principal;

        for (int i = 1; i <= term; i++) {
            double monthlyInterestRate = getRateForMonth(i) / 12;
            int remainingTerm = term - i + 1; // Remaining periods, including the current month
            double interestForTheMonth = currentBalance * monthlyInterestRate;

            // Calculate the monthly payment based on the current balance and the remaining term
            double monthlyPayment = currentBalance * monthlyInterestRate / (1 - Math.pow(1 + monthlyInterestRate, -remainingTerm));

            double principalForTheMonth = monthlyPayment - interestForTheMonth;
            double endingBalance = currentBalance - principalForTheMonth;

            AmortizationEntry entry = new AmortizationEntry();
            entry.setBeginningBalance(currentBalance);
            entry.setPayment(monthlyPayment);
            entry.setInterestPaid(interestForTheMonth);
            entry.setPrincipalPaid(principalForTheMonth);
            entry.setEndingBalance(endingBalance);

            schedule.add(entry);

            currentBalance = endingBalance;
        }
        cachedSchedule = schedule;
        return schedule;
    }


    //------------------------------------- Helper classes/ methods -------------------------------------//

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