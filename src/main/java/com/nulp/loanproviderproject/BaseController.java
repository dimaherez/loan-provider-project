package com.nulp.loanproviderproject;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BaseController implements Initializable {
    protected Stage stage;
    protected Scene scene;
    protected Parent parent;

    private static boolean isJustStarted = true;

    @FXML
    protected Label Menu;
    @FXML
    protected Label MenuClose;
    @FXML
    protected ImageView Close;
    @FXML
    protected AnchorPane slider;

    @FXML
    protected Button ShowButton;
    @FXML
    protected Button SearchButton;
    @FXML
    protected Button SelectButton;
    @FXML
    protected Button AddButton;
    @FXML
    protected Button DeleteButton;



    void activateButton(Button button){
        button.setStyle("-fx-background-color: WHITE;");
        button.setTextFill(Paint.valueOf("#4297A0"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void slider(){
        Close.setOnMouseClicked(event -> {
            System.exit(0);
        });

        double prefWidth = slider.getPrefWidth();
        slider.setTranslateX(-prefWidth);
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.001));
        slide.setNode(slider);

        if (!isJustStarted) {
            slide.setToX(0);
            slide.play();
        }else{
            slider.setTranslateX(-prefWidth-15);
        }

        Menu.setVisible(true);
        MenuClose.setVisible(false);

        slide.setDuration(Duration.seconds(0.4));
        Menu.setOnMouseClicked(event -> {
            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-prefWidth-15);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuClose.setVisible(true);
            });
        });

        MenuClose.setOnMouseClicked(event -> {
            slide.setToX(-prefWidth-15);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuClose.setVisible(false);
            });
        });
    }

    @FXML
    public void switchToSearchScene(ActionEvent event) throws IOException {
        isJustStarted = false;
        Parent root = FXMLLoader.load(getClass().getResource("SearchLoanScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void switchToShowScene(ActionEvent event) throws IOException {
        isJustStarted = false;
        Parent root = FXMLLoader.load(getClass().getResource("ShowLoansScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    public void switchToAddScene(ActionEvent event) throws IOException {
        isJustStarted = false;
        Parent root = FXMLLoader.load(getClass().getResource("CreateLoanScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void switchToDeleteScene(ActionEvent event) throws IOException {
        isJustStarted = false;
        Parent root = FXMLLoader.load(getClass().getResource("DeleteLoanScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        stage.show();

    }
}
