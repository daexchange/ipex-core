package ai.turbochain.ipex.service;

import com.alibaba.fastjson.JSONObject;

import ai.turbochain.ipex.constant.PageModel;
import ai.turbochain.ipex.constant.PromotionLevel;
import ai.turbochain.ipex.constant.PromotionRewardType;
import ai.turbochain.ipex.dao.MemberPromotionDao;
import ai.turbochain.ipex.dao.RewardPromotionSettingDao;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.MemberPromotion;
import ai.turbochain.ipex.entity.RewardPromotionSetting;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.vo.RegisterPromotionVO;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author GS
 * @date 2018年03月08日
 */
@Service
public class MemberPromotionService extends BaseService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MemberPromotionDao memberPromotionDao;

    @Autowired
    private RewardPromotionSettingService rewardPromotionSettingService ;

    public MemberPromotion save(MemberPromotion memberPromotion){
        return memberPromotionDao.save(memberPromotion);
    }

    public Page<RegisterPromotionVO> getPromotionDetails(long memberId, PageModel pageModel){

        StringBuilder headSql = new StringBuilder("select a.id id ,a.username presentee,a.email presenteeEmail, ")
                .append("a.mobile_phone presenteePhone,a.real_name presenteeRealName,a.registration_time promotionTime, ")
                .append("b.level promotionLevel ") ;

        StringBuilder endSql = new StringBuilder(" from member a join member_promotion b on a.inviter_id = b.inviter_id and a.inviter_id = "+memberId);

        StringBuilder countHead = new StringBuilder("select count(*) ") ;
        Page<RegisterPromotionVO> page = createNativePageQuery(countHead.append(endSql),headSql.append(endSql),pageModel,Transformers.aliasToBean(RegisterPromotionVO.class)) ;
        RewardPromotionSetting setting = rewardPromotionSettingService.findByType(PromotionRewardType.REGISTER) ;

        BigDecimal one = JSONObject.parseObject(setting.getInfo()).getBigDecimal("one");
        BigDecimal two = JSONObject.parseObject(setting.getInfo()).getBigDecimal("two");

        for(RegisterPromotionVO vo:page.getContent()){
            vo.setUnit(setting.getCoin().getUnit());
            if(vo.getPromotionLevel()== PromotionLevel.ONE.getOrdinal()){
                vo.setReward(one);
            }else if(vo.getPromotionLevel()== PromotionLevel.TWO.getOrdinal()){
                vo.setReward(two);
            }else{
                vo.setReward(BigDecimal.ZERO);
            }
        }
        return page ;
    }
}
