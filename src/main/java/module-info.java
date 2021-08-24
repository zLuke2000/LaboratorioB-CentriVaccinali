module it.uninsubria.laboratoriob {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.bpmn;
    requires org.kordamp.ikonli.carbonicons;
    requires org.kordamp.ikonli.elusive;
    requires java.sql;

    exports it.uninsubria.laboratoriob.client;
    opens it.uninsubria.laboratoriob.client to javafx.fxml;
    exports it.uninsubria.laboratoriob.client.centrivaccinali.controller;
    opens it.uninsubria.laboratoriob.client.centrivaccinali.controller to javafx.fxml;
}