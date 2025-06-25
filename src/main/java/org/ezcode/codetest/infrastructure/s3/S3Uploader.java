package org.ezcode.codetest.infrastructure.s3;

import java.io.IOException;
import java.util.UUID;

import org.ezcode.codetest.infrastructure.s3.exception.S3Exception;
import org.ezcode.codetest.infrastructure.s3.exception.code.S3ExceptionCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(MultipartFile multipartFile, String dirName) {
		try {
			// MIME 타입 검사 (png, jpeg, jpg, webp 만 가능)
			String contentType = multipartFile.getContentType();
			if (contentType == null || !contentType.startsWith("image/")) {
				throw new S3Exception(
					S3ExceptionCode.S3_INVALID_FILE_TYPE,
					S3ExceptionCode.S3_INVALID_FILE_TYPE.getStatus(),
					S3ExceptionCode.S3_INVALID_FILE_TYPE.getMessage()
				);
			}

			// S3 파일명 지정 ( 디렉토리/UUID-원본파일명 )
			String fileName = dirName + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(multipartFile.getSize());
			metadata.setContentType(contentType);

			amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
			String result = amazonS3.getUrl(bucket, fileName).toString(); // 업로드 파일 URL로 변환 ( 문자열 )
			log.info("S3 버킷 이미지 업로드 완료 {}", result);

			return result;

		} catch (IOException e) {
			log.error("S3 업로드 중 IO 오류 발생",e);
			throw new S3Exception(
				S3ExceptionCode.S3_UPLOAD_FAILED,
				S3ExceptionCode.S3_UPLOAD_FAILED.getStatus(),
				S3ExceptionCode.S3_UPLOAD_FAILED.getMessage()
			);
		} catch (AmazonS3Exception e) {
			log.error("S3 서비스 오류 발생: {}", e.getErrorMessage(), e);
			throw new S3Exception(
				S3ExceptionCode.S3_UPLOAD_FAILED,
				S3ExceptionCode.S3_UPLOAD_FAILED.getStatus(),
				S3ExceptionCode.S3_UPLOAD_FAILED.getMessage()
			);
		} catch (Exception e) {
			log.error("예상치 못한 업로드 오류 발생", e);
			throw new S3Exception(
				S3ExceptionCode.S3_UPLOAD_FAILED,
				S3ExceptionCode.S3_UPLOAD_FAILED.getStatus(),
				S3ExceptionCode.S3_UPLOAD_FAILED.getMessage()
			);
		}
	}
}
