package com.sellermatch.process.member.repository;

import com.sellermatch.process.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> { }
