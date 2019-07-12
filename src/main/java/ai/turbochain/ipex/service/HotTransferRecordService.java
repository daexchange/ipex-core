package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.dao.HotTransferRecordDao;
import ai.turbochain.ipex.entity.HotTransferRecord;
import ai.turbochain.ipex.service.Base.TopBaseService;

@Service
public class HotTransferRecordService extends TopBaseService<HotTransferRecord,HotTransferRecordDao> {

    @Override
    @Autowired
    public void setDao(HotTransferRecordDao dao) {
        super.setDao(dao);
    }
}
