package com.realestate.mpt;
import java.util.Collections;
import java.util.List;

public class RealEstateAsset {

    private String propertyId;
    private double expectedReturn;
    private List<Double> historicalReturns;
    private double annualRentalIncome;
    private double annualAppreciation;
    private double annualExpenses;
    private double initialPropertyValue;

    // Constructors
    public RealEstateAsset(String propertyId, double annualRentalIncome,
                           double annualAppreciation, double annualExpenses,
                           double initialPropertyValue, List<Double> historicalReturns) {
        setPropertyId(propertyId);
        setAnnualRentalIncome(annualRentalIncome);
        setAnnualAppreciation(annualAppreciation);
        setAnnualExpenses(annualExpenses);
        setInitialPropertyValue(initialPropertyValue);
        setHistoricalReturns(historicalReturns);
        computeExpectedReturn();
    }

    public RealEstateAsset(String propertyId, double expectedReturn, List<Double> historicalReturns) {
        setPropertyId(propertyId);
        this.expectedReturn = expectedReturn;
        setHistoricalReturns(historicalReturns);
    }

    // Compute expected return based on provided parameters
    private void computeExpectedReturn() {
        this.expectedReturn = (annualRentalIncome + annualAppreciation - annualExpenses) / initialPropertyValue;
    }

    // Getters
    public String getPropertyId() {
        return propertyId;
    }

    public double getExpectedReturn() {
        return expectedReturn;
    }

    public List<Double> getHistoricalReturns() {
        return Collections.unmodifiableList(historicalReturns);
    }

    public double getAnnualRentalIncome() {
        return annualRentalIncome;
    }

    public double getAnnualAppreciation() {
        return annualAppreciation;
    }

    public double getAnnualExpenses() {
        return annualExpenses;
    }

    public double getInitialPropertyValue() {
        return initialPropertyValue;
    }

    // Setters with appropriate validation
    public void setPropertyId(String propertyId) {
        if (propertyId == null || propertyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Property ID cannot be null or empty.");
        }
        this.propertyId = propertyId;
    }

    public void setAnnualRentalIncome(double annualRentalIncome) {
        if (annualRentalIncome < 0) {
            throw new IllegalArgumentException("Annual rental income cannot be negative.");
        }
        this.annualRentalIncome = annualRentalIncome;
        computeExpectedReturn();
    }

    public void setAnnualAppreciation(double annualAppreciation) {
        if (annualAppreciation < 0) {
            throw new IllegalArgumentException("Annual appreciation cannot be negative.");
        }
        this.annualAppreciation = annualAppreciation;
        computeExpectedReturn();
    }

    public void setAnnualExpenses(double annualExpenses) {
        if (annualExpenses < 0) {
            throw new IllegalArgumentException("Annual expenses cannot be negative.");
        }
        this.annualExpenses = annualExpenses;
        computeExpectedReturn();
    }

    public void setInitialPropertyValue(double initialPropertyValue) {
        if (initialPropertyValue <= 0) {
            throw new IllegalArgumentException("Initial property value must be greater than 0.");
        }
        this.initialPropertyValue = initialPropertyValue;
        computeExpectedReturn();
    }

    public void setHistoricalReturns(List<Double> historicalReturns) {
        if (historicalReturns == null || historicalReturns.isEmpty()) {
            throw new IllegalArgumentException("Historical returns cannot be null or empty.");
        }
        this.historicalReturns = historicalReturns;
    }

    @Override
    public String toString() {
        return "RealEstateAsset { " +
                "Property ID: '" + propertyId + '\'' +
                ", Expected Return: " + expectedReturn +
                " }";
    }
}
