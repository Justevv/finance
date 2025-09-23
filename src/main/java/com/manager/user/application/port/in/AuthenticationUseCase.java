package com.manager.user.application.port.in;

import com.manager.user.security.AuthenticationRequestDTO;
import eu.bitwalker.useragentutils.UserAgent;

import java.util.Map;

public interface AuthenticationUseCase {

    Map<String, String> authenticate(UserAgent userAgent, String remoteAddr, AuthenticationRequestDTO authentication);
}
