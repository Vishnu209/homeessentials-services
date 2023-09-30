package com.home.essentials.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.mongodb.DBObject;

@Service
public class AmazonClient {

	private final Logger log = LoggerFactory.getLogger(AmazonClient.class);

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@Value("${amazonProperties.region}")
	private String region;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
	}

	public String uploadContent(MultipartFile contentFile, String citationText, DBObject metadata, String fileName,
			InputStream fileToSubmit) throws Exception, IOException {
		// ObjectId contentId;
		String contentS3Url;

		log.info("Filename:::" + fileName);

		// GENERATING FILENAME WITH TIMESTAMP TO AVOID OVERRIDING OF EXISTING FILE WITH
		// SAME NAME IN S3

		contentS3Url = uploadFileInS3(fileToSubmit, fileName, "CONTENTDOC", contentFile.getContentType());

		fileToSubmit.close();
		Files.deleteIfExists(Paths.get(fileName));

		return contentS3Url;
	}

	public String uploadFile(MultipartFile multipartFile, String uploadType, String userId) {
		String fileUrl = "";
		try {
			File file = convertMultipartFiletoFile(multipartFile);
			String fileName = multipartFile.getOriginalFilename();

			String fileNameToSave = fileName.split("[.]")[0] + "_" + String.valueOf(LocalDateTime.now()) + "."
					+ fileName.split("[.]")[1];

			fileUrl = getS3Url(fileNameToSave);
			s3client.putObject(new PutObjectRequest(bucketName, fileNameToSave, file)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			file.delete();

		} catch (Exception e) {
			log.error("Error while uploading to s3:::", e);
		}
		return fileUrl;
	}

	public String uploadFileInS3(InputStream is, String fileName, String uploadType, String contentType) {
		String fileUrl = "";
		log.info("File NAme::" + fileName);

		try {

			fileUrl = getS3Url(fileName);

			ObjectMetadata metadata = new ObjectMetadata();

			metadata.setContentLength(is.available());

			metadata.setContentType(contentType);

			s3client.putObject(new PutObjectRequest(bucketName, fileName, is, metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));

			String s3Url = s3client.getUrl(bucketName, fileName).toString();

			return s3Url;

		} catch (Exception e) {
			e.printStackTrace();
			return ("Failure in uploading filr to S3::" + e.getMessage());
		}

	}

	public InputStream getFile(String key) {
		S3Object obj = s3client.getObject(bucketName, key);
		S3ObjectInputStream stream = obj.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(stream);
			InputStream myInputStream = new ByteArrayInputStream(content);
			obj.close();
			return myInputStream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param fileName
	 * @return
	 */
	public String getS3Url(String fileName) {
		String fileUrl;
		fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
		return fileUrl;
	}

	private File convertMultipartFiletoFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private File convertInputStreamtoFile(InputStream is, String fileName) throws IOException {
		File convFile = new File(fileName);
		OutputStream fos = new FileOutputStream(convFile);

		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		log.info("after before creating file buffer length:::" + buffer.length);
		fos.write(buffer);
		fos.close();
		log.info("after creating file:::" + convFile.getName());
		log.info("after creating file length:::" + convFile.length());
		return convFile;
	}

	private String generateFileName(MultipartFile file) {
		return new Date().getTime() + "-" + file.getOriginalFilename().replace(" ", "_");
	}

	public void deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
			log.info("Successfully Deleted");
		} catch (Exception e) {
			log.error("error while deleting file from s3", e);
		}

	}

	public void deleteFileFromS3(String fileName) {
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
		} catch (Exception e) {
			log.error("Error in deleting the file from S3!!", e);
		}

	}

	public void createBucket(String s3BucketName) {
		try {
			bucketName = s3client.createBucket(s3BucketName).getName();
			log.info(bucketName + "successfully created.");
		} catch (SdkClientException e) {
			log.error("Error in creating the bucket in S3!!", e);
		}
	}

}
