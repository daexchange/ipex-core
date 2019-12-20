package ai.turbochain.ipex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import ai.turbochain.ipex.constant.PageModel;
import ai.turbochain.ipex.dao.MemberDepositDao;
import ai.turbochain.ipex.entity.MemberDeposit;
import ai.turbochain.ipex.entity.QMember;
import ai.turbochain.ipex.entity.QMemberDeposit;
import ai.turbochain.ipex.pagination.Criteria;
import ai.turbochain.ipex.pagination.Restrictions;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.vo.MemberDepositVO;

@Service
public class MemberDepositService extends BaseService<MemberDeposit> {

    @Autowired
    private MemberDepositDao memberDepositDao ;

    public Page<MemberDepositVO> page(List<BooleanExpression> predicates,PageModel pageModel){
        JPAQuery<MemberDepositVO> query = queryFactory.select(Projections.fields(MemberDepositVO.class,
                QMemberDeposit.memberDeposit.id.as("id"),
                QMember.member.username,
                QMember.member.id.as("memberId"),
                QMemberDeposit.memberDeposit.address,
                QMemberDeposit.memberDeposit.amount,
                QMemberDeposit.memberDeposit.createTime.as("createTime"),
                QMemberDeposit.memberDeposit.unit)).from(QMember.member,QMemberDeposit.memberDeposit)
                .where(predicates.toArray(new BooleanExpression[predicates.size()]));
        List<OrderSpecifier> orderSpecifiers = pageModel.getOrderSpecifiers();
        query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()])) ;
        long total = query.fetchCount() ;
        query.offset(pageModel.getPageSize()*(pageModel.getPageNo()-1)).limit(pageModel.getPageSize());
        List<MemberDepositVO> list = query.fetch() ;
        return new PageImpl<MemberDepositVO>(list,pageModel.getPageable(),total);
    }
    
	@Transactional(readOnly = true)
	public Page<MemberDeposit> findAllByMemberId(Long memberId, int page, int pageSize) {
		Sort orders = Criteria.sortStatic("id.desc");
		PageRequest pageRequest = new PageRequest(page, pageSize, orders);
		Criteria<MemberDeposit> specification = new Criteria<MemberDeposit>();
		specification.add(Restrictions.eq("memberId", memberId, false));
		return memberDepositDao.findAll(specification, pageRequest);
	}

}
