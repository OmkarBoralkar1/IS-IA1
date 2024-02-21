import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class home {

    public void show(Stage primaryStage) {
        // Create a label to display the welcome message with custom styling
        Label welcomeLabel = new Label("Welcome to the Home Page!");
        welcomeLabel.setStyle("-fx-font-size:   20px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");

        // Create a logout button with custom styling
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size:   16px; -fx-font-weight: bold; -fx-border-radius:   5px; -fx-background-radius:   5px;");
        logoutButton.setOnAction(event -> {
            // Close the application when the logout button is clicked
            Platform.exit();
        });

        // Create the layout and add the controls with custom styling
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #ffffff; -fx-padding:   20px; -fx-border-radius:   10px;");
        layout.getChildren().addAll(welcomeLabel, logoutButton);

        // Create the scene and show the stage
        Scene scene = new Scene(layout,   300,   200);
        primaryStage.setTitle("Home Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
