module it.uninsubria.centrivaccinali {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.bpmn;
    requires org.kordamp.ikonli.carbonicons;
    requires java.sql;

    opens it.uninsubria.centrivaccinali.client to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client.centrivaccinali.controller;
    opens it.uninsubria.centrivaccinali.client.centrivaccinali.controller to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client;
}