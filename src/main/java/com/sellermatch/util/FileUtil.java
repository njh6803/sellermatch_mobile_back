package com.sellermatch.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sellermatch.process.file.domain.File;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class FileUtil {
    @Autowired
    private AmazonS3 awsS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /** 업로드 된 파일이 식별될 URL경로 */
    private String uploadPath;

    /** 업로드 가능한 최대 용량 */
    private int uploadMaxFileSize;

    /** Multipart 전송시 File정보를 저장하기 위한 컬렉션 */
    @Deprecated
    private List<File> fileList;

    /** Multipart 전송시 텍스트 데이터를 저장하기 위한 컬렉션 */
    @Deprecated
    private Map<String, String> paramMap;

    /**
     * 컨트롤러로부터 업로드 된 파일의 정보를 전달받아 지정된 위치에 저장한다.
     * @param multipartFile             업로드 된 파일 정보
     * @return                          파일 정보를 담고 있는 객체
     * @throws NullPointerException     업로드 된 파일이 없는 경우
     * @throws Exception                파일 저장에 실패한 경우
     */
    public File saveMultipartFile(MultipartFile multipartFile) throws NullPointerException, Exception {
        File item = null;

        /** 업로드 파일 저장하기 */
        // 파일의 원본 이름 추출
        String orginName = multipartFile.getOriginalFilename();

        // 업로드 된 파일이 존재하는지 확인한다.
        if (orginName.isEmpty()) {
            throw new NullPointerException("업로드 된 파일이 없음.");
        }

        /** 동일한 이름의 파일이 존재하는지 검사한다. */
        // 파일의 원본 이름에서 확장자만 추출
        String ext = orginName.substring(orginName.lastIndexOf("."));
        String fileName = null; // 웹 서버에 저장될 파일이름
        java.io.File targetFile = null; // 저장된 파일 정보를 담기 위한 File객체
        int count = 0;          // 중복된 파일 수

        int random = (int) (100 * Math.random()); // 랜덤번호

        // 일단 무한루프
        while (true) {
            // 저장될 파일 이름 --> 현재시각 + 카운트값 + 확장자
            fileName = String.format("%d%d%d%s", System.currentTimeMillis(), random, count, ext);
            // 업로드 파일이 저장될 폴더 + 파일이름으로 파일객체를 생성한다.
            targetFile = new java.io.File("https://sellmatchimg.s3.ap-northeast-2.amazonaws.com", fileName);

            // 동일한 이름의 파일이 없다면 반복 중단.
            if (!targetFile.exists()) {
                break;
            }

            // if문을 빠져나올 경우 중복된 이름의 파일이 존재한다는 의미이므로 count를 1증가
            count++;
        } // end while

        /** 업로드 된 파일을 결정된 파일 경로로 저장 */
        this.uploadPath = this.upload(multipartFile, fileName);

        /** 썸네일 이미지 생성*/

        // 리턴할 정보를 구성한다.
        item = new File();
        item.setContentType(multipartFile.getContentType());
        item.setFileSize(multipartFile.getSize());
        item.setOrginName(orginName);
        item.setFilePath(this.uploadPath);

        return item;
    }

    /**
     * 컨트롤러로부터 업로드 된 파일의 정보를 전달받아 지정된 위치에 저장한다.
     * @param uploadFiles               업로드 된 파일 정보
     * @return                          파일 정보를 담고 있는 객체들을 저장하는 컬렉션
     * @throws NullPointerException     업로드 된 파일이 없는 경우
     * @throws Exception                파일 저장에 실패한 경우
     */
    public List<File> saveMultipartFile(MultipartFile[] uploadFiles) throws NullPointerException, Exception {

        if (uploadFiles.length < 1) {
            throw new NullPointerException("업로드 된 파일이 없음.");
        }

        List<File> uploadList = new ArrayList<File>();

        for (int i=0; i<uploadFiles.length; i++) {
            try {
                File item = this.saveMultipartFile(uploadFiles[i]);
                uploadList.add(item);
            } catch (NullPointerException e) {
                //log.error(String.format("%d번째 항목이 업로드 되지 않음", i));
            } catch (Exception e) {
                //log.error(String.format("%d번째 항목 저장 실패 ::: %s", e.getLocalizedMessage()));
            }
        }

        if (uploadList.size() == 0) {
            throw new Exception("업로드 실패");
        }

        return uploadList;
    }

    public String upload(MultipartFile file, String fileName) throws IOException {

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getBytes().length);

        awsS3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return awsS3Client.getUrl(bucket, fileName).toString();
    }

    public void delete(File file) {
        java.io.File loadFile =  new java.io.File(file.getFilePath());
        String fileName = loadFile.getName();
        boolean isExistObject = awsS3Client.doesObjectExist(bucket, fileName);
        if(isExistObject) {
            awsS3Client.deleteObject(bucket, fileName);
        }
    }

    public void test() throws Exception {
        throw new Exception("test");
    }

}
