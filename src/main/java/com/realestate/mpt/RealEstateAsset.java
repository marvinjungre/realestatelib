package com.realestate.mpt;
import java.util.Collections;
import java.util.List;

/* Note that we can use this to represent a real estate asset, but we can also use it to represent real estate markets,
portfolios, or anything really. If we want to evaluate real estate markets we can e.g. use median house prices */
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
        this.propertyId = propertyId;
        setAnnualRentalIncome(annualRentalIncome);
        setAnnualAppreciation(annualAppreciation);
        setAnnualExpenses(annualExpenses);
        setInitialPropertyValue(initialPropertyValue);
        setHistoricalReturns(historicalReturns);
        computeExpectedReturn();
    }

    public RealEstateAsset(String propertyId, double expectedReturn, List<Double> historicalReturns) {
        this.propertyId = propertyId;
        this.expectedReturn = expectedReturn;
        setHistoricalReturns(historicalReturns);
    }

    public RealEstateAsset(double expectedReturn, List<Double> historicalReturns){
        this("", expectedReturn, historicalReturns);
    }

    public RealEstateAsset(double annualRentalIncome,
                           double annualAppreciation, double annualExpenses,
                           double initialPropertyValue, List<Double> historicalReturns) {
        this("", annualRentalIncome, annualAppreciation, annualExpenses, initialPropertyValue, historicalReturns);
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

    // Setters with appropriate validation.

    /* Technically, it would be ideal if IDs exist and are unique, but this is not needed
    for every function, so we will not enforce it here. You won't get the full value out of
    the recommendations if you don't include property IDs.*/
    public void setPropertyId(String propertyId) {
        /*if (propertyId == null || propertyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Property ID cannot be null or empty.");
        }*/
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

    public double getHistoricalVolatility() {
        if (historicalReturns == null || historicalReturns.isEmpty()) {
            throw new IllegalStateException("Historical returns are not set.");
        }

        int size = historicalReturns.size();
        double mean = 0.0;
        for (double returnVal : historicalReturns) {
            mean += returnVal;
        }
        mean /= size;

        double variance = 0.0;
        for (double returnVal : historicalReturns) {
            variance += (returnVal - mean) * (returnVal - mean);
        }
        variance /= size;

        return Math.sqrt(variance);
    }


    @Override
    public String toString() {
        return "RealEstateAsset { " +
                "Property ID: '" + propertyId + '\'' +
                ", Expected Return: " + expectedReturn +
                ", Historical Returns: " + historicalReturns +
                " }";
    }
}
