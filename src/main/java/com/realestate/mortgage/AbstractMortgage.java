package com.realestate.mortgage;

public abstract class AbstractMortgage {
    protected double principal;
    protected double interestRate;
    protected double purchasePrice;
    protected double downPayment;
    protected double term;

    public int ret_val () {
        return 1;
    }

}
