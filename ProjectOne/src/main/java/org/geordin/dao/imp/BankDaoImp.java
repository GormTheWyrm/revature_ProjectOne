package org.geordin.dao.imp;

import org.apache.log4j.Logger;
import org.geordin.dao.BankDao;
import org.geordin.dbutil.PostgresConnection;
import org.geordin.model.Account;
import org.geordin.model.Customer;
import org.geordin.model.Employee;
import org.geordin.model.Transaction;
import org.geordin.service.BusinessException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;

public class BankDaoImp implements BankDao {
//need to implement BankDao!!!


// need to fix logger!


    private static Logger log=Logger.getLogger(BankDaoImp.class);
    //CUSTOMERS
    public Customer createNewCustomer(String username, String name, String password) throws SQLException, BusinessException {
        //creates new customer if password, name and username exist in database
        Customer customer = new Customer();
        //database connection
        Connection connection = PostgresConnection.getConnection();
        String sql = "INSERT INTO gormbank.customers\n" +
                "(username, \"name\", \"password\")\n" +
                "VALUES(?, ?, ?);\n";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, username);    //variables sent into DB
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, password);
        int c = preparedStatement.executeUpdate();
        log.trace("dao: Inserted "+ c + " customers");
        if (c == 1) {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) { //this is needed to get a response
                customer.setId(resultSet.getLong(1));
                customer.setUsername(resultSet.getString(2)); //index 1 should be userid
                customer.setName(resultSet.getString(3));
                customer.setPassword(resultSet.getString(4));
//                log.trace(resultSet.getString(2)+ " added to user database");
                log.trace("customer creaded: "+customer.getUsername());
            }
        } else {
            throw new BusinessException("Failure in registration... Please retry.....");
        }
        return customer; //this returns a customer even if it doesnt work...?
    }
    public Customer findCustomerByLogin(String username, String pw) throws SQLException, BusinessException {
        Customer customer = new Customer();
        //step 2 connection
        Connection connection = PostgresConnection.getConnection();
        //Step 3- Create Statement
        String sql = "SELECT username, name, password, userid from gormbank.customers WHERE username = ? AND password = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql); //2nd par makes keys returnable...
        preparedStatement.setString(1, username);    //variables sent into DB
        preparedStatement.setString(2, pw);
        ResultSet resultSet = preparedStatement.executeQuery();
        log.trace("DAO-findCustomerByLogin");
        if (resultSet.next()) {
            customer.setUsername(resultSet.getString("username"));
            customer.setName(resultSet.getString("name"));
            customer.setPassword(resultSet.getString("password"));
            customer.setId(resultSet.getLong("userid"));
            log.trace("DAO loginOldCustomer: " + customer.getUsername());
        }
        else {
            throw new BusinessException("No User Found");
        } //if no results, throw exception
        return customer;
    }
    public Customer findCustomerByUsername(String username) throws SQLException, BusinessException {
        Customer customer = new Customer();
        //step 2 connection
        Connection connection = PostgresConnection.getConnection();
        //Step 3- Create Statement
        String sql = "SELECT username, name, password, userid from gormbank.customers WHERE username = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql); //2nd par makes keys returnable...
        preparedStatement.setString(1, username);    //variables sent into DB

        //Step 4 - Execute Query
        ResultSet resultSet = preparedStatement.executeQuery();
        log.trace("DAO-findCustomerByusername");

        if (resultSet.next()) {
            customer.setUsername(resultSet.getString("username"));
            customer.setName(resultSet.getString("name"));
            customer.setPassword(resultSet.getString("password"));
            customer.setId(resultSet.getLong("userid")); //userId should never leave the backend/service layers
            log.trace("DAO findCustomerByUsername: " + customer.getUsername());
        }
        else {
            throw new BusinessException("No User Found");
        } //if no results, throw exception
        return customer;
    }

    //ACCOUNTS
    public Vector<Account> findAccountsByUsername(String username) throws SQLException, BusinessException {   //used by employee and customer to view employees...
        //old function...
        Connection connection = PostgresConnection.getConnection();
        String sql = "select customers.userid, customers.username, customers.name, " +
                "accounts.account_number, accounts.balance, accounts.status " +
                "from gormbank.customers RIGHT join gormbank.accounts on accounts.userid = customers.userid " +
                "WHERE username = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);    //variables sent into sql/preparedStatement
        ResultSet resultSet = preparedStatement.executeQuery();
//        System.out.println("Query executed - replace with trace");
        Vector<Account> accounts = new Vector<>();
log.trace("dao-findAccountsByUsername: "+ username);
        while (resultSet.next()) {
            Account account = new Account();
            account.setAccountNumber(resultSet.getLong("account_number"));
            account.setBalance(resultSet.getBigDecimal("balance"));
            account.setUsername(resultSet.getString("username"));
            account.setStatus(resultSet.getString("status"));
            accounts.add(account);
            //needs account approved by
        }
        return accounts;
    }

    public Vector<Account> viewPendingApplications() throws SQLException, BusinessException {
        Connection connection = PostgresConnection.getConnection();
        String sql = "select customers.userid, customers.username, customers.name, approved_by, " +
                "accounts.account_number, accounts.balance, accounts.status " +
                "from gormbank.customers RIGHT join gormbank.accounts on accounts.userid = customers.userid " +
                "WHERE status = 'pending';";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        log.trace("DAO viewPendingApplications");
        Vector<Account> accounts = new Vector<>();
        while (resultSet.next()) {
            Account account = new Account();
            account.setAccountNumber(resultSet.getLong("account_number"));
            account.setBalance(resultSet.getBigDecimal("balance"));
            account.setUsername(resultSet.getString("username"));
            account.setStatus(resultSet.getString("status"));
            accounts.add(account);
        }
        return accounts;
    }

