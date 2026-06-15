package com.ems.model;

import java.util.Date;

/**
 * Model class representing an Employee.
 * Demonstrates encapsulation, constructor overloading.
 */
public class Employee {

    private int    empId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date   hireDate;
    private String jobTitle;
    private int    deptId;
    private char   isActive;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Employee() {}

    // Full constructor (used when reading from DB)
    public Employee(int empId, String firstName, String lastName, String email,
                    String phone, Date hireDate, String jobTitle, int deptId, char isActive) {
        this.empId     = empId;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.phone     = phone;
        this.hireDate  = hireDate;
        this.jobTitle  = jobTitle;
        this.deptId    = deptId;
        this.isActive  = isActive;
    }

    // Constructor for INSERT (no empId — comes from sequence)
    public Employee(String firstName, String lastName, String email,
                    String phone, String jobTitle, int deptId) {
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.phone     = phone;
        this.jobTitle  = jobTitle;
        this.deptId    = deptId;
        this.isActive  = 'Y';
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getEmpId()     { return empId; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getFullName()  { return firstName + " " + lastName; }
    public String getEmail()     { return email; }
    public String getPhone()     { return phone; }
    public Date   getHireDate()  { return hireDate; }
    public String getJobTitle()  { return jobTitle; }
    public int    getDeptId()    { return deptId; }
    public char   getIsActive()  { return isActive; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setEmpId(int id)          { this.empId     = id; }
    public void setFirstName(String fn)   { this.firstName = fn; }
    public void setLastName(String ln)    { this.lastName  = ln; }
    public void setEmail(String email)    { this.email     = email; }
    public void setPhone(String phone)    { this.phone     = phone; }
    public void setHireDate(Date date)    { this.hireDate  = date; }
    public void setJobTitle(String title) { this.jobTitle  = title; }
    public void setDeptId(int deptId)     { this.deptId    = deptId; }
    public void setIsActive(char active)  { this.isActive  = active; }

    @Override
    public String toString() {
        return String.format("Emp[%d] %-25s | %s | Dept: %d | Active: %s",
                empId, getFullName(), jobTitle, deptId, isActive);
    }
}
