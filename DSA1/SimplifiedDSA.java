import java.math.BigInteger;
import java.security.SecureRandom;


public class SimplifiedDSA {

    public static void main(String[] args) {
        // Generate DSA parameters
        BigInteger p = generatePrime(2048);
        BigInteger q = generatePrime(160);
        BigInteger g = generatePrime(2048);

        // Generate a private key x (which is a random number less than q)
        SecureRandom random = new SecureRandom();
        BigInteger x = new BigInteger(q.bitLength(), random);

        // Compute the public key y = g^x mod p
        BigInteger y = g.modPow(x, p);

        // Print the keys
        System.out.println("Private key: " + x);
        System.out.println("Public key: " + y);

        // Example message to sign
        String message = "Hello, world!";
        byte[] messageBytes = message.getBytes();

        // Sign the message
        BigInteger r = BigInteger.ZERO;
        BigInteger s = BigInteger.ZERO;
        BigInteger k = BigInteger.ZERO;
        do {
            k = new BigInteger(q.bitLength(), random);
            r = g.modPow(k, p).mod(q);
        } while (r.equals(BigInteger.ZERO));

        BigInteger h = new BigInteger(1, messageBytes).mod(q);
        s = k.modInverse(q).multiply(h.add(x.multiply(r))).mod(q);

        // Print the signature
        System.out.println("Signature: (" + r + ", " + s + ")");

        // Verify the signature
        BigInteger w = s.modInverse(q);
        BigInteger u1 = h.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);

        System.out.println("Signature is " + (v.equals(r) ? "valid" : "invalid"));
    }

    private static BigInteger generatePrime(int bitLength) {
        return BigInteger.probablePrime(bitLength, new SecureRandom());
    }
}
