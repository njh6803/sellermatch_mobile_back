package com.sellermatch.process.profile.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.member.domain.QMember;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.domain.QProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProfileRepositoryCustom {
    private final JPAQueryFactory query;

    public List<Profile> findAllSeller() {
        QProfile profile = QProfile.profile;
        QMember member = QMember.member;

        List<Profile> result = (List<Profile>) query.from(profile).fetch();
        return result;
    }
}
