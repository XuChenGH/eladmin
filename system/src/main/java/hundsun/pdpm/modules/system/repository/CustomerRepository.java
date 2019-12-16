package hundsun.pdpm.modules.system.repository;


import hundsun.pdpm.modules.system.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author yantt
* @date 2019-11-29
*/
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {

    List<Customer> findAllByIdIn(List<String> idlist);

    void  deleteAllByIdIn(List<String> idlist);

}