//    public void applyForAccount(Customer customer) throws SQLException, BusinessException { //fixme current
//        Connection connection = PostgresConnection.getConnection();
//        String sql="INSERT INTO gormbank.accounts (balance, status, userid) VALUES(0, 'pending', " +
//                "(select userid from gormbank.customers c where username = ?));";
//        PreparedStatement preparedStatement=connection.prepareStatement(sql);
//        preparedStatement.setString(1, customer.getUsername());
//        int executeUpdate=preparedStatement.executeUpdate();
//        // no error handling yet!
//    }

    public void applyForAccountByUsernamePassword(String username, String password, BigDecimal amount) throws SQLException, BusinessException { //fixme current
        Connection connection = PostgresConnection.getConnection();
        String sql="INSERT INTO gormbank.accounts (balance, status, userid) VALUES(?, 'pending', " +
                "(select userid from gormbank.customers c where username = ? AND password = ?));";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setString(2, username);
        preparedStatement.setString(3, password);
        int executeUpdate=preparedStatement.executeUpdate();
        log.trace("DAO applyForAccount");
        // no error handling yet!
        if (executeUpdate !=1){
            throw new BusinessException("Operation failed; Account not updated!");
        }
    }


    public Account getAccountByAccountNum(long accountNum) throws SQLException, BusinessException {   //singular!! fixme
        Connection connection = PostgresConnection.getConnection();
        String sql = "select customers.userid, customers.username, customers.name, approved_by " +
                "accounts.account_number, accounts.balance, accounts.status " +
                "from gormbank.customers RIGHT join gormbank.accounts on accounts.userid = customers.userid " +
                "WHERE account_number = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, accountNum);
        ResultSet resultSet = preparedStatement.executeQuery();
        Account account = new Account();
        if (resultSet.next()) {
            account.setAccountNumber(resultSet.getLong("account_number"));
            account.setBalance(resultSet.getBigDecimal("balance")); //wrong type, fixme
            account.setUsername(resultSet.getString("username"));
            account.setUsername(resultSet.getString("name"));
            account.setApprovedByEmployeeId(resultSet.getLong("approved_by"));
            account.setStatus(resultSet.getString("status"));
            log.trace("DAO-getAccByNum");

        } //no errors, just no results, fixme
        return account;
    }

    public Vector<Account> getAccountsByUsernameOnly(String username) throws SQLException, BusinessException {   //used by employee and customer to view employees...
        //fixme
        Connection connection = PostgresConnection.getConnection();
        String sql = "select customers.userid, customers.username, customers.name, accounts.approved_by, " +
                "accounts.account_number, accounts.balance, accounts.status " +
                "from gormbank.customers RIGHT join gormbank.accounts on accounts.userid = customers.userid " +
                "WHERE username = ? " +
                "order by accounts.account_number;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);    //variables sent into sql/preparedStatement
        ResultSet resultSet = preparedStatement.executeQuery();
        Vector<Account> accounts = new Vector<Account>();
        while (resultSet.next()) {
            Account account = new Account();
            account.setAccountNumber(resultSet.getLong("account_number"));
            account.setBalance(resultSet.getBigDecimal("balance")); //wrong type, fixme
            account.setUsername(resultSet.getString("username"));
            account.setUsername(resultSet.getString("name"));
//            account.setApprovedByEmployeeId(resultSet.getLong("approved_by"));
            account.setStatus(resultSet.getString("status"));
            accounts.add(account);
            log.trace("DAO-getAccByUser");
        } //no errors, just no results, fixme
        return accounts;
    }

    public void withdrawFunds(long accountNum, BigDecimal amount, String username, String password) throws SQLException, BusinessException {
//        System.out.println("temp function");
        //should i pass in customer and break it down here, or in business layer?
        //user already logged in, should be ok... but will need to be fixed for webservice
        Connection connection = PostgresConnection.getConnection();
//        log.trace("withdraw funds, DAO");
//        log.trace("amount:" + amount);
//        log.trace("account" + accountNum);
        String sql= "update gormbank.accounts set balance = balance - ? " +
                "where account_number = ? and status ='active' and userid in " +
                "(SELECT userid from gormbank.customers WHERE username = ? AND password = ?);";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setLong(2, accountNum);
        preparedStatement.setString(3, username);
        preparedStatement.setString(4, password);
        int executeUpdate=preparedStatement.executeUpdate();
        if (executeUpdate !=1){
            log.info("withdraw failed for account "+ accountNum);
            throw new BusinessException("Operation failed; Account not updated!");
        }
    }

    public void depositFunds(long accountNum, BigDecimal amount, String username, String password) throws SQLException, BusinessException {

        Connection connection = PostgresConnection.getConnection();
        log.trace("deposit funds, DAO");
        String sql= "update gormbank.accounts set balance = balance + ? " +
                "where account_number = ? and status ='active' and userid in " +
                "(SELECT userid from gormbank.customers WHERE username = ? AND password = ?);";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setLong(2, accountNum);
        preparedStatement.setString(3, username);
        preparedStatement.setString(4, password);
        int executeUpdate=preparedStatement.executeUpdate();
        if (executeUpdate !=1){
            log.info("deposit failed for account "+ accountNum);
            throw new BusinessException("Operation failed; Account not updated!");
        }
    }

    public void transferFunds(String username, String password, long accountNum, long accountNum2, BigDecimal amount) throws SQLException, BusinessException {

        Connection connection = PostgresConnection.getConnection();

        System.out.println("dao transfer1");
        String sql= "update gormbank.accounts set balance = balance - ? " +
                "where account_number = ? and status ='active' and userid in " +
                "(SELECT userid from gormbank.customers WHERE username = ? AND password = ?);";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1, amount);
        preparedStatement.setLong(2, accountNum);
        preparedStatement.setString(3, username);
        preparedStatement.setString(4, password);
        int executeUpdate=preparedStatement.executeUpdate();
    //fixme; execute update if statement not working!? its running first method twice
        if (executeUpdate == 1){
//            System.out.println("dao transfer2");
            String sql2= "update gormbank.accounts set balance = balance + ? " +
                    "where account_number = ? and status ='active' and userid in " +
                    "(SELECT userid from gormbank.customers WHERE username = ? AND password = ?);";
            PreparedStatement preparedStatement2=connection.prepareStatement(sql2);
            preparedStatement2.setBigDecimal(1, amount);
            preparedStatement2.setLong(2, accountNum2);
            preparedStatement2.setString(3, username);
            preparedStatement2.setString(4, password);
            int executeUpdate2=preparedStatement2.executeUpdate();
            if (executeUpdate2 !=1){
                //this needs to rollback the first transaction
                //https://www.tutorialspoint.com/jdbc/commit-rollback.htm
                log.info("critical rollback failure: "+ accountNum2);
                throw new BusinessException("Critical failure: " + amount+ " failed to be added to account "+ accountNum2 + ". Contact support immediately!");
                //this needs to be logged... and fixed

            }
            else if (executeUpdate !=1){
            throw new BusinessException("Withdraw Operation failed; Transfer aborted!");
            }
        }
    }
    public void approveAccount(Long accountNum, String username, String password) throws SQLException, BusinessException{
        Connection connection = PostgresConnection.getConnection();
        String sql = "update gormbank.accounts set status ='active', approved_by = " +
                "(select userid from gormbank.employees e " +
                "where username = ? and password = ?)" +
                "where account_number = ? AND status = 'pending';";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(3, accountNum);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        int executeUpdate=preparedStatement.executeUpdate();
        //need an if pw=pw and if user=user...
        if (executeUpdate !=1){
            log.info("approve failed for account "+ accountNum);
            throw new BusinessException("Operation failed; Account not updated!");
        }
    }
    public void denyAccount(Long accountNum) throws SQLException, BusinessException{ //fixme - approves
        Connection connection = PostgresConnection.getConnection();
        String sql = "delete from gormbank.accounts where status = 'pedning' and account_number = ? ;";
        // not deleting yet
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, accountNum);
        int executeUpdate=preparedStatement.executeUpdate();
        //need an if pw=pw and if user=user...
        if (executeUpdate !=1){
            log.info("denial failed for account "+ accountNum);
            throw new BusinessException("Operation failed; Account not updated!");
        }
    } //fixme does not require passwird to delete acc if pending
