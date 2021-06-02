package com.sellermatch.process.profile.repository;

import com.sellermatch.process.profile.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProfileRepository extends PagingAndSortingRepository<Profile, Integer> {

    Page<Profile> findAll(Pageable pageable);

    Profile findTop1ByProfileMemId(String profileMemId);

}
