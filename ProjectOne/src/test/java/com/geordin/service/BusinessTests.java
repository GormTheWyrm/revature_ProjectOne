package com.geordin.service;

import org.geordin.dao.imp.BankDaoImp;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Employee;
import org.geordin.model.Transaction;
import org.geordin.service.BankService;
import org.geordin.service.BusinessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Vector;

import static org.mockito.Mockito.*;


public class BusinessTests {
     //mocking the db...
     //

    private static BankDaoImp mockDao;
    private static BankService fauxService;
    private static Account one;
    private static Account two;
    private static Account three; //pending
    private static Customer bob;
    private static Customer rob;
    private static Vector<Account> accounts1;
    private static Vector<Account> accounts2;
    private static Transaction transaction1;
    private static Transaction transaction2;
    private static Employee karen;


        @BeforeEach
        private static void initBank() {
            mockDao = mock(BankDaoImp.class); //mockDao is now a fake dao with real methods that return fake data
            fauxService = new BankService(mockDao);
            //need to define a mockdao method

            one = new Account(1L, new BigDecimal(50.23), "active", "bob", 1);
            two = new Account(2L, new BigDecimal(20.23), "active", "rob", 1);
            three = new Account(3L, new BigDecimal(10.00), "pending", "bob", 1);
            bob = new Customer(1L,"bob", "pw", "bob saget", accounts1 );
            rob = new Customer(2L,"rob", "pw1", "robert downer", accounts2 );
            accounts1 = new Vector<>();
            accounts1.add(one);
            accounts1.add(three);
            accounts2 = new Vector<>();
            accounts2.add(two);
            transaction1 = new Transaction(); //probably need data in these...
            transaction2 = new Transaction();
            Employee karen = new Employee(1L,"karen", "pw1", "karen k");

        }
        /*
        dao methods:
        createNewCustomer(String username, String name, String password)

         */



        @Test
    public void testSignUp() throws BusinessException {
//    when(mockDao.createNewCustomer("bob", "pw")).thenReturn(bob);
//    Customer customer = fauxService.signInOldCustomer("bob", "pw");


    }











    //my bank service layer relies on
    //BankDaoImp bankImp = new BankDaoImp();
    //...how do I substitute that out?


//    private static BankDaoImp dao;

//    public static void initDAO(){
//        dao = new BankDaoImp(); //
//    }
//    public static void initBank() throws BusinessException {
//        BankService mockBank = mock(BankService.class); //should this be static?
//        when(mockBank.signInOldCustomer("bob",  "password"))
//                .thenReturn(new Customer(1L, "bob", "password", "bobby jo", new Vector< Account >()));
//
//
//    }


    //do I replace the DAO object with a mock... and ow do I swap them out?

}
