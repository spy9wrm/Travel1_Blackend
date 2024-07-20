package su.project.travel.repository.Impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import su.project.travel.model.response.InquiryTripResponse;
import su.project.travel.repository.TripRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository("TripRepo")
public class UserTripRepositoryImpl implements TripRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserTripRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Integer insertTbTrip(Integer userId,String tripName) {
        String sql = """
                insert into tb_trip (user_id, create_date, trip_name)
                values (:user_id, :create_date, :trip_name)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("create_date", LocalDateTime.now());
        mapSqlParameterSource.addValue("trip_name", tripName);

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
        mapSqlParameterSource.addValue("plan_date",planDate);

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public List<InquiryTripResponse> inquiryTripResponseList(Integer userId) {
        String sql = """
                select tt.trip_id,
                       trip_name,
                       (array_agg(tp.photo))[1] as photo,
                       tt.create_date
                from tb_trip tt
                         inner join tb_trip_detail td on td.trip_id = tt.trip_id
                         inner join (select place_id, unnest(photo) as photo
                                     from tb_place) tp on tp.place_id = td.place_id
                where tt.user_id = :user_id
                group by tt.trip_id, trip_name, tt.create_date
                """;

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("user_id", userId);

        return namedParameterJdbcTemplate.query(sql, parameters, new RowMapper<InquiryTripResponse>() {
            @Override
            public InquiryTripResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                InquiryTripResponse response = new InquiryTripResponse();
                response.setTripId(rs.getInt("trip_id"));
                response.setTripName(rs.getString("trip_name"));
                response.setPhoto(rs.getString("photo"));
                response.setCreateDate(rs.getString("create_date"));
                return response;
            }
        });
    }
}
