package com.sellermatch.process.file.controller;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import com.sellermatch.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileUtil fileUtil;

    @GetMapping("/file")
    public Page<File> selectFile() {
        Pageable pageable = PageRequest.of(0,1);
        return fileRepository.findAll(pageable);
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
    public File updateFile(File file) {
        fileRepository.findById(file.getFileIdx()).ifPresentOrElse(temp ->{
            fileRepository.save(file);
        },() ->{});
        return file;
    }

    @DeleteMapping("/file")
    public File deleteFile(File file) {
        fileRepository.findById(file.getFileIdx()).ifPresentOrElse(temp ->{
            fileRepository.delete(file);
        },() ->{});
        return file;
    }
}
