package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.DataDictionary;

/**
 * @author GS
 * @Title: ${file_name}
 * @Description:
 * @date 2018/4/1214:15
 */
public interface DataDictionaryDao extends BaseDao<DataDictionary> {
    DataDictionary findByBond(String bond);
}
