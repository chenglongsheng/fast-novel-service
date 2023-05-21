package com.loong.novel.core.common.constant;

/**
 * 通用常量
 *
 * @author rosen
 * @date 2023/5/21 0:22
 */
public class CommonConsts {

    /**
     * 是
     */
    public static final Integer YES = 1;
    public static final String TRUE = "true";


    /**
     * 否
     */
    public static final Integer NO = 0;
    public static final String FALSE = "false";

    /**
     * 性别常量
     */
    public enum SexEnum {

        /**
         * 男
         */
        MALE(0, "男"),

        /**
         * 女
         */
        FEMALE(1, "女");

        private int code;
        private String desc;

        SexEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }

}
