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
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Crypto
{
    private static final String KEY_TRANSPORT_ALGORITHM = "RSA";
    private static final int TRANSPORT_KEY_SIZE = 1024;
    
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int ENCRYPTION_KEY_SIZE = 256;

    public static final KeyPair generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_TRANSPORT_ALGORITHM);
        generator.initialize(TRANSPORT_KEY_SIZE, new SecureRandom());
        return generator.generateKeyPair();
    }

    public static final RSAPublicKey getPublicKey(BigInteger modulus, BigInteger exponent) throws GeneralSecurityException
    {
        return (RSAPublicKey) KeyFactory.getInstance(KEY_TRANSPORT_ALGORITHM).generatePublic(new RSAPublicKeySpec(modulus, exponent));
    }

    public static final SecretKey generateSecretKey() throws NoSuchAlgorithmException
    {
        KeyGenerator keygen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keygen.init(ENCRYPTION_KEY_SIZE);
        return keygen.generateKey();
    }

    public static final byte[] encrypt(Key key, byte[] data) throws GeneralSecurityException
    {
        return alter(key, data, Cipher.ENCRYPT_MODE);
    }

    public static final byte[] decrypt(Key key, byte[] data) throws GeneralSecurityException
    {
        return alter(key, data, Cipher.DECRYPT_MODE);
    }

    private static final byte[] alter(Key key, byte[] data, int mode) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(mode, key);
        return cipher.doFinal(data);
    }
}
