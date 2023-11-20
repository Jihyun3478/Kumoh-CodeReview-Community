package com.kcr.repository;

import com.kcr.domain.dto.member.MemberRequestDTO;
import com.kcr.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    MemberRequestDTO findByLoginId(String loginId);
}
