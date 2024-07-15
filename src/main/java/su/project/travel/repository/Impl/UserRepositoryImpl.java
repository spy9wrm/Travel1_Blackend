package su.project.travel.repository.Impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import su.project.travel.model.response.UserResponse;
import su.project.travel.repository.UserRepository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private JdbcTemplate jdbcTemplate;
    
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserResponse> getUsers() {
        String sql = "SELECT id_user, name, username, password, age, sex FROM tb_users;";

        // Use jdbcTemplate to execute query and map results to UserResponse objects
        List<UserResponse> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserResponse.class));

        return users;
    }
}
