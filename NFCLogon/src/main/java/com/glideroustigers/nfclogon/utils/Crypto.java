package com.glideroustigers.nfclogon.utils;

import android.content.Context;
import android.widget.Toast;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class Crypto
{
//    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA512";
    
    private static final String KEY_TRANSPORT_ALGORITHM = "RSA";
    private static final int TRANSPORT_KEY_LENGTH = 1024;
    
    private static final String ENCRYPTION_ALGORITHM = "AES";

//    private static KeyPair deviceKeyPair;

//    public static final void initialize(Context context) throws NoSuchAlgorithmException
//    {
//        if (deviceKeyPair == null)
//        {
//            SecureRandom rand = new SecureRandom(Device.getUUID(context).getBytes());
//            //Toast.makeText(context, rand.nextInt() + "", Toast.LENGTH_LONG).show();
//            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_TRANSPORT_ALGORITHM);
//            generator.initialize(TRANSPORT_KEY_LENGTH, rand);
//            deviceKeyPair = generator.generateKeyPair();
//        }
//    }

//    public static final PublicKey getDevicePublicKey()
//    {
//        return deviceKeyPair.getPublic();
//    }

    public static final KeyPair generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_TRANSPORT_ALGORITHM);
        generator.initialize(TRANSPORT_KEY_LENGTH, new SecureRandom());
        return generator.generateKeyPair();
    }

//    public static final byte[] sign(byte[] data) throws GeneralSecurityException
//    {
//        Signature rsa = Signature.getInstance(MESSAGE_DIGEST_ALGORITHM + "with" + KEY_TRANSPORT_ALGORITHM);
//        rsa.initSign(deviceKeyPair.getPrivate());
//        rsa.update(data);
//        return rsa.sign();
//    }

    public final static byte[] encrypt(SecretKey key, byte[] data) throws GeneralSecurityException
    {
        return alter(key, data, Cipher.ENCRYPT_MODE);
    }

    public final static byte[] decrypt(SecretKey key, byte[] data) throws GeneralSecurityException
    {
        return alter(key, data, Cipher.DECRYPT_MODE);
    }

    private final static byte[] alter(SecretKey key, byte[] data, int mode) throws GeneralSecurityException
    {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(mode, key);
        return cipher.doFinal(data);
    }
}
