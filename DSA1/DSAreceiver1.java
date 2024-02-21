import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;
public class DSAreceiver1 {

    private String signedMessageBase64;
    private String publicKeyBase64;
    private String message;

    public DSAreceiver1(String signedMessageBase64, String publicKeyBase64, String message) {
        this.signedMessageBase64 = signedMessageBase64;
        this.publicKeyBase64 = publicKeyBase64;
        this.message = message;
    }

    public void verifySignature() throws Exception {
        // Decode the signed message and public key
        byte[] signedMessage = Base64.getDecoder().decode(signedMessageBase64);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // Create a PublicKey object from the received public key bytes
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Print the public key
        System.out.println("Enter the message for verification is : " + message);

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
            System.out.println("Identity verified.");
        } else {
            System.out.println("Signature is not valid.");
        }

        // Close the scanner
        scanner.close();
    }
}
