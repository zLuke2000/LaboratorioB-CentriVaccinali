package it.uninsubria.centrivaccinali.controller;

import it.uninsubria.centrivaccinali.CentriVaccinali;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.MouseEvent;

public class CVRegistraCittadinoController {

    // TextFiled
    @FXML private TextField TF_CV_centroVaccinale;
    @FXML private TextField TF_CV_nomeCittadino;
    @FXML private TextField TF_CV_cognomeCittadino;
    @FXML private TextField TF_CV_cfCittadinoe;
    @FXML private TextField TF_CV_idVaccino;

    //RadioButton
    @FXML private RadioButton RB_CV_pfizer;
    @FXML private RadioButton RB_CV_astrazeneca;
    @FXML private RadioButton RB_CV_moderna;
    @FXML private RadioButton RB_CV_jj;

    //DatePicker
    @FXML private DatePicker DP_CV_datavaccino;

    //Button
    @FXML private Button  B_CV_registraCittadino;

    public void BackTo(MouseEvent mouseEvent) {
        System.out.println("Indietro");
        CentriVaccinali.setRoot("CV_changed");

    }
}
