package com.unis.crk.utils;

import javax.servlet.http.HttpSession;

public class UserSessionUtil {
    private static final String SESSION_KEY = "LOGIN_USER";
    private static final String SESSION_KEY_USERNAME = "LOGIN_USER_NAME";

    public static void setSession(HttpSession session, String userid, String userName) {
        session.setAttribute(SESSION_KEY, userid);
        session.setAttribute(SESSION_KEY_USERNAME, userName);
    }

    public static String getSessionUserId(HttpSession session) {
        if (session == null) {
            return "";
        }
        Object userId = session.getAttribute(SESSION_KEY);
        if (userId == null || userId.equals("")) {
            return "";
        }
        return userId.toString();
    }

    public static String getSessionUserName(HttpSession session) {
        if (session == null) {
            return "";
        }
        Object userName = session.getAttribute(SESSION_KEY_USERNAME);
        if (userName == null || userName.equals("")) {
            return "";
        }
        return userName.toString();
    }
}