//TRANSACTIONS

//        public void viewAllLogs () throws SQLException, BusinessException {}

    public void createLog(long account, BigDecimal amount, String transactionType) throws SQLException, BusinessException {

        Connection connection = PostgresConnection.getConnection();
        String sql="INSERT INTO gormbank.transactions (transaction_type, amount, account_number, \"time\") " +
                "VALUES(?, ?, ?, NOW());";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);

        preparedStatement.setString(1, transactionType);
        preparedStatement.setBigDecimal(2, amount);
        preparedStatement.setLong(3, account);
        int executeUpdate=preparedStatement.executeUpdate();
        // no error handling yet!

    }

// EMPLOYEE FUNCTIONS
public Employee createNewEmployee(String username, String name, String password) throws SQLException, BusinessException {
    //creates new customer if password, name and username exist in database
    Employee employee = new Employee();
    //database connection
    Connection connection = PostgresConnection.getConnection();
    String sql = "INSERT INTO gormbank.employees\n" +
            "(username, \"name\", \"password\")\n" +
            "VALUES(?, ?, ?);\n";
    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, username);    //variables sent into DB
    preparedStatement.setString(2, name);
    preparedStatement.setString(3, password);
    int c = preparedStatement.executeUpdate();
    //fixme - should this be executeQuery instead?
