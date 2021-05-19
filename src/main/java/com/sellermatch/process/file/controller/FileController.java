package com.sellermatch.process.file.controller;

import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    FileService fileService;

    @GetMapping("/file")
    public CommonDTO selectFile(int fileIdx) {
        CommonDTO result = new CommonDTO();
        result.setContent(fileRepository.findById(fileIdx));
        return result;
    }

    @GetMapping("/file/list")
    public CommonDTO selectFileList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(fileRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/file")
    public CommonDTO insertFile(MultipartFile file) throws Exception {
        CommonDTO result = new CommonDTO();
        File fileInfo = new File();
        result.setContent(fileService.insertFile(file,fileInfo));
        return result;
    }

    @PutMapping("/file")
    public CommonDTO updateFile(MultipartFile multipartFile, int fileIdx) {
        CommonDTO result = new CommonDTO();
        fileRepository.findById(fileIdx).ifPresentOrElse(file ->{
            try {
                fileService.editFile(file, fileIdx, multipartFile);
                // 프로필 or 프로젝트 테이블 수정 부분
            } catch (Exception e) {
                e.printStackTrace();
            }
        },() ->{});
        return result;
    }

/*    @DeleteMapping("/file")
    public CommonDTO deleteFile(int fileIdx) {
        CommonDTO result = new CommonDTO();
        fileRepository.findById(fileIdx).ifPresentOrElse(file ->{
            try {
                fileRepository.delete(file);
                fileService.removeFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        },() ->{});
        return result;
    }*/
}
