-- ============================================================
--  Employee Management System - Database Setup
--  Run this script in Oracle SQL Developer or SQL*Plus
--  as your schema user (e.g. system or ems_user)
-- ============================================================

-- ── 1. DROP existing objects (safe re-run) ──────────────────
DROP TABLE salary        CASCADE CONSTRAINTS PURGE;
DROP TABLE employees     CASCADE CONSTRAINTS PURGE;
DROP TABLE departments   CASCADE CONSTRAINTS PURGE;

DROP SEQUENCE dept_seq;
DROP SEQUENCE emp_seq;
DROP SEQUENCE sal_seq;

-- ── 2. SEQUENCES (auto-increment IDs) ───────────────────────
CREATE SEQUENCE dept_seq START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE emp_seq  START WITH 1001 INCREMENT BY 1 NOCACHE;
CREATE SEQUENCE sal_seq  START WITH 1 INCREMENT BY 1 NOCACHE;

-- ── 3. DEPARTMENTS table ────────────────────────────────────
CREATE TABLE departments (
    dept_id     NUMBER        PRIMARY KEY,
    dept_name   VARCHAR2(100) NOT NULL UNIQUE,
    location    VARCHAR2(100) NOT NULL,
    created_at  DATE          DEFAULT SYSDATE
);

-- ── 4. EMPLOYEES table ──────────────────────────────────────
CREATE TABLE employees (
    emp_id      NUMBER         PRIMARY KEY,
    first_name  VARCHAR2(50)   NOT NULL,
    last_name   VARCHAR2(50)   NOT NULL,
    email       VARCHAR2(100)  NOT NULL UNIQUE,
    phone       VARCHAR2(15),
    hire_date   DATE           DEFAULT SYSDATE,
    job_title   VARCHAR2(100)  NOT NULL,
    dept_id     NUMBER         NOT NULL,
    is_active   CHAR(1)        DEFAULT 'Y' CHECK (is_active IN ('Y','N')),
    CONSTRAINT fk_emp_dept FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);

-- ── 5. SALARY table ─────────────────────────────────────────
CREATE TABLE salary (
    sal_id       NUMBER        PRIMARY KEY,
    emp_id       NUMBER        NOT NULL,
    basic_salary NUMBER(10,2)  NOT NULL,
    bonus        NUMBER(10,2)  DEFAULT 0,
    deductions   NUMBER(10,2)  DEFAULT 0,
    net_salary   NUMBER(10,2)  GENERATED ALWAYS AS (basic_salary + bonus - deductions) VIRTUAL,
    pay_month    VARCHAR2(20)  NOT NULL,
    pay_year     NUMBER(4)     NOT NULL,
    CONSTRAINT fk_sal_emp FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);

-- ── 6. SAMPLE DATA ───────────────────────────────────────────
-- Departments
INSERT INTO departments VALUES (dept_seq.NEXTVAL, 'Engineering',   'Bengaluru', SYSDATE);
INSERT INTO departments VALUES (dept_seq.NEXTVAL, 'Human Resources','Pune',     SYSDATE);
INSERT INTO departments VALUES (dept_seq.NEXTVAL, 'Finance',        'Mumbai',   SYSDATE);
INSERT INTO departments VALUES (dept_seq.NEXTVAL, 'Marketing',      'Hyderabad',SYSDATE);

-- Employees
INSERT INTO employees VALUES (emp_seq.NEXTVAL,'Rahul',  'Dhope',   'rahul.dhope@ems.com',  '9834449302', SYSDATE,'Software Engineer',   1,'Y');
INSERT INTO employees VALUES (emp_seq.NEXTVAL,'Priya',  'Sharma',  'priya.sharma@ems.com', '9876543210', SYSDATE,'HR Manager',           2,'Y');
INSERT INTO employees VALUES (emp_seq.NEXTVAL,'Amit',   'Patel',   'amit.patel@ems.com',   '9812345678', SYSDATE,'Financial Analyst',    3,'Y');
INSERT INTO employees VALUES (emp_seq.NEXTVAL,'Sneha',  'Kulkarni','sneha.kulkarni@ems.com','9823456789', SYSDATE,'Marketing Executive',  4,'Y');
INSERT INTO employees VALUES (emp_seq.NEXTVAL,'Vikram', 'Singh',   'vikram.singh@ems.com', '9845678901', SYSDATE,'Senior Developer',     1,'Y');

-- Salaries
INSERT INTO salary (sal_id,emp_id,basic_salary,bonus,deductions,pay_month,pay_year)
    VALUES (sal_seq.NEXTVAL, 1001, 55000, 5000, 3000, 'JUNE', 2025);
INSERT INTO salary (sal_id,emp_id,basic_salary,bonus,deductions,pay_month,pay_year)
    VALUES (sal_seq.NEXTVAL, 1002, 65000, 7000, 4000, 'JUNE', 2025);
INSERT INTO salary (sal_id,emp_id,basic_salary,bonus,deductions,pay_month,pay_year)
    VALUES (sal_seq.NEXTVAL, 1003, 60000, 6000, 3500, 'JUNE', 2025);
INSERT INTO salary (sal_id,emp_id,basic_salary,bonus,deductions,pay_month,pay_year)
    VALUES (sal_seq.NEXTVAL, 1004, 50000, 4000, 2500, 'JUNE', 2025);
INSERT INTO salary (sal_id,emp_id,basic_salary,bonus,deductions,pay_month,pay_year)
    VALUES (sal_seq.NEXTVAL, 1005, 75000, 9000, 5000, 'JUNE', 2025);

COMMIT;

-- ── 7. VERIFY ────────────────────────────────────────────────
SELECT 'departments' AS tbl, COUNT(*) AS rows FROM departments
UNION ALL
SELECT 'employees',          COUNT(*)         FROM employees
UNION ALL
SELECT 'salary',             COUNT(*)         FROM salary;
