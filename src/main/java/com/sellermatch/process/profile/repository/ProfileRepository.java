package com.sellermatch.process.profile.repository;

import com.sellermatch.process.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
