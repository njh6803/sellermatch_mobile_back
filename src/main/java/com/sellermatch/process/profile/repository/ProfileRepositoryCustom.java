package com.sellermatch.process.profile.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sellermatch.process.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProfileRepositoryCustom {
    private final JPAQueryFactory query;

    public List<Profile> findAllSeller() {

        return null;
    }
}
