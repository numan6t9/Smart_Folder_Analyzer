package project.smart_file_analyzer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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

    @FXML
    private Label totalSizeLabel;

    @FXML
    private Label largestFileLabel;

    private ArrayList<FileInfo> fileInfos;

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

        fileSizeTV.setCellFactory(column ->
                new TableCell<FileInfo, Float>() {
                    @Override
                    protected void updateItem(Float size, boolean empty) {
                        super.updateItem(size, empty);

                        if (empty || size == null) {
                            setText(null);
                        } else {
                            setText(formatFileSize(size));
                        }
                    }
                }
        );

        fileCountLabel.setText("0");
        totalSizeLabel.setText("0 B");
        largestFileLabel.setText("N/A");
    }

    @FXML
    public void searchFolder(ActionEvent actionEvent) {

        String folderPath = enterFolderPath.getText();

        if (folderPath == null || folderPath.trim().isEmpty()) {
            folderDataTableView.getItems().clear();
            fileCountLabel.setText("0");
            totalSizeLabel.setText("0 B");
            largestFileLabel.setText("N/A");
            return;
        }

        Path path = Paths.get(folderPath);

        folderDataTableView.getItems().clear();

        fileCountLabel.setText("0");
        totalSizeLabel.setText("0 B");
        largestFileLabel.setText("N/A");

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

                updateStatistics();

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

    @FXML
    public void showStatistics(ActionEvent actionEvent) {
        updateStatistics();
    }

    private void updateStatistics() {

        int totalFiles =
                folderDataTableView
                        .getItems()
                        .size();

        if (totalFiles == 0) {

            fileCountLabel.setText("0");
            totalSizeLabel.setText("0 B");
            largestFileLabel.setText("N/A");

            return;
        }

        float totalSize = 0;
        float largestSize = -1;
        String largestFile = "";

        for (FileInfo file :
                folderDataTableView.getItems()) {

            float size = file.getFileSize();

            totalSize += size;

            if (size > largestSize) {

                largestSize = size;
                largestFile = file.getFileName();
            }
        }

        fileCountLabel.setText(
                String.valueOf(totalFiles)
        );

        totalSizeLabel.setText(
                formatFileSize(totalSize)
        );

        largestFileLabel.setText(
                largestFile
        );
    }

    private String formatFileSize(float bytes) {

        if (bytes < 1024) {
            return String.format(
                    "%.0f B",
                    bytes
            );
        }

        if (bytes < 1024 * 1024) {
            return String.format(
                    "%.2f KB",
                    bytes / 1024
            );
        }

        if (bytes < 1024 * 1024 * 1024) {
            return String.format(
                    "%.2f MB",
                    bytes / (1024 * 1024)
            );
        }

        return String.format(
                "%.2f GB",
                bytes / (1024 * 1024 * 1024)
        );
    }
}