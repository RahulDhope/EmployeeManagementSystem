package com.ems.service;

import com.ems.dao.DepartmentDAO;
import com.ems.dao.EmployeeDAO;
import com.ems.dao.SalaryDAO;
import com.ems.model.Department;
import com.ems.model.Employee;
import com.ems.model.Salary;
import com.ems.util.InputValidator;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer — contains business logic and validation.
 * Sits between the UI and the DAO.
 * The UI calls Service; Service validates, then calls DAO.
 */
public class EmployeeService {

    private final EmployeeDAO   empDAO  = new EmployeeDAO();
    private final DepartmentDAO deptDAO = new DepartmentDAO();
    private final SalaryDAO     salDAO  = new SalaryDAO();

    // ════════════════════════════════════════════════════════
    //  DEPARTMENT OPERATIONS
    // ════════════════════════════════════════════════════════

    public String addDepartment(String name, String location) {
        if (!InputValidator.isValidName(name))
            return "ERROR: Invalid department name. Use letters only (2-50 chars).";
        if (!InputValidator.isNotBlank(location))
            return "ERROR: Location cannot be blank.";
        try {
            Department d = new Department(name, location);
            boolean ok = deptDAO.addDepartment(d);
            return ok ? "SUCCESS: Department '" + name + "' added." : "ERROR: Insert failed.";
        } catch (SQLException e) {
            if (e.getMessage().contains("unique constraint") || e.getMessage().contains("ORA-00001"))
                return "ERROR: Department '" + name + "' already exists.";
            return "DB ERROR: " + e.getMessage();
        }
    }

    public List<Department> getAllDepartments() throws SQLException {
        return deptDAO.getAllDepartments();
    }

    public String updateDepartment(int deptId, String name, String location) {
        if (!InputValidator.isValidDeptId(deptId))  return "ERROR: Invalid department ID.";
        if (!InputValidator.isValidName(name))       return "ERROR: Invalid department name.";
        if (!InputValidator.isNotBlank(location))    return "ERROR: Location cannot be blank.";
        try {
            Department d = new Department(deptId, name, location);
            return deptDAO.updateDepartment(d)
                    ? "SUCCESS: Department updated."
                    : "ERROR: Department ID " + deptId + " not found.";
        } catch (SQLException e) { return "DB ERROR: " + e.getMessage(); }
    }

    public String deleteDepartment(int deptId) {
        try {
            return deptDAO.deleteDepartment(deptId)
                    ? "SUCCESS: Department deleted."
                    : "ERROR: Department ID " + deptId + " not found.";
        } catch (SQLException e) {
            if (e.getMessage().contains("ORA-02292"))
                return "ERROR: Cannot delete — employees exist in this department.";
            return "DB ERROR: " + e.getMessage();
        }
    }

    public void showDeptHeadcountReport() {
        try { deptDAO.printDeptHeadcountReport(); }
        catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }

    // ════════════════════════════════════════════════════════
    //  EMPLOYEE OPERATIONS
    // ════════════════════════════════════════════════════════

    public String addEmployee(String firstName, String lastName, String email,
                              String phone, String jobTitle, int deptId) {
        if (!InputValidator.isValidName(firstName))  return "ERROR: Invalid first name.";
        if (!InputValidator.isValidName(lastName))   return "ERROR: Invalid last name.";
        if (!InputValidator.isValidEmail(email))     return "ERROR: Invalid email format.";
        if (!InputValidator.isValidPhone(phone))     return "ERROR: Invalid phone (10-digit Indian mobile).";
        if (!InputValidator.isNotBlank(jobTitle))    return "ERROR: Job title cannot be blank.";
        if (!InputValidator.isValidDeptId(deptId))   return "ERROR: Invalid department ID.";

        try {
            // Check department exists
            if (deptDAO.getDepartmentById(deptId) == null)
                return "ERROR: Department ID " + deptId + " does not exist.";

            Employee emp = new Employee(firstName, lastName, email, phone, jobTitle, deptId);
            return empDAO.addEmployee(emp)
                    ? "SUCCESS: Employee '" + firstName + " " + lastName + "' added."
                    : "ERROR: Insert failed.";
        } catch (SQLException e) {
            if (e.getMessage().contains("ORA-00001"))
                return "ERROR: Email '" + email + "' already exists.";
            return "DB ERROR: " + e.getMessage();
        }
    }

