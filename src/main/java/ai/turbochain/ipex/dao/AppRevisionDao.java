package ai.turbochain.ipex.dao;

import ai.turbochain.ipex.constant.Platform;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.AppRevision;

/**
 * @author GS
 * @Title: ${file_name}
 * @Description:
 * @date 2018/4/2416:18
 */
public interface AppRevisionDao extends BaseDao<AppRevision> {
    AppRevision findAppRevisionByPlatformOrderByIdDesc(Platform platform);
}
