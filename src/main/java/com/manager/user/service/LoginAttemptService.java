package com.manager.user.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    @Value("${authentication.maxFailedLoginAttempts}")
    private int maxFailedLoginAttempts;
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService(@Value("${authentication.blockPeriod}") int blockPeriod) {
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(blockPeriod, TimeUnit.SECONDS).build(new CacheLoader<>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key) {
        var attempts = attemptsCache.getUnchecked(key);
        attemptsCache.put(key, ++attempts);
    }

    public boolean isBlocked(String key) {
        return attemptsCache.getUnchecked(key) >= maxFailedLoginAttempts;
    }
}
