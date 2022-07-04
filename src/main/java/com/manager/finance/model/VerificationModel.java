package com.manager.finance.model;

import com.manager.finance.service.ConfirmationService;
import com.manager.finance.entity.UserEntity;
import com.manager.finance.entity.VerificationEntity;
import com.manager.finance.entity.VerificationType;
import com.manager.finance.exception.VerificationNotFoundException;
import com.manager.finance.repository.UserRepository;
import com.manager.finance.repository.VerificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.manager.finance.constant.Constant.USER_DOES_NOT_EXISTS;


@Service
@Slf4j
public class VerificationModel {
    private static final String VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE = "Verification doesn't exists";
    private static final int NUMBER_DIGITS_VERIFICATION_CODE = 6;
    @Value("${confirmationRegistrationCodeExpiredTime}")
    private int confirmationRegistrationCodeExpiredTime;
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationService confirmationService;

    @Transactional
    public boolean confirmPhone(long userId, String code) {
        log.debug("Try to confirm phone for userId {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));

        var phoneVerificationCode = verificationRepository.findByUserAndType(user, VerificationType.PHONE)
                .orElseThrow(() -> new VerificationNotFoundException(VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE));
        if (phoneVerificationCode != null && phoneVerificationCode.getCode().equals(code)
                && !phoneVerificationCode.isExpire() && !confirmationService.isPhoneAlreadyConfirmed(user.getPhone())) {
            user.setPhoneConfirmed(true);
            verificationRepository.delete(phoneVerificationCode);
            log.info("Phone for user {} was confirmed successfully", user);
            return true;
        }
        log.debug("Phone confirmation for user {} was rejected", user);
        return false;
    }

    @Transactional
    public boolean confirmEmail(long userId, String code) {
        log.debug("Try to confirm email for userId {}", userId);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXISTS));

        var emailVerificationCode = verificationRepository.findByUserAndType(user, VerificationType.EMAIL)
                .orElseThrow(() -> new VerificationNotFoundException(VERIFICATION_DOES_NOT_EXISTS_ERROR_MESSAGE));
        if (emailVerificationCode != null && emailVerificationCode.getCode().equals(code)
                && !emailVerificationCode.isExpire() && !confirmationService.isEmailAlreadyConfirmed(user.getEmail())) {
            user.setEmailConfirmed(true);
            verificationRepository.delete(emailVerificationCode);
            log.info("Email for user {} was confirmed successfully", user);
            return true;
        }
        log.debug("Email confirmation for user {} was rejected", user);
        return false;
    }

    @Transactional
    public VerificationEntity createAndSaveVerification(UserEntity user, VerificationType verificationType) {
        log.debug("Current user is {}, Verification type is {}", user, verificationType);
        var verification = createVerificationCode(verificationType);
        verification.setUser(user);
        verificationRepository.save(verification);
        log.info("Saved verificationCode is {}", verification);
        return verification;
    }

    private VerificationEntity createVerificationCode(VerificationType type) {
        String code = RandomStringUtils.randomNumeric(NUMBER_DIGITS_VERIFICATION_CODE);
        return new VerificationEntity(code, confirmationRegistrationCodeExpiredTime, type);
    }

}