//        log.trace("Inserted "+ c + " records");
    if (c == 1) {
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) { //this is needed to get a response
            employee.setId(resultSet.getLong(1));
            employee.setUsername(resultSet.getString(2)); //index 1 should be userid
            employee.setName(resultSet.getString(3));
            employee.setPassword(resultSet.getString(4));
//                log.trace(resultSet.getString(2)+ " added to user database");
        }
    } else {
        throw new BusinessException("Failure in registration... Please retry.....");
    }
    return employee; //this returns a customer even if it doesnt work...?
}
public Employee findEmployeeByLogin(String username, String pw) throws SQLException, BusinessException {
    Employee employee = new Employee();
    //step 2 connection
    Connection connection = PostgresConnection.getConnection();
    //Step 3- Create Statement
    String sql = "SELECT username, name, password, userid from gormbank.employees WHERE username = ? AND password = ?;";
    PreparedStatement preparedStatement = connection.prepareStatement(sql); //2nd par makes keys returnable...
    preparedStatement.setString(1, username);    //variables sent into DB
    preparedStatement.setString(2, pw);
    //Step 4 - Execute Query
    ResultSet resultSet = preparedStatement.executeQuery();
//        log.trace("DAO-findCustomerByLogin");
    //Step 5 - Process Results  THIS WILL BE IMPORTANT~
    //        while (resultSet.next()){
    if (resultSet.next()) {
        employee.setUsername(resultSet.getString("username"));
        employee.setName(resultSet.getString("name"));
        employee.setPassword(resultSet.getString("password"));
        employee.setId(resultSet.getLong("userid"));
        //is this necessarry?
//            log.trace("DAO loginOldCustomer: " + customer.getUsername());
    }
    else {
        throw new BusinessException("No User Found");
    } //if no results, throw exception
    return employee;
}




    public Vector<Transaction> viewAllLogs() throws SQLException, BusinessException { //fixme
        Connection connection = PostgresConnection.getConnection();
        String sql = "select transaction_id, account_number, transaction_type, amount, time " +
                "from gormbank.transactions;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
//    if (!resultSet.next()){
//        throw new BusinessException("No logs to view");
//    }
        Vector<Transaction> transactions = new Vector<>();
        while (resultSet.next()){
            Transaction transaction = new Transaction();
            transaction.setTransactionId(resultSet.getLong("transaction_id"));
            transaction.setAccountNumber(resultSet.getLong("account_number"));
            transaction.setTransactionType(resultSet.getString("transaction_type"));
            transaction.setAmount(resultSet.getBigDecimal("amount"));
            transaction.setTimestamp(resultSet.getTimestamp("time"));
            transactions.add(transaction);

//        System.out.println("dao test, log: " + transaction.getTransactionType());
        }
        return transactions;
    }
    public Vector<Transaction> viewLogsByDay(Timestamp time, Timestamp time2) throws SQLException, BusinessException { //fixme
        Connection connection = PostgresConnection.getConnection();
        String sql = "select transaction_id, account_number, transaction_type, amount, time " +
                "from gormbank.transactions " +
                "where " +
                "time >= ? " +
                "AND " +
                "time <= ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
               preparedStatement.setTimestamp(1, time);    //variables sent into DB
        preparedStatement.setTimestamp(2, time2);
        ResultSet resultSet = preparedStatement.executeQuery();

        Vector<Transaction> transactions = new Vector<>();
        while (resultSet.next()){
            Transaction transaction = new Transaction();
            transaction.setTransactionId(resultSet.getLong("transaction_id"));
            transaction.setAccountNumber(resultSet.getLong("account_number"));
            transaction.setTransactionType(resultSet.getString("transaction_type"));
            transaction.setAmount(resultSet.getBigDecimal("amount"));
            transaction.setTimestamp(resultSet.getTimestamp("time"));
            transactions.add(transaction);
//            System.out.println("dao");
//            System.out.println(transaction);
        }
        return transactions;
    }

    public Vector<Transaction> viewLogsByUser(String user) throws SQLException, BusinessException { //fixme
        Connection connection = PostgresConnection.getConnection();
//        String sql = "select transaction_id, account_number, transaction_type, amount, time, username " +
//                "FROM gormbank.transactions join gormbank.customers c on transaction_id = userid WHERE username = ?;";
        String sql =
        "select transaction_id, a.account_number, transaction_type, amount, time " +
        "from gormbank.customers c " +
        "inner join gormbank.accounts a " +
        "on c.userid = a.userid " +
        "inner join gormbank.transactions t " +
        "on a.account_number = t.account_number " +
        "WHERE username = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user);    //variables sent into DB
        ResultSet resultSet = preparedStatement.executeQuery();
//    if (!resultSet.next()){
//        throw new BusinessException("No logs to view");
//    }
        Vector<Transaction> transactions = new Vector<>();
        while (resultSet.next()){
            Transaction transaction = new Transaction();
            transaction.setTransactionId(resultSet.getLong("transaction_id"));
            transaction.setAccountNumber(resultSet.getLong("account_number"));
            transaction.setTransactionType(resultSet.getString("transaction_type"));
            transaction.setAmount(resultSet.getBigDecimal("amount"));
            transaction.setTimestamp(resultSet.getTimestamp("time"));
            transactions.add(transaction);
            System.out.println("dao");
            System.out.println(transaction);
//        System.out.println("dao test, log: " + transaction.getTransactionType());
        }
        return transactions;
    }










}
