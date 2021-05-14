package com.sellermatch.process.profile.service;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.process.profile.domain.Profile;
import com.sellermatch.process.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    FileService fileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public Profile insertProfile(Profile profile, MultipartFile file) throws Exception {
        profileRepository.save(profile);
        File fileDto = new File();
        fileDto.setProfileId(profile.getProfileId());
        fileService.insertFile(file,fileDto);
        return profile;
    }
}
