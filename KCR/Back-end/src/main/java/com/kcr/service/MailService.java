package com.kcr.service;

public interface MailService {
    void sendMail(String to, String sub, String text);
}
