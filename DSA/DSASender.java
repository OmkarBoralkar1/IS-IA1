import java.security.*;
import java.util.Base64;
import java.util.Random;
import javafx.stage.Stage;

public class DSASender {

    public static void send(Stage primaryStage) throws Exception {
        // Generate a DSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Get the public and private keys
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Generate a random message of length   8 consisting of letters and numbers
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder messageBuilder = new StringBuilder(8);
        for (int i =   0; i <   8; i++) {
            int index = random.nextInt(alphabet.length());
            messageBuilder.append(alphabet.charAt(index));
        }
        String message = messageBuilder.toString();

        // Sign the message
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] signedMessage = signature.sign();

        // Print the signed message and the public key
        String signedMessageBase64 = Base64.getEncoder().encodeToString(signedMessage);
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        // Print the public key and the message
        System.out.println("Public key: " + publicKeyBase64);
        System.out.println("Message: " + message);

        // Start the receiver thread
        Thread receiverThread = new Thread(() -> {
            try {
                DSAReceiver.verify(primaryStage, signedMessageBase64, publicKeyBase64, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        receiverThread.start();

        // Wait for the receiver thread to complete
        receiverThread.join();

        // After the DSA process is complete, redirect to the home page
        // This should be done on the JavaFX Application Thread
        // For demonstration purposes, we'll just print a message
        System.out.println("DSA process complete. Redirecting to home page...");
    }
}
