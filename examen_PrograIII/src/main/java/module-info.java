module cr.ac.lab.examen_prograiii {
    requires javafx.controls;
    requires javafx.fxml;

    opens cr.ac.lab.examen_prograiii to javafx.fxml;
    exports cr.ac.lab.examen_prograiii;
}
