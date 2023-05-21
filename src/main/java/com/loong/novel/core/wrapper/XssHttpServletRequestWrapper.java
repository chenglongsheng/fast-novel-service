package com.loong.novel.core.wrapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rosen
 * @date 2023/5/21 0:38
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Map<String, String> RE_PLACE_RULE = new HashMap<>();

    static {
        RE_PLACE_RULE.put("<", "&lt;");
        RE_PLACE_RULE.put(">", "&gt;");
    }

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapeValues[i] = values[i];
                int index = i;
                RE_PLACE_RULE.forEach((k, v) ->
                        escapeValues[index] = escapeValues[index].replaceAll(k, v)
                );
            }
            return escapeValues;
        }
        return new String[0];
    }
}
