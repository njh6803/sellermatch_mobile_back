package com.sellermatch.process.file.controller;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    FileService fileService;

    @GetMapping("/file")
    public Optional<File> selectFile(int fileIdx) {
        return fileRepository.findById(fileIdx);
    }

    @GetMapping("/file/list")
    public Page<File> selectFileList(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    @PostMapping("/file")
    public File insertFile(MultipartFile file) throws Exception {
        File fileDto = new File();
        fileDto = fileUtil.saveMultipartFile(file);
        return fileRepository.save(fileDto);
    }

    @PutMapping("/file")
    public void updateFile(MultipartFile multipartFile, int fileIdx) {
        fileRepository.findById(fileIdx).ifPresentOrElse(file ->{
            try {
                fileService.editFile(file, fileIdx, multipartFile);
                // 프로필 or 프로젝트 테이블 수정 부분
            } catch (Exception e) {
                e.printStackTrace();
            }
        },() ->{});
    }

    @DeleteMapping("/file")
    public void deleteFile(int fileIdx) {
        fileRepository.findById(fileIdx).ifPresentOrElse(file ->{
            try {
                fileRepository.delete(file);
                fileService.removeFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        },() ->{});
    }
}
