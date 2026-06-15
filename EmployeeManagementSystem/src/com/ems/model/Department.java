package com.ems.model;

/**
 * Model class representing a Department.
 * Follows encapsulation — all fields are private, accessed via getters/setters.
 */
public class Department {

    private int    deptId;
    private String deptName;
    private String location;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Department() {}

    public Department(int deptId, String deptName, String location) {
        this.deptId   = deptId;
        this.deptName = deptName;
        this.location = location;
    }

    // Constructor without ID (for INSERT — ID comes from sequence)
    public Department(String deptName, String location) {
        this.deptName = deptName;
        this.location = location;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int    getDeptId()   { return deptId; }
    public String getDeptName() { return deptName; }
    public String getLocation() { return location; }

    public void setDeptId(int deptId)      { this.deptId   = deptId; }
    public void setDeptName(String name)   { this.deptName = name; }
    public void setLocation(String loc)    { this.location = loc; }

    @Override
    public String toString() {
        return String.format("Dept[%d] %-20s | Location: %s", deptId, deptName, location);
    }
}
