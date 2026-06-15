# Employee Management System
### Java + Oracle SQL + JDBC | IntelliJ IDEA Setup Guide

---

## PROJECT STRUCTURE

```
EmployeeManagementSystem/
├── src/
│   └── com/ems/
│       ├── model/
│       │   ├── Department.java      ← data object for departments
│       │   ├── Employee.java        ← data object for employees
│       │   └── Salary.java          ← data object for salary
│       ├── dao/
│       │   ├── DepartmentDAO.java   ← all SQL for departments
│       │   ├── EmployeeDAO.java     ← all SQL for employees
│       │   └── SalaryDAO.java       ← all SQL for salary
│       ├── service/
│       │   └── EmployeeService.java ← business logic + validation
│       ├── util/
│       │   ├── DBConnection.java    ← JDBC singleton connection
│       │   └── InputValidator.java  ← input validation methods
│       └── ui/
│           └── MainMenu.java        ← console UI + main() method
├── sql/
│   ├── 01_setup.sql                 ← run this first in SQL Developer
│   └── 02_reports.sql               ← optional report queries
├── lib/
│   └── ojdbc11.jar                  ← put Oracle JDBC driver here
└── README.md
```

---

## STEP 1 — Install Oracle Database XE

1. Download **Oracle Database 21c Express Edition (XE)** from:
   https://www.oracle.com/database/technologies/xe-downloads.html

2. Install it. During install, set a password for `SYS` and `SYSTEM` (e.g. `oracle`)

3. After install, Oracle runs automatically on port **1521**.

4. Open **SQL Developer** (free download from Oracle) and connect:
   - Username: `system`
   - Password: `oracle` (what you set)
   - Hostname: `localhost`
   - Port: `1521`
   - SID: `xe`

---

## STEP 2 — Run the SQL Setup Script

1. In SQL Developer, open `sql/01_setup.sql`
2. Press **F5** or click the "Run Script" button (▶ with lines)
3. You should see at the bottom:
   ```
   departments  4
   employees    5
   salary       5
   ```
   That means all tables are created and sample data is inserted.

---

## STEP 3 — Download Oracle JDBC Driver

1. Go to: https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html
2. Download **ojdbc11.jar** (for Oracle 21c/23c)
3. Copy `ojdbc11.jar` into the `lib/` folder of this project

---

## STEP 4 — Set Up IntelliJ IDEA Project

### 4.1 — Create the Project
1. Open IntelliJ IDEA
2. **File → New → Project**
3. Choose **Java** (not Maven/Gradle — plain Java)
4. Set project name: `EmployeeManagementSystem`
5. Set project location to the folder you extracted
6. Click **Finish**

### 4.2 — Set the Source Root
1. Right-click the `src` folder in the Project panel
2. **Mark Directory as → Sources Root**
   (It should turn blue)

### 4.3 — Add the JDBC Driver (ojdbc11.jar)
1. **File → Project Structure** (Ctrl+Alt+Shift+S)
2. Go to **Modules → Dependencies** tab
3. Click **+** (bottom left) → **JARs or Directories**
4. Browse to `lib/ojdbc11.jar` → **OK**
5. Make sure scope is **Compile**
6. Click **Apply → OK**

### 4.4 — Set Main Class
1. **Run → Edit Configurations**
2. Click **+** → **Application**
3. Name: `EMS`
4. Main class: `com.ems.ui.MainMenu`
5. Click **Apply → OK**

---

## STEP 5 — Configure DB Connection

Open `src/com/ems/util/DBConnection.java` and update:

```java
private static final String URL      = "jdbc:oracle:thin:@localhost:1521:xe";
private static final String USERNAME = "system";   // ← your Oracle username
private static final String PASSWORD = "oracle";   // ← your Oracle password
```

> If using Oracle 21c with pluggable DB, try URL:
> `jdbc:oracle:thin:@localhost:1521/XEPDB1`

---

## STEP 6 — Run the Application

1. Press **Shift+F10** or click the green ▶ button
2. You should see in the console:
   ```
   ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
      EMPLOYEE MANAGEMENT SYSTEM  v1.0
   ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★

   [DB] Connection established successfully.

   ────────────────────────────────────────
     MAIN MENU
   ────────────────────────────────────────
     1. Department Management
     2. Employee Management
     3. Salary Management
     4. Reports
     0. Exit
   ────────────────────────────────────────
   ```

---

## HOW TO USE

### Add an Employee (example flow)
```
Main Menu → 2 (Employee Management) → 1 (Add Employee)
  First name  : Rahul
  Last name   : Dhope
  Email       : rahul.dhope@ems.com
  Phone       : 9834449302
  Job title   : Software Engineer
  Dept ID     : 1
  SUCCESS: Employee 'Rahul Dhope' added.
```

### View Payroll Report
```
Main Menu → 4 (Reports) → 3 (Payroll Report)
  Enter month : JUNE
  Enter year  : 2025
```

---

## COMMON ERRORS & FIXES

| Error | Fix |
|-------|-----|
| `ClassNotFoundException: oracle.jdbc.driver.OracleDriver` | ojdbc11.jar not added to dependencies. Redo Step 4.3 |
| `ORA-01017: invalid username/password` | Wrong credentials in DBConnection.java |
| `Connection refused (localhost:1521)` | Oracle service not running. Open Services → start OracleServiceXE |
| `ORA-00955: name already used` | Tables exist already. The DROP in 01_setup.sql handles this. |

---

## CONCEPTS DEMONSTRATED (for interviews)

- **Encapsulation** — all model fields private, accessed via getters/setters
- **Inheritance** — constructors overloaded for different use cases
- **Polymorphism** — method overloading in constructors
- **JDBC** — `DriverManager`, `PreparedStatement`, `ResultSet`, `Connection`
- **DAO Pattern** — separates SQL from business logic
- **Service Layer** — validation and orchestration
- **Exception Handling** — try-catch on all DB calls, custom error messages
- **Singleton Pattern** — single DB connection via DBConnection class
- **SQL** — DDL (CREATE, DROP), DML (INSERT, UPDATE, DELETE, SELECT), JOINs,
            GROUP BY, subqueries, window functions, sequences, constraints
