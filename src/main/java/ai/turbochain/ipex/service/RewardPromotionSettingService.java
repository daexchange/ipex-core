package ai.turbochain.ipex.service;

import com.querydsl.core.types.Predicate;

import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.constant.PromotionRewardType;
import ai.turbochain.ipex.dao.RewardPromotionSettingDao;
import ai.turbochain.ipex.entity.QRewardPromotionSetting;
import ai.turbochain.ipex.entity.RewardPromotionSetting;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.service.Base.TopBaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GS
 * @date 2018年03月08日
 */
@Service
public class RewardPromotionSettingService  extends TopBaseService<RewardPromotionSetting,RewardPromotionSettingDao> {

    @Override
    @Autowired
    public void setDao(RewardPromotionSettingDao dao) {
        super.setDao(dao);
    }

    public RewardPromotionSetting findByType(PromotionRewardType type){
        return dao.findByStatusAndType(BooleanEnum.IS_TRUE, type);
    }

    @Override
    public RewardPromotionSetting save(RewardPromotionSetting setting){
        return dao.save(setting);
    }

    public void deletes(long[] ids){
        for(long id : ids){
            delete(id);
        }
    }

}
