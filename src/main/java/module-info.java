module com.chomsy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires lombok;
    requires org.yaml.snakeyaml;

    opens com.chomsy to javafx.fxml;
    opens com.chomsy.models to org.yaml.snakeyaml;

    exports com.chomsy;
}
