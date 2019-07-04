package com.unis.crk.utils;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class ThreadSessionUtil {

	private static ThreadLocal _session = new ThreadLocal();

	public static HttpSession getHttpSession() {
		ThreadLocal threadlocal = _session;
		synchronized (threadlocal) {
			HashMap map = (HashMap) _session.get();
			if (map != null)
				return (HttpSession) map.get("httpSession");
		}
		return null;
	}

	public static Map getHttpData() {
		ThreadLocal threadlocal = _session;
		synchronized (threadlocal) {
			HashMap map = (HashMap) _session.get();
			if (map != null)
				return (Map) map.get("data");
		}
		return null;
	}

	public static void setHttpSession(HttpSession httpSession) {
		synchronized (_session) {
			HashMap map = (HashMap) _session.get();
			if (map == null)
				map = new HashMap();
			map.put("httpSession", httpSession);
			_session.set(map);
		}
	}

	public static void setHttpSession(HttpSession httpSession, Map data) {
		synchronized (_session) {
			HashMap map = (HashMap) _session.get();
			if (map == null)
				map = new HashMap();
			map.put("httpSession", httpSession);
			map.put("data", data);
			_session.set(map);
		}
	}
}