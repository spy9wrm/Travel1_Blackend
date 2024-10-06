package su.project.travel.repository.Impl;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import su.project.travel.model.tourism.Tourism;
import su.project.travel.repository.UpdateDbRepository;

@Repository
public class UpdateDBimpl implements UpdateDbRepository
{
   private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public UpdateDBimpl (NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void UpdateDb(Tourism tourism){
        for(int i =0;i<tourism.getResults().size();i++){
            String sql = """
                    UPDATE tb_place SET longitude = :longitude, latitude = :latitude WHERE place_id = :placeId
                    """;
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("longitude", tourism.getResults().get(i).getLongitude());
            mapSqlParameterSource.addValue("latitude", tourism.getResults().get(i).getLatitude());
            mapSqlParameterSource.addValue("placeId", tourism.getResults().get(i).getId());

            namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
        }
    }
}
