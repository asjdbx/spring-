package com.wang.utils;

public class UrlReplaceUtil {
    /**
     * 旧域名（需替换的域名）
     */
    private static final String OLD_DOMAIN = "blong1.sypo3c5e8.hn-bkt.clouddn.com";
    /**
     * 新域名（目标域名）
     */
    private static final String NEW_DOMAIN = "t42b10v5v.hn-bkt.clouddn.com";

    /**
     * 将URL中的旧域名替换为新域名
     * @param oldUrl 包含旧域名的URL
     * @return 替换后的新URL，若输入为null或不包含旧域名则返回原URL
     */
    public static String replaceDomain(String oldUrl) {
        if (oldUrl == null) {
            return null;
        }
        return oldUrl.replace(OLD_DOMAIN, NEW_DOMAIN);
    }

}