package com.ems.ui;

import com.ems.model.Department;
import com.ems.model.Employee;
import com.ems.model.Salary;
import com.ems.service.EmployeeService;
import com.ems.util.DBConnection;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main console UI for the Employee Management System.
 * Run this class — it has the main() method.
 *
 * Flow:
 *  main()
 *    └─ MainMenu (loop)
 *         ├─ Department Menu → EmployeeService → DepartmentDAO → Oracle DB
 *         ├─ Employee Menu   → EmployeeService → EmployeeDAO   → Oracle DB
 *         └─ Salary Menu     → EmployeeService → SalaryDAO     → Oracle DB
 */
public class MainMenu {

    private static final Scanner sc      = new Scanner(System.in);
    private static final EmployeeService svc = new EmployeeService();

    // ── Entry point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("\n" + "★".repeat(50));
        System.out.println("   EMPLOYEE MANAGEMENT SYSTEM  v1.0");
        System.out.println("   Built with Java + Oracle SQL + JDBC");
        System.out.println("★".repeat(50));

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> departmentMenu();
                case 2 -> employeeMenu();
                case 3 -> salaryMenu();
                case 4 -> reportsMenu();
                case 0 -> { running = false; exit(); }
                default -> System.out.println("  ✗ Invalid choice. Try again.");
            }
        }
    }

    // ════════════════════════════════════════════════════════
    //  MENUS
    // ════════════════════════════════════════════════════════

    private static void printMainMenu() {
        System.out.println("\n" + "─".repeat(40));
        System.out.println("  MAIN MENU");
        System.out.println("─".repeat(40));
        System.out.println("  1. Department Management");
        System.out.println("  2. Employee Management");
        System.out.println("  3. Salary Management");
        System.out.println("  4. Reports");
        System.out.println("  0. Exit");
        System.out.println("─".repeat(40));
    }

    // ── Department Menu ───────────────────────────────────────────────────────
    private static void departmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── DEPARTMENT MENU ──");
            System.out.println("  1. Add Department");
            System.out.println("  2. View All Departments");
            System.out.println("  3. Update Department");
            System.out.println("  4. Delete Department");
            System.out.println("  0. Back");

            switch (readInt("  Choice: ")) {
                case 1 -> addDepartment();
                case 2 -> viewDepartments();
                case 3 -> updateDepartment();
                case 4 -> deleteDepartment();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid.");
            }
        }
    }

    // ── Employee Menu ─────────────────────────────────────────────────────────
    private static void employeeMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── EMPLOYEE MENU ──");
            System.out.println("  1. Add Employee");
            System.out.println("  2. View All Employees");
            System.out.println("  3. Search Employee by Name");
            System.out.println("  4. Update Employee");
            System.out.println("  5. Deactivate Employee");
            System.out.println("  0. Back");

            switch (readInt("  Choice: ")) {
                case 1 -> addEmployee();
                case 2 -> viewEmployees();
                case 3 -> searchEmployee();
                case 4 -> updateEmployee();
                case 5 -> deactivateEmployee();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid.");
            }
        }
    }

    // ── Salary Menu ───────────────────────────────────────────────────────────
    private static void salaryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── SALARY MENU ──");
            System.out.println("  1. Add Salary Record");
            System.out.println("  2. View Salary History (by Employee)");
            System.out.println("  0. Back");

            switch (readInt("  Choice: ")) {
                case 1 -> addSalary();
                case 2 -> viewSalaryHistory();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid.");
            }
        }
    }

    // ── Reports Menu ──────────────────────────────────────────────────────────
    private static void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n  ── REPORTS ──");
            System.out.println("  1. Employee Report (all active)");
            System.out.println("  2. Department Headcount Report");
            System.out.println("  3. Payroll Report (by Month/Year)");
            System.out.println("  4. Department-wise Salary Summary");
            System.out.println("  0. Back");

            switch (readInt("  Choice: ")) {
                case 1 -> svc.showEmployeeReport();
                case 2 -> svc.showDeptHeadcountReport();
                case 3 -> {
                    String m = readString("  Enter month (e.g. JUNE): ").toUpperCase();
                    int    y = readInt("  Enter year  (e.g. 2025): ");
                    svc.showPayrollReport(m, y);
                }
                case 4 -> svc.showDeptSalaryReport();
                case 0 -> back = true;
                default -> System.out.println("  ✗ Invalid.");
            }
        }
    }

    // ════════════════════════════════════════════════════════
    //  DEPARTMENT ACTIONS
    // ════════════════════════════════════════════════════════

    private static void addDepartment() {
        System.out.println("\n  -- Add Department --");
        String name = readString("  Name     : ");
        String loc  = readString("  Location : ");
        System.out.println("  " + svc.addDepartment(name, loc));
    }

    private static void viewDepartments() {
        try {
            List<Department> list = svc.getAllDepartments();
            System.out.println("\n  ID   Department            Location");
            System.out.println("  " + "-".repeat(45));
            for (Department d : list)
                System.out.printf("  %-4d %-22s %s%n", d.getDeptId(), d.getDeptName(), d.getLocation());
        } catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }

    private static void updateDepartment() {
        System.out.println("\n  -- Update Department --");
        int    id   = readInt("  Department ID : ");
        String name = readString("  New name      : ");
        String loc  = readString("  New location  : ");
        System.out.println("  " + svc.updateDepartment(id, name, loc));
    }

    private static void deleteDepartment() {
        int id = readInt("\n  Department ID to delete: ");
        System.out.println("  " + svc.deleteDepartment(id));
    }

    // ════════════════════════════════════════════════════════
    //  EMPLOYEE ACTIONS
    // ════════════════════════════════════════════════════════

    private static void addEmployee() {
        System.out.println("\n  -- Add Employee --");
        String fn     = readString("  First name  : ");
        String ln     = readString("  Last name   : ");
        String email  = readString("  Email       : ");
        String phone  = readString("  Phone       : ");
        String title  = readString("  Job title   : ");
        int    deptId = readInt("  Dept ID     : ");
        System.out.println("  " + svc.addEmployee(fn, ln, email, phone, title, deptId));
    }

    private static void viewEmployees() {
        try {
            List<Employee> list = svc.getAllEmployees();
            if (list.isEmpty()) { System.out.println("  No active employees found."); return; }
            System.out.println();
            for (Employee e : list) System.out.println("  " + e);
        } catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }

    private static void searchEmployee() {
        String kw = readString("\n  Enter name to search: ");
        try {
            List<Employee> list = svc.searchEmployee(kw);
            if (list.isEmpty()) { System.out.println("  No results found."); return; }
            for (Employee e : list) System.out.println("  " + e);
        } catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }

    private static void updateEmployee() {
        System.out.println("\n  -- Update Employee --");
        int    id    = readInt("  Employee ID : ");
        String fn    = readString("  First name  : ");
        String ln    = readString("  Last name   : ");
        String email = readString("  Email       : ");
        String phone = readString("  Phone       : ");
        String title = readString("  Job title   : ");
        int    dept  = readInt("  Dept ID     : ");
        System.out.println("  " + svc.updateEmployee(id, fn, ln, email, phone, title, dept));
    }

    private static void deactivateEmployee() {
        int id = readInt("\n  Employee ID to deactivate: ");
        System.out.print("  Confirm deactivate Emp " + id + "? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y"))
            System.out.println("  " + svc.deactivateEmployee(id));
        else
            System.out.println("  Cancelled.");
    }

    // ════════════════════════════════════════════════════════
    //  SALARY ACTIONS
    // ════════════════════════════════════════════════════════

    private static void addSalary() {
        System.out.println("\n  -- Add Salary Record --");
        int    empId = readInt("  Employee ID   : ");
        double basic = readDouble("  Basic salary  : ");
        double bonus = readDouble("  Bonus         : ");
        double ded   = readDouble("  Deductions    : ");
        String month = readString("  Month (JUNE)  : ");
        int    year  = readInt("  Year (2025)   : ");
        System.out.println("  " + svc.addSalary(empId, basic, bonus, ded, month, year));
    }

    private static void viewSalaryHistory() {
        int empId = readInt("\n  Employee ID: ");
        try {
            List<Salary> list = svc.getSalaryHistory(empId);
            if (list.isEmpty()) { System.out.println("  No salary records found."); return; }
            System.out.println();
            for (Salary s : list) System.out.println("  " + s);
        } catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }

    // ════════════════════════════════════════════════════════
    //  HELPERS
    // ════════════════════════════════════════════════════════

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Please enter a valid number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static void exit() {
        DBConnection.closeConnection();
        System.out.println("\n  Goodbye! Connection closed.");
        System.out.println("★".repeat(50));
    }
}
