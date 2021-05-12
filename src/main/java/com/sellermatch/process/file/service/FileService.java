package com.sellermatch.process.file.service;

import com.sellermatch.process.file.domain.File;
import com.sellermatch.process.file.repository.FileRepository;
import com.sellermatch.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private FileRepository fileRepository;

    @Transactional
    public void removeFile(File file) throws Exception {
        fileRepository.delete(file);
        fileUtil.delete(file);
    };
}
