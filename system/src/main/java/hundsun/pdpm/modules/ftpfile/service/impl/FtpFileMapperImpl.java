package hundsun.pdpm.modules.ftpfile.service.impl;

import hundsun.pdpm.modules.ftpfile.domain.FtpFile;
import hundsun.pdpm.modules.ftpfile.service.dto.FtpFileDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

import hundsun.pdpm.modules.ftpfile.service.mapper.FtpFileMapper;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-31T10:12:18+0800",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_191 (Oracle Corporation)"
)
@Component
public class FtpFileMapperImpl implements FtpFileMapper {

    @Override
    public FtpFile toEntity(FtpFileDTO dto) {
        if ( dto == null ) {
            return null;
        }

        FtpFile ftpFile = new FtpFile();

        ftpFile.setId( dto.getId() );
        ftpFile.setFileName( dto.getFileName() );
        ftpFile.setIsDir( dto.getIsDir() );
        ftpFile.setFileSize( dto.getFileSize() );
        ftpFile.setFileAtime( dto.getFileAtime() );
        ftpFile.setFileMtime( dto.getFileMtime() );


        return ftpFile;
    }

    @Override
    public FtpFileDTO toDto(FtpFile entity) {
        if ( entity == null ) {
            return null;
        }

        FtpFileDTO ftpFileDTO = new FtpFileDTO();

        ftpFileDTO.setId( entity.getId() );
        ftpFileDTO.setFileName( entity.getFileName() );
        ftpFileDTO.setIsDir( entity.getIsDir() );
        ftpFileDTO.setFileSize( entity.getFileSize() );
        ftpFileDTO.setFileAtime( entity.getFileAtime() );
        ftpFileDTO.setFileMtime( entity.getFileMtime() );


        return ftpFileDTO;
    }

    @Override
    public List<FtpFile> toEntity(List<FtpFileDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<FtpFile> list = new ArrayList<FtpFile>( dtoList.size() );
        for ( FtpFileDTO ftpFileDTO : dtoList ) {
            list.add( toEntity( ftpFileDTO ) );
        }

        return list;
    }

    @Override
    public List<FtpFileDTO> toDto(List<FtpFile> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<FtpFileDTO> list = new ArrayList<FtpFileDTO>( entityList.size() );
        for ( FtpFile ftpFile : entityList ) {
            list.add( toDto( ftpFile ) );
        }

        return list;
    }
}
