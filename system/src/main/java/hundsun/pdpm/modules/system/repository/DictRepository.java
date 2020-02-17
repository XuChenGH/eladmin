package hundsun.pdpm.modules.system.repository;

import hundsun.pdpm.modules.system.domain.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
public interface DictRepository extends JpaRepository<Dict, Long>, JpaSpecificationExecutor<Dict> {

    Dict findByName(String name);

    List<Dict> findAllByNameIn(List<String> name);
}
