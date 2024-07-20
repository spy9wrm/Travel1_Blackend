package su.project.travel.repository.Impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import su.project.travel.repository.TripRepository;

import java.time.LocalDateTime;

@Repository("TripRepo")
public class UserTripRepositoryImpl implements TripRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserTripRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Integer insertTbTrip(Integer userId) {
        String sql = """
                insert into tb_trip (user_id, create_date)
                values (:user_id, :create_date)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("create_date", LocalDateTime.now());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource, keyHolder, new String[] {"trip_id"});

        return keyHolder.getKey().intValue();
    }

    @Override
    public void insertTbTripDetails(Integer tripId,Integer placeId,LocalDateTime planDate) {
        String sql = """
                insert into tb_trip_detail (trip_id,place_id,plan_date)
                values (:trip_id, :place_id, :plan_date)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("trip_id", tripId);
        mapSqlParameterSource.addValue("place_id",placeId);
        mapSqlParameterSource.addValue("place_date",planDate);

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }
}
