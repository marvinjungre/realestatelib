# realestatelib

(Updated Sep 18 2023)

!!! Check out Tests to better understand the usage of objects. !!!

A library designed for real estate-related computations. Key components:
-------------------------------------------------
Interest Package:

Object: Interest
Features:
Static methods tailored for both simple and compound interest calculations.
-------------------------------------------
Mortgage Package:

Base Class: AbstractMortgage
Methods:
- calculateMonthlyPayment(): Determine the monthly payment amount.
- calculateTotalInterest(): Compute the total interest over the mortgage term.
- calculateTotalCost(): Evaluate the overall cost of the mortgage throughout its term.
- calculateRemainingBalance(int monthsPaid): Find the remaining balance after a specified number of payments.
- calculateRemainingPayments(int monthsPaid): Estimate remaining payments after a specified duration.
- generateAmortizationSchedule(): Generate a detailed amortization schedule.
Derived Classes:
- FixedRateMortgage
- ARMMortgage (Adjustable Rate Mortgage)

-------------------------------------  
MPT Package: Designed for computations related to Modern Portfolio Theory.

Class MPT:
This class represents a portfolio based on the principles of Modern Portfolio Theory (MPT). It consists of a collection of real estate assets (RealEstateAsset objects) and their respective weights in the portfolio.

Attributes:

- assetsWeights: A map that associates each RealEstateAsset with its weight in the portfolio. The weight is the fraction of the portfolio's total value that is attributed to this particular asset.
- cleanHistory: A flag that indicates if the historical returns of all assets in the portfolio have been adjusted to have the same length.
- cleanMethod: A string indicating the method used to adjust the historical returns (either "i" for interpolation or "c" to cut to the shortest history).
- 
Constructors:

Default constructor and constructors accepting Map or List inputs, each of which comes in two variations: with and without specifying the cleanMethod. This allows for flexibility in constructing the portfolio.

Methods:

- addAsset: Adds a new asset to the portfolio with its associated weight.
- portfolioExpectedReturn: Calculates the expected return of the portfolio based on the expected returns of its assets and their weights.
- historyCleaner: Ensures that the historical returns of all assets in the portfolio have the same length. If not, the cleanMethod is used to adjust them.
- portfolioVolatility: Computes the portfolio's volatility based on the covariance matrix of its assets. This utilizes matrix multiplication operations provided by the Apache Commons Math library.
- setClean: Setter for the cleanMethod attribute, which ensures valid input.

---
RealEstateAsset Class:

Represents an individual real estate property, detailing its financial performance and parameters essential for investment analysis.

Attributes:

- propertyId: Unique identifier for the property.
- expectedReturn: Calculated rate of return on the property, derived from rental income, appreciation, expenses, and its initial value.
- historicalReturns: List of past returns (e.g., monthly rental income) for the asset.
- annualRentalIncome: Yearly income generated from renting out the property.
- annualAppreciation: Yearly increase in property value.
- annualExpenses: Yearly expenditures associated with the property (maintenance, taxes, etc.).
- initialPropertyValue: The property's initial acquisition or valuation cost.
  
Constructors:

Main Constructor: Accepts all attributes. Computes the expected return upon initialization.
Secondary Constructors: Allows for instantiating the object with varied combinations of attributes, catering to different use-cases.
Private Methods:

computeExpectedReturn(): Determines the property's expected return based on rental income, appreciation, expenses, and initial value.
Getters:

Methods to retrieve the values of the class attributes. getHistoricalReturns() provides an unmodifiable list to ensure data integrity.
Setters:

Methods to update the class attributes. Incorporate validation checks to ensure data integrity (e.g., non-negative values for income and appreciation). Some setters trigger the re-computation of the expected return.
Overrides:

toString(): Provides a string representation of the RealEstateAsset object, detailing the property ID, expected return, and historical returns.
---
PortfolioUtils Class:

Provides utility functions essential for managing a portfolio of RealEstateAsset objects. These functions include data resampling, validation, historical data extraction, and covariance matrix calculation.

Methods:

Data Extraction:

- extractAssetsFromMap(Map<RealEstateAsset, Double> assetsMap): Returns a list of RealEstateAsset objects from a given map.
- extractWeightsFromMap(Map<RealEstateAsset, Double> assetsMap): Returns a list of weights (double values) associated with each asset.
- extractWeightsFromMapAsArray(Map<RealEstateAsset, Double> assetsMap): Same as above but returns the weights as a primitive double array.
Validation:

- validateHistoricalReturnsLength(Map<RealEstateAsset, Double> assetsMap): Validates if all assets in the given map have historical returns of the same length.
- validateHistoricalReturnsLength(List<RealEstateAsset> assets): Overloaded version that takes in a list of assets and checks for consistency in historical returns' length.
Weight Computation:

- helpComputeWeights(List<Double> assetValues): Helps compute relative weights of provided asset values.
  
Historical Data Resampling:

- interpolateHistories(Map<RealEstateAsset, Double> assetsWeights): Uses interpolation to resample the historical returns of assets so that they all have the same length, equal to the maximum history length present.
- cutHistoriesToShortest(Map<RealEstateAsset, Double> assetsWeights): Trims the historical returns of all assets to match the length of the shortest one.
- historyCleaning(Map<RealEstateAsset, Double> assetsWeights, String cleanMethod): Chooses the appropriate resampling technique based on the provided method ("i" for interpolation and "c" for cutting to the shortest).
Matrix Computation:

- computeCovarianceMatrix(List<RealEstateAsset> assets): Computes and returns the covariance matrix for a given list of RealEstateAsset objects. This matrix is crucial for risk assessment and portfolio optimization.

