import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class home {

    public void show(Stage primaryStage) {
        // Create a label to display the welcome message
        Label welcomeLabel = new Label("Welcome to the Home Page!");

        // Create a logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            // Close the application when the logout button is clicked
            Platform.exit();
        });

        // Create the layout and add the controls
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(welcomeLabel, logoutButton);

        // Create the scene and show the stage
        Scene scene = new Scene(layout,   300,   200);
        primaryStage.setTitle("Home Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
