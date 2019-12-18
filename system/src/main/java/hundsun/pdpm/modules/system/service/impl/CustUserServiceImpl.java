package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.modules.system.domain.CustUser;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.utils.FileUtil;
import hundsun.pdpm.modules.system.repository.CustUserRepository;
import hundsun.pdpm.modules.system.service.CustUserService;
import hundsun.pdpm.modules.system.service.dto.CustUserDTO;
import hundsun.pdpm.modules.system.service.dto.CustUserQueryCriteria;
import hundsun.pdpm.modules.system.service.mapper.CustUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import hundsun.pdpm.utils.PageUtil;
import hundsun.pdpm.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author yantt
* @date 2019-11-30
*/
@Service
@CacheConfig(cacheNames = "custUser")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CustUserServiceImpl implements CustUserService {

    private final CustUserRepository custUserRepository;

    private final CustUserMapper custUserMapper;

    public CustUserServiceImpl(CustUserRepository custUserRepository, CustUserMapper custUserMapper) {
        this.custUserRepository = custUserRepository;
        this.custUserMapper = custUserMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(CustUserQueryCriteria criteria, Pageable pageable){
        Page<CustUser> page = custUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(custUserMapper::toDto));
    }

    @Override
    @Cacheable
    public List<CustUserDTO> queryAll(CustUserQueryCriteria criteria){
        return custUserMapper.toDto(custUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public CustUserDTO findById(Long id) {
        CustUser custUser = custUserRepository.findById(id).orElseGet(CustUser::new);
        ValidationUtil.isNull(custUser.getId(),"CustUser","id",id);
        return custUserMapper.toDto(custUser);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public CustUserDTO create(CustUser resources) {
        return custUserMapper.toDto(custUserRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(CustUser resources) {
        CustUser custUser = custUserRepository.findById(resources.getId()).orElseGet(CustUser::new);
        ValidationUtil.isNull( custUser.getId(),"CustUser","id",resources.getId());
        custUser.copy(resources);
        custUserRepository.save(custUser);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        custUserRepository.deleteById(id);
    }


    @Override
    public void download(List<CustUserDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CustUserDTO custUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客户ID", custUser.getCustId());
            map.put("用户ID", custUser.getUserId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
