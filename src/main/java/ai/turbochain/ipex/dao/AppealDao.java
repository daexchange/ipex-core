package ai.turbochain.ipex.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.constant.AppealStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.Appeal;

import java.util.Map;

/**
 * @author GS
 * @date 2018年01月23日
 */
public interface AppealDao extends BaseDao<Appeal> {

    @Query("select count(a.id) as complainantNum from Appeal a where a.initiatorId = :memberId")
    Long getBusinessAppealInitiatorIdStatistics(@Param("memberId")Long memberId);

    @Query("select count(a.id) as defendantNum from Appeal a where a.associateId = :memberId")
    Long getBusinessAppealAssociateIdStatistics(@Param("memberId")Long memberId);

    long countAllByStatus(AppealStatus status);
}
