package com.ems.dao;

import com.ems.model.Employee;
import com.ems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee.
 * All database operations for the EMPLOYEES table.
 */
public class EmployeeDAO {

    // ── Helper: map ResultSet row to Employee object ───────────────────────────
    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("emp_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDate("hire_date"),
                rs.getString("job_title"),
                rs.getInt("dept_id"),
                rs.getString("is_active").charAt(0)
        );
    }

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees " +
                     "(emp_id, first_name, last_name, email, phone, hire_date, job_title, dept_id, is_active) " +
                     "VALUES (emp_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, ?, ?, 'Y')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getFirstName().trim());
            ps.setString(2, emp.getLastName().trim());
            ps.setString(3, emp.getEmail().trim().toLowerCase());
            ps.setString(4, emp.getPhone().trim());
            ps.setString(5, emp.getJobTitle().trim());
            ps.setInt(6, emp.getDeptId());

            return ps.executeUpdate() > 0;
        }
    }

    // ── READ: by ID ───────────────────────────────────────────────────────────
    public Employee getEmployeeById(int empId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE emp_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        }
    }

    // ── READ: all active employees ────────────────────────────────────────────
    public List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employees WHERE is_active = 'Y' ORDER BY emp_id";
        List<Employee> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── READ: by department ───────────────────────────────────────────────────
    public List<Employee> getEmployeesByDept(int deptId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE dept_id = ? AND is_active = 'Y' ORDER BY first_name";
        List<Employee> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── READ: search by name ──────────────────────────────────────────────────
    public List<Employee> searchByName(String keyword) throws SQLException {
        String sql = "SELECT * FROM employees " +
                     "WHERE (UPPER(first_name) LIKE UPPER(?) OR UPPER(last_name) LIKE UPPER(?)) " +
                     "AND is_active = 'Y'";
        List<Employee> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET first_name=?, last_name=?, email=?, " +
                     "phone=?, job_title=?, dept_id=? WHERE emp_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getFirstName().trim());
            ps.setString(2, emp.getLastName().trim());
            ps.setString(3, emp.getEmail().trim().toLowerCase());
            ps.setString(4, emp.getPhone().trim());
            ps.setString(5, emp.getJobTitle().trim());
            ps.setInt(6, emp.getDeptId());
            ps.setInt(7, emp.getEmpId());

            return ps.executeUpdate() > 0;
        }
    }

    // ── SOFT DELETE (set is_active = 'N') ────────────────────────────────────
    public boolean deactivateEmployee(int empId) throws SQLException {
        String sql = "UPDATE employees SET is_active = 'N' WHERE emp_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── HARD DELETE ──────────────────────────────────────────────────────────
    public boolean deleteEmployee(int empId) throws SQLException {
        String sql = "DELETE FROM employees WHERE emp_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── REPORT: all employees with department name ─────────────────────────────
    public void printEmployeeReport() throws SQLException {
        String sql = "SELECT e.emp_id, e.first_name || ' ' || e.last_name AS name, " +
                     "e.job_title, d.dept_name, e.hire_date " +
                     "FROM employees e JOIN departments d ON e.dept_id = d.dept_id " +
                     "WHERE e.is_active = 'Y' ORDER BY d.dept_name, e.first_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n" + "=".repeat(80));
            System.out.printf("%-6s %-25s %-22s %-18s %s%n",
                    "ID", "Name", "Job Title", "Department", "Hire Date");
            System.out.println("=".repeat(80));
            while (rs.next()) {
                System.out.printf("%-6d %-25s %-22s %-18s %s%n",
                        rs.getInt("emp_id"),
                        rs.getString("name"),
                        rs.getString("job_title"),
                        rs.getString("dept_name"),
                        rs.getDate("hire_date"));
            }
            System.out.println("=".repeat(80));
        }
    }
}
