package ai.turbochain.ipex.service;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.constant.TransactionType;
import ai.turbochain.ipex.dao.WalletTransferRecordDao;
import ai.turbochain.ipex.entity.MemberTransaction;
import ai.turbochain.ipex.entity.WalletTransferRecord;
import ai.turbochain.ipex.pagination.Criteria;
import ai.turbochain.ipex.pagination.Restrictions;
import ai.turbochain.ipex.service.Base.BaseService;
import ai.turbochain.ipex.util.DateUtil;

@Service
public class WalletTransferRecordService extends BaseService<WalletTransferRecord> {
    @Autowired
    private WalletTransferRecordDao walletTransferRecordDao;

    /**
     * 保存交易记录
     *
     * @param WalletTransferRecord
     * @return
     */
    public WalletTransferRecord save(WalletTransferRecord bean) {
        return walletTransferRecordDao.saveAndFlush(bean);
    }
    
    
    public Page<WalletTransferRecord> queryByMember(Long uid, Integer pageNo, Integer pageSize,TransactionType type,String startDate,String endDate,String symbol) throws ParseException {
        //排序方式 (需要倒序 这样    Criteria.sort("id","createTime.desc") ) //参数实体类为字段名
        Sort orders = Criteria.sortStatic("createTime.desc");
        //分页参数
        PageRequest pageRequest = new PageRequest(pageNo-1, pageSize, orders);
        //查询条件
        Criteria<WalletTransferRecord> specification = new Criteria<WalletTransferRecord>();
        specification.add(Restrictions.eq("memberId", uid, false));
        if(type != null){
            specification.add(Restrictions.eq("type",type,false));
        }
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
            specification.add(Restrictions.gte("createTime",DateUtil.YYYY_MM_DD_MM_HH_SS.parse(startDate+" 00:00:00"),false));
            specification.add(Restrictions.lte("createTime",DateUtil.YYYY_MM_DD_MM_HH_SS.parse(endDate+" 23:59:59"),false));
        }
        if(StringUtils.isNotEmpty(symbol)){
            specification.add(Restrictions.eq("symbol",symbol,false));
        }
       
        return walletTransferRecordDao.findAll(specification, pageRequest);
    }
}
