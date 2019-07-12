package ai.turbochain.ipex.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.MemberLevel;

/**
 * @author GS
 * @description 会员等级Dao
 * @date 2017/12/26 17:24
 */
public interface MemberLevelDao extends BaseDao<MemberLevel> {

    MemberLevel findOneByIsDefault(Boolean isDefault);

    @Query("update MemberLevel set isDefault = false  where isDefault = true ")
    @Modifying
    int updateDefault();
}
