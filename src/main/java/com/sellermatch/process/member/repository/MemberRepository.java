package com.sellermatch.process.member.repository;

import com.sellermatch.process.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends PagingAndSortingRepository<Member, Integer> {

    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByMemNick(String memNick);

    Optional<Member> findTop1ByMemId(String memId);

    Member findByMemId(String memId);

    Optional<Member> findTop1ByMemIdAndMemSnsCh(String memId, String memSnsCh);

    Optional<Member> findByMemIdAndMemPw(String memId, String memPw);

    // 아이디 찾기
    @Query(value = "SELECT mem_id FROM MemList WHERE mem_tel = :memTel and mem_state != 1", nativeQuery = true)
    List<String> findId(@Param("memTel") String memTel);

    // 비밀번호 찾기 변경
    @Transactional
    @Modifying
    @Query(value = "UPDATE MemList SET mem_pw = :memPw where mem_id = :memId and mem_state != 1", nativeQuery = true)
    Integer changePw(@Param("memPw") String memPw, @Param("memId") String memId);

    // 최근 로그인 시간 갱신
    @Transactional
    @Modifying
    @Query(value = "UPDATE MemList SET mem_login_date = now() WHERE mem_id= :memId", nativeQuery = true)
    Integer changePw(@Param("memId") String memId);

    // 회원탈퇴 업데이트
    @Transactional
    @Modifying
    @Query(value = "UPDATE MemList SET mem_State = :memState, Mem_out_date = now() WHERE mem_id= :memId", nativeQuery = true)
    Integer withdraw(@Param("memState") String memState, @Param("memId") String memId);
}
