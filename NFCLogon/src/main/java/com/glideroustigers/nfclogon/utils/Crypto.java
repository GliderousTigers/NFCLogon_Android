package com.glideroustigers.nfclogon.utils;

import android.content.Context;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Utility class to perform crypography-related operations.
 * It uses RSA for asymetric keys and AES for symetric keys.
 */
public final class Crypto
{
    // algorithm to use for asymetric keys used for key transport
    private static final String KEY_TRANSPORT_ALGORITHM = "RSA";

    // size of asymetric keys used for key transport
    private static final int TRANSPORT_KEY_SIZE = 1024;

    // algorithm to use for symetric keys used for encryption
    private static final String ENCRYPTION_ALGORITHM = "AES";

    // size of the symetric keys used for encryption
    private static final int ENCRYPTION_KEY_SIZE = 256;

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
     * Encrypts data with the specified key.
     * @param key the key to encrypt with.
     * @param data the data to encrypt.
     * @return the encrypted data.
     * @throws GeneralSecurityException if the devices does not support the encryption scheme.
     */
    public static byte[] encrypt(Key key, byte[] data) throws GeneralSecurityException
    {
        return alter(key, data, Cipher.ENCRYPT_MODE);
    }

    /**
     * Decrypts data with the specified key.
     * @param key the key to decrypt with.
     * @param data the data to decrypt.
     * @return the decrypted data.
     * @throws GeneralSecurityException if the devices does not support the encryption scheme.
     */
    public static byte[] decrypt(Key key, byte[] data) throws GeneralSecurityException
    {
        return alter(key, data, Cipher.DECRYPT_MODE);
    }

    // encrypts or decrypts data
    private static byte[] alter(Key key, byte[] data, int mode) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(mode, key);
        return cipher.doFinal(data);
    }
}
