package com.manager.user.config;


import com.manager.user.infrastructure.adapter.in.rest.dto.response.PhoneVerificationResponseDto;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;

@Configuration
public class PhoneVerificationKafkaProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.batch-size}")
    private int batchSize;
    @Value("${spring.kafka.producer.linger-ms}")
    private String lingerMs;
    @Value("${spring.kafka.producer.request-timeout}")
    private String requestTimeout;
    @Value("${spring.kafka.producer.delivery-timeout}")
    private String deliveryTimeout;
    @Value("${spring.kafka.ssl.enabled}")
    private boolean sslEnabled;
    @Value("${spring.kafka.producer.ssl.security.protocol}")
    private String securityProtocol;
    @Value("${spring.kafka.producer.ssl.trust-store-location}")
    private String trustStoreLocation;
    @Value("${spring.kafka.producer.ssl.trust-store-password}")
    private String trustStorePassword;
    @Value("${spring.kafka.producer.ssl.key-store-location}")
    private String keyStoreLocation;
    @Value("${spring.kafka.producer.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${spring.kafka.producer.ssl.key-password}")
    private String keyPassword;
    @Value("${spring.kafka.producer.topics.phone-verification}")
    private String topic;

    @Bean
    public ProducerFactory<String, PhoneVerificationResponseDto> phoneVerificationProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(BATCH_SIZE_CONFIG, batchSize);
        props.put(LINGER_MS_CONFIG, lingerMs);
        props.put(REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        props.put(DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);

        if (sslEnabled) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStoreLocation);
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword);

            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStoreLocation);
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyStorePassword);
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, keyPassword);
        }

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, PhoneVerificationResponseDto> phoneVerificationKafkaTemplate() {
        KafkaTemplate<String, PhoneVerificationResponseDto> kafkaTemplate = new KafkaTemplate<>(
                phoneVerificationProducerFactory());
        kafkaTemplate.setDefaultTopic(topic);
        return kafkaTemplate;
    }

}
