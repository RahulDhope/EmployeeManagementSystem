package com.ems.model;

/**
 * Model class representing an employee's Salary record.
 * net_salary is a VIRTUAL column in Oracle — computed as basic + bonus - deductions.
 */
public class Salary {

    private int    salId;
    private int    empId;
    private double basicSalary;
    private double bonus;
    private double deductions;
    private double netSalary;   // read-only: computed by Oracle
    private String payMonth;
    private int    payYear;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Salary() {}

    // Full constructor (reading from DB)
    public Salary(int salId, int empId, double basicSalary, double bonus,
                  double deductions, double netSalary, String payMonth, int payYear) {
        this.salId       = salId;
        this.empId       = empId;
        this.basicSalary = basicSalary;
        this.bonus       = bonus;
        this.deductions  = deductions;
        this.netSalary   = netSalary;
        this.payMonth    = payMonth;
        this.payYear     = payYear;
    }

    // Constructor for INSERT (no salId, no netSalary — both from Oracle)
    public Salary(int empId, double basicSalary, double bonus,
                  double deductions, String payMonth, int payYear) {
        this.empId       = empId;
        this.basicSalary = basicSalary;
        this.bonus       = bonus;
        this.deductions  = deductions;
        this.payMonth    = payMonth;
        this.payYear     = payYear;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getSalId()       { return salId; }
    public int    getEmpId()       { return empId; }
    public double getBasicSalary() { return basicSalary; }
    public double getBonus()       { return bonus; }
    public double getDeductions()  { return deductions; }
    public double getNetSalary()   { return netSalary; }
    public String getPayMonth()    { return payMonth; }
    public int    getPayYear()     { return payYear; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setSalId(int id)             { this.salId       = id; }
    public void setEmpId(int id)             { this.empId       = id; }
    public void setBasicSalary(double sal)   { this.basicSalary = sal; }
    public void setBonus(double bonus)       { this.bonus       = bonus; }
    public void setDeductions(double ded)    { this.deductions  = ded; }
    public void setNetSalary(double net)     { this.netSalary   = net; }
    public void setPayMonth(String month)    { this.payMonth    = month; }
    public void setPayYear(int year)         { this.payYear     = year; }

    @Override
    public String toString() {
        return String.format("Sal[%d] EmpId:%-5d | Basic:%-8.0f Bonus:%-6.0f Ded:%-6.0f Net:%-8.0f | %s %d",
                salId, empId, basicSalary, bonus, deductions, netSalary, payMonth, payYear);
    }
}
