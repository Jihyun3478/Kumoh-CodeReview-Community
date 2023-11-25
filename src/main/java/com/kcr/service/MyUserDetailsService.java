package com.kcr.service;

//import com.kcr.domain.dto.member.security.MyUserDetails;
import com.kcr.domain.dto.member.SecurityMemberDTO;
import com.kcr.domain.dto.member.security.MyUserDetails;
import com.kcr.domain.entity.Member;
import com.kcr.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService { // 1

//    private MemberRepository memberRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        Member member = memberRepository.findByLoginId(username);
//        if(member == null){
//            throw new UsernameNotFoundException(username + " : 사용자 존재하지 않음");
//        }
//
//        return new SecurityMemberDTO(member);
//    }
    private final MemberRepository memberRepository;

    // DB 에 저장된 사용자 정보와 일치 여부
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException { // 2
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) {
            throw new UsernameNotFoundException("Not Found " + loginId);
        }
        return new MyUserDetails(member);
    }
}