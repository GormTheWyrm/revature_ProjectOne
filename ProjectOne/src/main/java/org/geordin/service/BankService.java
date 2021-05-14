package org.geordin.service;

import org.geordin.dao.imp.BankDaoImp;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Employee;
import org.geordin.model.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.Vector;

public class BankService {
    BankDaoImp bankImp = new BankDaoImp();

    //need to reconfigure logging for this level

    //getReturningCustomerInfo
    //should send customer and account info to server ...
    public Customer signInOldCustomer(String user, String pw) throws BusinessException { //fixme not returning accoutns
        try{
            Customer customer = new Customer();
            customer = bankImp.findCustomerByLogin(user, pw); //if DB has that user, create it
            //log me!
            Vector<Account> accounts = bankImp.getAccountsByUsernameOnly(user);
            accounts.forEach(customer::addAccount);
            //may need to adjust exceptions for the above..
//            System.out.println(customer.getAccounts());
            return customer;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException("Could not find username or password. Please check your spelling and try again.");
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException("Username already taken.");
        }
        //fixme exceptions need to be reworked to give more useful info... how to tell which is which?
    }
    public Customer getCustomerByUser(String user) throws BusinessException { //
        try{
            Customer customer = new Customer();
            customer = bankImp.findCustomerByUsername(user); //if DB has that user, create it
            //log me!
            Vector<Account> accounts = bankImp.getAccountsByUsernameOnly(user);
            accounts.forEach(customer::addAccount);
            //may need to adjust exceptions for the above..
//            System.out.println(customer);
            return customer;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
//            throw new BusinessException("Could not find username. Please check your spelling and try again.");
            throw new BusinessException(e.getMessage());
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException("Username already taken.");
        }
        //fixme exceptions need to be reworked to give more useful info... how to tell which is which?
    }
    public Customer createNewCustomer(String user, String name, String password) throws BusinessException { //create new customer fixme
        try{
            Customer customer = new Customer();
            customer = bankImp.createNewCustomer(user, name, password); //if DB has that user, create it
            //log me!
            Vector<Account> accounts = new Vector<Account>();
            //may need to adjust exceptions for the above..
            return customer;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException("Username already exists");
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException("Error in database.");
        }
        //fixme exceptions need to be reworked to give more useful info... how to tell which is which?
    } //fixme ; change this to reflect actual methos

