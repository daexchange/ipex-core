package ai.turbochain.ipex.service;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.dao.TransferSelfRecordDao;
import ai.turbochain.ipex.entity.TransferSelfRecord;
import ai.turbochain.ipex.pagination.Criteria;
import ai.turbochain.ipex.pagination.Restrictions;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.util.DateUtil;

@Service
public class TransferSelfRecordService extends BaseService<TransferSelfRecord> {
    @Autowired
    private TransferSelfRecordDao transferSelfRecordDao;

    /**
     * 保存交易记录
     *
     * @param WalletTransferRecord
     * @return
     */
    public TransferSelfRecord save(TransferSelfRecord bean) {
        return transferSelfRecordDao.saveAndFlush(bean);
    }
    
    
    public Page<TransferSelfRecord> queryByMember(Long uid, Integer pageNo, Integer pageSize,Integer type,String startDate,String endDate,String symbol) throws ParseException {
        Sort orders = Criteria.sortStatic("createTime.desc");
        //分页参数
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, orders);
        //查询条件
        Criteria<TransferSelfRecord> specification = new Criteria<TransferSelfRecord>();
        specification.add(Restrictions.eq("memberId", uid, false));
        if(type != null){
            specification.add(Restrictions.eq("type",type,false));
        }
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
            specification.add(Restrictions.gte("createTime",DateUtil.YYYY_MM_DD_MM_HH_SS.parse(startDate+" 00:00:00"),false));
            specification.add(Restrictions.lte("createTime",DateUtil.YYYY_MM_DD_MM_HH_SS.parse(endDate+" 23:59:59"),false));
        }
        if(StringUtils.isNotEmpty(symbol)){
            specification.add(Restrictions.eq("coin.name",symbol,false));
        }
       
        return transferSelfRecordDao.findAll(specification, pageRequest);
    }
}
