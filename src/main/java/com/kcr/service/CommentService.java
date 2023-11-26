package com.kcr.service;

import com.kcr.domain.dto.member.MsgResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

   // public abstract void save(Object o);
 //   public abstract void update(Long id);
    public abstract MsgResponseDTO delete(Long id);
   // public abstract void updateLikes(Long id);
 //   public abstract void cancelLikes(Long id);
}
