package hundsun.pdpm.modules.security.service;

import hundsun.pdpm.exception.BadRequestException;
import hundsun.pdpm.modules.security.security.JwtUser;
import hundsun.pdpm.modules.system.service.UserService;
import hundsun.pdpm.modules.system.service.dto.DeptSmallDTO;
import hundsun.pdpm.modules.system.service.dto.JobSmallDTO;
import hundsun.pdpm.modules.system.service.dto.UserDTO;
import hundsun.pdpm.modules.system.service.dto.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private final JwtPermissionService permissionService;

    public JwtUserDetailsService(UserService userService, JwtPermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){

        UserDTO user = userService.findByName(username);
        if (user == null) {
            throw new BadRequestException("账号不存在");
        } else {
            return createJwtUser(user);
        }
    }

    public UserDetails createJwtUser(UserDTO user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getAvatar(),
                user.getEmail(),
                user.getPhone(),
                Optional.ofNullable(user.getDept()).map(DeptSmallDTO::getName).orElse(null),
                Optional.ofNullable(user.getJob()).map(JobSmallDTO::getName).orElse(null),
                permissionService.mapToGrantedAuthorities(user),
                user.getEnabled(),
                user.getCreateTime(),
                user.getLastPasswordResetTime()
        );
    }
}
