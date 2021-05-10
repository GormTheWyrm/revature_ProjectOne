package org.geordin;

import io.javalin.Javalin;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.service.BankService;
import org.geordin.service.BusinessException;

import java.util.HashMap;
import java.util.Vector;

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
            //should not go here directly... but perhaps if not logged in, page shows different html, and suggests you login via button...

        });
        app.post("/api/customer/:username", ctx ->{        //login!!
           //return info for customer only if password and username match...
            //could use a splat to get username and password
            
            Customer customer = ctx.bodyAsClass(Customer.class);//
            System.out.println(customer.getUsername());
//
//            System.out.println(ctx.body());
//            System.out.println(ctx.formParam("username"));
//            System.out.println(ctx.formParam("password"));
//            System.out.println(ctx.queryParam("password"));
//            Customer customer = ctx.bodyAsClass(Customer.class);
            //send to business layer to be verified...
//            ctx.json(customer);
            //testing to see what happens if bad object sent in- error?

            /*
             Employee employee = ctx.bodyAsClass(Employee.class);
            employee = service.createEmployee(employee);
            ctx.json(employee);
            ~~~
            app.before(ctx -> ctx.register(MyMapper.class, new MyMapper(ctx, otherDependency));
             */

        });
        //post to /api/customers to create new customer
        app.get("/api/customer/:username", ctx -> { //currently working
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



        app.post("/api/accounts", ctx -> {
            //create
        });


        app.delete("/api/account/:num", ctx -> {
            //deny new account
            //verify count is pending before dleeting...
        });


        // should I do a endpoint just to a approve accounts or ...
        // ~~~~~~
        //will need to make usernames conform to regex in order to prevent wierd URIs...

        //  /employee and /customer should return html!



        app.put("/api/account/deposit/:num", ctx -> {
            //
        });
        app.put("/api/account/withdrawal/:num", ctx -> {
            //
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
        app.get("/transctions", ctx -> {
           //get all transactions
        });
        app.get("/transction/day/:day", ctx -> {
            //get all transactions from a day...
        });
        app.get("/transctions/account/:acc", ctx -> {
            //get all transactions for an account
        });
        //new transactions should be sent into database when account changed...


        app.post("/customer", ctx -> {



        });

//        app.put("/customer")  //could be used to change username and pw... stretch goal








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