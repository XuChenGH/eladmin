package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.FunctionScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-27
*/
public interface FunctionScriptRepository extends JpaRepository<FunctionScript, String>, JpaSpecificationExecutor<FunctionScript> {
List<FunctionScript> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);
void  deleteAllByFunctionIdEqualsAndScriptIdEquals(String functionId,String scriptId);
void deleteAllByFunctionIdEquals(String functionId);
List<FunctionScript> findAllByFunctionId(String functionId);
List<FunctionScript> deleteAllByFunctionId(String functionId);
List<FunctionScript> findAllByScriptId(String scriptId);
List<FunctionScript> findAllByScriptIdIn(List<String> scriptId);
}
