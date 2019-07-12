package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.constant.PromotionRewardType;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.RewardPromotionSetting;

/**
 * @author GS
 * @date 2018年03月08日
 */
public interface RewardPromotionSettingDao extends BaseDao<RewardPromotionSetting> {
    RewardPromotionSetting findByStatusAndType(BooleanEnum booleanEnum, PromotionRewardType type);
}
