package ai.turbochain.ipex.dao;

import java.util.List;

import ai.turbochain.ipex.constant.DepositStatusEnum;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.DepositRecord;
import ai.turbochain.ipex.entity.Member;

/**
 * @author zhang yingxin
 * @date 2018/5/7
 */
public interface DepositRecordDao extends BaseDao<DepositRecord> {
    public DepositRecord findById(String id);

    public List<DepositRecord> findByMemberAndStatus(Member member, DepositStatusEnum status);
}
