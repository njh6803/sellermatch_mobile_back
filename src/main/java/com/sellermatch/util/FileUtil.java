package com.sellermatch.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sellermatch.process.file.domain.File;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
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

        // 리턴할 정보를 구성한다.
        item = new File();

        if (multipartFile.getContentType().indexOf("image") > -1) {
            String thumbnail = this.createThumbnail(multipartFile, fileName, 240, 240, true, multipartFile.getContentType());
            item.setThumbnailPath(thumbnail);
        } else {
            item.setThumbnailPath("none");
        }
        item.setContentType(multipartFile.getContentType());
        item.setFileSize((int) multipartFile.getSize());
        item.setOrginName(orginName);
        item.setFilePath(this.uploadPath);
        return item;
    }

    /**
     * 리사이즈 된 썸네일 이미지를 생성하고 경로를 리턴한다.
     *
     * @param loadFile - 원본 파일의 경로
     * @param width    - 최대 이미지 가로 크기
     * @param height   - 최대 이미지 세로 크기
     * @param crop     - 이미지 크롭 사용 여부
     * @return 생성된 이미지의 절대 경로
     * @throws Exception
     */
    public String createThumbnail(MultipartFile loadFile, String fileName, int width, int height, boolean crop, String contentType) throws Exception {

        /** 저장될 썸네일 이미지의 경로 문자열 만들기 */
        int p = fileName.lastIndexOf(".");              // 파일이름에서 마지막 점(.)의 위치
        String name = fileName.substring(0, p);         // 파일명 분리 --> 파일이름에서 마지막 점의 위치 전까지
        String ext = fileName.substring(p + 1);         // 확장자 분리 --> 파일이름에서 마지막 점위 위치 다음부터 끝까지
        String prefix = crop ? "_crop_" : "_resize_";   // 크롭인지 리사이즈 인지에 대한 문자열

        // 최종 파일이름을 구성한다. --> 원본이름 + 크롭여부 + 요청된 사이즈
        // -> ex) myphoto.jpg --> myphoto_resize_320x240.jpg
        String thumbName = name + prefix + width + "x" + height + "." + ext;

        /** 썸네일 이미지 생성하고 최종 경로 리턴 */
        // 원본 이미지 가져오기
        BufferedImage image = ImageIO.read(loadFile.getInputStream());
        // 이미지 크롭 여부 파라미터에 따라 크롭 옵션을 지정한다.
        BufferedImage thumbnail_medium = Thumbnails.of(image)
                .crop(Positions.CENTER).size(width,height)
                .useExifOrientation(true).outputFormat(ext).asBufferedImage();

        /** 업로드 된 파일을 결정된 파일 경로로 저장 */
        this.uploadImageToAWSS3(thumbnail_medium, thumbName, ext);

        return thumbName;
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

    public void uploadImageToAWSS3(BufferedImage image,String filename, String ext)
            throws IllegalStateException, IOException {
        java.io.File outputfile = null;
        try {
            int bytesRead = 0;
            byte[] buff = new byte[1024];

            outputfile = new java.io.File(filename);
            ImageIO.write(image, ext, outputfile);
            FileInputStream fin = new FileInputStream(outputfile);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            while((bytesRead = fin.read(buff)) > 0) {
                bao.write(buff, 0, bytesRead);
            }

            //byte[]로 변환
            byte[] bytes = bao.toByteArray();

            //metadata 설정
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(bytes.length);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filename, byteArrayInputStream, objMeta);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            awsS3Client.putObject(putObjectRequest);

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } finally {
            if (!Util.isEmpty(outputfile)) {
                if (outputfile.exists()) {
                    outputfile.delete();
                }
            }
        }
    }

    public void test() throws Exception {
        throw new Exception("test");
    }

}
