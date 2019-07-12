package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.constant.SignStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.Sign;

/**
 * @author GS
 * @Description:
 * @date 2018/5/311:10
 */
public interface SignDao extends BaseDao<Sign> {
    Sign findByStatus(SignStatus status);
}
