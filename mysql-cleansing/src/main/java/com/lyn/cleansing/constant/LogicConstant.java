package com.lyn.cleansing.constant;

/**
 * @author lyn
 * @date 2023/8/1
 */
public class LogicConstant {

    /**
     * 默认执行器执行优先度
     */
    public static Integer DEFAULT_ORDER_NO = 100;

    /**
     * 解析#CL注释正则表达式
     */
    public static String ANNOTATION_PARSE_REGEX =
            "\\$cl:(.*?)(?=,\\$cdt:|,\\$order:|</#CL>)|\\$cdt:(.*?)(?=," + "\\$order:|</#CL>)|\\$order:(.*?)(?=</#CL>)";

    /**
     * 解析#CL注释中的条件字符串正则表达式
     */
    public static String ANNOTATION_PARSE_CONDITION_REGEX = "\\(\\s*([^\\s,]+),([^\\s,]+)\\s*\\)";

    /**
     * 表注释正则表达式
     */
    public static String ANNOTATION_REGEX = ".*?<#CL>(.*?)</#CL>.*";

    /**
     * 混合表注释注释正则表达式
     */
    public static String MIX_REGEX = "<#CL>(.*?)</#CL>";

    /**
     * 注释正则表达式
     */
    public static String[] ANNOTATION_MARK = {"<#CL>", "</#CL>"};

    /**
     * 分隔符逗号
     */
    public static String SPLIT_NODE = ",";

}
