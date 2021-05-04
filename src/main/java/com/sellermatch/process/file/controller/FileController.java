package com.sellermatch.process.file.controller;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/file")
    public List<File> selectFile() {
        return fileRepository.findAll();
    }

}
