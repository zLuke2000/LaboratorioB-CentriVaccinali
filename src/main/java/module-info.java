module it.uninsubria.centrivaccinali {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.bpmn;
    requires org.kordamp.ikonli.carbonicons;
    requires org.kordamp.ikonli.elusive;
    requires org.kordamp.ikonli.subway;
    requires org.kordamp.ikonli.fluentui;
    requires org.kordamp.ikonli.boxicons;
    requires org.kordamp.ikonli.fontawesome5;
    requires com.jfoenix;
    requires json.simple;
    requires java.sql;
    requires java.rmi;
    requires javafx.base;
    requires javafx.graphics;

    exports it.uninsubria.centrivaccinali;
    opens it.uninsubria.centrivaccinali to javafx.fxml;
    exports it.uninsubria.centrivaccinali.controller;
    opens it.uninsubria.centrivaccinali.controller to javafx.fxml;
    exports it.uninsubria.centrivaccinali.controller.dialog;
    opens it.uninsubria.centrivaccinali.controller.dialog to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client;
    opens it.uninsubria.centrivaccinali.client to javafx.fxml;
    exports it.uninsubria.centrivaccinali.server;
    opens it.uninsubria.centrivaccinali.server to javafx.fxml;
    exports it.uninsubria.centrivaccinali.models;
    opens it.uninsubria.centrivaccinali.models to javafx.fxml;
    exports it.uninsubria.centrivaccinali.enumerator;
    opens it.uninsubria.centrivaccinali.enumerator to javafx.fxml;
}