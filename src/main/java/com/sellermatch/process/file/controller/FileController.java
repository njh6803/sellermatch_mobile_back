package com.sellermatch.process.file.controller;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/file")
    public Page<File> selectFile() {
        Pageable pageable = PageRequest.of(0,1);
        return fileRepository.findAll(pageable);
    }

    @GetMapping("/file/list")
    public Page<File> selectFileList(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    @PostMapping("/file/list")
    public File insertFile(File file) {
        return fileRepository.save(file);
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
