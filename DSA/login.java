import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class login extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the email and password fields
        TextField emailField = new TextField();
        emailField.setPromptText("Email ID");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Create the login button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            // String email = emailField.getText();
            // String password = passwordField.getText();

            // Check the credentials (this is a placeholder, replace with actual logic)
            if (true) { // Always true, for demonstration purposes only
                // Redirect to the DSASender page
                try {
                    DSASender.send(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle the exception, e.g., show an error message to the user
                }
            } 
            // else {
            //     System.out.println("Invalid credentials.");
            // }
        });

        // Create the layout and add the controls
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(emailField, passwordField, loginButton);

        // Create the scene and show the stage
        Scene scene = new Scene(layout,   300,   200);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
