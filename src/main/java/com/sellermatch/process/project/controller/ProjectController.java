package com.sellermatch.process.project.controller;

import com.sellermatch.process.common.domain.CommonConstant;
import com.sellermatch.process.common.domain.CommonDTO;
import com.sellermatch.process.project.domain.Project;
import com.sellermatch.process.project.domain.ProjectDto;
import com.sellermatch.process.project.repository.ProjectRepository;
import com.sellermatch.process.project.service.ProjectService;
import com.sellermatch.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class ProjectController {
    @Autowired
    public ProjectRepository projectRepository;

    @Autowired
    public ProjectService projectService;

    @GetMapping("/project")
    public CommonDTO selectProject() {
        CommonDTO result = new CommonDTO();
        Pageable pageable = PageRequest.of(0,1);
        result.setContent(projectRepository.findAll(pageable));
        return result;
    }

    @GetMapping("/project/list")
    public CommonDTO selectProjectList(Pageable pageable) {
        CommonDTO result = new CommonDTO();
        result.setContent(projectRepository.findAll(pageable));
        return result;
    }

    @PostMapping("/project")
    public CommonDTO inseretProject(ProjectDto projectDto,MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) throws Exception {
        CommonDTO result = new CommonDTO();

        //대표이미지: NULL 체크
        if(Util.isEmpty(profileImg)) {
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_152);
            return result;
        }
        //제목: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjTitle())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_132);
            return result;
        }
        //제목: 길이 제한 체크
        if(Util.isLengthChk(projectDto.getProject().getProjTitle(),0,100)){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_132);
            return result;
        }
        //상품분류: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjIndus())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_127);
            return result;
        }
        //상품단가: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjPrice())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_134);
            return result;
        }
        //판매마진: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjMargin())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_135);
            return result;
        }
        //등록지역: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjNation())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_136);
            return result;
        }
        //공급방법: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjSupplyType())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_137);
            return result;
        }
        //모집마감일: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjEndDate())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_139);
            return result;
        }
        //모집인원: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjRecruitNum())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_140);
            return result;
        }
        //모집인원: 숫자 형식 체크
        if(Util.isNumeric(projectDto.getProject().getProjRecruitNum().toString())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_FORMAT_142);
            return result;
        }
        //상세설명: NULL체크
        if(Util.isEmpty(projectDto.getProject().getProjDetail())){
            result.setResult(CommonConstant.ERROR);
            result.setStatus(CommonConstant.ERROR_NULL_143);
            return result;
        }


        projectDto.setProfileImgFile(profileImg);
        projectDto.setProjImgFile(projectImg);
        projectDto.setProjAttFile(projectAttFile);



        result.setContent(projectService.insertAndUpdateProject(projectDto));
        return result;
    }

    @PutMapping("/project")
    public CommonDTO updateProject(ProjectDto projectDto, MultipartFile profileImg, MultipartFile projectImg, MultipartFile projectAttFile) {
        CommonDTO result = new CommonDTO();
        projectRepository.findById(projectDto.getProject().getProjIdx()).ifPresentOrElse(temp -> {
            projectDto.setProfileImgFile(profileImg);
            projectDto.setProjImgFile(projectImg);
            projectDto.setProjAttFile(projectAttFile);
            try {
                result.setContent(projectService.insertAndUpdateProject(projectDto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, () -> {});
        return result;
    }

    @DeleteMapping("/project")
    public Project deleteProject(Project project) {
        projectRepository.findById(project.getProjIdx()).ifPresentOrElse(temp -> {
            projectRepository.save(project);
        }, () -> {});
        return project;
    }
}
