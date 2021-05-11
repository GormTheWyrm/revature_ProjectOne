package org.geordin;

import io.javalin.Javalin;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.service.BankService;
import org.geordin.service.BusinessException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import org.json.JSONObject;


public class Server {

    public static void main(String[] args) {
    //create server
        Javalin app = Javalin.create(config->config.enableCorsForAllOrigins()).
                start(9000);
        //maybe remove config..
        BankService bankService = new BankService();



        //CUSTOMER ROUTES
        app.get("/customer", ctx -> {
           //return html... login...
        }); //fixme



        app.post("/api/customer/:username", ctx ->{        //login!!
            System.out.println(ctx.body());

            try{
                Customer customerLogin = ctx.bodyAsClass(Customer.class);//need this for password
                Customer customer = bankService.signInOldCustomer(customerLogin.getUsername(),customerLogin.getPassword());
//                customer.setPassword("null");
                System.out.println(customer);
                ctx.json(customer);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", e.getMessage());
                ctx.json(errorObj);
            }
        });
        app.post("/api/customers", ctx -> { //create new customer, little/no validation... working in postman!
            Customer customer = ctx.bodyAsClass(Customer.class);
            //create new customer
            try{
                bankService.createNewCustomer(customer.getUsername(), customer.getName(), customer.getPassword()); //user,name,pw - in that order
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", e.getMessage());
                ctx.json(errorObj);
            }
        });
        app.get("/api/customer/:username", ctx -> { //gets customer info and accounts, no pw. currently working
                //get customer object (including accounts list) from business layer (via dao)
                    //should validate username and password of customer against info in the header...
                        //that can be a reach goal...
//            ctx.json(customer);
            //this could be hit on any change for min coding effort and low efficiency...
            try{
                //should I remove password before returning this? probably... fixme
                Customer customer = bankService.getCustomerByUser(ctx.pathParam("username"));
                    customer.setPassword("null");
                ctx.json(customer);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", e.getMessage());
                ctx.json(errorObj);
            }

        });



    //ACCOUNTS
        app.get("/api/account/:num", ctx -> {   //get single account, currently working
            //get account info - used to see a single account
            try{
                Account account = bankService.getAccountByNumber(Long.parseLong(ctx.pathParam("num")));
                ctx.json(account);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", e.getMessage());
                ctx.json(errorObj);
            }

        });
        app.get("/api/accounts/:username", ctx -> {   //get all accounts associated with a user - working
            //dont immediately need this...
            try{
                Vector<Account> accounts = bankService.getAccountsByUser
                        (ctx.pathParam("username"));
                ctx.json(accounts);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", e.getMessage());
                ctx.json(errorObj);
            }
        });
        app.post("/api/accounts", ctx -> { //create new account, currently working
            //create a new account
            //pass in username via JSON, does not return the new account!
            try{
                Account accountObj = ctx.bodyAsClass(Account.class);   //should be null except username
                //accnum, status, userid, approvedby...
                bankService.createNewAccount(accountObj.getUsername());
                //if success... nothing returned here
                //so return a response manually
                HashMap<String, String> successObj = new HashMap<>();
                successObj.put("success", "Account added Successfully, please allow up to 24 hours for approval");
                ctx.json(successObj);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", e.getMessage());
                ctx.json(errorObj);
            }

        });


        app.delete("/api/account/:num", ctx -> {
            //deny new account
            //verify count is pending before dleeting...
        });


        // should I do a endpoint just to a approve accounts or ...
        // ~~~~~~
        //will need to make usernames conform to regex in order to prevent wierd URIs...

        //  /employee and /customer should return html!



//        app.put("/api/account/deposit/:num", ctx -> {   //deposit amount  ...perhaps amount should be in uri...
            app.put("/api/account/deposit", ctx -> {   //deposit amount, seems to work
            //need long accountNum, BigDecimal amount, String username, String password
            try{
                JSONObject jsonObj = new JSONObject(ctx.body());
                String username = jsonObj.getString("username");
                String password = jsonObj.getString("password");
                long accountNum = (jsonObj.getLong("accountNumber"));
                BigDecimal amount = jsonObj.getBigDecimal("amount");
                amount = amount.setScale(2, BigDecimal.ROUND_FLOOR);
                bankService.depositFunds(accountNum, amount, username, password);
                //if success
                HashMap<String, String> successObj = new HashMap<>();
                successObj.put("success", "$" + amount +" deposited Successfully");
                //fixme amount needs to be truncated... optimally return amount
                ctx.json(successObj);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "Error; account not updated."); //not sure what other errors would result in this
                ctx.json(errorObj);
            }
            catch (Exception e){ //catching number exceptions and type exceptions for parsing data
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "Amount not deposited: unable to read input type");
                ctx.json(errorObj);
            }
        });
        app.put("/api/account/withdrawal/", ctx -> {
            try{
                JSONObject jsonObj = new JSONObject(ctx.body());
                String username = jsonObj.getString("username");
                String password = jsonObj.getString("password");
                long accountNum = (jsonObj.getLong("accountNumber"));
                BigDecimal amount = jsonObj.getBigDecimal("amount");
                amount = amount.setScale(2, BigDecimal.ROUND_FLOOR);
                bankService.withdrawFunds(accountNum, amount, username, password);
                //if success
                HashMap<String, String> successObj = new HashMap<>();
                successObj.put("success", "$" + amount +" withdrawn Successfully");
                //fixme amount needs to be truncated... optimally return amount
                ctx.json(successObj);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "Error; account not updated."); //not sure what other errors would result in this
                ctx.json(errorObj);
            }
            catch (Exception e){ //catching number exceptions and type exceptions for parsing data
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "Amount not withdrawn: unable to read input type");
                ctx.json(errorObj);
            }
        });



        app.put("/api/account/transfer/:num", ctx -> {
            //
        });
        //may not need separate routes for withdrawal and deposit...
        app.put("/api/account/:num", ctx -> {
            //approve
        });





    //EMPLOYEES
        //new account created
        //
        app.get("/employee", ctx -> {
            //return html... login...
            //should not go here directly... but perhaps if not logged in, page shows different html, and suggests you login via button...
        });


    //TRANSACTIONS
        app.get("/transactions", ctx -> {
           //get all transactions
        });
        app.get("/transaction/day/:day", ctx -> {
            //get all transactions from a day...
        });
        app.get("/transactions/account/:acc", ctx -> {
            //get all transactions for an account
        });
        //new transactions should be sent into database when account changed...


        app.post("/customer", ctx -> {



        });

//        app.put("/customer")  //could be used to change username and pw... stretch goal




/*
still need...
 - logs
 - add list of needed things
 - mocking
 - fix logs
 - unit testing
 -...transaction routes...
 -...employee routes
 -...employee dao
 - client
 - html routes...optional?
 - delete account via deny account
 - add status codes
 - should account be singular for deposit and withdrawal? and should it use uri or body...
 */



        //GET



        //POST

        //PUT

        //patch?

        //delete?

    }

}
/*
employees;
get:


customer;
get:
    get customer details;
        - name, username, password
        - accounts!
POST:
    apply for new account
PUT:
    withdraw
    deposit
    transfer

 */