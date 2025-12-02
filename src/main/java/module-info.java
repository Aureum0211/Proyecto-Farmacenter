module com.mycompany.farmacia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    
    // CRÍTICO: Abrir paquetes a JavaFX para que pueda acceder a las clases
    opens com.mycompany.farmacia to javafx.fxml;
    opens com.mycompany.farmacia.controller to javafx.fxml;
    opens com.mycompany.farmacia.model to javafx.base, javafx.fxml;  // ← ESTA ES LA LÍNEA CLAVE
    
    // Exportar paquetes
    exports com.mycompany.farmacia;
    exports com.mycompany.farmacia.controller;
    exports com.mycompany.farmacia.model;
}