package hundsun.pdpm.modules.datapermission.repository;

import hundsun.pdpm.modules.datapermission.domain.DataPermission;
import hundsun.pdpm.modules.datapermission.domain.DataPermissionField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-13
*/
public interface DataPermissionFieldRepository extends JpaRepository<DataPermissionField, String>, JpaSpecificationExecutor<DataPermissionField> {
List<DataPermissionField> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);

List<DataPermissionField> findAllByTableIn(List<DataPermission> idList);

void  deleteAllByTable(DataPermission dataPermission);
}
