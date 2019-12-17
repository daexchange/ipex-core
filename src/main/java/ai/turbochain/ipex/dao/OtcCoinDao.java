package ai.turbochain.ipex.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;

import ai.turbochain.ipex.constant.CommonStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.OtcCoin;
import ai.turbochain.ipex.service.OtcCoinService;

import java.util.List;

/**
 * @author GS
 * @date 2018年01月12日
 */
public interface OtcCoinDao extends BaseDao<OtcCoin> {

	OtcCoin findOtcCoinByUnitAndStatus(String unit, CommonStatus status);

	List<OtcCoin> findAllByStatus(CommonStatus status);

	OtcCoin findOtcCoinByUnit(String unit);

	@Query(value = "select * from OtcCoin where  name=:name")
	OtcCoin findOtcCoinByName(String name);

	@Query("select distinct a.unit from OtcCoin a where a.status = 0")
	List<String> findAllUnits();

}
