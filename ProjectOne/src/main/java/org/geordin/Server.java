package org.geordin;

import io.javalin.Javalin;
import org.geordin.dao.imp.BankDaoImp;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Employee;
import org.geordin.model.Transaction;
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
//            System.out.println(ctx.body());

            try{
                Customer customerLogin = ctx.bodyAsClass(Customer.class);//need this for password
                Customer customer = bankService.signInOldCustomer(customerLogin.getUsername(),customerLogin.getPassword());
//                customer.setPassword("null");
//                System.out.println(customer);
                ctx.json(customer);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "user or password not found");
                ctx.json(errorObj);
            }
        });
        app.post("/api/customers", ctx -> { //create new customer,  working in postman!
            Customer customerObj = ctx.bodyAsClass(Customer.class);
            //create new customer
            try{
                Customer customer = bankService.createNewCustomer(customerObj.getUsername(), customerObj.getName(), customerObj.getPassword()); //user,name,pw - in that order
                ctx.json(customer);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "failed to create customer");
                ctx.json(errorObj);
            }
        });
        app.get("/api/customer/:username", ctx -> { //gets customer info and accounts, no pw. currently working
            //this could be hit on any change for min coding effort and low efficiency...
            try{
//                System.out.println("hit ");
                Customer customer = bankService.getCustomerByUser(ctx.pathParam("username"));
                    customer.setPassword("null"); //removing password so its not as insecure
                ctx.json(customer);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "failed to get user info");
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
                errorObj.put("error", "no accounts found");
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
                errorObj.put("error", "no accounts found by that username");
                ctx.json(errorObj);
            }
        });



        app.delete("/api/account/:num", ctx -> {
            //deny new account
            //verify count is pending before deleting...
            try{
                bankService.denyAccount(Long.parseLong(ctx.pathParam("num")));
                HashMap<String, String> successObj = new HashMap<>();
                successObj.put("success", "successfully deleted account");
                ctx.json(successObj);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "failed to delete account");
                ctx.json(errorObj);
            }



        });


        // should I do a endpoint just to a approve accounts or ...
        // ~~~~~~
        //will need to make usernames conform to regex in order to prevent wierd URIs...

        //  /employee and /customer should return html!



