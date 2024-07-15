package su.project.travel.repository.Impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import su.project.travel.model.common.UserIdModel;
import su.project.travel.repository.AuthRepository;

import java.util.List;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private JdbcTemplate jdbcTemplate;

    public AuthRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserIdModel login(String username, String password) {
        String sql = "SELECT user_id FROM tb_users WHERE username = ? AND password = ? AND is_deleted = 'N'";
        List<Integer> userIds = jdbcTemplate.query(sql, new Object[]{username, password}, (rs, rowNum) -> rs.getInt("user_id"));

        if (!userIds.isEmpty()) {
            Integer userId = userIds.get(0);
            UserIdModel user = new UserIdModel();
            user.setUserId(userId);
            return user;
        }

        return null;
    }
}
