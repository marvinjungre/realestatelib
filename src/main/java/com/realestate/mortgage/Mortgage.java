package com.realestate.mortgage;

public interface Mortgage {
    // Calculates the monthly payment for the mortgage
    double calculateMonthlyPayment();

    // Calculates the total interest paid over the mortgage term
    double calculateTotalInterest();

    // Calculates the total cost of the mortgage over its term
    double calculateTotalCost();

    double calculateRemainingBalance(int monthsPaid);
}
