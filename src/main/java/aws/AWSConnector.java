package aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSConnector {
	private AWSCredentials credentials;
	private AmazonS3 s3client;
	
	public AWSConnector() {}
	
	public void setCredentials(String accesskey, String secretkey) {
		credentials = new BasicAWSCredentials(accesskey, secretkey);
		s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.EU_CENTRAL_1)
				  .build();
	}
	
	public void createBucket(String name) {
		if(s3client.doesBucketExist(name)) {
		    System.err.println("Bucket with name " + name + " is already exist!");
		    return;
		}		 
		s3client.createBucket(name);
	}
	
	public void deleteBucket(String name) {
		try {
		    s3client.deleteBucket(name);
		} catch (AmazonServiceException asExp) {
		    asExp.printStackTrace();
		    return;
		}
	}
	
	public List<Bucket> getAllBuckets(){
		List<Bucket> buckets = s3client.listBuckets();
		return buckets;
	}
	
	public List<String> getBucketFileKeysList(String bucketName){
		List<String> fileKeys = new ArrayList<>();
		ObjectListing objectListing = s3client.listObjects(bucketName);
		for(S3ObjectSummary summary : objectListing.getObjectSummaries()) {
			fileKeys.add(summary.getKey());
		}	
		return fileKeys;
	}
	
	public void uploadFile(String bucketName, String fileKey, File file) {
		s3client.putObject(bucketName, fileKey, file);
		return;
	}
	
	public void downloadFile(String bucketName,  String fileKey, String downloadingPath) throws IOException {
		S3Object s3object = s3client.getObject(bucketName, fileKey);
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		FileUtils.copyInputStreamToFile(inputStream, new File(downloadingPath));

		/*
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		
		File targetFile = new File(downloadingPath);
	    OutputStream outStream = new FileOutputStream(targetFile);
	    outStream.write(buffer);
	    */
	}
}
