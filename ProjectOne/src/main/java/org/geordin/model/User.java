package org.geordin.model;

public class User {
    String username;
    String password;

    public String getPassword() {
        return this.password;
    }
    public String getUsername() {
        return this.username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
//    public boolean verifyUser(String user, String pw){  // using this instead of a login method because we are not enabling any sort of tracking method to ensure they remain logged in. Its just a loop.
      //this should connect to xxx which connects to DB, and returns true if username and pw verified (logged in)

//CONSTRUCTOR
    public User(){

    }
    public User(String username, String pw) {
        this.username = username;
        this.password = pw;
    }


}
//no arg constructor gets pw and username from sql?


//need to figure out login before filling out this class...
//will employe and customer logins work the same?
//username and password stored in DB?
//so need a get username and getpw functions!

//need ot overwrite toString, hashcode and equals methods!