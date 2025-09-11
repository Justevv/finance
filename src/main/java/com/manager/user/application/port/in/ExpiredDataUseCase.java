package com.manager.user.application.port.in;

public interface ExpiredDataUseCase {

    void cleanExpiredEmailVerification();

    void cleanExpiredPhoneVerification();

    void cleanExpiredPasswordResetToken();

}