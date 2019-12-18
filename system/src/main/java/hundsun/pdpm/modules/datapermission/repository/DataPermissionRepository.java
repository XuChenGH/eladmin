package hundsun.pdpm.modules.datapermission.repository;

import hundsun.pdpm.modules.datapermission.domain.DataPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-13
*/
public interface DataPermissionRepository extends JpaRepository<DataPermission, String>, JpaSpecificationExecutor<DataPermission> {
List<DataPermission> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);

List<DataPermission> findAllByRoleIdInAndTableCode(List<Long> roleIdList,String tableCode);


}
