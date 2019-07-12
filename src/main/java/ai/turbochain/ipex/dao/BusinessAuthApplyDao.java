package ai.turbochain.ipex.dao;

import java.util.List;

import ai.turbochain.ipex.constant.BooleanEnum;
import ai.turbochain.ipex.constant.CertifiedBusinessStatus;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.BusinessAuthApply;
import ai.turbochain.ipex.entity.Member;

/**
 * @author zhang yingxin
 * @date 2018/5/7
 */
public interface BusinessAuthApplyDao extends BaseDao<BusinessAuthApply> {

    List<BusinessAuthApply> findByMemberOrderByIdDesc(Member member);

    List<BusinessAuthApply> findByMemberAndCertifiedBusinessStatusOrderByIdDesc(Member member, CertifiedBusinessStatus certifiedBusinessStatus);

    long countAllByCertifiedBusinessStatus(CertifiedBusinessStatus status);

}
