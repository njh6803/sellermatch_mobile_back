package com.sellermatch.process.file.service;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import com.sellermatch.util.FileUtil;
import com.sellermatch.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private FileRepository fileRepository;

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void removeFile(File file) {
        fileRepository.delete(file);
        fileUtil.delete(file);
    };

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void editFile(File file,int fileIdx ,MultipartFile multipartFile) throws Exception{
        File newFile = fileUtil.saveMultipartFile(multipartFile);
        newFile.setFileIdx(fileIdx);
        fileRepository.save(newFile);
        fileUtil.delete(file);
    }

    public File insertFile(MultipartFile file,File FileInfo) throws Exception{
        File fileDto  = fileUtil.saveMultipartFile(file);
        if(!Util.isEmpty(FileInfo.getProfileId())) {
            fileDto.setProfileId(FileInfo.getProfileId());
        }
        if(!Util.isEmpty(FileInfo.getProjId())) {
            fileDto.setProfileId(FileInfo.getProfileId());
            fileDto.setProfileId(FileInfo.getProjThumbnail());
        }
        fileDto.setThumbnailPath("none"); //모바일 썸네일 path는 이미지 서버 정리전까지 none으로 고정
        return fileRepository.save(fileDto);
    }
}
