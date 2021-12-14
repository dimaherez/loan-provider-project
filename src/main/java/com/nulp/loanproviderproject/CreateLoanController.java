package com.nulp.loanproviderproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateLoanController extends BaseController {

    ObservableList<String> earlyPaymentList = FXCollections.observableArrayList("yes", "no");
    ObservableList<String> expandLimitList = FXCollections.observableArrayList("yes", "no");
    ObservableList<String> purposeList = FXCollections.observableArrayList("Student", "Business", "Auto", "House");

    @FXML
    private TextField amountField;

    @FXML
    private TextField annualInterestRateField;

    @FXML
    private TextField bankField;

    @FXML
    private ChoiceBox earlyPaymentBox;

    @FXML
    private ChoiceBox expandLimitBox;

    @FXML
    private TextField numOfYearsField;

    @FXML
    private ChoiceBox purposeBox;

    @FXML
    private Label succsessfulCreateLabel;

    @FXML
    void insertLoan(ActionEvent event) {
        String url = "jdbc:sqlserver://localhost;databaseName=LoansDB;user=LoansDBUser;password=loansdb";
        try (Connection connection = DriverManager.getConnection(url)) {

            //System.out.println("Connected to Microsoft SQL Server");
            Statement stmt = connection.createStatement();

            String query = "INSERT INTO [LoansTable] (Bank, Purpose, NumberOfYears, AnnualInterestRate, Amount, EarlyPaymentAvailable, ExpandLimitAvailable) VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement rs = connection.prepareStatement(query);
            //ResultSet rs = prs.executeQuery();

            try {
                rs.setString(1, bankField.getText());
                rs.setString(2, purposeBox.getSelectionModel().getSelectedItem().toString());
                rs.setInt(3, Integer.parseInt(numOfYearsField.getText()));
                rs.setDouble(4, Double.parseDouble(annualInterestRateField.getText()));
                rs.setDouble(5, Double.parseDouble(amountField.getText()));
                rs.setInt(6, earlyPaymentBox.getSelectionModel().getSelectedItem().equals("yes") ? 1 : 0);
                rs.setInt(7, expandLimitBox.getSelectionModel().getSelectedItem().equals("yes") ? 1 : 0);

                rs.execute();
                succsessfulCreateLabel.setText("Loan created successful");
                clearTextFields();
            } catch (NumberFormatException | SQLException | NullPointerException e){
                succsessfulCreateLabel.setText("Error occurred");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearTextFields(){
        amountField.clear();
        annualInterestRateField.clear();
        bankField.clear();
        numOfYearsField.clear();
        purposeBox.setValue("");
        expandLimitBox.setValue("");
        earlyPaymentBox.setValue("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activateButton(AddButton);
        slider();

        earlyPaymentBox.setItems(earlyPaymentList);
        expandLimitBox.setItems(expandLimitList);
        purposeBox.setItems(purposeList);

        check();
    }

    private void check() {
        numOfYearsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            int intValue;
            try {
                intValue = Integer.parseInt(newValue);
            } catch (IllegalArgumentException e) {
                numOfYearsField.setText(newValue.substring(0, newValue.length() - 1));
            }
        });

        annualInterestRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            double doubleValue;
            try {
                doubleValue = Double.parseDouble(newValue);
            } catch (IllegalArgumentException e) {
                annualInterestRateField.setText(newValue.substring(0, newValue.length() - 1));
            }
        });

        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            double doubleValue;
            try {
                doubleValue = Double.parseDouble(newValue);
            } catch (IllegalArgumentException e) {
                amountField.setText(newValue.substring(0, newValue.length() - 1));
            }
        });
    }
}