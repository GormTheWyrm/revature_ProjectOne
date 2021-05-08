package org.geordin.dao;

import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Transaction;
import org.geordin.service.BusinessException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public interface BankDao {

    Customer createNewCustomer(String username, String name, String password) throws SQLException, BusinessException;


    void applyForAccount(Customer customer) throws SQLException, BusinessException; //create account,set status to pending
    Customer findCustomerByLogin(String username, String pw) throws SQLException, BusinessException; //returns customer object when logging in with user and pw
    List<Account> findAccountsByUsername(String username) throws SQLException, BusinessException;   //used by employee to view customers... without getting pw fixme
    Vector<Account> viewPendingApplications() throws SQLException, BusinessException;
    public void approveAccount(Long accountNum) throws SQLException, BusinessException; //fixme ; next step
    public void withdrawFunds(long accountNum, BigDecimal amount, String username, String password) throws SQLException, BusinessException;
    //    public void depositFunds(Customer customer, long accountNum, double amount) throws SQLException, BusinessException;
    public void depositFunds(long accountNum, BigDecimal amount, String username, String password) throws SQLException, BusinessException;

    //fixme working^
    public void createLog(long account, BigDecimal amount, String transactionType) throws SQLException, BusinessException;
    Vector<Transaction> viewAllLogs() throws SQLException, BusinessException;

    public void viewAccountsByUsername(String username) throws SQLException, BusinessException;   //used by employee to view customers... without getting pw
    public void viewAccountsByAccountNum(long accountNum) throws SQLException, BusinessException;   //used by ...

    public void transferFunds(Customer customer, long accountNum, long accountNum2, double amount) throws SQLException, BusinessException;

}
