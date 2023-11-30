package com.kcr.service;

import com.kcr.domain.dto.member.MsgResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    public abstract MsgResponseDTO delete(Long id);
}
