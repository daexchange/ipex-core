package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ai.turbochain.ipex.dao.TransferRecordDao;
import ai.turbochain.ipex.entity.TransferRecord;
import ai.turbochain.ipex.pagination.Criteria;
import ai.turbochain.ipex.pagination.Restrictions;
import ai.turbochain.ipex.service.Base.BaseService;

/**
 * @author GS
 * @date 2018年02月27日
 */
@Service
public class TransferRecordService extends BaseService {
    @Autowired
    private TransferRecordDao transferRecordDao;

    public TransferRecord save(TransferRecord transferRecord){
        return transferRecordDao.save(transferRecord);
    }

    @Transactional(readOnly = true)
    public Page<TransferRecord> findAllByMemberId(Long memberId, int page, int pageSize) {
        Sort orders = Criteria.sortStatic("id.desc");
        PageRequest pageRequest = new PageRequest(page, pageSize, orders);
        Criteria<TransferRecord> specification = new Criteria<>();
        specification.add(Restrictions.eq("memberId", memberId, false));
        return transferRecordDao.findAll(specification, pageRequest);
    }
}
