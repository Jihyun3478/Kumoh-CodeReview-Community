package com.kcr.repository;

import com.kcr.domain.dto.member.signup.SignupRequestDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    void save(SignupRequestDTO signupRequestDTO);

    @Query("SELECT m FROM Member m WHERE m.roleType = :roleType")
    Page<Member> findAll(Pageable pageable, @Param("roleType") RoleType roleType);

    Member findByLoginId(String loginId);
    Member findByNickname(String nickname);
}
