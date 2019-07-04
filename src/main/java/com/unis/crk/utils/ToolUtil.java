package com.unis.crk.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/06/03 9:39
 */
public class ToolUtil {


    /**
     * 字符串是否包含中文
     * @param str 待校验字符串
     * @return true 包含中文字符  false 不包含中文字符
     */
    public static boolean isContainChinese(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
