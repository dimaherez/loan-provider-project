package com.nulp.loan;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Loan {
    protected int id;
    protected String bank;
    protected String purpose;
    protected int numberOfYears;
    protected double annualInterestRate;
    protected double amount;
    protected boolean earlyPaymentAvailable;
    protected boolean expandLoanLimitAvailable;

    public Loan(int id, String bank, String purpose, int numberOfYears, double annualInterestRate, double amount, boolean earlyPaymentAvailable, boolean expandLoanLimitAvailable) {
        this.id = id;
        this.bank = bank;
        this.purpose = purpose;
        this.numberOfYears = numberOfYears;
        this.annualInterestRate = annualInterestRate;
        this.amount = amount;
        this.earlyPaymentAvailable = earlyPaymentAvailable;
        this.expandLoanLimitAvailable = expandLoanLimitAvailable;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getNumberOfYears() {
        return numberOfYears;
    }

    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isEarlyPaymentAvailable() {
        return earlyPaymentAvailable;
    }

    public void setEarlyPaymentAvailable(boolean earlyPaymentAvailable) {
        this.earlyPaymentAvailable = earlyPaymentAvailable;
    }

    public boolean isExpandLoanLimitAvailable() {
        return expandLoanLimitAvailable;
    }

    public void setExpandLoanLimitAvailable(boolean expandLoanLimitAvailable) {
        this.expandLoanLimitAvailable = expandLoanLimitAvailable;
    }

    public void expandLoanLimit(double newLoanLimit){
        this.amount = newLoanLimit;
    }

    // Get monthly payment
    public double getMonthlyPayment() {
        double monthlyInterestRate = annualInterestRate / 1200;
        return round(amount * monthlyInterestRate / (1 -
                (1 / Math.pow(1 + monthlyInterestRate, numberOfYears * 12))));
    }
    // Get monthly payment
    public double getMonthlyPayment(double amount) {
        double monthlyInterestRate = annualInterestRate / 1200;
        return round(amount * monthlyInterestRate / (1 -
                (1 / Math.pow(1 + monthlyInterestRate, numberOfYears * 12))));
    }

    // Get total payment
    public double getTotalPayment() {
        return round(getMonthlyPayment() * numberOfYears * 12);
    }
    // Get total payment
    public double getTotalPayment(double amount) {
        return round(getMonthlyPayment(amount) * numberOfYears * 12);
    }

    // Get total amount for early payment
    public double getTotalEarlyPayment(int years) {
        if (years < this.numberOfYears && years > 1){
            return round(getMonthlyPayment() * years * 12);
        }
        return 0;
    }

    private static double round(double value) {
        int places = 2;

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
