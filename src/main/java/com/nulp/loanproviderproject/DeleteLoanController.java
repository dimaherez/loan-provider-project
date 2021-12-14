package com.nulp.loanproviderproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DeleteLoanController extends BaseController {

    @FXML
    private TextField IdField;

    @FXML
    private Label succsessfullDeleteLabel;


    @FXML
    void deleteLoan(ActionEvent event) {
        String url = "jdbc:sqlserver://localhost;databaseName=LoansDB;user=LoansDBUser;password=loansdb";
        try (Connection connection = DriverManager.getConnection(url)) {

            //System.out.println("Connected to Microsoft SQL Server");
            Statement stmt = connection.createStatement();

            String query = "DELETE FROM [LoansTable] WHERE LoanId = ?";
            PreparedStatement rs = connection.prepareStatement(query);

            rs.setString(1, IdField.getText());
            try
            {
                rs.executeUpdate();
                succsessfullDeleteLabel.setText("Loan deleted successful");
                clearTextFields();
            }catch(SQLException e){
                succsessfullDeleteLabel.setText("Error occurred");
            }

        } catch (SQLException e) {
            System.out.println("Oops, there is an error");
            e.printStackTrace();
        }
    }

    private void clearTextFields(){
        IdField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activateButton(DeleteButton);

        slider();

        IdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                return;
            }

            int intValue;
            try {
                intValue = Integer.parseInt(newValue);
            } catch (IllegalArgumentException e) {
                IdField.setText(newValue.substring(0, newValue.length() - 1));
            }
        });
    }
}