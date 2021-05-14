package com.geordin.service;

import org.apache.log4j.Logger;
import org.geordin.dao.imp.BankDaoImp;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Employee;
import org.geordin.model.Transaction;
import org.geordin.service.BankService;
import org.geordin.service.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)  //oh oh, i added to build path... what does that mean?
//do I want to do this? is thisright?
public class BusinessTests {
    private static Logger log=Logger.getLogger(BusinessTests.class);
    //mocking the db...
    //

    private static BankDaoImp mockDao;
    private static BankService fauxService;
    private static Account one;
    private static Account two;
    private static Account three; //pending

    private static Customer bob;
    private static Customer rob;
    private static Customer klein; //new customer...
    //2 indicates they have no account associated with them...
    private static Customer bob2;
    private static Customer rob2;
    private static Customer klein2; //new customer...

    private static Vector<Account> accounts1;
    private static Vector<Account> accounts2;
    private static Transaction transaction1;
    private static Transaction transaction2;
    private static Employee karen;

//no need for annotations for mockito... i guess

    @BeforeEach
    private void initBank() {
        mockDao = mock(BankDaoImp.class); //mockDao should now have fake methods
        fauxService = new BankService(mockDao);

        one = new Account(1L, new BigDecimal(50.23), "active", "bob", 1);
        two = new Account(2L, new BigDecimal(20.23), "active", "rob", 1);
        three = new Account(3L, new BigDecimal(10.00), "pending", "bob", 1);

        accounts1 = new Vector<>();
        accounts1.add(one);
        accounts1.add(three);
        accounts2 = new Vector<>();
        accounts2.add(two);
        transaction1 = new Transaction(); //probably need data in these...
        transaction2 = new Transaction();


        bob = new Customer(1L, "bob", "pw", "bob saget", accounts1);
        rob = new Customer(2L, "rob", "pw1", "robert downer", accounts2);
        klein = new Customer(3L, "klein", "pass", "klein stein"); //no accounts
        bob2 = new Customer(1L, "bob", "pw", "bob saget");
        rob2 = new Customer(2L, "rob", "pw1", "robert downer");
//            klein2 = new Customer(3L, "klein", "pass", "klein stein");

        karen = new Employee(1L, "karen", "pw1", "karen k");

    }
        /*
        dao methods:
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
         */

    //where to put the when/thenReturns?


    @Test
    public void testSignUpCustomer() throws BusinessException, SQLException {
        when(mockDao.createNewCustomer("bob", "bob saget", "pw")).thenReturn(bob2);
        Customer customer = fauxService.createNewCustomer("bob", "bob saget", "pw"); //this is main test
//            log.trace(bob);
//            log.trace(customer);
        Assertions.assertEquals(customer, bob2);
//            log.trace("test pass");
    }
    @Test
    public void testLoginCustomer() throws BusinessException, SQLException {
        when(mockDao.getAccountsByUsernameOnly("bob")).thenReturn(accounts1);
        when(mockDao.findCustomerByLogin("bob", "pw")).thenReturn(bob);
        Customer customer = mockDao.findCustomerByLogin("bob", "pw");
        System.out.println(customer);
        System.out.println(bob);
        Assertions.assertEquals(bob, customer);
    }

    @Test
    public void testGetCustomerByUser() throws BusinessException, SQLException {
        when(mockDao.findCustomerByUsername("bob")).thenReturn(bob);
        when(mockDao.getAccountsByUsernameOnly("bob")).thenReturn(accounts1);
        Customer customer = fauxService.createNewCustomer("bob", "bob saget", "pw"); //this is main test
//            System.out.println(bob);
//            System.out.println(customer);
        Assertions.assertEquals(bob2, customer);

    }

    @Test
    public void testGetAccNum() throws BusinessException, SQLException {
        when(mockDao.getAccountByAccountNum(1L)).thenReturn(one);
        //this is worthless...
        Account account = mockDao.getAccountByAccountNum(1L);
        System.out.println(one);
        System.out.println(account);
        Assertions.assertEquals(one, account);
    }


//getAccountsByUser(String user)
    //not worth testing

//@Test
//        public void testApproveAccount_pos() throws BusinessException, SQLException {
//            String user = "karen";
//            String pw = "pw1";
//            BigDecimal amount = new BigDecimal(50.23);
//           verify(mockDao.applyForAccountByUsernamePassword(user, pw, amount));
//}

//signin and create employee... and logs/transactions areonly usefulish methods to test...

}

