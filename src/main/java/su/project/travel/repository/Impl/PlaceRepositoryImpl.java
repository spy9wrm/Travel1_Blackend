package su.project.travel.repository.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import su.project.travel.model.request.PlaceDetailsRequest;
import su.project.travel.model.request.PlaceRequest;
import su.project.travel.model.request.UserRegisterRequest;
import su.project.travel.model.response.PlaceOpeningHours;
import su.project.travel.model.response.PlaceResponse;
import su.project.travel.model.response.PredictResponse;
import su.project.travel.repository.PlaceRepository;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PlaceRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<PlaceResponse> getPlace(PlaceRequest placeRequest, List<PredictResponse> predictResponseList) {
        String sql = """
            SELECT place_id, name, description, type, photo, tourist_type,
                   maximum_attendee, has_map, latitude, longitude,
                   telephone, email, street_address, city,
                   city_sub_division, country, pr.province_name_th as country_sub_division,
                   post_code FROM tb_place pl
                LEFT JOIN tb_province pr ON pl.country_sub_division = pr.province_id
            WHERE 1=1
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();

        String name = placeRequest.getSearch();
        String province = placeRequest.getProvince();
        String type = placeRequest.getType();
        String groups = placeRequest.getTouristType();

        if (!predictResponseList.isEmpty()) {
            List<String> predictNames = predictResponseList.stream()
                    .map(PredictResponse::getPlaceName)  // Assuming PlaceResponse has a getName() method
                    .collect(Collectors.toList());

            sql += " AND pl.name IN (:predict)";
            params.addValue("predict", predictNames);
        }


        if (StringUtils.isNotEmpty(name)) {
            sql += " AND pl.name LIKE :name ";
            params.addValue("name", "%" + name + "%");
        }

        if (StringUtils.isNotEmpty(province)) {
            sql += " AND pr.province_name_th LIKE :province ";
            params.addValue("province", "%" + province + "%");
        }

        if (StringUtils.isNotEmpty(groups)) {
            sql += " AND :touristType = ANY(pl.tourist_type) ";
            params.addValue("touristType", groups);
        }

        if (StringUtils.isNotEmpty(type)) {
            sql += " AND :type = ANY(pl.type) ";
            params.addValue("type", type);
        }

        sql += " ORDER BY pl.place_id ";
        sql += " OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY ";
        params.addValue("offset", placeRequest.getPage() * placeRequest.getSize());
        params.addValue("limit", placeRequest.getSize());

        return namedParameterJdbcTemplate.query(sql, params, new RowMapper<PlaceResponse>() {
            @Override
            public PlaceResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlaceResponse place = new PlaceResponse();
                place.setPlaceId(rs.getInt("place_id"));
                place.setName(rs.getString("name"));
                place.setDescription(convertPgArrayToList(rs.getArray("description")));
                place.setType(convertPgArrayToList(rs.getArray("type")));
                place.setPhoto(convertPgArrayToList(rs.getArray("photo")));
                place.setTouristType(convertPgArrayToList(rs.getArray("tourist_type")));
                place.setMaximumAttendee(rs.getInt("maximum_attendee"));
                place.setHasMap(rs.getString("has_map"));
                place.setLatitude(rs.getDouble("latitude"));
                place.setLongitude(rs.getDouble("longitude"));
                place.setTelephone(rs.getString("telephone"));
                place.setEmail(rs.getString("email"));
                place.setStreetAddress(convertPgArrayToList(rs.getArray("street_address")));
                place.setCity(rs.getString("city"));
                place.setCitySubDivision(rs.getString("city_sub_division"));
                place.setCountry(rs.getString("country"));
                place.setCountrySubDivision(rs.getString("country_sub_division"));
                place.setPostCode(rs.getString("post_code"));
                return place;
            }

            private List<String> convertPgArrayToList(Array pgArray) throws SQLException {
                if (pgArray == null) {
                    return null;
                }
                Object[] array = (Object[]) pgArray.getArray();
                if (array == null) {
                    return null;
                }
                return Arrays.stream(array)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.toList());
            }
        });
    }


    public List<PlaceResponse> getPlaceDetails(PlaceDetailsRequest placeDetailsRequest) {
        String sql = """
                SELECT place_id, name, description, type, photo, tourist_type,
                       maximum_attendee, has_map, latitude, longitude,
                       telephone, email, street_address, city,
                       city_sub_division, country, pr.province_name_th as country_sub_division,
                       post_code FROM tb_place pl
                    LEFT JOIN tb_province pr ON pl.country_sub_division = pr.province_id
                WHERE pl.place_id = ?
                """;

        List<Object> params = new ArrayList<>();
        params.add(placeDetailsRequest.getPlaceId());
        List<PlaceResponse> placeResponses = jdbcTemplate.query(sql, params.toArray(), new RowMapper<PlaceResponse>() {
            @Override
             public PlaceResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlaceResponse place = new PlaceResponse();
                place.setPlaceId(rs.getInt("place_id"));
                place.setName(rs.getString("name"));
                place.setDescription(convertPgArrayToList(rs.getArray("description")));
                place.setType(convertPgArrayToList(rs.getArray("type")));
                place.setPhoto(convertPgArrayToList(rs.getArray("photo")));
                place.setTouristType(convertPgArrayToList(rs.getArray("tourist_type")));
                place.setMaximumAttendee(rs.getInt("maximum_attendee"));
                place.setHasMap(rs.getString("has_map"));
                place.setLatitude(rs.getDouble("latitude"));
                place.setLongitude(rs.getDouble("longitude"));
                place.setTelephone(rs.getString("telephone"));
                place.setEmail(rs.getString("email"));
                place.setStreetAddress(convertPgArrayToList(rs.getArray("street_address")));
                place.setCity(rs.getString("city"));
                place.setCitySubDivision(rs.getString("city_sub_division"));
                place.setCountry(rs.getString("country"));
                place.setCountrySubDivision(rs.getString("country_sub_division"));
                place.setPostCode(rs.getString("post_code"));
                return place;
            }

            private List<String> convertPgArrayToList(Array pgArray) throws SQLException {
                if (pgArray == null) {
                    return null;
                }
                Object[] array = (Object[]) pgArray.getArray();
                if (array == null) {
                    return null;
                }
                return Arrays.stream(array)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.toList());
            }
        });
        sql = """
                    select place_id, td.name_th as day_of_week, opens, closes
                    from tb_day td
                             left join (select place_id,day_of_week, opens, closes from tb_place_opening_hours where place_id = ?) ph
                                       on ph.day_of_week = td.day_id
                    order by td.day_id;
                """;
        List<PlaceOpeningHours> placeOpeningHours = jdbcTemplate.query(sql, params.toArray(), new RowMapper<PlaceOpeningHours>() {
            @Override
            public PlaceOpeningHours mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlaceOpeningHours place = new PlaceOpeningHours();
                place.setPlaceId(rs.getInt("place_id"));
                place.setDayOfWeek(rs.getString("day_of_week"));
                place.setOpens(rs.getString("opens"));
                place.setCloses(rs.getString("closes"));
                return place;
            }
        });
        placeResponses.getFirst().setOpeningHours(placeOpeningHours);


        return placeResponses;
    }


}
