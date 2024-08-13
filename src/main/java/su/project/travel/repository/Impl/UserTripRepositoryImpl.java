package su.project.travel.repository.Impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import su.project.travel.model.common.TripDetails;
import su.project.travel.model.response.InquiryTripResponse;
import su.project.travel.model.response.TripResponse;
import su.project.travel.repository.TripRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                and tt.is_deleted = 'N'
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

    public void deleteTrip(Integer tripId) {
        String sql = """
                update tb_trip set is_deleted = 'Y' where trip_id = :trip_id
                """;
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("trip_id", tripId);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public List<TripResponse> getTripDetails(Integer tripId) {
        String tripSql = """
            SELECT 
                trip_id,
                trip_name,
                create_date
            FROM 
                tb_trip
            WHERE 
                trip_id = :trip_id;
            """;

        String tripDetailSql = """
            SELECT
                td.place_id,
                td.plan_date,
                tp.name,
                tp.photo[1] AS photo,
                tp.longitude,
                tp.latitude
            FROM
                tb_trip_detail td
                INNER JOIN tb_place tp ON tp.place_id = td.place_id
            WHERE
                td.trip_id = :trip_id;
            """;

        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("trip_id", tripId);

        // Fetch trip information
        TripResponse tripResponse = namedParameterJdbcTemplate.queryForObject(tripSql, parameter, new RowMapper<TripResponse>() {
            @Override
            public TripResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                TripResponse tripResponse = new TripResponse();
                tripResponse.setTripId(rs.getInt("trip_id"));
                tripResponse.setTripName(rs.getString("trip_name"));
                tripResponse.setCreateDate(rs.getString("create_date"));

                return tripResponse;
            }
        });

        // Fetch trip details
        List<TripDetails> tripDetails = namedParameterJdbcTemplate.query(tripDetailSql, parameter, new RowMapper<TripDetails>() {
            @Override
            public TripDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                TripDetails details = new TripDetails();
                details.setPlaceId(rs.getInt("place_id"));
                details.setPlaceName(rs.getString("name"));
                details.setPlacePhoto(rs.getString("photo"));
                Timestamp planDate = rs.getTimestamp("plan_date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                details.setPlanDate(planDate.toLocalDateTime().format(formatter));
                return details;
            }
        });

        tripResponse.setPlaceList(tripDetails);
        return List.of(tripResponse);
    }
}
