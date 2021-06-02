package com.sellermatch.process.member.repository;

import com.sellermatch.process.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MemberRepository extends PagingAndSortingRepository<Member, Integer> {

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByMemNick(String memNick);

    Optional<Member> findByMemIdxAndWidthdrawAuthCode(Integer memId,String widthdrawAuthCode);

    Optional<Member> findTop1ByMemId(String memId);

    Optional<Member> findTop1ByMemIdAndMemSnsCh(String memId, String memSnsCh);

    Optional<Member> findByMemIdAndMemPw(String memId, String memPw);

}
