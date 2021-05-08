package org.geordin.model;

import java.util.Objects;

public class Account {
    long accountNumber;
    double balance; //this needs to be a bigInt...
//    String userId;
    String status;
    String username;
    long approvedByEmployeeId;  //new to project 1; foreign key to employee..
    //not added to DB yet

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public long getApprovedByEmployeeId() {
        return approvedByEmployeeId;
    }

    public void setApprovedByEmployeeId(long approvedByEmployeeId) {
        this.approvedByEmployeeId = approvedByEmployeeId;
    }
//    public String getUserId() {
//        return this.userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.accountNumber);
    }
    @Override
    public String toString(){
        return ("Account Number: "+ this.accountNumber + " Balance: "+ this.balance + " Username: " + this.username + " Status: " + this.status); //may need to change this to userid?
    } //switch to getters!
    @Override
    public boolean equals(Object o) { //for now, just comparing usernames should suffice
        if (!(o instanceof Account)){ return false;}
        Account other = (Account) o;
        if (this.getAccountNumber() != other.getAccountNumber()){
            return false;
        }
        if (this.getBalance() != other.getBalance()){
            return false;
        }
        if (this.getStatus() != other.getStatus()){
            return false;
        }
        if (this.getUsername() != other.getUsername()){
            return false;
        }
        return true;
        // special thanks to https://mkyong.com/java/java-how-to-overrides-equals-and-hashcode/
    }
}
// https://docs.oracle.com/javase/8/docs/api/java/util/Hashtable.html
//adding a dateCreated would be good practice
//need to fix my equals, hashcode,ToString fixme