package com.iit.project.cms.CMSServer.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void addCustomHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        // Check if the header is present in the custom headers map
        String customHeaderValue = customHeaders.get(name);
        if (customHeaderValue != null) {
            return customHeaderValue;
        }
        // If not present in custom headers, delegate to the original request
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> headerNames = new HashSet<>(customHeaders.keySet());
        Enumeration<String> originalHeaderNames = super.getHeaderNames();
        while (originalHeaderNames.hasMoreElements()) {
            headerNames.add(originalHeaderNames.nextElement());
        }
        return Collections.enumeration(headerNames);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> headerValues = new ArrayList<>();
        // Add custom header value if present
        String customHeaderValue = customHeaders.get(name);
        if (customHeaderValue != null) {
            headerValues.add(customHeaderValue);
        }
        // Add original header values
        Enumeration<String> originalHeaderValues = super.getHeaders(name);
        while (originalHeaderValues.hasMoreElements()) {
            headerValues.add(originalHeaderValues.nextElement());
        }
        return Collections.enumeration(headerValues);
    }
}

