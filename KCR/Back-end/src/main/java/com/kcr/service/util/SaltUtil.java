package com.kcr.service.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class SaltUtil {
    public String encodePassword(String salt, String loginPw) {
        return BCrypt.hashpw(loginPw, salt);
    }

    public String genSalt() {
        return BCrypt.gensalt();
    }
}
