module dk.easv.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires java.naming;


    opens dk.easv.privatemoviecollection to javafx.fxml;
    //exports dk.easv.privatemoviecollection;
    exports dk.easv.privatemoviecollection.GUI;
    opens dk.easv.privatemoviecollection.GUI to javafx.fxml;
    exports dk.easv.privatemoviecollection.GUI.Controller;
    opens dk.easv.privatemoviecollection.GUI.Controller to javafx.fxml;
}