package ai.turbochain.ipex.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.constant.WithdrawStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.WithdrawRecord;

import java.util.List;

/**
 * @author GS
 * @date 2018年01月29日
 */
public interface WithdrawRecordDao extends BaseDao<WithdrawRecord> {

    @Query(value = "select a.unit , sum(b.total_amount) amount,sum(b.fee) from withdraw_record b,coin a where a.name = b.coin_id and date_format(b.deal_time,'%Y-%m-%d') = :date and b.status = 3 group by a.unit",nativeQuery = true)
    List<Object[]> getWithdrawStatistics(@Param("date")String date);

    long countAllByStatus(WithdrawStatus status);
}
