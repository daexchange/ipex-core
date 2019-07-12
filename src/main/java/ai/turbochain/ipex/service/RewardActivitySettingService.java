package ai.turbochain.ipex.service;

import com.querydsl.core.types.Predicate;

import ai.turbochain.ipex.constant.ActivityRewardType;
import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.dao.RewardActivitySettingDao;
import ai.turbochain.ipex.dto.PageParam;
import ai.turbochain.ipex.entity.QRewardActivitySetting;
import ai.turbochain.ipex.entity.QRewardPromotionSetting;
import ai.turbochain.ipex.entity.RewardActivitySetting;
import ai.turbochain.ipex.entity.RewardPromotionSetting;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.service.Base.TopBaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GS
 * @date 2018年03月08日
 */
@Service
public class RewardActivitySettingService extends TopBaseService<RewardActivitySetting,RewardActivitySettingDao> {

    @Override
    @Autowired
    public void setDao(RewardActivitySettingDao dao) {
        this.dao = dao ;
    }


    public RewardActivitySetting findByType(ActivityRewardType type){
        return dao.findByStatusAndType(BooleanEnum.IS_TRUE, type);
    }

    @Override
    public RewardActivitySetting save(RewardActivitySetting rewardActivitySetting){
        return dao.save(rewardActivitySetting);
    }

   /* public List<RewardActivitySetting> page(Predicate predicate){
        Pageable pageable = new PageRequest()
        Iterable<RewardActivitySetting> iterable = rewardActivitySettingDao.findAll(predicate, QRewardActivitySetting.rewardActivitySetting.updateTime.desc());
        return (List<RewardActivitySetting>) iterable ;
    }*/


}
