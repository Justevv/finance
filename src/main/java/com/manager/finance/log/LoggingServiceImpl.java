package com.manager.finance.log;

import com.manager.finance.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LoggingServiceImpl implements LoggingService {
    private static final String MASK = "********";
    private final List<String> maskHeaderFieldByName = List.of("password", "authorization", "token");


    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        var stringBuilder = new StringBuilder();
        var parameters = buildParametersMap(httpServletRequest);
        var user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        stringBuilder.append("REQUEST ");
        stringBuilder.append("principal=[").append(user.getUsername()).append("] ");
        stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");

        if (!parameters.isEmpty()) {
            stringBuilder.append("parameters=[").append(parameters).append("] ");
        }

        if (body != null) {
            stringBuilder.append("body=[").append(body).append("]");
        }

        log.debug(stringBuilder.toString());
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {

        var response = "RESPONSE " +
                "method=[" + httpServletRequest.getMethod() + "] " +
                "path=[" + httpServletRequest.getRequestURI() + "] " +
                "responseHeaders=[" + buildHeadersMap(httpServletResponse) + "] " +
                "responseBody=[" + body + "] ";

        log.debug(response);
    }

    private Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        var parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            var key = parameterNames.nextElement();
            var value = maskHeaderFieldByName.stream().anyMatch(h -> h.equalsIgnoreCase(key))
                    ? MASK : httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }

        return resultMap;
    }

    private Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();

        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            var key = headerNames.nextElement();
            var value = maskHeaderFieldByName.stream().anyMatch(h -> h.equalsIgnoreCase(key))
                    ? MASK : request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        return response.getHeaderNames().stream()
                .collect(Collectors.toMap(x -> x, x -> maskHeaderFieldByName.stream()
                        .anyMatch(h -> h.equalsIgnoreCase(x)) ? MASK : response.getHeader(x)));
    }
}

