package com.manager.finance.dto.response;

import com.manager.finance.entity.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RedisHash(timeToLive = 3600L)
public class ExpenseResponseDTO extends BaseCrudResponseDTO implements Serializable {
    private String description;
    private LocalDateTime date;
    private CategoryResponseDTO category;
    private PlaceResponseDTO place;
    private PaymentTypeResponseDTO paymentType;
    private double sum;
    private AccountResponseDTO account;
    private TransactionType transactionType;
    private UserResponseDTO user;

}

