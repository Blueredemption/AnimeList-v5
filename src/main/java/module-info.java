module org.coopereisnor.animelist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires javafx.swing;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires org.jsoup;
    requires org.slf4j;

    opens org.coopereisnor.animeApplication to javafx.fxml;
    exports org.coopereisnor.animeApplication;
    exports org.coopereisnor.animeApplication.controllers;
    opens org.coopereisnor.animeApplication.controllers to javafx.fxml;
}