package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.dao.SmsDao;
import ai.turbochain.ipex.dto.SmsDTO;
import ai.turbochain.ipex.service.Base.BaseService;

/**
 * @Description:
 * @author: GuoShuai
 * @date: create in 9:44 2018/6/28
 * @Modified:
 */
@Service
public class SmsService extends BaseService{
    
    @Autowired
    private SmsDao smsDao;

    /**
     * 获取有效的短信配置
     * @return
     */
    public SmsDTO getByStatus(){
        return smsDao.findBySmsStatus();
    }
    
    
}
