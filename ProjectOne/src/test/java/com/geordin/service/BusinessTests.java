package com.geordin.service;

import org.geordin.dao.imp.BankDaoImp;
import org.junit.jupiter.api.BeforeAll;


// need to mock some data...
//https://www.tutorialspoint.com/mockito/mockito_overview.htm
//better link?
//https://howtodoinjava.com/mockito/junit-mockito-example/

public class BusinessTests {

    //my bank service layer relies on
    //BankDaoImp bankImp = new BankDaoImp();
    //...how do I substitute that out?


    private static BankDaoImp dao;
    @BeforeAll
    public static void initDAO(){
        dao = new BankDaoImp(); //
    }



    //do I replace the DAO object with a mock... and ow do I swap them out?

}
