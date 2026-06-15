package com.ems.dao;

import com.ems.model.Department;
import com.ems.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Department.
 * Contains all CRUD operations that talk to the database.
 * Business logic stays in the Service layer — DAO only does SQL.
 */
public class DepartmentDAO {

    // ── CREATE ────────────────────────────────────────────────────────────────
    public boolean addDepartment(Department dept) throws SQLException {
        String sql = "INSERT INTO departments (dept_id, dept_name, location, created_at) " +
                     "VALUES (dept_seq.NEXTVAL, ?, ?, SYSDATE)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dept.getDeptName().trim());
            ps.setString(2, dept.getLocation().trim());

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // ── READ: single by ID ────────────────────────────────────────────────────
    public Department getDepartmentById(int deptId) throws SQLException {
        String sql = "SELECT dept_id, dept_name, location FROM departments WHERE dept_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Department(
                        rs.getInt("dept_id"),
                        rs.getString("dept_name"),
                        rs.getString("location")
                );
            }
        }
        return null; // not found
    }

    // ── READ: all departments ─────────────────────────────────────────────────
    public List<Department> getAllDepartments() throws SQLException {
        String sql = "SELECT dept_id, dept_name, location FROM departments ORDER BY dept_id";
        List<Department> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Department(
                        rs.getInt("dept_id"),
                        rs.getString("dept_name"),
                        rs.getString("location")
                ));
            }
        }
        return list;
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    public boolean updateDepartment(Department dept) throws SQLException {
        String sql = "UPDATE departments SET dept_name = ?, location = ? WHERE dept_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dept.getDeptName().trim());
            ps.setString(2, dept.getLocation().trim());
            ps.setInt(3, dept.getDeptId());

            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean deleteDepartment(int deptId) throws SQLException {
        String sql = "DELETE FROM departments WHERE dept_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    // ── REPORT: Dept-wise headcount ───────────────────────────────────────────
    public void printDeptHeadcountReport() throws SQLException {
        String sql = "SELECT d.dept_name, d.location, COUNT(e.emp_id) AS headcount " +
                     "FROM departments d " +
                     "LEFT JOIN employees e ON d.dept_id = e.dept_id AND e.is_active = 'Y' " +
                     "GROUP BY d.dept_name, d.location ORDER BY headcount DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n" + "=".repeat(55));
            System.out.printf("%-25s %-15s %s%n", "Department", "Location", "Headcount");
            System.out.println("=".repeat(55));
            while (rs.next()) {
                System.out.printf("%-25s %-15s %d%n",
                        rs.getString("dept_name"),
                        rs.getString("location"),
                        rs.getInt("headcount"));
            }
            System.out.println("=".repeat(55));
        }
    }
}
