package org.geordin.service;

import org.geordin.dao.imp.BankDaoImp;
import org.geordin.model.Account;
import org.geordin.model.Customer;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;

public class BankService {
    BankDaoImp bankImp = new BankDaoImp();

    //need to reconfigure logging for this level

    //getReturningCustomerInfo
    //should send customer and account info to server ...
    public Customer signInOldCustomer(String user, String pw) throws BusinessException { //
        try{
            Customer customer = new Customer();
            customer = bankImp.findCustomerByLogin(user, pw); //if DB has that user, create it
            //log me!
            Vector<Account> accounts = new Vector<Account>();
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
    public Customer getCustomerByUser(String user) throws BusinessException { //
        try{
            Customer customer = new Customer();
            customer = bankImp.findCustomerByUsername(user); //if DB has that user, create it
            //log me!
            Vector<Account> accounts = new Vector<Account>();
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
    public Account getAccountByNumber(long num) throws BusinessException { //
        try{
            Account account = bankImp.viewAccountByAccountNum(num);
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



}
