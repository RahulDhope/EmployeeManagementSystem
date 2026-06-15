package com.ems.dao;

import com.ems.model.Salary;
import com.ems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Salary.
 * Handles all salary CRUD + reporting queries.
 */
public class SalaryDAO {

    // ── Helper: map ResultSet row to Salary object ────────────────────────────
    private Salary mapRow(ResultSet rs) throws SQLException {
        return new Salary(
                rs.getInt("sal_id"),
                rs.getInt("emp_id"),
                rs.getDouble("basic_salary"),
                rs.getDouble("bonus"),
                rs.getDouble("deductions"),
                rs.getDouble("net_salary"),
                rs.getString("pay_month"),
                rs.getInt("pay_year")
        );
    }

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addSalary(Salary sal) throws SQLException {
        // Note: net_salary is a VIRTUAL column — do NOT insert it
        String sql = "INSERT INTO salary (sal_id, emp_id, basic_salary, bonus, deductions, pay_month, pay_year) " +
                     "VALUES (sal_seq.NEXTVAL, ?, ?, ?, ?, UPPER(?), ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sal.getEmpId());
            ps.setDouble(2, sal.getBasicSalary());
            ps.setDouble(3, sal.getBonus());
            ps.setDouble(4, sal.getDeductions());
            ps.setString(5, sal.getPayMonth());
            ps.setInt(6, sal.getPayYear());

            return ps.executeUpdate() > 0;
        }
    }

    // ── READ: by employee ID ──────────────────────────────────────────────────
    public List<Salary> getSalaryByEmployee(int empId) throws SQLException {
        String sql = "SELECT * FROM salary WHERE emp_id = ? ORDER BY pay_year DESC, pay_month";
        List<Salary> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── READ: by month and year ───────────────────────────────────────────────
    public List<Salary> getSalaryByMonthYear(String month, int year) throws SQLException {
        String sql = "SELECT * FROM salary WHERE UPPER(pay_month) = UPPER(?) AND pay_year = ?";
        List<Salary> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean updateSalary(Salary sal) throws SQLException {
        String sql = "UPDATE salary SET basic_salary=?, bonus=?, deductions=? WHERE sal_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, sal.getBasicSalary());
            ps.setDouble(2, sal.getBonus());
            ps.setDouble(3, sal.getDeductions());
            ps.setInt(4, sal.getSalId());

            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean deleteSalary(int salId) throws SQLException {
        String sql = "DELETE FROM salary WHERE sal_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, salId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── REPORT: Full payroll report ───────────────────────────────────────────
    public void printPayrollReport(String month, int year) throws SQLException {
        String sql = "SELECT e.emp_id, e.first_name || ' ' || e.last_name AS employee, " +
                     "d.dept_name, s.basic_salary, s.bonus, s.deductions, s.net_salary " +
                     "FROM salary s " +
                     "JOIN employees e ON s.emp_id = e.emp_id " +
                     "JOIN departments d ON e.dept_id = d.dept_id " +
                     "WHERE UPPER(s.pay_month) = UPPER(?) AND s.pay_year = ? " +
                     "ORDER BY s.net_salary DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n" + "=".repeat(85));
            System.out.println("  PAYROLL REPORT — " + month.toUpperCase() + " " + year);
            System.out.println("=".repeat(85));
            System.out.printf("%-6s %-22s %-18s %10s %8s %10s %10s%n",
                    "ID", "Employee", "Department", "Basic", "Bonus", "Deductions", "Net");
            System.out.println("-".repeat(85));

            double totalNet = 0;
            while (rs.next()) {
                double net = rs.getDouble("net_salary");
                totalNet += net;
                System.out.printf("%-6d %-22s %-18s %10.0f %8.0f %10.0f %10.0f%n",
                        rs.getInt("emp_id"),
                        rs.getString("employee"),
                        rs.getString("dept_name"),
                        rs.getDouble("basic_salary"),
                        rs.getDouble("bonus"),
                        rs.getDouble("deductions"),
                        net);
            }
            System.out.println("-".repeat(85));
            System.out.printf("%-58s TOTAL NET: %10.0f%n", "", totalNet);
            System.out.println("=".repeat(85));
        }
    }

    // ── REPORT: Dept-wise average salary ─────────────────────────────────────
    public void printDeptSalaryReport() throws SQLException {
        String sql = "SELECT d.dept_name, ROUND(AVG(s.basic_salary),0) AS avg_basic, " +
                     "ROUND(AVG(s.net_salary),0) AS avg_net, " +
                     "MIN(s.net_salary) AS min_net, MAX(s.net_salary) AS max_net " +
                     "FROM salary s " +
                     "JOIN employees e ON s.emp_id = e.emp_id " +
                     "JOIN departments d ON e.dept_id = d.dept_id " +
                     "GROUP BY d.dept_name ORDER BY avg_net DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n" + "=".repeat(70));
            System.out.println("  DEPARTMENT-WISE SALARY SUMMARY");
            System.out.println("=".repeat(70));
            System.out.printf("%-20s %12s %12s %10s %10s%n",
                    "Department", "Avg Basic", "Avg Net", "Min Net", "Max Net");
            System.out.println("-".repeat(70));
            while (rs.next()) {
                System.out.printf("%-20s %12.0f %12.0f %10.0f %10.0f%n",
                        rs.getString("dept_name"),
                        rs.getDouble("avg_basic"),
                        rs.getDouble("avg_net"),
                        rs.getDouble("min_net"),
                        rs.getDouble("max_net"));
            }
            System.out.println("=".repeat(70));
        }
    }
}
