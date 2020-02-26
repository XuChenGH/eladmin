package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.FunctionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2019-12-17
*/
public interface FunctionInfoRepository extends JpaRepository<FunctionInfo, String>, JpaSpecificationExecutor<FunctionInfo> {
List<FunctionInfo> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);

List<FunctionInfo> findAllByFunctionNameEquals(String sfunctionName);

List<FunctionInfo> findAllByFunctionNameEqualsAndFunctionModeEqualsAndProductIdEquals(String sfunctionName,String sFunctionMode,String sProductId);
}
