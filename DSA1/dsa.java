import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class dsa {

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

        // Sign the message
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes());
        byte[] signedMessage = signature.sign();

        // Print the signed message
        System.out.println("Signed message: " + Base64.getEncoder().encodeToString(signedMessage));

        // Verify the signature
        signature.initVerify(publicKey);
        signature.update(message.getBytes());
        boolean isSignatureValid = signature.verify(signedMessage);

        System.out.println("Signature is valid: " + isSignatureValid);

        // Print the original message
        if (isSignatureValid) {
            System.out.println("Original message: " + message);
        }

        // Close the scanner
        scanner.close();
    }
}
