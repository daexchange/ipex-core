package ai.turbochain.ipex.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.constant.PromotionRewardType;
import ai.turbochain.ipex.constant.RewardRecordType;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.RewardRecord;

import java.util.List;

/**
 * @author GS
 * @date 2018年03月08日
 */
public interface RewardRecordDao extends BaseDao<RewardRecord> {
    List<RewardRecord> findAllByMemberAndType(Member member, RewardRecordType type);

    @Query(value = "select coin_id , sum(amount) from reward_record where member_id = :memberId and type = :type group by coin_id",nativeQuery = true)
    List<Object[]> getAllPromotionReward(@Param("memberId") long memberId ,@Param("type") int type);
}
