import java.security.*;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Scanner;

public class Datasendet1 {

    public static void main(String[] args) throws Exception {
        // Define the DSA parameters
        BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A63A3620FFFFFFFFFFFFFFFF",   16);
        BigInteger q = new BigInteger("C71CAEB9C6B1C9048E6C522F7011EE7E8863E4C9E93B4279BED27A5A72E6D3D1",   16);
        BigInteger g = new BigInteger("627719696E6B72F8D287DF916E91B108A216D5D9897768232F0FA0C41D3EB6",   16);

        // Generate a DSA private key
        SecureRandom random = new SecureRandom();
        BigInteger x = new BigInteger(q.bitLength(), random);
        BigInteger y = g.modPow(x, p);

        // Create a DSAPrivateKeySpec
        DSAPrivateKeySpec privateKeySpec = new DSAPrivateKeySpec(x, p, q, g);

        // Generate a DSA public key
        DSAPublicKeySpec publicKeySpec = new DSAPublicKeySpec(y, p, q, g);

        // Create a KeyFactory
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");

        // Generate the private key
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // Generate the public key
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Read the message from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the message to sign: ");
        String message = scanner.nextLine();

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
                DSAreceiver1 receiver = new DSAreceiver1(signedMessageBase64, publicKeyBase64, message);
                receiver.verifySignature();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        receiverThread.start();

        // Wait for the receiver thread to complete
        receiverThread.join();

        // Close the scanner
        scanner.close();
    }
}
