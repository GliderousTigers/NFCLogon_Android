package com.glideroustigers.nfclogon.utils;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class to perform cryptography-related operations.
 * It uses RSA for asymmetric keys and AES for symmetric keys.
 * @author Alexandre Cormier
 */
public final class Crypto
{
    // algorithm to use for asymmetric keys used for key transport
    private static final String KEY_TRANSPORT_ALGORITHM = "RSA";

    // size of asymmetric keys used for key transport
    private static final int TRANSPORT_KEY_SIZE = 1024;

    // algorithm to use for symmetric keys used for encryption
    private static final String ENCRYPTION_ALGORITHM = "AES";

    // cipher to use for encryption
    private static final String ENCRYPTION_CIPHER = "CBC";

    // padding to use for encryption
    private static final String ENCRYPTION_PADDING = "PKCS5Padding";

    // size of the symmetric keys used for encryption
    private static final int ENCRYPTION_KEY_SIZE = 128;

    /**
     * Generates a new RSA key pair.
     * @return the generated key pair.
     * @throws NoSuchAlgorithmException if the device does not support RSA.
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_TRANSPORT_ALGORITHM);
        generator.initialize(TRANSPORT_KEY_SIZE);
        return generator.generateKeyPair();
    }

    /**
     * Gets the RSA public key associated with the given modulus and public exponent.
     * @param modulus the public key's modulus.
     * @param exponent the public key's exponent.
     * @return the public key.
     * @throws NoSuchAlgorithmException if the device does not support RSA.
     * @throws InvalidKeySpecException if the key specs are invalid.
     */
    public static RSAPublicKey getPublicKey(BigInteger modulus, BigInteger exponent) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return (RSAPublicKey) KeyFactory.getInstance(KEY_TRANSPORT_ALGORITHM).generatePublic(new RSAPublicKeySpec(modulus, exponent));
    }

    /**
     * Gets the AES secret key represented by the specified byte array.
     * @param bytes the key as bytes.
     * @return the secret key.
     */
    public static SecretKey getSecretKey(byte[] bytes)
    {
        return new SecretKeySpec(bytes, ENCRYPTION_ALGORITHM);
    }

    /**
     * Generates a new AES secret key.
     * @return the generated key.
     * @throws NoSuchAlgorithmException if the device does not support AES.
     */
    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException
    {
        KeyGenerator keygen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keygen.init(ENCRYPTION_KEY_SIZE);
        return keygen.generateKey();
    }

    /**
     * Generates a new IV for AES encryption and decryption.
     * @return the IV.
     */
    public static byte[] generateIV()
    {
        return new SecureRandom().generateSeed(ENCRYPTION_KEY_SIZE / 8);
    }

    /**
     * Encrypts data with the specified secret key and IV.
     * @param key the key to encrypt with.
     * @param iv the IV to use.
     * @param data the data to encrypt.
     * @return the encrypted data.
     * @throws GeneralSecurityException if the devices does not support the encryption scheme.
     */
    public static byte[] encrypt(SecretKey key, byte[] iv, byte[] data) throws GeneralSecurityException
    {
        return alterAES(key, iv, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypts data with the specified secret key and IV.
     * @param key the secret key to decrypt with.
     * @param iv the IV to use.
     * @param data the data to decrypt.
     * @return the decrypted data.
     * @throws GeneralSecurityException if the devices does not support the encryption scheme.
     */
    public static byte[] decrypt(SecretKey key, byte[] iv, byte[] data) throws GeneralSecurityException
    {
        return alterAES(key, iv, data, Cipher.DECRYPT_MODE);
    }

    /**
     * Encrypts data with the specified public key.
     * @param key the public key to encrypt with.
     * @param data the data to encrypt.
     * @return the encrypted data.
     * @throws GeneralSecurityException if the devices does not support the encryption scheme.
     */
    public static byte[] encrypt(PublicKey key, byte[] data) throws GeneralSecurityException
    {
        return alterRSA(key, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypts data with the specified private key.
     * @param key the private key to decrypt with.
     * @param data the data to decrypt.
     * @return the decrypted data.
     * @throws GeneralSecurityException if the devices does not support the encryption scheme.
     */
    public static byte[] decrypt(PrivateKey key, byte[] data) throws GeneralSecurityException
    {
        return alterRSA(key, data, Cipher.DECRYPT_MODE);
    }

    // encrypts or decrypts data using RSA
    private static byte[] alterRSA(Key key, byte[] data, int mode) throws GeneralSecurityException
    {
        Cipher cipherInstance = Cipher.getInstance(KEY_TRANSPORT_ALGORITHM);
        cipherInstance.init(mode, key);
        return cipherInstance.doFinal(data);
    }

    // encrypts or decrypts data using AES
    private static byte[] alterAES(SecretKey key, byte[] iv, byte[] data, int mode) throws GeneralSecurityException
    {
        Cipher cipherInstance = Cipher.getInstance(ENCRYPTION_ALGORITHM + "/" + ENCRYPTION_CIPHER + "/" + ENCRYPTION_PADDING);
        cipherInstance.init(mode, key, new IvParameterSpec(iv));
        return cipherInstance.doFinal(data);
    }
}
