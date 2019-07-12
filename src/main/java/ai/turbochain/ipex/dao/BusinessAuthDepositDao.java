package ai.turbochain.ipex.dao;

import java.util.List;

import ai.turbochain.ipex.constant.CommonStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.BusinessAuthDeposit;

/**
 * @author zhang yingxin
 * @date 2018/5/5
 */
public interface BusinessAuthDepositDao extends BaseDao<BusinessAuthDeposit> {
    public List<BusinessAuthDeposit> findAllByStatus(CommonStatus status);
}
