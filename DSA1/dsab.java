import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class dsab {

    public static void main(String[] args) throws Exception {
        // Generate a DSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Get the public and private keys
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // Read the message from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the message to sign: ");
        String message = scanner.nextLine();

        // Start the sender thread
        Thread senderThread = new Thread(() -> {
            try {
                // Sign the message
                Signature signature = Signature.getInstance("SHA256withDSA");
                signature.initSign(privateKey);
                signature.update(message.getBytes());
                byte[] signedMessage = signature.sign();

                // Print the signed message and the public key
                System.out.println("Signed message: " + Base64.getEncoder().encodeToString(signedMessage));
                System.out.println("Public key: " + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        senderThread.start();

        // Start the receiver thread
        Thread receiverThread = new Thread(() -> {
            try {
                // Assume the signed message and public key are received from Device A
                String signedMessageBase64 = "signedMessageFromDeviceA"; // Replace with actual signed message
                String publicKeyBase64 = "publicKeyFromDeviceA"; // Replace with actual public key

                // Decode the signed message and public key
                byte[] signedMessage = Base64.getDecoder().decode(signedMessageBase64);
                byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

                // Create a PublicKey object from the received public key bytes
                X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                PublicKey receivedPublicKey = keyFactory.generatePublic(publicKeySpec); // Renamed variable

                // Verify the signature
                Signature signature = Signature.getInstance("SHA256withDSA");
                signature.initVerify(receivedPublicKey); // Use the renamed variable
                signature.update(message.getBytes());
                boolean isSignatureValid = signature.verify(signedMessage);

                System.out.println("Signature is valid: " + isSignatureValid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        receiverThread.start();

        // Close the scanner
        scanner.close();
    }
}