    public Employee getEmployee(int empId) throws SQLException {
        return empDAO.getEmployeeById(empId);
    }

    public List<Employee> getAllEmployees() throws SQLException {
        return empDAO.getAllEmployees();
    }

    public List<Employee> searchEmployee(String keyword) throws SQLException {
        if (!InputValidator.isNotBlank(keyword)) return getAllEmployees();
        return empDAO.searchByName(keyword);
    }

    public String updateEmployee(int empId, String firstName, String lastName,
                                 String email, String phone, String jobTitle, int deptId) {
        if (!InputValidator.isValidEmpId(empId))     return "ERROR: Invalid employee ID.";
        if (!InputValidator.isValidName(firstName))  return "ERROR: Invalid first name.";
        if (!InputValidator.isValidName(lastName))   return "ERROR: Invalid last name.";
        if (!InputValidator.isValidEmail(email))     return "ERROR: Invalid email.";
        if (!InputValidator.isValidPhone(phone))     return "ERROR: Invalid phone.";
        if (!InputValidator.isNotBlank(jobTitle))    return "ERROR: Job title cannot be blank.";
        if (!InputValidator.isValidDeptId(deptId))   return "ERROR: Invalid department ID.";

        try {
            Employee emp = new Employee(empId, firstName, lastName, email,
                    phone, null, jobTitle, deptId, 'Y');
            return empDAO.updateEmployee(emp)
                    ? "SUCCESS: Employee updated."
                    : "ERROR: Employee ID " + empId + " not found.";
        } catch (SQLException e) { return "DB ERROR: " + e.getMessage(); }
    }

    public String deactivateEmployee(int empId) {
        if (!InputValidator.isValidEmpId(empId)) return "ERROR: Invalid employee ID.";
        try {
            return empDAO.deactivateEmployee(empId)
                    ? "SUCCESS: Employee " + empId + " deactivated."
                    : "ERROR: Employee not found.";
        } catch (SQLException e) { return "DB ERROR: " + e.getMessage(); }
    }

    public void showEmployeeReport() {
        try { empDAO.printEmployeeReport(); }
        catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }

    // ════════════════════════════════════════════════════════
    //  SALARY OPERATIONS
    // ════════════════════════════════════════════════════════

    public String addSalary(int empId, double basic, double bonus,
                            double deductions, String month, int year) {
        if (!InputValidator.isValidEmpId(empId))      return "ERROR: Invalid employee ID.";
        if (!InputValidator.isValidSalary(basic))     return "ERROR: Basic salary must be >= 0.";
        if (!InputValidator.isValidSalary(bonus))     return "ERROR: Bonus must be >= 0.";
        if (!InputValidator.isValidSalary(deductions))return "ERROR: Deductions must be >= 0.";
        if (!InputValidator.isValidMonth(month))      return "ERROR: Invalid month name.";
        if (!InputValidator.isValidYear(year))        return "ERROR: Invalid year.";

        try {
            Salary sal = new Salary(empId, basic, bonus, deductions, month.toUpperCase(), year);
            return salDAO.addSalary(sal)
                    ? "SUCCESS: Salary record added."
                    : "ERROR: Insert failed.";
        } catch (SQLException e) { return "DB ERROR: " + e.getMessage(); }
    }

    public List<Salary> getSalaryHistory(int empId) throws SQLException {
        return salDAO.getSalaryByEmployee(empId);
    }

    public void showPayrollReport(String month, int year) {
        try { salDAO.printPayrollReport(month, year); }
        catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }

    public void showDeptSalaryReport() {
        try { salDAO.printDeptSalaryReport(); }
        catch (SQLException e) { System.err.println("DB ERROR: " + e.getMessage()); }
    }
}
