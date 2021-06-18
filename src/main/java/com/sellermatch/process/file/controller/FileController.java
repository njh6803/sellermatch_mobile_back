package com.sellermatch.process.file.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import com.sellermatch.process.file.service.FileService;
import com.sellermatch.util.ControllerResultSet;
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

    @GetMapping("/file/{id}")
    public CommonDTO selectFile(@PathVariable Integer id) {
        CommonDTO result = new CommonDTO();
        fileRepository.findById(id).ifPresentOrElse(temp -> {
            result.setContent(temp);
        }, () -> {
            File emptyContent =  new File();
            ControllerResultSet.errorCode(result, CommonConstant.ERROR_998, emptyContent);
        });
        return result;
    }

    @GetMapping("/file/list")
    public CommonDTO selectFileList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(fileRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/file")
    public CommonDTO insertFile(MultipartFile file,File fileInfo) throws Exception {
        CommonDTO result = new CommonDTO();
        result.setContent(fileService.insertFile(file,fileInfo));
        return result;
    }

    @PutMapping("/file")
    public CommonDTO updateFile(MultipartFile multipartFile, int fileIdx) {
        CommonDTO result = new CommonDTO();
        fileRepository.findById(fileIdx).ifPresentOrElse(file ->{
            try {
                fileService.editFile(file, fileIdx, multipartFile);
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
