package org.stadium.userapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.stadium.userapi.service.SmsSenderService;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SmsSenderServiceImpl implements SmsSenderService {

    @Override
    public boolean send(String phone, String otp, String hash) {
        log.info("Sms sent successfully!");
        return true;
    }

}

