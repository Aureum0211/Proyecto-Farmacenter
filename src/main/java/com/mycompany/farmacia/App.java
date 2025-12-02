package com.mycompany.farmacia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // La variable "login" se pasa como argumento, y el método loadFXML
        // se encarga de construir la ruta completa.
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setTitle("FARMA CENTER");
        // Asegúrate de usar el nombre correcto del Stage, aquí usé 'stage'
        stage.setMaximized(true); 
        
        // Opcional: Centrar la ventana al iniciar (útil si NO usas maximized/fullscreen)
        // Si usas maximized=true, esta línea no es estrictamente necesaria.
        stage.centerOnScreen(); 
        stage.setScene(scene); 
        stage.show();
    }

    // Método para cambiar la raíz de la escena (útil para cambiar de pantalla)
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // *** MODIFICACIÓN CRÍTICA AQUÍ ***
    // Este método ahora construye la ruta completa del recurso para encontrar
    // los archivos FXML que están dentro del paquete 'com.mycompany.farmacia'
    // en la carpeta 'resources'.
    private static Parent loadFXML(String fxml) throws IOException {
        // Ruta completa del recurso. Se asume que fxmlLoader está en el paquete raíz de resources,
        // que es 'com/mycompany/farmacia/'.
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/mycompany/farmacia/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
    // **********************************

    public static void main(String[] args) {
        launch();
    }

}

