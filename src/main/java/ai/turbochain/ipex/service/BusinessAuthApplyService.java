package ai.turbochain.ipex.service;

import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import ai.turbochain.ipex.constant.CertifiedBusinessStatus;
import ai.turbochain.ipex.constant.PageModel;
import ai.turbochain.ipex.dao.BusinessAuthApplyDao;
import ai.turbochain.ipex.entity.BusinessAuthApply;
import ai.turbochain.ipex.entity.BusinessAuthApplyDetailVO;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.QBusinessAuthApply;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.util.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author zhang yingxin
 * @date 2018/5/7
 */
@Service
@Slf4j
public class BusinessAuthApplyService extends BaseService{
    @Autowired
    private BusinessAuthApplyDao businessAuthApplyDao;

    public Page<BusinessAuthApply> page(Predicate predicate, PageModel pageModel){
        return businessAuthApplyDao.findAll(predicate,pageModel.getPageable());
    }

    public List<BusinessAuthApply> findByMember(Member member){
        return businessAuthApplyDao.findByMemberOrderByIdDesc(member);
    }
    public BusinessAuthApply findOne(Long id){
        return businessAuthApplyDao.findOne(id);
    }

    public void create(BusinessAuthApply businessAuthApply){
        businessAuthApplyDao.save(businessAuthApply);
    }

    public void update(BusinessAuthApply businessAuthApply){
        businessAuthApplyDao.save(businessAuthApply);
    }

    public List<BusinessAuthApply> findByMemberAndCertifiedBusinessStatus(Member member, CertifiedBusinessStatus certifiedBusinessStatus){
        return businessAuthApplyDao.findByMemberAndCertifiedBusinessStatusOrderByIdDesc(member,certifiedBusinessStatus);
    }

    public BusinessAuthApply save(BusinessAuthApply businessAuthApply){
        return businessAuthApplyDao.save(businessAuthApply);
    }

    public  Page<BusinessAuthApply> page(Predicate predicate, Pageable pageable){
        return businessAuthApplyDao.findAll(predicate,pageable);
    }

    public MessageResult detail(Long id){
        QBusinessAuthApply qBusinessAuthApply = QBusinessAuthApply.businessAuthApply ;
        JPAQuery<BusinessAuthApplyDetailVO> query = queryFactory.select(
                Projections.fields(BusinessAuthApplyDetailVO.class,qBusinessAuthApply.id.as("id")
                        ,qBusinessAuthApply.certifiedBusinessStatus.as("status")
                        ,qBusinessAuthApply.amount.as("amount")
                        ,qBusinessAuthApply.authInfo.as("authInfo")
                        ,qBusinessAuthApply.member.realName.as("realName")
                        ,qBusinessAuthApply.detail.as("detail")
                        ,qBusinessAuthApply.auditingTime.as("checkTime"))).from(qBusinessAuthApply);

        query.where(qBusinessAuthApply.id.eq(id)) ;

        BusinessAuthApplyDetailVO vo = query.fetchOne() ;

        MessageResult result;
        String jsonStr = vo.getAuthInfo() ;
        log.info("认证信息 jsonStr = {}", jsonStr);
        if (StringUtils.isEmpty(jsonStr)) {
            result = MessageResult.error("认证相关信息不存在");
            result.setData(vo);
            return result;
        }
        try {
            JSONObject json = JSONObject.parseObject(jsonStr);
            vo.setInfo(json);
            result = MessageResult.success("认证详情");
            result.setData(vo);
            return result;
        } catch (Exception e) {
            log.info("认证信息格式异常:{}", e);
            result = MessageResult.error("认证信息格式异常");
            return result;
        }
    }

    public long countAuditing(){
        return businessAuthApplyDao.countAllByCertifiedBusinessStatus(CertifiedBusinessStatus.AUDITING) ;
    }

}
