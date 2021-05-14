package org.geordin.dao;

import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Employee;
import org.geordin.model.Transaction;
import org.geordin.service.BusinessException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

public interface BankDao {


    public Customer createNewCustomer(String username, String name, String password) throws SQLException, BusinessException;
    public Customer findCustomerByLogin(String username, String pw) throws SQLException, BusinessException;
    public Customer findCustomerByUsername(String username) throws SQLException, BusinessException;
    public Vector<Account> findAccountsByUsername(String username) throws SQLException, BusinessException;
    public Vector<Account> viewPendingApplications() throws SQLException, BusinessException;
    public void applyForAccountByUsernamePassword(String username, String password, BigDecimal amount) throws SQLException, BusinessException;
    public Account getAccountByAccountNum(long accountNum) throws SQLException, BusinessException;
    public Vector<Account> getAccountsByUsernameOnly(String username) throws SQLException, BusinessException;
    public void withdrawFunds(long accountNum, BigDecimal amount, String username, String password) throws SQLException, BusinessException;
    public void depositFunds(long accountNum, BigDecimal amount, String username, String password) throws SQLException, BusinessException;
    public void transferFunds(String username, String password, long accountNum, long accountNum2, BigDecimal amount) throws SQLException, BusinessException;
    public void approveAccount(Long accountNum, String username, String password) throws SQLException, BusinessException;
    public void createLog(long account, BigDecimal amount, String transactionType) throws SQLException, BusinessException;
    public Employee createNewEmployee(String username, String name, String password) throws SQLException, BusinessException;
    public Employee findEmployeeByLogin(String username, String pw) throws SQLException, BusinessException;
    public Vector<Transaction> viewAllLogs() throws SQLException, BusinessException;
    public Vector<Transaction> viewLogsByDay(Timestamp time, Timestamp time2) throws SQLException, BusinessException;
    public Vector<Transaction> viewLogsByUser(String user) throws SQLException, BusinessException;

}
