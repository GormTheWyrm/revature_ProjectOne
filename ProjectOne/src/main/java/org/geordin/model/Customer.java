package org.geordin.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

//toString method needs love
public class Customer extends User{
    //inherits username and password
    private Long id;    //may add id back in if I implement ability to change username... but should not be necessary
    private String name;
    Vector<Account> accounts = new Vector<>();


//getters, setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {this.id = id;}
    public Long getId(){return this.id;}

    public Vector<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Vector<Account> accounts) {
        this.accounts = accounts;
    }
//cleaning up below...



    public void addAccount(Account account){
        this.accounts.add(account);
//        return true; //may need to change to a bool... so she tells me if error
    }
    public void removeAccount(Long accountNumber){ //only adds to object, not DB
        this.accounts.remove(accountNumber);
    }

    //constructors
    public Customer(){};
    public Customer(Long id, String username, String password, String name, Vector<Account> accounts){
        //fixme added a long id to above
        setId(id);
        setUsername(username);
        setPassword(password);
        setName(name);
        //need to set accounts
    }
    public Customer(String username, String password, String name){ //no account collection
//        setId(id);
        setUsername(username);
        setPassword(password);
        setName(name);
        //naccount intentionally not set
    }
    //I might not need to use passord at all...
    public Customer(String username, String password){ //no account collection
        setUsername(username);
        setPassword(password);
    }






    // ~~~~~~~~~~~~~~~fixme!
//    @Override
//    public int hashCode() {
//        return Objects.hash(this.username);
//    }
//    @Override
//    public String toString(){
//        Set<Long> keys = accounts.keySet();
//        Iterator<Long> itr = keys.iterator();
//        String myString = "Username: " + this.username; //should be a stringbuilder...
//        while(itr.hasNext()){
//            myString += "\n";
//            myString += itr.next();
//        }
//            return (myString);
//
//    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer)){ return false;}
        Customer other = (Customer) o;
        if (!(this.getUsername().equals(other.getUsername()))){
            return false;
        }
        if(this.accounts.size() != other.accounts.size()){
            return false;
        }
        for (int i=0; i< this.accounts.size(); i++){
            if (!(this.accounts.elementAt(i).equals(other.accounts.elementAt(i)))){
                return false;
            }
        }
        return true;
        // probably need to take into account number of accounts...


    }
    //do I need to override accounts toString?
    @Override
    public String toString(){
        return ("Username: "+ this.getUsername() + " Name: "+ this.getName()); //may need to change this to userid?
    }
}
//https://docs.oracle.com/javase/8/docs/api/java/util/Hashtable.html
// https://www.w3spoint.com/hashtable-in-java#:~:text=Hashtable%20in%20java%20example%20program%20code%20%3A%20Hashtable,in%20key-value%20pair.%20It%20not%20allowed%20duplicate%20key.
