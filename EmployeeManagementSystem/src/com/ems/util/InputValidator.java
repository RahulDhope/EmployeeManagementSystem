package com.ems.util;

/**
 * Utility class for input validation.
 * All methods are static — no need to create an object.
 */
public class InputValidator {

    // ── String / Name ─────────────────────────────────────────────────────────
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.trim().matches("[A-Za-z ]{2,50}");
    }

    // ── Email ─────────────────────────────────────────────────────────────────
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    // ── Phone ─────────────────────────────────────────────────────────────────
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return phone.trim().matches("[6-9][0-9]{9}"); // Indian mobile format
    }

    // ── Salary / Amount ───────────────────────────────────────────────────────
    public static boolean isValidSalary(double amount) {
        return amount >= 0;
    }

    // ── Employee ID ───────────────────────────────────────────────────────────
    public static boolean isValidEmpId(int id) {
        return id >= 1001;
    }

    // ── Department ID ─────────────────────────────────────────────────────────
    public static boolean isValidDeptId(int id) {
        return id >= 1;
    }

    // ── Year ──────────────────────────────────────────────────────────────────
    public static boolean isValidYear(int year) {
        return year >= 2000 && year <= 2100;
    }

    // ── Month ─────────────────────────────────────────────────────────────────
    public static boolean isValidMonth(String month) {
        String[] months = {"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE",
                           "JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};
        if (month == null) return false;
        for (String m : months) {
            if (m.equalsIgnoreCase(month.trim())) return true;
        }
        return false;
    }

    // ── Not blank ─────────────────────────────────────────────────────────────
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
