package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.dao.DataDictionaryDao;
import ai.turbochain.ipex.entity.DataDictionary;
import ai.turbochain.ipex.service.Base.TopBaseService;

/**
 * @author GS
 * @Title: ${file_name}
 * @Description:
 * @date 2018/4/1214:19
 */
@Service
public class DataDictionaryService extends TopBaseService<DataDictionary, DataDictionaryDao> {
    @Autowired
    DataDictionaryDao dataDictionaryDao;

    @Override
    @Autowired
    public void setDao(DataDictionaryDao dao) {
        super.setDao(dao);
    }

    public DataDictionary findByBond(String bond) {
        return dataDictionaryDao.findByBond(bond);
    }

}
