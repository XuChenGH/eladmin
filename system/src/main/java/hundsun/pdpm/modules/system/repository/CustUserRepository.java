package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.CustUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author yantt
* @date 2019-11-30
*/
public interface CustUserRepository extends JpaRepository<CustUser, Long>, JpaSpecificationExecutor<CustUser> {
}