package org.geordin.model;

public class Employee extends User {
    //employee object should be the same as a customer object - just in a different database
    //if I was smart I'd go back and give all the properties to "user" abstract class.
    private Long id;
    private String name;
    //constructor
    public Employee(){
        super("employee1", "password");
    } //constructor hardcodes employee password
        //fixme

    public Employee(String username, String password, String name){
        this.setUsername(username);
        this.setPassword(password);
        this.setName(name);

        //what to do about employee id?
    };
    public Employee(Long id, String username, String password, String name){
        this.setUsername(username);
        this.setPassword(password);
        this.setName(name);
        this.setId(id);
        //what to do about employee id?
    };

    public Long getId() {
        return id;
    }

    public void setId(Long employeeId) {
        this.id = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String employeeName) {
        this.name = employeeName;
    }

    //no class variables right now,
    @Override
    public String toString(){
        return ("Username: "+ this.getUsername() + " Name: "+ this.getName()); //may need to change this to userid?
    }
}
