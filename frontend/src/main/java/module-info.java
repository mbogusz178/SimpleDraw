module graphics.zadone.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires graphics.zadone.threedimensions;
    requires graphics.zadone.filter;
    requires graphics.zadone.analysis;
    requires lombok;

    requires org.kordamp.bootstrapfx.core;

    opens schumi178.javaprojects.graphics.zad1 to javafx.fxml;
    opens schumi178.javaprojects.graphics.zad1.shapes to javafx.fxml;
    exports schumi178.javaprojects.graphics.zad1;
    exports schumi178.javaprojects.graphics.zad1.shapes;
}