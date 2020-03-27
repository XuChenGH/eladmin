package hundsun.pdpm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {
    /**
     * Excel表头显示名称
     * @return
     */
    String title();
    /**
     * 需要翻译出字典值，为字典Title
     * @return
     */
    String dictname() default "";

    /**
     * 可选值，多个用逗号隔开
     * @return
     */
    String values() default "";

    /**
     * 是否是超链接
     * @return
     */
    boolean link() default false;
    /**
     * 是否自适应单元格宽度
     * @return
     */
    boolean autosize() default false;
    /**
     * 是否是导出
     */
    boolean export() default true;
    /**
     * 是否是百分比
     */
    boolean percent() default false;

    /**
     * 转换名
     * */
    String tranfer() default "";

    /*
    * 列宽
    * */
    int colwidth() default 0;

    boolean sheet() default false;

    boolean plain() default false;

    /*排序 多sheet使用,按sheet下标 以逗号分隔 0 不需要*/
    String  order() default "";

    Align   align() default Align.ALIGN_CENTER;

    enum Align {
        /**
         * 左对齐
         * */
        ALIGN_LEFT,

        /**
         * 中间对齐
         * */
        ALIGN_CENTER,

        /**
         * 右对齐
         * */
        ALIGN_RIGHT
    }
}
