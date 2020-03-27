package hundsun.pdpm.modules.ftpfile.service.impl;

import com.jcraft.jsch.*;
import hundsun.pdpm.modules.ftpfile.domain.FtpFile;
import hundsun.pdpm.modules.datapermission.utils.PermissionUtils;
import hundsun.pdpm.modules.ftpfile.domain.FtpUser;
import hundsun.pdpm.modules.ftpfile.service.dto.SftpDTO;
import hundsun.pdpm.modules.system.service.DictDetailService;
import org.springframework.util.CollectionUtils;
import hundsun.pdpm.utils.ValidationUtil;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import hundsun.pdpm.modules.ftpfile.repository.FtpFileRepository;
import hundsun.pdpm.modules.ftpfile.service.FtpFileService;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileDTO;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileQueryCriteria;
import hundsun.pdpm.modules.ftpfile.service.mapper.FtpFileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import hundsun.pdpm.modules.execl.ExcelHelper;
import hundsun.pdpm.modules.system.domain.DictDetail;
import org.springframework.web.multipart.MultipartFile;
import hundsun.pdpm.utils.PageUtil;
import hundsun.pdpm.utils.QueryHelp;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import hundsun.pdpm.utils.*;
/**
* @author yantt
* @date 2019-12-31
*/
@Service
@CacheConfig(cacheNames = "ftpFile")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FtpFileServiceImpl implements FtpFileService {

    private final FtpFileRepository ftpFileRepository;

    private final FtpFileMapper ftpFileMapper;


    @Autowired
    private DictDetailService dictDetailService;

    public FtpFileServiceImpl(FtpFileRepository ftpFileRepository, FtpFileMapper ftpFileMapper) {
        this.ftpFileRepository = ftpFileRepository;
        this.ftpFileMapper = ftpFileMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(FtpFileQueryCriteria criteria, Pageable pageable){
        Page<FtpFile> page = ftpFileRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                   PermissionUtils.getPredicate(root,
                                       QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                       criteriaBuilder,FtpFileDTO.class),pageable);
        return PageUtil.toPage(page.map(ftpFileMapper::toDto));
    }

    @Override
    @Cacheable
    public List<FtpFileDTO> queryAll(FtpFileQueryCriteria criteria){
        return ftpFileMapper.toDto(ftpFileRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                                                PermissionUtils.getPredicate(root,
                                                QueryHelp.getPredicate(root,criteria,criteriaBuilder),
                                                criteriaBuilder,FtpFileDTO.class)));
    }

    @Override
    @Cacheable(key = "#p0")
    public FtpFileDTO findById(Integer id) {
        FtpFile ftpFile = ftpFileRepository.findById(id).orElseGet(FtpFile::new);
        ValidationUtil.isNull(ftpFile.getId(),"FtpFile","id",id);
        return ftpFileMapper.toDto(ftpFile);
    }
    @Override
    @Cacheable
    public List<FtpFileDTO> findByIdlist(List<FtpFileDTO> ftpFileList) {
        if (CollectionUtils.isEmpty(ftpFileList)){
        return  new ArrayList<>();
        }
        List<Integer> idlist = ftpFileList.stream().map(FtpFileDTO::getId).collect(Collectors.toList());
        return ftpFileMapper.toDto(ftpFileRepository.findAllByIdIn(idlist));
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public FtpFileDTO create(FtpFile resources) {
        return ftpFileMapper.toDto(ftpFileRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(FtpFile resources) {
        FtpFile ftpFile = ftpFileRepository.findById(resources.getId()).orElseGet(FtpFile::new);
        ValidationUtil.isNull( ftpFile.getId(),"FtpFile","id",resources.getId());
        ftpFile.copy(resources);
        ftpFileRepository.save(ftpFile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ftpFileRepository.deleteById(id);
    }


    @Override
    public void download(List<FtpFileDTO> all, HttpServletResponse response) throws IOException {
        Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FtpFileDTO.class);
        ExcelHelper.exportExcel(response,all,dictMap,FtpFileDTO.class,false);
    }
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public List<FtpFileDTO> upload(MultipartFile multipartFiles) throws Exception {
       Map<String, List<DictDetail>> dictMap = dictDetailService.queryAll(FtpFileDTO.class);
       List<FtpFileDTO> data = ExcelHelper.importExcel(multipartFiles,FtpFileDTO.class,dictMap,false);
       if(!CollectionUtils.isEmpty(data)){
          List<FtpFile> savelist =  new ArrayList<>();
          List<String> idlist = new ArrayList<>();
//          for(FtpFileDTO ftpFileDTO:data){
//             if(!StringUtils.isEmpty(ftpFileDTO.getId())){
//                    //删除库中
//                 idlist.add(ftpFileDTO.getId());
//             }else {
//                 ftpFileDTO.setId(StringUtils.get32UUID());
//             }
//             savelist.add(ftpFileMapper.toEntity(ftpFileDTO));
//          }
       ftpFileRepository.deleteAllByIdIn(idlist);
       ftpFileRepository.saveAll(savelist);
       }
        return  data;
     }


    @Override
    public void refresh(FtpUser ftpUser,String pathName,int parentId) throws Exception {
        if(ftpUser !=null){
            SftpDTO sftp = login(ftpUser.getSftpHost(),ftpUser.getSftpPort(),ftpUser.getName(),ftpUser.getPassword());
            if(!sftp.isBLogin()){
                throw new Exception("连接失败");
            }else {
                if(StringUtils.isEmpty(pathName)){
                   pathName = ftpUser.getSftpPath();
                   List<FtpFileDTO> files = getFileList(pathName,sftp);
                   save(files,ftpUser.getId(),0);
                }
            }
            disConnection(sftp);
        }
    }

    private void  save(List<FtpFileDTO> files,int ftpId,int parentId){
        //一级一级存储
        for(FtpFileDTO ftpFile:files){
            ftpFile.setFtpId(ftpId);
            ftpFile.setParentId(parentId);
            FtpFile file = ftpFileRepository.save(ftpFileMapper.toEntity(ftpFile));
            if(!CollectionUtils.isEmpty(ftpFile.getChildren())){
                save(ftpFile.getChildren(),ftpId,file.getId());
            }
        }
    }

    public SftpDTO login(String host, int port, String username, String password){
        SftpDTO sftp = new SftpDTO();
        try {
            JSch ftp = new JSch();
            Session sshSession =   ftp.getSession(username,host,port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;
            sftp.setFtp(ftp);
            sftp.setChannel(channel);
            sftp.setSftp(channelSftp);
            sftp.setSshSession(sshSession);
            sftp.setBLogin(true);
            return  sftp;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  sftp;
    }


    public void disConnection(SftpDTO sftp) throws IOException {
        if(sftp.getChannel()!=null){
            if(sftp.getChannel().isConnected()){
                sftp.getChannel().disconnect();
            }
        }
        if(sftp.getSshSession() != null){
            if(sftp.getSshSession() .isConnected()){
                sftp.getSshSession() .disconnect();
            }
        }
    }

    @Override
    public List<FtpFileDTO> getFileList(String pathName,SftpDTO sftp) throws SftpException {
        List<FtpFileDTO> files = new ArrayList<>();
        getFileList(pathName,files,sftp.getSftp());
        return  files;
    }

    @Override
    public void getFileList(String pathName, List<FtpFileDTO> fileList,ChannelSftp sftp) throws SftpException {
        if (pathName.startsWith("/") && pathName.endsWith("/")) {
            //更换目录到当前目录
            sftp.cd(pathName);
            Vector<?> files = sftp.ls("./");
            for (Object item:files) {
                LsEntry file = (LsEntry) item;
                //开头带点的不读
                if(file.getFilename().startsWith(".")){
                    continue;
                }
                SftpATTRS attrs = file.getAttrs();
                FtpFileDTO ftpFile = new FtpFileDTO();
                fileList.add(ftpFile);
                ftpFile.setFileName(file.getFilename());
                ftpFile.setFileSize((int)attrs.getSize());
                ftpFile.setFileAtime(attrs.getAtimeString());
                ftpFile.setFileMtime(attrs.getMtimeString());
                ftpFile.setPathName(pathName);
                if (attrs.isDir()) {
                    ftpFile.setIsDir("1");
                    List<FtpFileDTO> children = new ArrayList<>();
                    ftpFile.setChildren(children);
                    // 需要加此判断。否则，ftp默认将‘项目文件所在目录之下的目录（./）’与‘项目文件所在目录向上一级目录下的目录（../）’都纳入递归，这样下去就陷入一个死循环了。需将其过滤掉。
                    if (!".".equals(file.getFilename()) && !"..".equals(file.getFilename())) {
                        String nextPathName = pathName + file.getFilename() + "/";
                        getFileList(nextPathName,children,sftp);
                    }
                }else {
                    ftpFile.setIsDir("0");
                }
            }
        }
    }

    @Override
    public void getFileList(String pathName, String ext) throws SftpException {

    }
}
