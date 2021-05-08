package org.geordin.model;

public class Employee extends User {
    Long employeeId;
    String employeeName;
    //constructor
    public Employee(){
        super("employee1", "password");
    } //constructor hardcodes employee password
        //fixme

    public Employee(String username, String password, String name){
        this.setUsername(username);
        this.setPassword(password);
        this.setEmployeeName(name);

        //what to do about employee id?
    };
    public Employee(Long id, String username, String password, String name){
        this.setUsername(username);
        this.setPassword(password);
        this.setEmployeeName(name);
        this.setEmployeeId(id);
        //what to do about employee id?
    };

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    //no class variables right now,

}
