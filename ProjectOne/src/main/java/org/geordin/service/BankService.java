package org.geordin.service;

import org.geordin.dao.imp.BankDaoImp;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Employee;

import java.math.BigDecimal;
import java.sql.SQLException;
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


    public void createNewAccount(String user) throws BusinessException { //
        try{
            bankImp.applyForAccountByUsername(user);
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
            if ((amount.compareTo(BigDecimal.ZERO))<=0){    //not tested yet
                throw new BusinessException("Amount must be greater than Zero");
            }

            bankImp.depositFunds(accountNum, amount, username, password);
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







    }
