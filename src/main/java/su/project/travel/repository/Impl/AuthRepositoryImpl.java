package su.project.travel.repository.Impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import su.project.travel.model.common.CurrentUser;
import su.project.travel.model.common.UserIdModel;
import su.project.travel.model.request.UserRegisterRequest;
import su.project.travel.repository.AuthRepository;

import java.util.List;

@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public AuthRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public CurrentUser login(String username, String password) {
        String sql = "SELECT user_id, name FROM tb_users WHERE username = ? AND password = ? AND is_deleted = 'N'";

        // Use jdbcTemplate to query the database and retrieve user_id and name
        List<CurrentUser> users = jdbcTemplate.query(sql, new Object[]{username, password}, (rs, rowNum) -> {
            CurrentUser user = new CurrentUser();
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("name"));
            return user;
        });

        // Check if the list is not empty and return the first user found
        if (!users.isEmpty()) {
            return users.get(0);
        }

        return null; // Return null if no user is found
    }

    public Integer insertTblUser(UserRegisterRequest userRequest) {
        String sql = "INSERT INTO tb_users (name, birthday, username, password, sex) " +
                "VALUES (:name, :birthday, :username, :password, :sex)";

        // Create a MapSqlParameterSource to hold named parameters and their values
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", userRequest.getName())
                .addValue("birthday", userRequest.getBirthday())
                .addValue("username", userRequest.getUsername())
                .addValue("password", userRequest.getPassword())
                .addValue("sex", userRequest.getSex());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"user_id"});

        return keyHolder.getKey().intValue();
    }

    public void insertToUserFav(UserRegisterRequest userRequest) {
        String sqlInsertFavoriteType = "INSERT INTO tb_user_favorite_types (user_id, type) " +
                "VALUES (:user_id, :type)";

        for(String type: userRequest.getTypeFav()) {
            MapSqlParameterSource paramsFavoriteType = new MapSqlParameterSource()
                    .addValue("user_id", userRequest.getUserId())
                    .addValue("type", type);

            namedParameterJdbcTemplate.update(sqlInsertFavoriteType, paramsFavoriteType);
        }

    }
}
