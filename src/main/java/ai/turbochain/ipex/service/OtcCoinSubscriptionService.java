package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.constant.PageModel;
import ai.turbochain.ipex.dao.OtcCoinSubscriptionDao;
import ai.turbochain.ipex.entity.OtcCoin;
import ai.turbochain.ipex.entity.OtcCoinSubscription;
import ai.turbochain.ipex.service.Base.BaseService;

/**
*
* @author 
* @date 2019年12月18日
*/
@Service
public class OtcCoinSubscriptionService extends BaseService<OtcCoinSubscription> {
    @Autowired
    private OtcCoinSubscriptionDao otcCoinSubscriptionDao;

    @Override
    public Page<OtcCoinSubscription> findAll(com.querydsl.core.types.Predicate predicate, PageModel pageModel) {
        return otcCoinSubscriptionDao.findAll(predicate, pageModel.getPageable());
    }
   
    public OtcCoinSubscription findByOtcCoinIdAndOrigin(Long otcCoinId, Integer origin) {
    	OtcCoin otcCoin = new OtcCoin();
    	
    	otcCoin.setId(otcCoinId);
    	
    	return otcCoinSubscriptionDao.findByOtcCoinAndOrigin(otcCoin, origin);
    }

}
