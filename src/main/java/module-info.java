module com.nulp.loanproviderproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.nulp.loanproviderproject to javafx.fxml;
    exports com.nulp.loanproviderproject;
}