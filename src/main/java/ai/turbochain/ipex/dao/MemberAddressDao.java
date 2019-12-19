package ai.turbochain.ipex.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.constant.CommonStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.Coin;
import ai.turbochain.ipex.entity.MemberAddress;

/**
 * @author GS
 * @date 2018年01月26日
 */
public interface MemberAddressDao extends BaseDao<MemberAddress> {
	@Modifying
	@Query("update MemberAddress a set a.deleteTime=:date,a.status=1 where a.status=0 and a.id=:id and a.memberId=:memberId")
	int deleteMemberAddress(@Param("date") Date date, @Param("id") long id, @Param("memberId") long memberId);

	@Modifying
	@Query("update MemberAddress a set a.address=:address, a.remark=:remark where a.id=:id and a.coin=:coin and a.memberId=:memberId")
	int updateMemberAddressOfwithDraw(@Param("address") String address, @Param("remark") String remark,
			@Param("id") long id, @Param("memberId") long memberId, @Param("coin") Coin coin);

	@Query("select a from MemberAddress a where a.coin=:coin and a.memberId=:memberId")
	MemberAddress findByMemberIdAndCoinId(@Param("memberId") long memberId, @Param("coin") Coin coin);

	List<MemberAddress> findAllByMemberIdAndAddressAndStatus(Long memberId, String address, CommonStatus status);

}