//        app.put("/api/account/deposit/:num", ctx -> {   //deposit amount  ...perhaps amount should be in uri...
            app.put("/api/accounts/deposit", ctx -> {   //deposit amount, seems to work
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
        app.put("/api/accounts/withdraw/", ctx -> {
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
        app.put("/api/accounts/transfer", ctx -> {   //fixme WIP;
            //fixme need to implement rollback if withdrawal does not go through...
            // only within single customers accounts... good or bad?
            try{
                JSONObject jsonObj = new JSONObject(ctx.body());
                String username = jsonObj.getString("username");
                String password = jsonObj.getString("password");
                long accountNum1 = (jsonObj.getLong("accountNumberFrom"));
                long accountNum2 = (jsonObj.getLong("accountNumberTo"));
                BigDecimal amount = jsonObj.getBigDecimal("amount");
                amount = amount.setScale(2, BigDecimal.ROUND_FLOOR);
                //(String username, String password, long accountNum1, long accountNum2, BigDecimal amount)
//                System.out.println(amount);
                bankService.transferFunds(username, password, accountNum1, accountNum2, amount);
//                System.out.println(amount);
                //if success
                HashMap<String, String> successObj = new HashMap<>();
                successObj.put("success", "$" + amount +" transferedSuccessfully");
                ctx.json(successObj);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "transfer failed!"); //not sure what other errors would result in this
                ctx.json(errorObj);
            }
            catch (Exception e){ //catching number exceptions and type exceptions for parsing data
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "Amount not withdrawn: unable to read input type");
                ctx.json(errorObj);
            }
        });
        //may not need separate routes for withdrawal and deposit...
        app.put("/api/accounts/approve", ctx -> {
            try{
                JSONObject jsonObj = new JSONObject(ctx.body());
                String username = jsonObj.getString("username");
                String password = jsonObj.getString("password");
                long accountNum = (jsonObj.getLong("accountNumber"));
                // accnum, amount, user, pw
                bankService.approveAccount(accountNum, username, password);
                //if no exception, return successObject
                HashMap<String, String> successObj = new HashMap<>();
                successObj.put("success", "account approved");
                ctx.json(successObj);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "failed to approve account"); //not sure what other errors would result in this
                ctx.json(errorObj);
            }
            catch (Exception e){ //catching number exceptions and type exceptions for parsing data
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "error: wrong variable types");
                ctx.json(errorObj);
            }
        });
        app.post("/api/accounts", ctx -> {   //create-apply for new account!
            //need long accountNum, BigDecimal amount, String username, String password
            try{
                JSONObject jsonObj = new JSONObject(ctx.body());
                String username = jsonObj.getString("username");
                String password = jsonObj.getString("password");
                BigDecimal amount = jsonObj.getBigDecimal("amount");
                amount = amount.setScale(2, BigDecimal.ROUND_FLOOR);
//                bankService.depositFunds(accountNum, amount, username, password);
                bankService.createNewAccount(username, password, amount);
                //if success

                HashMap<String, String> successObj = new HashMap<>();
                successObj.put("success", "$" + amount +" deposited Successfully");
                //fixme amount needs to be truncated... optimally return amount
                ctx.json(successObj);
            }
            catch (BusinessException e){
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "Error; account not created."); //not sure what other errors would result in this
                ctx.json(errorObj);
            }
            catch (Exception e){ //catching number exceptions and type exceptions for parsing data
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "Amount not created: unable to read input type");
                ctx.json(errorObj);
            }
        });
        app.get("/api/accounts/pending", ctx ->{

            //
        });




    //EMPLOYEES
        //new account created
        //
        app.get("/employee", ctx -> {
            //return html... login...
        });



        app.post("/api/employee/:username", ctx ->{        //login!! fixme test this with data!
//            System.out.println(ctx.body());
            try{
                Employee employeeLogin = ctx.bodyAsClass(Employee.class);//need this for password
                Employee employee = bankService.signInOldEmployee(employeeLogin.getUsername(),employeeLogin.getPassword());
//                customer.setPassword("null");
//                System.out.println(employee);
                ctx.json(employee);
                //fixme not utilizing route as variable, should I use it to get username?
                //can lead to error where path doesnt matter but post request does... implying I should just change it to /employee/login
                //employee/login vs /employee/new would be easier...
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "no password or username found");
                ctx.json(errorObj);
            }
        });
        app.post("/api/employees", ctx -> { //create new employee fixme test with data
            Employee employee = ctx.bodyAsClass(Employee.class);
            //create new customer
            try{
                employee = bankService.createNewEmployee(employee.getUsername(), employee.getName(), employee.getPassword()); //user,name,pw - in that order
           ctx.json(employee);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "failure to create employee");
                ctx.json(errorObj);
            }
        });




    //TRANSACTIONS
        app.get("/api/transactions", ctx -> {
           //get all transactions
            try{
                //need to pass logs into here...
                Vector<Transaction> transactions = bankService.getAllLogs();
                ctx.json(transactions);
                //does not get size of vector!
                HashMap <String, Object >successObj = new HashMap<>();
                successObj.put("length", transactions.size());
                successObj.put("data", transactions);
                ctx.json(successObj);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "no transactions found");
                ctx.json(errorObj);
            }
        });
        app.get("/api/transactions/day/:day", ctx -> {
            //get all transactions from a day...
            //needs to come in "2021-02-11" format
            try{
                //need to pass logs into here...
                Vector<Transaction> transactions = bankService.getLogsByDay(ctx.pathParam("day"));
                ctx.json(transactions);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "no transactions found on that day");
                ctx.json(errorObj);
            }
        });
        app.get("/api/transactions/user/:user", ctx -> {
            //get all transactions for an account
            try{
                //need to pass logs into here...
                Vector<Transaction> transactions = bankService.getLogsByUser(ctx.pathParam("user"));
                HashMap <String, Object >successObj = new HashMap<>();
                successObj.put("length", transactions.size());
                successObj.put("data", transactions);
                ctx.json(successObj);
            }
            catch (BusinessException e){
                //create error object for javascript
                HashMap<String, String> errorObj = new HashMap<>();
                errorObj.put("error", "no transactions found for that user");
                ctx.json(errorObj);
            }
        });
        //new transactions should be sent into database when account changed...
        //app.get("/transactions/account/:num", ctx -> {
//        app.get("/transactions/id/:id", ctx -> {
//        by transaction id, type,customerid,accountno,date
//        not a priority yet



//        app.put("/customer")  //could be used to change username and pw... stretch goal


//* As an employee, I can approve or reject an account.
//	* 2 points
//* As an employee, I can view a customer's bank accounts.
//	* 1 point
//* As a user, I can register for a customer account.
//	* 3 points
//* As a customer, I can post a money transfer to another account.
//	* 3 points
//* As a customer, I can accept a money transfer from another account.
//	* 2 points
//* As an employee, I can view a log of all transactions.
//	* 2 points

        /*
        an amployee can
            - approve account
            - see all accounts
            - see logs...
            - deny account
            - see account info
            - see customer info...
         */

/*
do next
0. fix approve and apply methods - approve currently has the amount and apply does not
    - how to show the amount?
    - just have amount be in the account but status of pending!?
 1. approve account
 2. option to see who approved account
 3. frontend...
 4. mockito
 5. unit tests
 6. logs
 7. aws
 8. present
 - refactor uri endpoints...

//need to fix approved by

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
 ...figure out money transfer - post, accept, and rollback
 - should logs say transfer, deposit/withdraw, or transfer-deposit, etc?
 */


    }

}