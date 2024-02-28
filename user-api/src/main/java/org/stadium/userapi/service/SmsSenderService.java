package org.stadium.userapi.service;

public interface SmsSenderService {
    boolean send(String phone, String otp, String hash);
}