    public Account getAccountByNumber(long num) throws BusinessException { //
        try{
            Account account = bankImp.getAccountByAccountNum(num);
            return account;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage()); //change message...
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException(e.getMessage()); //change message...
        }
    }
    public Vector<Account> getAccountsByUser(String user) throws BusinessException { //
        try{
            Vector<Account> accounts = bankImp.getAccountsByUsernameOnly(user);
            return accounts;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage()); //change message...
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException(e.getMessage()); //change message...
        }
    }


    public void createNewAccount(String user, String password, BigDecimal amount) throws BusinessException { //
        try{
            if ((amount.compareTo(BigDecimal.ZERO))<=0){
                throw new BusinessException("Amount must be greater than Zero");
            }
            bankImp.applyForAccountByUsernamePassword(user, password, amount);
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage()); //change message...
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException(e.getMessage()); //change message...
        }
    }
    public void depositFunds(long accountNum, BigDecimal amount, String username, String password) throws BusinessException { // fixme
        try{
//            //if amount <0 abort!
            if ((amount.compareTo(BigDecimal.ZERO))<=0){
                throw new BusinessException("Amount must be greater than Zero");
            }

            bankImp.depositFunds(accountNum, amount, username, password);
//            creating log here!
//            public void createLog(long account, BigDecimal amount, String transactionType) throws SQLException, BusinessException;
            bankImp.createLog(accountNum, amount, "deposit");
            // consider error msg if create log fails but deposit goes thru... fixme
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage()); //change message...
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException(e.getMessage()); //change message...
        }
    }
    public void withdrawFunds(long accountNum, BigDecimal amount, String username, String password) throws BusinessException { // fixme
        try{
//            //if amount <0 abort!
            if ((amount.compareTo(BigDecimal.ZERO))<=0){    //not tested yet
                throw new BusinessException("Amount must be greater than Zero");
            }
            bankImp.withdrawFunds(accountNum, amount, username, password);
            bankImp.createLog(accountNum, amount, "withdrawal");
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage()); //change message...
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException(e.getMessage()); //change message...
        }
    }
    public void transferFunds(String username, String password, long accountNum1, long accountNum2, BigDecimal amount) throws BusinessException { // fixme
        try {
            System.out.println("bankservice");
            bankImp.transferFunds(username, password, accountNum1, accountNum2, amount);
            bankImp.createLog(accountNum1, amount, "deposit");
            bankImp.createLog(accountNum2, amount, "withdrawal");
            //fixme what if log fails but transaction goes thru?
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage()); //change message...
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException(e.getMessage()); //change message...
        }

    }

    //EMPLOYEE FUNCTIONS

    public Employee signInOldEmployee(String user, String pw) throws BusinessException { //fixme test with data!
        try{
            Employee employee = new Employee();
            employee = bankImp.findEmployeeByLogin(user, pw); //if DB has that user, create it
            //log me!
//            System.out.println(customer.getAccounts());
            return employee;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException("Could not find username or password. Please check your spelling and try again.");
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException("Username already taken.");
        }
        //fixme exceptions need to be reworked to give more useful info... how to tell which is which?
    }
    public Employee createNewEmployee(String user, String name, String password) throws BusinessException { //create new customer fixme
        try{
            Employee employee = new Employee();
            employee = bankImp.createNewEmployee(user, name, password); //if DB has that user, create it
            //log me!
//            System.out.println(employee);
            //may need to adjust exceptions for the above..
            return employee;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException("Username already exists");
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException("Error in database.");
        }
        //fixme exceptions need to be reworked to give more useful info... how to tell which is which?
    } //fixme ; change this to reflect actual methos



    public Vector<Transaction> getAllLogs() throws BusinessException{
        try{
            Vector<Transaction> transactions = bankImp.viewAllLogs();
            return transactions;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException("Username already exists");
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException("Error in database.");
        }
    }
    public Vector<Transaction> getLogsByDay(String time) throws BusinessException{
        try{
            //needs to come in "2021-02-11" format
            //need to turn a day into a timestamp..
            //get a day, month, year...as 1 str...
        //yyyy-mm-dd hh:mm:ss[.fffffffff]
//            String myTime = year + "-" + day + "-" + month + " 00:00:00";
                String myTime = time + " 00:00:00";
                String myTime2 = time + " 24:00:00";
            System.out.println(myTime); //fixme
            System.out.println(Timestamp.valueOf(myTime)); //fixme
            Timestamp time1 = Timestamp.valueOf(myTime);
            Timestamp time2 = Timestamp.valueOf(myTime2);
            Vector<Transaction> transactions = bankImp.viewLogsByDay(time1, time2);
            return transactions;
        }
        catch (SQLException e){
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage());
        }
        catch (BusinessException e){
            //if found someone
            throw new BusinessException("Error in database.");
        }
    }
    public Vector<Transaction> getLogsByUser(String user) throws BusinessException {
        try {
            Vector<Transaction> transactions = bankImp.viewLogsByUser(user);
            return transactions;
        } catch (SQLException e) {
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage());
        } catch (BusinessException e) {
            //if found someone
            throw new BusinessException("Error in database.");
        }
    }

    public void approveAccount(Long accountNum, String username, String password) throws BusinessException{
        //note username must be employee
        try{
            bankImp.approveAccount(accountNum, username, password);
        }
        catch (SQLException e) {
//            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
            throw new BusinessException(e.getMessage());
        } catch (BusinessException e) {
            //if found someone
            throw new BusinessException("Error in database.");
        }
        //need to put employee id in account!

    }
//    public void denyAccount(long accountNum, String employeeUser, String password){
//        try{
//            bankImp.denyAccount(accountNum, employeeUser, password);
//        }
//        catch (SQLException e) {
////            log.trace(e.getMessage()); //hopefully that logs the actual error for the developers to find
//            throw new BusinessException(e.getMessage());
//        } catch (BusinessException e) {
//            //if found someone
//            throw new BusinessException("Error in database.");
//        }
//        //need to put employee id in account!
//    }

    //get pending








    }
