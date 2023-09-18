package com.realestate.mortgage;
import java.util.ArrayList;
import java.util.List;

public class FixedRateMortgage extends AbstractMortgage{

    public FixedRateMortgage(double principal, double interestRate, int term, String termType) {
        setPrincipal(principal);
        setInterestRate(interestRate);
        setTerm(term, termType);
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

    public double calculateRemainingPayments(int monthsPaid) {
        checkMonthValidity(monthsPaid);
        double monthlyInterestRate = interestRate / 12;
        return principal * Math.pow(1 + monthlyInterestRate, monthsPaid) - calculateMonthlyPayment() * (Math.pow(1 + monthlyInterestRate, monthsPaid) - 1) / monthlyInterestRate;
    }


    public double calculateRemainingBalance(int monthsPaid) {
        checkMonthValidity(monthsPaid);
        double remainingPrincipal = principal;
        double monthlyPayment = calculateMonthlyPayment();

        for (int i = 1; i <= monthsPaid; i++) {
            double monthlyInterest = remainingPrincipal * (interestRate / 12);
            double principalPortion = monthlyPayment - monthlyInterest;
            remainingPrincipal -= principalPortion;
        }

        return remainingPrincipal;
    }

    public List<AmortizationEntry> generateAmortizationSchedule() {
        List<AmortizationEntry> schedule = new ArrayList<>();

        double currentBalance = principal;
        double monthlyPayment = calculateMonthlyPayment();

        for (int i = 0; i < term; i++) {
            double interestForTheMonth = currentBalance * (interestRate / 12);
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

        return schedule;
    }
}
