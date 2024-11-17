module org.mavenproject1.coffeeshopproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires mysql.connector.j;
//    requires org.kordamp.bootstrapfx.core;

    opens org.mavenproject1.coffeeshopproject to javafx.fxml;
    exports org.mavenproject1.coffeeshopproject;
}