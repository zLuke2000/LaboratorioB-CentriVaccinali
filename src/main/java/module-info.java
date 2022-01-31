/**
 * @since 1.0.0
 * @author Centore Luca 740951
 * @author Lattarulo Luca 742597
 * @author Marelli Samuele
 * @author Pintonello Christian 741112
 */
module it.uninsubria.centrivaccinali {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires java.sql;
    requires java.rmi;
    requires javafx.base;
    requires javafx.graphics;

    exports it.uninsubria.centrivaccinali;
    opens it.uninsubria.centrivaccinali to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client.controller;
    opens it.uninsubria.centrivaccinali.client.controller to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client.controller.dialog;
    opens it.uninsubria.centrivaccinali.client.controller.dialog to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client;
    opens it.uninsubria.centrivaccinali.client to javafx.fxml;
    exports it.uninsubria.server;
    opens it.uninsubria.server to javafx.fxml;
    exports it.uninsubria.centrivaccinali.models;
    opens it.uninsubria.centrivaccinali.models to javafx.fxml;
    exports it.uninsubria.centrivaccinali.enumerator;
    opens it.uninsubria.centrivaccinali.enumerator to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client.controller.centri;
    opens it.uninsubria.centrivaccinali.client.controller.centri to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client.controller.cittadini;
    opens it.uninsubria.centrivaccinali.client.controller.cittadini to javafx.fxml;
    exports it.uninsubria.centrivaccinali.client.controller.cittadini.dashboard;
    opens it.uninsubria.centrivaccinali.client.controller.cittadini.dashboard to javafx.fxml;
    exports it.uninsubria.centrivaccinali.util;
    opens it.uninsubria.centrivaccinali.util to javafx.fxml;
}