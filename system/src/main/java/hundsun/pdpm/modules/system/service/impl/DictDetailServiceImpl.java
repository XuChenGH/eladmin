package hundsun.pdpm.modules.system.service.impl;

import hundsun.pdpm.annotation.Excel;
import hundsun.pdpm.modules.system.domain.Dict;
import hundsun.pdpm.modules.system.repository.DictDetailRepository;
import hundsun.pdpm.modules.system.repository.DictRepository;
import hundsun.pdpm.modules.system.service.mapper.DictDetailMapper;
import hundsun.pdpm.utils.PageUtil;
import hundsun.pdpm.utils.QueryHelp;
import hundsun.pdpm.utils.StringUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.modules.system.domain.DictDetail;
import hundsun.pdpm.modules.system.service.dto.DictDetailQueryCriteria;
import hundsun.pdpm.modules.system.service.DictDetailService;
import hundsun.pdpm.modules.system.service.dto.DictDetailDTO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@Service
@CacheConfig(cacheNames = "dictDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictDetailServiceImpl implements DictDetailService {

    private final DictDetailRepository dictDetailRepository;

    private final DictDetailMapper dictDetailMapper;

    private final DictRepository dictRepository;


    public DictDetailServiceImpl(DictDetailRepository dictDetailRepository, DictDetailMapper dictDetailMapper,DictRepository dictRepository) {
        this.dictDetailRepository = dictDetailRepository;
        this.dictDetailMapper = dictDetailMapper;
        this.dictRepository = dictRepository;
    }

    @Override
    @Cacheable
    public Map queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {
        Page<DictDetail> page = dictDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dictDetailMapper::toDto));
    }

    @Override
    public Map<String, List<DictDetail>> queryAll(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, List<DictDetail>> map = new HashMap<>();
        List<String> dictNameList = new ArrayList<>();
        for(Field field:fields){
            Excel excel = field.getAnnotation(Excel.class);
            if(excel!=null){
                List<String> dictName =Arrays.asList(excel.dictname().split(","));
                if(!CollectionUtils.isEmpty(dictName)){
                    dictNameList.addAll(dictName);
                }
            }
        }
        if(!CollectionUtils.isEmpty(dictNameList)){
            List<Dict> dictList = dictRepository.findAllByNameIn(dictNameList);
            for(Dict dict : dictList){
                map.put(dict.getName(),dict.getDictDetails());
            }
        }
        return map;
    }

    @Override
    @Cacheable(key = "#p0")
    public DictDetailDTO findById(Long id) {
        DictDetail dictDetail = dictDetailRepository.findById(id).orElseGet(DictDetail::new);
        ValidationUtil.isNull(dictDetail.getId(),"DictDetail","id",id);
        return dictDetailMapper.toDto(dictDetail);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public DictDetailDTO create(DictDetail resources) {
        return dictDetailMapper.toDto(dictDetailRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        DictDetail dictDetail = dictDetailRepository.findById(resources.getId()).orElseGet(DictDetail::new);
        ValidationUtil.isNull( dictDetail.getId(),"DictDetail","id",resources.getId());
        resources.setId(dictDetail.getId());
        dictDetailRepository.save(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dictDetailRepository.deleteById(id);
    }
}
