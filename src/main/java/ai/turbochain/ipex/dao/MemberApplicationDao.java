package ai.turbochain.ipex.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.constant.AuditStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.MemberApplication;

import java.util.List;

/**
 * @author GS
 * @description
 * @date 2017/12/26 15:12
 */
public interface MemberApplicationDao extends BaseDao<MemberApplication> {
    List<MemberApplication> findMemberApplicationByMemberAndAuditStatusOrderByIdDesc(Member var1, AuditStatus var2);

    long countAllByAuditStatus(AuditStatus auditStatus);
    @Query(value = "select count(1) from member_application where  id_card = :idCard and audit_status=0",nativeQuery = true)
    int queryByIdCard(@Param("idCard") String idCard);
}
