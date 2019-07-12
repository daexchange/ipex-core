package ai.turbochain.ipex.service;

import com.querydsl.core.types.Predicate;

import ai.turbochain.ipex.constant.CommonStatus;
import ai.turbochain.ipex.constant.PageModel;
import ai.turbochain.ipex.dao.BusinessAuthDepositDao;
import ai.turbochain.ipex.entity.BusinessAuthApply;
import ai.turbochain.ipex.entity.BusinessAuthDeposit;
import ai.turbochain.ipex.service.Base.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhang yingxin
 * @date 2018/5/5
 */
@Service
public class BusinessAuthDepositService extends BaseService {
    @Autowired
    private BusinessAuthDepositDao businessAuthDepositDao;

    @Override
    public Page<BusinessAuthDeposit> findAll(Predicate predicate, PageModel pageModel) {
        return businessAuthDepositDao.findAll(predicate, pageModel.getPageable());
    }

    public List<BusinessAuthDeposit> findAllByStatus(CommonStatus status){
        return businessAuthDepositDao.findAllByStatus(status);
    }

    public BusinessAuthDeposit findById(Long id){
        return businessAuthDepositDao.findOne(id);
    }

    public void save(BusinessAuthDeposit businessAuthDeposit){
        businessAuthDepositDao.save(businessAuthDeposit);
    }

    public void update(BusinessAuthDeposit businessAuthDeposit){
        businessAuthDepositDao.save(businessAuthDeposit);
    }
}
