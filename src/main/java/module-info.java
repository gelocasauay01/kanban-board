module com.chomsy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;
    requires org.yaml.snakeyaml;

    opens com.chomsy to javafx.fxml;
    exports com.chomsy;
}
