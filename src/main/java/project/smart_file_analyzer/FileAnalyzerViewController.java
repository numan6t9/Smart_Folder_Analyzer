package project.smart_file_analyzer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileAnalyzerViewController {

    @FXML
    private TableView<FileInfo> folderDataTableView;

    @FXML
    private TableColumn<FileInfo, String> fileLocationTV;

    @FXML
    private TextField enterFolderPath;

    @FXML
    private TableColumn<FileInfo, Float> fileSizeTV;

    @FXML
    private TableColumn<FileInfo, String> fileNameTV;

    @FXML
    private TableColumn<FileInfo, String> fileTypeTV;

    @FXML
    private AnchorPane searchPanalMainPane;

    @FXML
    private Label fileCountLabel;


    ArrayList<FileInfo> fileInfos;


    @FXML
    public void initialize() {

        fileInfos = new ArrayList<>();

        fileLocationTV.setCellValueFactory(
                new PropertyValueFactory<>("fileLocation")
        );

        fileSizeTV.setCellValueFactory(
                new PropertyValueFactory<>("fileSize")
        );

        fileNameTV.setCellValueFactory(
                new PropertyValueFactory<>("fileName")
        );

        fileTypeTV.setCellValueFactory(
                new PropertyValueFactory<>("fileType")
        );

        fileCountLabel.setText("0");
    }


    @FXML
    public void searchFolder(ActionEvent actionEvent) {

        String folderPath = enterFolderPath.getText();

        if (folderPath == null || folderPath.trim().isEmpty()) {
            fileCountLabel.setText("0");
            return;
        }

        Path path = Paths.get(folderPath);

        folderDataTableView.getItems().clear();
        fileCountLabel.setText("0");

        if (Files.exists(path) && Files.isDirectory(path)) {

            try (DirectoryStream<Path> files =
                         Files.newDirectoryStream(path)) {

                for (Path file : files) {

                    if (Files.isRegularFile(file)) {

                        String fileName =
                                file.getFileName().toString();

                        long fileSize =
                                Files.size(file);

                        String fileType = "Unknown";

                        int dotIndex =
                                fileName.lastIndexOf(".");

                        if (dotIndex > 0 &&
                                dotIndex < fileName.length() - 1) {

                            fileType =
                                    fileName
                                            .substring(dotIndex + 1)
                                            .toUpperCase();
                        }

                        FileInfo info = new FileInfo(
                                file.toString(),
                                fileName,
                                fileSize,
                                fileType
                        );

                        folderDataTableView
                                .getItems()
                                .add(info);
                    }
                }

                int totalFiles =
                        folderDataTableView
                                .getItems()
                                .size();

                fileCountLabel.setText(
                        String.valueOf(totalFiles)
                );

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void browseFolder(ActionEvent actionEvent) {

        DirectoryChooser directoryChooser =
                new DirectoryChooser();

        directoryChooser.setTitle("Select Folder");

        File selectedFolder =
                directoryChooser.showDialog(
                        enterFolderPath
                                .getScene()
                                .getWindow()
                );

        if (selectedFolder != null) {

            enterFolderPath.setText(
                    selectedFolder.getAbsolutePath()
            );

            searchFolder(actionEvent);
        }
    }
}