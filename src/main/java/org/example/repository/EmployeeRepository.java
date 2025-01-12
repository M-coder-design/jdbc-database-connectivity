package org.example.repository;

import org.example.model.Employee;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Row mapper to convert result set to Employee object
    private final RowMapper<Employee> employeeRowMapper = (rs, rowNum) -> {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setEmail(rs.getString("email"));
        employee.setSalary(rs.getBigDecimal("salary"));
        return employee;
    };

    public EmployeeRepository(JdbcTemplate jdbcTemplate,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Using JdbcTemplate for simple query
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql, employeeRowMapper);
    }

    // Using NamedParameterJdbcTemplate for parameterized query
    public Optional<Employee> findById(Long id) {
        String sql = "SELECT * FROM employees WHERE id = :id";

        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("id", id);

            Employee employee = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    params,
                    employeeRowMapper
            );

            return Optional.ofNullable(employee);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Using JdbcTemplate for insert with generated keys
    public Employee save(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, email, salary) " +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setString(3, employee.getEmail());
            ps.setBigDecimal(4, employee.getSalary());
            return ps;
        }, keyHolder);

        employee.setId(keyHolder.getKey().longValue());
        return employee;
    }

    // Using NamedParameterJdbcTemplate for update
    public void update(Employee employee) {
        String sql = "UPDATE employees SET first_name = :firstName, " +
                "last_name = :lastName, email = :email, salary = :salary " +
                "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", employee.getId())
                .addValue("firstName", employee.getFirstName())
                .addValue("lastName", employee.getLastName())
                .addValue("email", employee.getEmail())
                .addValue("salary", employee.getSalary());

        namedParameterJdbcTemplate.update(sql, params);
    }

    // Using JdbcTemplate for delete
    public void deleteById(Long id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Example of batch update using JdbcTemplate
    public void saveAll(List<Employee> employees) {
        String sql = "INSERT INTO employees (first_name, last_name, email, salary) " +
                "VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, employees, employees.size(),
                (ps, employee) -> {
                    ps.setString(1, employee.getFirstName());
                    ps.setString(2, employee.getLastName());
                    ps.setString(3, employee.getEmail());
                    ps.setBigDecimal(4, employee.getSalary());
                });
    }

    //deleting all employees
    public void deleteAll() {
        String sql = "DELETE FROM employees";
        jdbcTemplate.update(sql);
    }
}

