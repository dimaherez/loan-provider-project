package com.nulp.loanproviderproject;

import com.nulp.dbconnection.DBConnect;
import com.nulp.loan.Loan;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SearchLoanController extends BaseController {

    private ObservableList<Loan> masterData;
    FilteredList<Loan> filteredData;

    ObservableList<String> earlyPaymentList = FXCollections.observableArrayList("yes", "no");
    ObservableList<String> expandLimitList = FXCollections.observableArrayList("yes", "no");
    ObservableList<String> purposeList = FXCollections.observableArrayList("Student", "Business", "Auto", "House");


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




    @FXML
    private TableView searchTable;

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
    private Label infoAmount;

    @FXML
    private Label infoAnnualInterestRate;

    @FXML
    private Label infoBank;

    @FXML
    private Label infoEarlyPayment;

    @FXML
    private Label infoExpandLimit;

    @FXML
    private Label infoMonthlyPayment;

    @FXML
    private Label infoNumOfYears;

    @FXML
    private Label infoPurpose;

    @FXML
    private Label infoTotalEarlyPaymentLabel;
    @FXML
    private Label infoTotalEarlyPayment;

    @FXML
    private Label infoTotalPayment;

    @FXML
    private Label infoSetLoanLimitLabel;
    @FXML
    private TextField infoSetLoanLimit;

    @FXML
    private Label infoSetNumOfYearsLabel;
    @FXML
    private TextField infoSetNumOfYears;

    @FXML
    private VBox infoVBox;


    @FXML
    private HBox infoSetNumOfYearsHBox;
    @FXML
    private HBox infoSetLoanLimitHBox;


    Loan selectedLoan;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activateButton(SearchButton);
        slider();

        earlyPaymentBox.setItems(earlyPaymentList);
        expandLimitBox.setItems(expandLimitList);
        purposeBox.setItems(purposeList);

        try {
            buildData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        columnsInit();


        selectedLoan = (Loan) searchTable.getSelectionModel().getSelectedItem();

        filter();

        infoVBox.setVisible(false);
        loanInfo();

    }

    public void buildData() throws SQLException {
        masterData = FXCollections.observableArrayList();

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

            masterData.add(newLoan);
        }

        filteredData = new FilteredList<>(masterData, p -> true);

        columnsInit();
        searchTable.setItems(masterData);

        connection.close();
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

    private void filter(){

        //FilteredList<Loan> temp = new FilteredList<>(filteredData);


        bankField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(loan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (loan.getBank().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        numOfYearsField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(loan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                int intFilter;
                try {
                    intFilter = Integer.parseInt(newValue);
                    if (loan.getNumberOfYears() == intFilter) {
                        return true;
                    }

                }catch (IllegalArgumentException e){
                    numOfYearsField.setText(newValue.substring(0, newValue.length() - 1));
                    return false;
                }

                return false;
            });
        });

        annualInterestRateField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(loan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                double doubleFilter;
                try {
                    doubleFilter = Double.parseDouble(newValue);
                    if (loan.getAnnualInterestRate() == doubleFilter) {
                        return true;
                    }

                }catch (IllegalArgumentException e){
                    annualInterestRateField.setText(newValue.substring(0, newValue.length() - 1));
                    return false;
                }
                return false;
            });
        });

        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(loan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                double doubleFilter;
                try {
                    doubleFilter = Double.parseDouble(newValue);
                    if (loan.getAmount() >= doubleFilter) {
                        return true;
                    }

                }catch (IllegalArgumentException e){
                    amountField.setText(newValue.substring(0, newValue.length() - 1));
                    return false;
                }
                return false;
            });
        });


        purposeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                filteredData.setPredicate(loan -> {
                    if (t1.intValue() == -1){
                        return true;
                    }
                    if (purposeBox.getItems().get((Integer) t1) == null) {
                        return true;
                    }

                    String lowerCaseFilter = purposeBox.getItems().get((Integer) t1).toString().toLowerCase(Locale.ROOT);

                    if (loan.getPurpose().toLowerCase(Locale.ROOT).equals(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            }
        });

        earlyPaymentBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                filteredData.setPredicate(loan -> {
                    if (t1.intValue() == -1){
                        return true;
                    }
                    if (earlyPaymentBox.getItems().get((Integer) t1) == null) {
                        return true;
                    }

                    String filterValue = earlyPaymentBox.getItems().get((Integer) t1).toString();

                    if ((loan.isEarlyPaymentAvailable() && filterValue.equals("yes")) || (!loan.isEarlyPaymentAvailable() && filterValue.equals("no"))) {
                        return true;
                    }
                    return false;
                });
            }
        });

        expandLimitBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                filteredData.setPredicate(loan -> {
                    if (t1.intValue() == -1){
                        return true;
                    }
                    if (expandLimitBox.getItems().get((Integer) t1) == null) {
                        return true;
                    }

                    String filterValue = expandLimitBox.getItems().get((Integer) t1).toString();

                    if ((loan.isEarlyPaymentAvailable() && filterValue.equals("yes")) || (!loan.isEarlyPaymentAvailable() && filterValue.equals("no"))) {
                        return true;
                    }
                    return false;
                });
            }
        });


        searchTable.setItems(filteredData);


    }

