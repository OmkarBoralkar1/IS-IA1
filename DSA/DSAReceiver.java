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

        // Create a label to display the message for verification
        Label messageLabel = new Label("Enter the message for verification is: " + message);

        // Create a text field for the user to enter the original message
        TextField originalMessageField = new TextField();
        originalMessageField.setPromptText("Enter the original message to verify");

        // Create a verify button
        Button verifyButton = new Button("Verify");
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Create the layout and add the controls
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(messageLabel, originalMessageField, verifyButton);

        // Schedule the scene creation and stage setup on the JavaFX Application Thread
        Platform.runLater(() -> {
            // Create the scene and show the stage
            Scene scene = new Scene(layout,   300,   200);
            primaryStage.setTitle("Verify Identity");
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }
}
