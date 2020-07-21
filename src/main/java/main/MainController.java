package main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amazonaws.services.s3.model.Bucket;

import aws.AWSConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {

	@FXML
	private Button connectBtn;
	@FXML
	private Button newBucketBtn;
	@FXML
	private Button uploadBtn;
	@FXML
	private Button downloadBtn;
	@FXML
	private Button deleteFileBtn;
	
	@FXML
	private TextField accessKeyIdTextField;
	@FXML
	private TextField secretAccessKeyTextField;
	@FXML
	private TextField newBucketNameTextField;
	
	@FXML
	private ListView<String> bucketsListView;
	private ObservableList<String> bucketsObservableList = FXCollections.observableArrayList();
	
	@FXML
	private ListView<String> contentListView;
	private ObservableList<String> contentObservableList = FXCollections.observableArrayList();
	
	private AWSConnector awsConnector;
	private List<Bucket> bucketsList;
	@FXML
	private void initialize() {
		awsConnector = new AWSConnector();	
		bucketsListView.setItems(bucketsObservableList);
		contentListView.setItems(contentObservableList);
	}
	
	@FXML
	private void connectBtnClick() {		
		String accesskey = accessKeyIdTextField.getText();
		String secretkey = secretAccessKeyTextField.getText();
		awsConnector.setCredentials(accesskey, secretkey);
		bucketsList = awsConnector.getAllBuckets();
		
		bucketsObservableList.clear();
		for(Bucket bucket : bucketsList) {
			String item = bucket.getCreationDate() + " " + bucket.getName();
			bucketsObservableList.add(item);
		}
	}
	
	@FXML
	private void bucketsListViewClick() {		
		String currentBucketName = bucketsList.get(bucketsListView.getSelectionModel().getSelectedIndex()).getName();
		List<String> fileItems = awsConnector.getBucketFileKeysList(currentBucketName);
		contentObservableList.clear();
		for (String fileItem : fileItems) {
			contentObservableList.add(fileItem);
		}
	}
	
	@FXML
	private void contentListViewClick() {
		
	}
	
	@FXML
	private void newBucketBtnClick() {
		
	}
	
	@FXML
	private void uploadBtnClick() {
		
	}
	@FXML
	private void downloadBtnClick() {
		String path = new File(".").getAbsolutePath() + "\\file.jpg";
		String currentBucketName = bucketsList.get(bucketsListView.getSelectionModel().getSelectedIndex()).getName();
		String downloadedFileKey = this.contentListView.getSelectionModel().getSelectedItem();
		try {
			awsConnector.downloadFile(currentBucketName, downloadedFileKey, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void deleteFileBtnClick() {
		
	}
}
