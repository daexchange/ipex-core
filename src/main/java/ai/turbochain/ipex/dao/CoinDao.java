package ai.turbochain.ipex.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.constant.CommonStatus;
import ai.turbochain.ipex.dto.CoinDTO;
import ai.turbochain.ipex.entity.Coin;

/**
 * @author GS
 * @description 货币操作
 * @date 2017/12/29 14:41
 */
@Repository
public interface CoinDao extends JpaRepository<Coin, String>, JpaSpecificationExecutor<Coin>, QueryDslPredicateExecutor<Coin> {
    Coin findByUnit(String unit);

    List<Coin> findAllByCanWithdrawAndStatusAndHasLegal(BooleanEnum is, CommonStatus status, boolean hasLegal);

    List<Coin> findAllByStatusAndHasLegal(CommonStatus status, boolean hasLegal);

    Coin findCoinByIsPlatformCoin(BooleanEnum is);

    List<Coin> findByHasLegal(Boolean hasLegal);

    @Query("select a from Coin a where a.unit in (:units) ")
    List<Coin> findAllByOtc(@Param("units") List<String> otcUnits);

    @Query("select a.name from Coin a")
    List<String> findAllName();

    @Query(value = "select  new ai.turbochain.ipex.dto.CoinDTO(a.name,a.unit) from Coin a")
    List<CoinDTO> findAllNameAndUnit();

    @Query("select a.name from Coin a where a.hasLegal = true ")
    List<String> findAllCoinNameLegal();

    @Query("select a.unit from Coin a where a.enableRpc = 1")
    List<String> findAllRpcUnit();

    List<Coin> findAllByIsPlatformCoin(BooleanEnum isPlatformCoin);

    @Query("select a.unit from Coin a where a.status = :status order by a.sort")
    Set<String> findAllUnit(@Param("status") CommonStatus status);
}
