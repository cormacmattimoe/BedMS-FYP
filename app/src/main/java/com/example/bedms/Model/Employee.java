package com.example.bedms.Model;

public class Employee {

    private String employeeName;
    private String employeeEmail;
    private String employeeDOB;
    private String employeeStatus;
    private String employeeRole;


    public Employee(){

    }
    public Employee(String employeeName, String employeeEmail, String employeeDOB, String employeeStatus, String employeeRole) {
        this.employeeName = employeeName;
        this.employeeEmail = employeeEmail;
        this.employeeDOB = employeeDOB;
        this.employeeStatus = employeeStatus;
        this.employeeRole = employeeRole;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeDOB() {
        return employeeDOB;
    }

    public void setEmployeeDOB(String employeeDOB) {
        this.employeeDOB = employeeDOB;
    }

    public String getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }
}
