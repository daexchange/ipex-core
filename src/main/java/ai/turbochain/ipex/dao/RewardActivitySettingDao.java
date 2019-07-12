package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.constant.ActivityRewardType;
import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.RewardActivitySetting;

/**
 * @author GS
 * @date 2018年03月08日
 */
public interface RewardActivitySettingDao extends BaseDao<RewardActivitySetting> {
    RewardActivitySetting findByStatusAndType(BooleanEnum booleanEnum, ActivityRewardType type);
}
