package org.geordin.service;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {

    Long transactionId;
    String transactionType;
    BigDecimal Amount;
    Long accountNumber;
    Timestamp timestamp;
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return Amount;
    }

    public void setAmount(BigDecimal amount) {
        Amount = amount;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    //time stamp


    @Override
    public String toString(){
        return ("Transaction id: " + this.getTransactionId() + "Account Number: "+ this.getAccountNumber()+ "Transaction Type: "+ this.getTransactionType() + "Time of Transaction: " + this.getTimestamp());
    } //switch to getters!

}
