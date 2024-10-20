package su.project.travel.repository.Impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

@Repository("TripRepo")
public class UserTripRepositoryImpl implements TripRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserTripRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Integer insertTbTrip(Integer userId, String tripName) {
        String sql = """
                insert into tb_trip (user_id, create_date, trip_name)
                values (:user_id, :create_date, :trip_name)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("create_date", LocalDateTime.now());
        mapSqlParameterSource.addValue("trip_name", tripName);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource, keyHolder, new String[]{"trip_id"});

        return keyHolder.getKey().intValue();
    }

    @Override
    public void insertTbTripDetails(Integer tripId, Integer placeId, LocalDateTime planDate) {
        String sql = """
                insert into tb_trip_detail (trip_id,place_id,plan_date)
                values (:trip_id, :place_id, :plan_date)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("trip_id", tripId);
        mapSqlParameterSource.addValue("place_id", placeId);
        mapSqlParameterSource.addValue("plan_date", planDate);

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    public void updateTbTrip(Integer userId, String tripName, Integer tripId) {
        String sql = """
                UPDATE tb_trip SET trip_name = :trip_name WHERE trip_id = :trip_id AND user_id = :user_id;
                """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("user_id", userId);
        mapSqlParameterSource.addValue("trip_name", tripName);
        mapSqlParameterSource.addValue("trip_id", tripId);
        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);

    }

    public void updateTbTripDetails(Integer tripId, List<TripDetails> tripDetailsList) {
        List<Integer> getTripDetailIds = new ArrayList<>();
        for (TripDetails tripDetails : tripDetailsList) {
            if (ObjectUtils.isNotEmpty(tripDetails.getTripDetailId())) {
                getTripDetailIds.add(tripDetails.getTripDetailId());
            }
        }

        String sql = """
                DELETE FROM tb_trip_detail WHERE trip_id = :trip_id AND trip_detail_id NOT IN(:trip_detail_id);
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("trip_id", tripId);
        mapSqlParameterSource.addValue("trip_detail_id", getTripDetailIds);
        if (!getTripDetailIds.isEmpty()) {
            namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
        }
        for (TripDetails tripDetails : tripDetailsList) {
            if (ObjectUtils.isEmpty(tripDetails.getTripDetailId())) {

                String planDateString = tripDetails.getPlanDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime planDate = LocalDateTime.parse(planDateString, formatter);

                String sql2 =
                        """
                                INSERT INTO tb_trip_detail (trip_id,place_id,plan_date) values (:trip_id, :place_id, :plan_date)
                                """;
                MapSqlParameterSource mapSqlParameterSource2 = new MapSqlParameterSource();
                mapSqlParameterSource2.addValue("trip_id", tripId);
                mapSqlParameterSource2.addValue("plan_date", planDate);
                mapSqlParameterSource2.addValue("place_id", tripDetails.getPlaceId());

                namedParameterJdbcTemplate.update(sql2, mapSqlParameterSource2);
            } else {

                String planDateString = tripDetails.getPlanDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime planDate = LocalDateTime.parse(planDateString, formatter);

                String sql2 =
                        """
                                UPDATE tb_trip_detail SET plan_date = :plan_date WHERE trip_detail_id = :trip_detail_id;
                                """;

                MapSqlParameterSource mapSqlParameterSource2 = new MapSqlParameterSource();
                mapSqlParameterSource2.addValue("plan_date", planDate);
                mapSqlParameterSource2.addValue("trip_detail_id", tripDetails.getTripDetailId());
                namedParameterJdbcTemplate.update(sql2, mapSqlParameterSource2);
            }
        }

    }

    public List<InquiryTripResponse> inquiryTripResponseList(Integer userId) {
        String sql = """
                select tt.trip_id,
                       trip_name,
                       (array_agg(tp.photo))[1] as photo,
                       tt.create_date,
                        CASE
                        WHEN CURRENT_DATE > MAX(td.plan_date) THEN 'Y'
                            ELSE 'N'
                        END AS is_finish,
                        tt.is_review
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
                response.setIsFinish(rs.getString("is_finish"));
                response.setIsReview(rs.getString("is_review"));
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
                    td.trip_detail_id,
                    td.place_id,
                    td.plan_date,
                    tp.name,
                    tp.photo[1] AS photo,
                    tp.longitude,
                    tp.latitude,
                    td.user_rating
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
                details.setLongitude(rs.getDouble("longitude"));  // ดึง latitude
                details.setLatitude(rs.getDouble("latitude"));    // ดึง longitudedetails.setLongitude(rs.getDouble("longitude"));  // ดึง latitude
                Timestamp planDate = rs.getTimestamp("plan_date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                details.setPlanDate(planDate.toLocalDateTime().format(formatter));
                details.setTripDetailId(rs.getInt("trip_detail_id"));
                details.setUserRating(rs.getInt("user_rating"));
                return details;
            }
        });

        tripResponse.setPlaceList(tripDetails);
        return List.of(tripResponse);
    }

    public void updateUserRating(List<TripDetails> tripDetails) {

        String sql = """
                UPDATE tb_trip_detail SET user_rating = :user_rating WHERE trip_detail_id = :trip_detail_id;
                """;
        for (int i = 0; i < tripDetails.size(); i++) {
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("user_rating", tripDetails.get(i).getUserRating());
            mapSqlParameterSource.addValue("trip_detail_id", tripDetails.get(i).getTripDetailId());
            namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
        }
        String sql2 = """
                UPDATE tb_trip
                SET is_review = 'Y'
                WHERE trip_id = (select trip_id from tb_trip_detail where trip_detail_id = :trip_detials);
                """;
        MapSqlParameterSource parameter = new MapSqlParameterSource();
        parameter.addValue("trip_detials", tripDetails.getFirst().getTripDetailId());
        namedParameterJdbcTemplate.update(sql2, parameter);

    }

}
