package com.sellermatch.process.profile.repository;

import com.sellermatch.process.profile.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProfileRepository extends PagingAndSortingRepository<Profile, Integer> {

    Page<Profile> findAll(Pageable pageable);

    @Query("SELECT p.profileIdx, p.profileId, p.profileMemId, p.profileIntro, p.profileChChk, p.profileCareer, " +
            "p.profileSaleChk, p.profileNation, p.profileBizCerti, p.profileCh, p.profilePhoto, p.profileVolume, " +
            "m.memNick, m.memRname, m.memState " +
            "FROM Profile p INNER JOIN Member m ON p.profileMemId = m.memId " +
            "WHERE p.profileSort = '2' AND m.memRname = '1' AND p.profileIndus IS NOT NULL AND p.profileIndus <> ''")
    Page<Profile> findAllSeller(Pageable pageable, Profile profile);

}
