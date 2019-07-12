package ai.turbochain.ipex.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ai.turbochain.ipex.dao.base.BaseDao;
import ai.turbochain.ipex.entity.SysPermission;

/**
 * @author GS
 * @date 2017年12月18日
 */
public interface SysPermissionDao extends BaseDao<SysPermission> {

    @Modifying
    @Query(value="delete from admin_role_permission where rule_id = ?1",nativeQuery = true)
    int deletePermission(long permissionId);
}
