package ai.turbochain.ipex.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ai.turbochain.ipex.constant.SysHelpClassification;
import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.SysHelp;

import java.util.List;

/**
 * @author GS
 * @description
 * @date 2018/1/9 9:58
 */
public interface SysHelpDao extends BaseDao<SysHelp> {
    List<SysHelp> findAllBySysHelpClassification(SysHelpClassification sysHelpClassification);

    @Query("select max(s.sort) from SysHelp s")
    int findMaxSort();

    @Query(value = "select * from sys_help WHERE sys_help_classification=:cate and is_top='0' ",nativeQuery = true)
    List<SysHelp> getCateTop(@Param("cate") String cate);


}
