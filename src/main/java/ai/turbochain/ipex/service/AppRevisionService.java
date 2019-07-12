package ai.turbochain.ipex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.turbochain.ipex.constant.Platform;
import ai.turbochain.ipex.dao.AppRevisionDao;
import ai.turbochain.ipex.entity.AppRevision;
import ai.turbochain.ipex.service.Base.TopBaseService;

/**
 * @author GS
 * @Title: ${file_name}
 * @Description:
 * @date 2018/4/2416:19
 */
@Service
public class AppRevisionService extends TopBaseService<AppRevision, AppRevisionDao> {

    @Override
    @Autowired
    public void setDao(AppRevisionDao dao) {
        super.setDao(dao);
    }

    public AppRevision findRecentVersion(Platform p){
        return dao.findAppRevisionByPlatformOrderByIdDesc(p);
    }
}
