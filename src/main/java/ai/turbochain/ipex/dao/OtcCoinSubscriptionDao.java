package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.OtcCoin;
import ai.turbochain.ipex.entity.OtcCoinSubscription;

/**
 *
 * @author 
 * @date 2019年12月18日
 */
public interface OtcCoinSubscriptionDao extends BaseDao<OtcCoinSubscription> {

	OtcCoinSubscription findByOtcCoinAndOrigin(OtcCoin otcCoin,Integer origin);
}
