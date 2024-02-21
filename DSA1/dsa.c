#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/dsa.h>
#include <openssl/pem.h>
#include <openssl/err.h>

// Function to generate a DSA key pair
DSA *generate_dsa_keypair() {
    DSA *dsa = DSA_new();
    if (!dsa) {
        fprintf(stderr, "Error creating DSA object.\n");
        return NULL;
    }

    if (!DSA_generate_parameters_ex(dsa,  2048, NULL,  0)) {
        fprintf(stderr, "Error generating DSA parameters.\n");
        DSA_free(dsa);
        return NULL;
    }

    if (!DSA_generate_key(dsa)) {
        fprintf(stderr, "Error generating DSA key.\n");
        DSA_free(dsa);
        return NULL;
    }

    return dsa;
}

// Function to sign a message using the private key
int sign_message(DSA *dsa, const unsigned char *message, size_t message_len, unsigned char **signature, size_t *signature_len) {
    EVP_MD_CTX *mdctx = EVP_MD_CTX_new();
    if (!mdctx) {
        fprintf(stderr, "Error creating message digest context.\n");
        return  0;
    }

    if (EVP_DigestSignInit(mdctx, NULL, EVP_sha256(), NULL, dsa) !=  1) {
        fprintf(stderr, "Error initializing message digest.\n");
        EVP_MD_CTX_free(mdctx);
        return  0;
    }

    if (EVP_DigestSignUpdate(mdctx, message, message_len) !=  1) {
        fprintf(stderr, "Error updating message digest.\n");
        EVP_MD_CTX_free(mdctx);
        return  0;
    }

    if (EVP_DigestSignFinal(mdctx, NULL, signature_len) !=  1) {
        fprintf(stderr, "Error finalizing message digest.\n");
        EVP_MD_CTX_free(mdctx);
        return  0;
    }

    *signature = (unsigned char *)malloc(*signature_len);
    if (!*signature) {
        fprintf(stderr, "Error allocating memory for signature.\n");
        EVP_MD_CTX_free(mdctx);
        return  0;
    }

    if (EVP_DigestSignFinal(mdctx, *signature, signature_len) !=  1) {
        fprintf(stderr, "Error finalizing message digest.\n");
        free(*signature);
        EVP_MD_CTX_free(mdctx);
        return  0;
    }

    EVP_MD_CTX_free(mdctx);
    return  1;
}

// Function to verify a signature using the public key
int verify_signature(DSA *dsa, const unsigned char *message, size_t message_len, const unsigned char *signature, size_t signature_len) {
    EVP_MD_CTX *mdctx = EVP_MD_CTX_new();
    if (!mdctx) {
        fprintf(stderr, "Error creating message digest context.\n");
        return  0;
    }

    if (EVP_DigestVerifyInit(mdctx, NULL, EVP_sha256(), NULL, dsa) !=  1) {
        fprintf(stderr, "Error initializing message digest.\n");
        EVP_MD_CTX_free(mdctx);
        return  0;
    }

    if (EVP_DigestVerifyUpdate(mdctx, message, message_len) !=  1) {
        fprintf(stderr, "Error updating message digest.\n");
        EVP_MD_CTX_free(mdctx);
        return  0;
    }

    int result = EVP_DigestVerifyFinal(mdctx, signature, signature_len);
    EVP_MD_CTX_free(mdctx);

    if (result ==  1) {
        return  1; // Signature is valid
    } else if (result ==  0) {
        fprintf(stderr, "Signature verification failed.\n");
        return  0; // Signature is invalid
    } else {
        fprintf(stderr, "Error verifying signature.\n");
        return -1; // Error occurred
    }
}

int main() {
    // Generate a DSA key pair
    DSA *dsa = generate_dsa_keypair();
    if (!dsa) {
        fprintf(stderr, "Failed to generate DSA key pair.\n");
        return  1;
    }

    // Read the message from the user
    printf("Enter the message to sign: ");
    char message[1024]; // Adjust the size as needed
    if (fgets(message, sizeof(message), stdin) == NULL) {
        fprintf(stderr, "Failed to read message.\n");
        DSA_free(dsa);
        return  1;
    }

    // Remove the newline character from the message
    message[strcspn(message, "\n")] = '\0';

    // Sign the message
    unsigned char *signature = NULL;
    size_t signature_len;
    if (!sign_message(dsa, (unsigned char *)message, strlen(message), &signature, &signature_len)) {
        fprintf(stderr, "Failed to sign message.\n");
        DSA_free(dsa);
        return  1;
    }

    // Verify the signature
    if (verify_signature(dsa, (unsigned char *)message, strlen(message), signature, signature_len) !=  1) {
        fprintf(stderr, "Failed to verify signature.\n");
        free(signature);
        DSA_free(dsa);
        return  1;
    }

    printf("Signature verified successfully.\n");

    // Clean up
    free(signature);
    DSA_free(dsa);

    return  0;
}
