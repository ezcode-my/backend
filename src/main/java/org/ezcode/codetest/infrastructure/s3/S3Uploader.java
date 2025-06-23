package org.ezcode.codetest.infrastructure.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class S3Uploader {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket")
	private String bucket;


	public String upload(MultipartFile multipartFile, String dirName) throws IOException {

		String fileName = dirName + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
		ObjectMetadata metadata = new ObjectMetadata();

		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);

		return amazonS3.getUrl(bucket, fileName).toString(); // 업로드 파일 URL로 변환 ( 문자열 )
	}
}
