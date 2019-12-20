package ai.turbochain.ipex.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.MemberDeposit;

public interface MemberDepositDao extends BaseDao<MemberDeposit>{
    MemberDeposit findByAddressAndTxid(String address,String txid);

    @Query(value="select unit ,sum(amount) from member_deposit where date_format(create_time,'%Y-%m-%d') = :date group by unit",nativeQuery = true)
    List<Object[]> getDepositStatistics(@Param("date")String date);

}