//    private FilteredList<Loan> intersect(){
//        FilteredList<Loan> filteredDataList = new FilteredList<>(masterData, p -> true);;
//        for (Loan loan : masterData){
//            if (
//                    filteredDataByBank.contains(loan) &&
//                            filteredDataByPurpose.contains(loan) &&
//                            filteredDataByNumOfYears.contains(loan) &&
//                            filteredDataByAnnualInterestRate.contains(loan) &&
//                            filteredDataByAmount.contains(loan) &&
//                            filteredDataByEarlyPayment.contains(loan) &&
//                            filteredDataByExpandLimit.contains(loan)
//            ){
//                filteredDataList.add(loan);
//            }
//        }
//
//        return filteredDataList;
//    }

    @FXML
    private void reset(){
        amountField.clear();
        annualInterestRateField.clear();
        numOfYearsField.clear();
        bankField.clear();

        purposeBox.setValue(null);
        earlyPaymentBox.setValue(null);
        expandLimitBox.setValue(null);

        searchTable.setItems(masterData);


        filter();
    }

    private void loanInfo(){
        searchTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->{
            if (newSelection != null){
                infoVBox.setVisible(true);
                infoTotalEarlyPaymentLabel.setVisible(false);
                infoTotalEarlyPayment.setVisible(false);

                infoSetLoanLimitLabel.setVisible(false);
                infoSetLoanLimit.setVisible(false);

                infoSetNumOfYearsLabel.setVisible(false);
                infoSetNumOfYears.setVisible(false);

                Loan selectedLoan = (Loan) searchTable.getSelectionModel().getSelectedItem();

                infoBank.setText(selectedLoan.getBank());
                infoPurpose.setText(selectedLoan.getPurpose());
                infoNumOfYears.setText(String.valueOf(selectedLoan.getNumberOfYears()));
                infoAnnualInterestRate.setText(String.valueOf(selectedLoan.getAnnualInterestRate()));
                infoAmount.setText(String.valueOf(selectedLoan.getAmount()));
                infoEarlyPayment.setText(selectedLoan.isEarlyPaymentAvailable() ? "yes" : "no");
                infoExpandLimit.setText(selectedLoan.isExpandLoanLimitAvailable() ? "yes" : "no");
                infoMonthlyPayment.setText(String.valueOf(selectedLoan.getMonthlyPayment()));
                infoTotalPayment.setText(String.valueOf(selectedLoan.getTotalPayment()));
                if (selectedLoan.isEarlyPaymentAvailable()){
                    //infoTotalEarlyPayment.setText(String.valueOf(selectedLoan.getTotalEarlyPayment(Integer.parseInt(infoNumOfYears.getText()))));
                    infoTotalEarlyPaymentLabel.setVisible(true);
                    infoTotalEarlyPayment.setVisible(true);
                    infoSetNumOfYears.setText(String.valueOf(selectedLoan.getNumberOfYears() - 1));
                    infoTotalEarlyPayment.setText(String.valueOf(selectedLoan.getTotalEarlyPayment(Integer.parseInt(infoSetNumOfYears.getText()))));

                    infoSetNumOfYearsLabel.setVisible(true);
                    infoSetNumOfYears.setVisible(true);

                    infoSetNumOfYears.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.isEmpty()){
                            return;
                        }
                        try {
                            infoTotalEarlyPayment.setText(String.valueOf(selectedLoan.getTotalEarlyPayment(Integer.parseInt(newValue))));
                        }catch (IllegalArgumentException e){
                            infoTotalEarlyPayment.setText(newValue.substring(0, newValue.length() - 1));
                        }
                    });
                }
                if (selectedLoan.isExpandLoanLimitAvailable()){
                    infoSetLoanLimitLabel.setVisible(true);
                    infoSetLoanLimit.setVisible(true);
                    if (!selectedLoan.isEarlyPaymentAvailable()){
                        infoSetLoanLimitHBox.setTranslateY(infoSetNumOfYearsHBox.getTranslateY() - 85);
                    }else{
                        infoSetLoanLimitHBox.setTranslateY(infoSetNumOfYearsHBox.getTranslateY());
                    }
                    infoSetLoanLimit.setText(String.valueOf(selectedLoan.getAmount()));
                    infoMonthlyPayment.setText(String.valueOf(selectedLoan.getMonthlyPayment(Double.parseDouble(infoSetLoanLimit.getText()))));
                    infoTotalPayment.setText(String.valueOf(selectedLoan.getTotalPayment(Double.parseDouble(infoSetLoanLimit.getText()))));

                    infoSetLoanLimit.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.isEmpty()){
                            return;
                        }
                        try {
                            infoAmount.setText(String.valueOf(Integer.parseInt(newValue)));
                            infoMonthlyPayment.setText(String.valueOf(selectedLoan.getMonthlyPayment(Double.parseDouble(infoSetLoanLimit.getText()))));
                            infoTotalPayment.setText(String.valueOf(selectedLoan.getTotalPayment(Double.parseDouble(infoSetLoanLimit.getText()))));
                        }catch (IllegalArgumentException e){
                            Platform.runLater(() ->{
                                infoSetLoanLimit.setText(newValue.substring(0, newValue.length() - 1));
                            });

                        }
                    });
                }
            }
        });
    }
}