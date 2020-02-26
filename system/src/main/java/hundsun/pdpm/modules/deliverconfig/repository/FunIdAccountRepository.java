package hundsun.pdpm.modules.deliverconfig.repository;

import hundsun.pdpm.modules.deliverconfig.domain.FunIdAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
/**
* @author yantt
* @date 2020-02-23
*/
public interface FunIdAccountRepository extends JpaRepository<FunIdAccount, String>, JpaSpecificationExecutor<FunIdAccount> {
List<FunIdAccount> findAllByIdIn(List<String> idlist);

void  deleteAllByIdIn(List<String> idlist);

void deleteAllByFunctionNoIn(List<String> functionList);
}
