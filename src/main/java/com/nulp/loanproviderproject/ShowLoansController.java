package com.nulp.loanproviderproject;

import com.nulp.dbconnection.DBConnect;
import com.nulp.loan.Loan;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ShowLoansController extends BaseController {
    public static ObservableList<Loan> loans;

    @FXML
    private TableView<Loan> tableOfLoans;
    @FXML
    private TableColumn<Loan, Double> amountColumn ;
    @FXML
    private TableColumn<Loan, Double> annualInterestRateColumn ;
    @FXML
    private TableColumn<Loan, String> bankColumn ;
    @FXML
    private TableColumn<Loan, Boolean> earlyPaymentAvailableColumn;

    @FXML
    private TableColumn<Loan, Boolean> expandLimitAvailableColumn ;

    @FXML
    private TableColumn<Loan, Integer> idColumn;

    @FXML
    private TableColumn<Loan, Integer> numberOfYearsColumn;

    @FXML
    private TableColumn<Loan, String> purposeColumn;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        activateButton(ShowButton);
        slider();

        try {
            buildData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void columnsInit(){
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        bankColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBank()));
        purposeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        numberOfYearsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumberOfYears()).asObject());
        annualInterestRateColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAnnualInterestRate()).asObject());
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        earlyPaymentAvailableColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isEarlyPaymentAvailable()));
        expandLimitAvailableColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isExpandLoanLimitAvailable()));
    }


    public void buildData() throws SQLException {
        String url = "jdbc:sqlserver://localhost;databaseName=LoansDB;user=LoansDBUser;password=loansdb";

        loans = FXCollections.observableArrayList();

        Connection connection = DBConnect.getConnection();

        Statement stmt = connection.createStatement();

        String SQL = "SELECT LoanID, Bank, Purpose, NumberOfYears, AnnualInterestRate, Amount, EarlyPaymentAvailable, ExpandLimitAvailable FROM [LoansTable]";
        ResultSet rs = stmt.executeQuery(SQL);

        while (rs.next()){
            Loan newLoan = new Loan(
                    rs.getInt("LoanID"),
                    rs.getString("Bank"),
                    rs.getString("Purpose"),
                    rs.getInt("NumberOfYears"),
                    rs.getDouble("AnnualInterestRate"),
                    rs.getDouble("Amount"),
                    rs.getByte("EarlyPaymentAvailable") == 1,
                    rs.getByte("ExpandLimitAvailable") == 1
            );

            loans.add(newLoan);
        }

        columnsInit();
        tableOfLoans.setItems(loans);

        connection.close();
    }
}