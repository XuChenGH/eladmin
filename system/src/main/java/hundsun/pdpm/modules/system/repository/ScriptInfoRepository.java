package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.ScriptInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-18
*/
public interface ScriptInfoRepository extends JpaRepository<ScriptInfo, String>, JpaSpecificationExecutor<ScriptInfo> {
List<ScriptInfo> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);
}
