package ai.turbochain.ipex.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.InitPlate;

public interface InitPlateDao extends BaseDao<InitPlate> {

    @Query(value = "select * from init_plate where symbol=:symbol" ,nativeQuery = true)
    InitPlate findInitPlateBySymbol(@Param("symbol") String symbol);
}
