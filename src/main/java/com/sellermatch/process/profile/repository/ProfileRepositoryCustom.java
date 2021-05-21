package com.sellermatch.process.profile.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.member.domain.QMember;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.domain.QProfile;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProfileRepositoryCustom {
    private final JPAQueryFactory query;

    public List<Profile> findAllSeller(Profile profile) {
        QProfile qProfile = QProfile.profile;
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        if (!Util.isEmpty(profile.getProfileSort())){
            builder.and(qProfile.profileSort.eq(profile.getProfileSort()));
        }

        List<Profile> jpaQuery = (List<Profile>) query.from(qProfile).where(builder).fetch();
        return jpaQuery;
    }
}
