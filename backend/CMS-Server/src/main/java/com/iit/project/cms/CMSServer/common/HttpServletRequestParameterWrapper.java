package com.iit.project.cms.CMSServer.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class HttpServletRequestParameterWrapper extends HttpServletRequestWrapper {
    private final Map<String, String[]> params = new HashMap<>();

    public HttpServletRequestParameterWrapper(HttpServletRequest request) {
        super(request);
        this.params.putAll(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<>(params.keySet()).elements();
    }

    public void addAllParameters(Map<String, Object> extendParams) {
        for (Map.Entry<String, Object> entry : extendParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    public void addParameter(String key, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                params.put(key, (String[]) value);
            } else if (value instanceof String) {
                params.put(key, new String[]{(String) value});
            } else {
                params.put(key, new String[]{String.valueOf(value)});
            }
        }
    }
}
