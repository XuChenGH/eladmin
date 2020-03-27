package hundsun.pdpm.modules.ftpfile.service.impl;

import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.ftpfile.repository.FtpUserRepository;
import hundsun.pdpm.modules.system.service.DictDetailService;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import hundsun.pdpm.modules.ftpfile.service.FtpUserService;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpUserDTO;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpUserQueryCriteria;
import hundsun.pdpm.modules.ftpfile.service.mapper.FtpUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import hundsun.pdpm.modules.execl.ExcelHelper;
import hundsun.pdpm.modules.system.domain.DictDetail;
import org.springframework.web.multipart.MultipartFile;
import hundsun.pdpm.utils.PageUtil;
import hundsun.pdpm.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.stream.Collectors;
import hundsun.pdpm.utils.*;
/**
* @author yantt
* @date 2019-12-31
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FtpUserServiceImpl implements FtpUserService {

    private final FtpUserRepository ftpUserRepository;

    private final FtpUserMapper ftpUserMapper;

    @Autowired
    private DictDetailService dictDetailService;

    public FtpUserServiceImpl(FtpUserRepository ftpUserRepository, FtpUserMapper ftpUserMapper) {
        this.ftpUserRepository = ftpUserRepository;
        this.ftpUserMapper = ftpUserMapper;
    }

    @Override
    public Map<String,Object> queryAll(FtpUserQueryCriteria criteria, Pageable pageable){

        Page<FtpUser> page = ftpUserRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,FtpUserDTO.class),pageable);
        return PageUtil.toPage(page.map(ftpUserMapper::toDto));
    }

    @Override
    public List<FtpUserDTO> queryAll(FtpUserQueryCriteria criteria){

        return ftpUserMapper.toDto(ftpUserRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,FtpUserDTO.class)));
    }

    @Override
    public FtpUserDTO findById(Integer id) {
        FtpUser ftpUser = ftpUserRepository.findById(id).orElseGet(FtpUser::new);
        ValidationUtil.isNull(ftpUser.getId(),"FtpUser","id",id);
        return ftpUserMapper.toDto(ftpUser);
    }
    @Override
    public List<FtpUserDTO> findByIdlist(List<FtpUserDTO> ftpUserList) {
        if (CollectionUtils.isEmpty(ftpUserList)){
        return  new ArrayList<>();
        }
        List<Integer> idlist = ftpUserList.stream().map(FtpUserDTO::getId).collect(Collectors.toList());
        return ftpUserMapper.toDto(ftpUserRepository.findAllByIdIn(idlist));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FtpUserDTO create(FtpUser resources) {
        if(StringUtils.isEmpty(resources.getUsername())){
            resources.setUsername(SecurityUtils.getUsername());
        }
        return ftpUserMapper.toDto(ftpUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FtpUser resources) {
        FtpUser ftpUser = ftpUserRepository.findById(resources.getId()).orElseGet(FtpUser::new);
        ValidationUtil.isNull( ftpUser.getId(),"FtpUser","id",resources.getId());
        ftpUser.copy(resources);
        ftpUserRepository.save(ftpUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ftpUserRepository.deleteById(id);
    }


    @Override
    public void download(List<FtpUserDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FtpUserDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,FtpUserDTO.class,false);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<FtpUserDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FtpUserDTO.class);
       List<FtpUserDTO> data = ExcelHelper.importExcel(multipartFiles,FtpUserDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<FtpUser> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
//          for(FtpUserDTO ftpUserDTO:data){
//             if(!StringUtils.isEmpty(ftpUserDTO.getId())){
//                    //删除库中
//                 idlist.add(ftpUserDTO.getId());
//             }else {
//                 ftpUserDTO.setId(StringUtils.get32UUID());
//             }
//             savelist.add(ftpUserMapper.toEntity(ftpUserDTO));
//          }
       ftpUserRepository.deleteAllByIdIn(idlist);
       ftpUserRepository.saveAll(savelist);
       }
        return  data;
     }
}
