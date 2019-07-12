package ai.turbochain.ipex.dao;

import java.util.List;

import ai.turbochain.ipex.constant.CertifiedBusinessStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.BusinessCancelApply;
import ai.turbochain.ipex.entity.Member;

/**
 * @author jiangtao
 * @date 2018/5/17
 */
public interface BusinessCancelApplyDao extends BaseDao<BusinessCancelApply>{

    List<BusinessCancelApply> findByMemberAndStatusOrderByIdDesc(Member member , CertifiedBusinessStatus status);

    List<BusinessCancelApply> findByMemberOrderByIdDesc(Member member);

    long countAllByStatus(CertifiedBusinessStatus status);
}
