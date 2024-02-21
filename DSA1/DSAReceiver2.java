import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class DSAReceiver2 {

    public static void main(String[] args) throws Exception {
        if (args.length !=   2) {
            throw new IllegalArgumentException("Expected two arguments: signedMessageBase64 and publicKeyBase64");
        }
        String signedMessageBase64 = args[0];
        String publicKeyBase64 = args[1];
        // Decode the signed message and public key
        byte[] signedMessage = Base64.getDecoder().decode(signedMessageBase64);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // Create a PublicKey object from the received public key bytes
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Print the public key
        System.out.println("Public key: " + publicKeyBase64);

        // Read the original message from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the original message to verify: ");
        String originalMessage = scanner.nextLine();

        // Verify the signature
        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initVerify(publicKey);
        signature.update(originalMessage.getBytes());
        boolean isSignatureValid = signature.verify(signedMessage);

        if (isSignatureValid) {
            System.out.println("Signature is valid. Original message: " + originalMessage);
        } else {
            System.out.println("Signature is not valid.");
        }

        // Close the scanner
        scanner.close();
    }
}
