module it.uninsubria.centrivaccinali {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens it.uninsubria.centrivaccinali to javafx.fxml;
    exports it.uninsubria.centrivaccinali;
}