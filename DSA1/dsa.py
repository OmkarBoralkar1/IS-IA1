from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import dsa
from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.backends import default_backend

# Function to generate a DSA key pair
def generate_dsa_keypair():
    private_key = dsa.generate_private_key(
        key_size=2048,
        backend=default_backend()
    )
    public_key = private_key.public_key()
    return private_key, public_key

# Function to sign a message using the private key
def sign_message(private_key, message):
    signature = private_key.sign(
        message,
        padding.PSS(
            mgf=padding.MGF1(hashes.SHA256()),
            salt_length=padding.PSS.MAX_LENGTH
        ),
        hashes.SHA256()
    )
    return signature

# Function to verify a signature using the public key
def verify_signature(public_key, message, signature):
    try:
        public_key.verify(
            signature,
            message,
            padding.PSS(
                mgf=padding.MGF1(hashes.SHA256()),
                salt_length=padding.PSS.MAX_LENGTH
            ),
            hashes.SHA256()
        )
        return True
    except:
        return False

# Main function
def main():
    # Generate a DSA key pair
    private_key, public_key = generate_dsa_keypair()

    # Read the message from the user
    message = input("Enter the message to sign: ")

    # Sign the message
    signature = sign_message(private_key, message.encode())

    # Verify the signature
    if verify_signature(public_key, message.encode(), signature):
        print("Signature verified successfully.")
    else:
        print("Failed to verify signature.")

# Run the main function
if __name__ == "__main__":
    main()
