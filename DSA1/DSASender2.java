import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Scanner;

public class DSASender2 {

    public static void main(String[] args) throws Exception {
        // Step   1: Generate two large prime numbers p and q
        BigInteger p = generateLargePrime();
        BigInteger q = generateLargePrime();

        // Step   2: Calculate g (the generator) using p and q
        BigInteger g = generateG(p, q);

        // Step   3: Generate a private key x which is a random number less than q
        BigInteger x = generatePrivateKey(q);

        // Step   4: Calculate the public key y by raising g to the power of x modulo p
        BigInteger y = g.modPow(x, p);

        // Create a PublicKey object from the public key components
        PublicKey publicKey = new PublicKey(y, p, q, g);

        // Create a PrivateKey object from the private key components
        PrivateKey privateKey = new PrivateKey(x, p, q, g);

        // Read the message from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the message to sign: ");
        String message = scanner.nextLine();

        // Sign the message
        Signature signature = Signature.getInstance("SHA256withDSA", "SUN");

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
                DSAReceiver2.main(new String[]{signedMessageBase64, publicKeyBase64});
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

    private static BigInteger generateLargePrime() {
        return BigInteger.probablePrime(2048, new SecureRandom());
    }

    private static BigInteger generateG(BigInteger p, BigInteger q) {
        // This is a placeholder for the actual g generation algorithm
        // In a real implementation, you would use a hash function and modular exponentiation
        return BigInteger.valueOf(2);
    }

    private static BigInteger generatePrivateKey(BigInteger q) {
        SecureRandom secureRandom = new SecureRandom();
        return new BigInteger(2048, secureRandom);
    }
    static class PublicKey implements DSAPublicKey {
        private BigInteger y;
        private BigInteger p;
        private BigInteger q;
        private BigInteger g;

        public PublicKey(BigInteger y, BigInteger p, BigInteger q, BigInteger g) {
            this.y = y;
            this.p = p;
            this.q = q;
            this.g = g;
        }

        @Override
        public String getAlgorithm() {
            return "DSA";
        }

        @Override
        public String getFormat() {
            return "X.09";
        }

        @Override
        public byte[] getEncoded() {
            try {
                KeySpec publicKeySpec = new DSAPublicKeySpec(y, p, q, g);
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                DSAPublicKey publicKey = (DSAPublicKey) keyFactory.generatePublic(publicKeySpec);
                return publicKey.getEncoded();
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new RuntimeException("Could not generate public key", e);
            }
        }

        @Override
        public BigInteger getY() {
            return y;
        }

        @Override
        public DSAParams getParams() {
            return new DSAParams() {
                @Override
                public BigInteger getP() {
                    return p;
                }

                @Override
                public BigInteger getQ() {
                    return q;
                }

                @Override
                public BigInteger getG() {
                    return g;
                }
            };
        }
    }

    static class PrivateKey implements DSAPrivateKey {
        private BigInteger x;
        private BigInteger p;
        private BigInteger q;
        private BigInteger g;

        public PrivateKey(BigInteger x, BigInteger p, BigInteger q, BigInteger g) {
            this.x = x;
            this.p = p;
            this.q = q;
            this.g = g;
        }

        @Override
        public String getAlgorithm() {
            return "DSA";
        }

        @Override
        public String getFormat() {
            return "PKCS#8";
        }

        @Override
        public byte[] getEncoded() {
            try {
                KeySpec privateKeySpec = new DSAPrivateKeySpec(x, p, q, g);
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                DSAPrivateKey privateKey = (DSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
                return privateKey.getEncoded();
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new RuntimeException("Could not generate private key", e);
            }
        }

        @Override
        public BigInteger getX() {
            return x;
        }

        @Override
        public DSAParams getParams() {
            return new DSAParams() {
                @Override
                public BigInteger getP() {
                    return p;
                }

                @Override
                public BigInteger getQ() {
                    return q;
                }

                @Override
                public BigInteger getG() {
                    return g;
                }
            };
        }
    }
}
