package com.sellermatch.process.member.repository;

import com.sellermatch.process.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface MemberRepository extends PagingAndSortingRepository<Member, Integer> {

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByMemId(String memId);

}
