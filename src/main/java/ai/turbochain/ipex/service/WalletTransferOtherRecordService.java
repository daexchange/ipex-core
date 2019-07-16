package ai.turbochain.ipex.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.dsl.BooleanExpression;

import ai.turbochain.ipex.dao.WalletTransferOtherRecordDao;
import ai.turbochain.ipex.entity.Member;
import ai.turbochain.ipex.entity.TransferOtherRecord;
import ai.turbochain.ipex.entity.TransferOtherRecord;
import ai.turbochain.ipex.service.Base.BaseService;

@Service
public class WalletTransferOtherRecordService extends BaseService<TransferOtherRecord> {
    @Autowired
    private WalletTransferOtherRecordDao walletTransferOtherRecordDao;

    /**
     * 保存交易记录
     *
     * @param WalletTransferRecord
     * @return
     */
    public TransferOtherRecord save(TransferOtherRecord bean) {
        return walletTransferOtherRecordDao.saveAndFlush(bean);
    }
    
    
    public Page<TransferOtherRecord> queryRewardPromotionPage(int pageNo, int pageSize, Member member) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, sort);

        ArrayList<BooleanExpression> booleanExpressions = new ArrayList<>();

        Specification specification = new Specification<TransferOtherRecord>() {
            List<Predicate> predicates = new ArrayList<>();

            @Override
            public Predicate toPredicate(Root<TransferOtherRecord> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicates.add(criteriaBuilder.equal(root.get("memberIdFrom"), member.getId()));
               // predicates.add(criteriaBuilder.equal(root.get("type"), 0));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return walletTransferOtherRecordDao.findAll(specification,pageable);
    }

}
