module project.smart_file_analyzer {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.smart_file_analyzer to javafx.fxml;
    exports project.smart_file_analyzer;
}