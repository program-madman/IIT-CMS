package com.iit.project.cms.CMSServer.util;

import org.apache.commons.codec.digest.DigestUtils;


public class CmsPasswordEncoder {
    public static String encode(CharSequence rawPassword) {
        return DigestUtils.sha256Hex(rawPassword.toString());
    }

    public static boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(DigestUtils.sha256Hex(encodedPassword));
    }
}
