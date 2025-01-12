public List<Employee> findHighPaidEmployeesByDepartment(String department, BigDecimal minSalary) {
    String sql = "SELECT * FROM employees e " +
    "JOIN department_employees de ON e.id = de.employee_id " +
    "WHERE de.department_name = :dept " +
    "AND e.salary >= :minSalary";

    MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("dept", department)
        .addValue("minSalary", minSalary);
        
    return namedParameterJdbcTemplate.query(sql, params, employeeRowMapper);
}

public void updateSalariesInBatch(Map<Long, BigDecimal> salaryUpdates) {
    String sql = "UPDATE employees SET salary = :salary WHERE id = :id";

    List<MapSqlParameterSource> batchParams = salaryUpdates.entrySet().stream()
        .map(entry -> new MapSqlParameterSource()
            .addValue("id", entry.getKey())
            .addValue("salary", entry.getValue()))
        .collect(Collectors.toList());
        
    namedParameterJdbcTemplate.batchUpdate(sql, batchParams.toArray(new MapSqlParameterSource[0]));
}