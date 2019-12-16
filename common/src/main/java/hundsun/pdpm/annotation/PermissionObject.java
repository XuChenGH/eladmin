package hundsun.pdpm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：yantt21019
 * @date ：Created in 2019/12/12 15:47
 * @description：
 * @version:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionObject {

    String tablecode();

    String tablename();
}
