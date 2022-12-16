module org.coopereisnor.animelist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires java.desktop;
    requires org.jsoup;
    requires org.slf4j;

    opens org.coopereisnor.animeApplication to javafx.fxml;
    exports org.coopereisnor.animeApplication;
    exports org.coopereisnor.animeApplication.controllers;
    opens org.coopereisnor.animeApplication.controllers to javafx.fxml;
}