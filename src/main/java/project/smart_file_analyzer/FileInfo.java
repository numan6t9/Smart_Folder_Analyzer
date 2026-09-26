package project.smart_file_analyzer;

public class FileInfo {
    private String fileType;
    private String fileLocation;
    private String fileName;
    private float fileSize;

    public FileInfo(String fileLocation, String fileName,
                    float fileSize, String fileType) {

        this.fileLocation = fileLocation;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public String getFileName() {
        return fileName;
    }

    public float getFileSize() {
        return fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileLocation='" + fileLocation + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }


}
