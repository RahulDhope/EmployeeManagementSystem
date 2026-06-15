-- ============================================================
--  Employee Management System - Report Queries
-- ============================================================

-- 1. All active employees with department
SELECT e.emp_id, e.first_name || ' ' || e.last_name AS full_name,
       e.job_title, d.dept_name, d.location, e.hire_date
FROM   employees e
JOIN   departments d ON e.dept_id = d.dept_id
WHERE  e.is_active = 'Y'
ORDER  BY d.dept_name, e.first_name;

-- 2. Department-wise headcount
SELECT d.dept_name, COUNT(e.emp_id) AS headcount
FROM   departments d
LEFT   JOIN employees e ON d.dept_id = e.dept_id AND e.is_active = 'Y'
GROUP  BY d.dept_name
ORDER  BY headcount DESC;

-- 3. Salary report for a given month/year
SELECT e.emp_id, e.first_name || ' ' || e.last_name AS employee,
       d.dept_name, s.basic_salary, s.bonus, s.deductions, s.net_salary,
       s.pay_month, s.pay_year
FROM   salary s
JOIN   employees e ON s.emp_id = e.emp_id
JOIN   departments d ON e.dept_id = d.dept_id
WHERE  s.pay_month = 'JUNE' AND s.pay_year = 2025
ORDER  BY s.net_salary DESC;

-- 4. Department-wise average salary
SELECT d.dept_name,
       ROUND(AVG(s.basic_salary), 2) AS avg_basic,
       ROUND(AVG(s.net_salary), 2)   AS avg_net,
       MIN(s.net_salary)             AS min_net,
       MAX(s.net_salary)             AS max_net
FROM   salary s
JOIN   employees e ON s.emp_id = e.emp_id
JOIN   departments d ON e.dept_id = d.dept_id
GROUP  BY d.dept_name;

-- 5. Employees earning above department average
SELECT e.emp_id, e.first_name || ' ' || e.last_name AS employee,
       d.dept_name, s.net_salary,
       ROUND(AVG(s2.net_salary) OVER (PARTITION BY d.dept_id), 2) AS dept_avg
FROM   employees e
JOIN   departments d ON e.dept_id = d.dept_id
JOIN   salary s  ON e.emp_id = s.emp_id
JOIN   salary s2 ON e.dept_id = (SELECT dept_id FROM employees WHERE emp_id = s2.emp_id)
WHERE  s.net_salary > (
           SELECT AVG(s3.net_salary)
           FROM   salary s3
           JOIN   employees e3 ON s3.emp_id = e3.emp_id
           WHERE  e3.dept_id = e.dept_id
       )
ORDER  BY dept_name;
