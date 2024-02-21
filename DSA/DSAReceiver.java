import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DSAReceiver {

    public static void verify(Stage primaryStage, String signedMessageBase64, String publicKeyBase64, String message) throws Exception {
        // Decode the signed message and public key
        byte[] signedMessage = Base64.getDecoder().decode(signedMessageBase64);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // Create a PublicKey object from the received public key bytes
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Create a label to display the message for verification with custom styling
        Label messageLabel = new Label("Verification code is: " + message);
        messageLabel.setStyle("-fx-font-size:   14px; -fx-font-weight: bold;");

        // Create a text field for the user to enter the original message with custom styling
        TextField originalMessageField = new TextField();
        originalMessageField.setPromptText("Enter the original message to verify");
        originalMessageField.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #c0c0c0; -fx-border-radius:   5px; -fx-padding:   5px;");

        // Create a verify button with custom styling
        Button verifyButton = new Button("Verify");
        verifyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size:   16px; -fx-font-weight: bold; -fx-border-radius:   5px; -fx-background-radius:   5px;");

        // Create the layout and add the controls with custom styling
        final VBox[] layout = new VBox[1]; // Use a final one-dimensional array to hold the layout reference
        layout[0] = new VBox(10);
        layout[0].setAlignment(Pos.CENTER);
        layout[0].setStyle("-fx-background-color: #ffffff; -fx-padding:   20px; -fx-border-radius:   10px;");
        layout[0].getChildren().addAll(messageLabel, originalMessageField, verifyButton);

        verifyButton.setOnAction(event -> {
            String originalMessage = originalMessageField.getText();

            // Verify the signature
            try {
                Signature signature = Signature.getInstance("SHA256withDSA");
                signature.initVerify(publicKey);
                signature.update(originalMessage.getBytes());
                boolean isSignatureValid = signature.verify(signedMessage);

                if (isSignatureValid) {
                    // If the signature is valid, redirect to the home page
                    Platform.runLater(() -> {
                        home homePage = new home();
                        homePage.show(primaryStage);
                    });
                } else {
                    System.out.println("Signature is not valid.");
                    Label messageLabelerror = new Label("Signature is not valid.");
                    messageLabelerror.setStyle("-fx-font-size:   14px; -fx-font-weight: bold; -fx-text-fill: #f44336;"); // Red text for error
                    layout[0].getChildren().add(messageLabelerror); // Add the error label to the layout
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Schedule the scene creation and stage setup on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Create the scene and show the stage
            Scene scene = new Scene(layout[0],   300,   200);
            primaryStage.setTitle("Verify Identity");
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }
}
