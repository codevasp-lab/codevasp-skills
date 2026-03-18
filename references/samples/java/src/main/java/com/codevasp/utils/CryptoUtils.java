package com.codevasp.utils;

import com.goterl.lazysodium.LazySodiumJava;
import com.goterl.lazysodium.SodiumJava;
import com.goterl.lazysodium.exceptions.SodiumException;
import com.goterl.lazysodium.interfaces.Box;
import com.goterl.lazysodium.interfaces.MessageEncoder;
import com.goterl.lazysodium.interfaces.Sign;
import com.goterl.lazysodium.utils.KeyPair;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class CryptoUtils {

    private static final LazySodiumJava sodium;

    static {
        sodium = new LazySodiumJava(new SodiumJava(), new UrlSafeBase64MessageEncoder());
        sodium.sodiumInit();
    }

    public static class UrlSafeBase64MessageEncoder implements MessageEncoder {

        @Override
        public String encode(byte[] cipher) {
            return Base64.getEncoder().encodeToString(cipher);
        }

        @Override
        public byte[] decode(String cipherText) {
            return Base64.getDecoder().decode(cipherText);
        }
    }

    public static String generateSignKey() {
        try {
            KeyPair newKey = sodium.cryptoSignKeypair();
            byte[] key64 = newKey.getSecretKey().getAsBytes();
            byte[] key32 = new byte[32];
            System.arraycopy(key64, 0, key32, 0, 32);
            return Base64.getEncoder().encodeToString(key32);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] decrypt(byte[] data, byte[] sharedKey) throws SodiumException {
        byte[] nonce = Arrays.copyOfRange(data, 0, Box.NONCEBYTES);
        byte[] encryptedMsg = Arrays.copyOfRange(data, Box.NONCEBYTES, data.length);
        byte[] decrypted = new byte[encryptedMsg.length - Box.MACBYTES];
        if (!sodium.cryptoBoxOpenEasyAfterNm(
                decrypted, encryptedMsg, encryptedMsg.length, nonce, sharedKey)) {
            throw new SodiumException("Could not decrypt data");
        }
        return decrypted;
    }

    public static String decrypt(String data, String selfPrivateKey, String remotePublicKey)
            throws SodiumException {
        byte[] dataBytes = Base64.getDecoder().decode(data);
        byte[] sharedKey = getSharedKey(remotePublicKey, selfPrivateKey);
        return new String(decrypt(dataBytes, sharedKey));
    }

    private static byte[] getSharedKey(byte[] publicKey, byte[] privateKey) {
        byte[] sharedKey = new byte[Box.BEFORENMBYTES];
        byte[] curPublicKey = new byte[Sign.CURVE25519_PUBLICKEYBYTES];
        byte[] curPrivateKey = new byte[Sign.CURVE25519_SECRETKEYBYTES];
        sodium.convertPublicKeyEd25519ToCurve25519(curPublicKey, publicKey);
        sodium.convertSecretKeyEd25519ToCurve25519(curPrivateKey, privateKey);
        sodium.cryptoBoxBeforeNm(sharedKey, curPublicKey, curPrivateKey);
        return sharedKey;
    }

    private static byte[] getSharedKey(String publicKey, String privateKey) {
        byte[] pukBytes = Base64.getDecoder().decode(publicKey);
        byte[] prkBytes = Base64.getDecoder().decode(privateKey);
        return getSharedKey(pukBytes, prkBytes);
    }

    public static byte[] encrypt(byte[] data, byte[] sharedKey) throws SodiumException, IOException {
        byte[] nonce = sodium.randomBytesBuf(Box.NONCEBYTES);
        byte[] encrypted = new byte[data.length + Box.MACBYTES];
        if (!sodium.cryptoBoxEasyAfterNm(encrypted, data, data.length, nonce, sharedKey)) {
            throw new SodiumException("Could not encrypt data");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(nonce);
        out.write(encrypted);
        return out.toByteArray();
    }

    public static String encrypt(String data, String selfPrivateKey, String remotePublicKey)
            throws SodiumException, IOException {
        byte[] dataBytes = data.getBytes();
        byte[] sharedKey = getSharedKey(remotePublicKey, selfPrivateKey);
        return Base64.getEncoder().encodeToString(encrypt(dataBytes, sharedKey));
    }

    @Getter
    public static class SignatureContainer {
        // Getters
        private final Integer iNonce;
        private final String signatureBase64;
        private final String strDateTime;

        public SignatureContainer(Integer iNonce, String signatureBase64, String strDateTime) {
            this.iNonce = iNonce;
            this.signatureBase64 = signatureBase64;
            this.strDateTime = strDateTime;
        }
    }

    public static SignatureContainer generateSignature(String body, String ownPrivateKey) throws IOException, SodiumException {
        // Generating Signature
        int iNonce = (int) (Math.random() * 10000);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
        String strDateTime = sdf.format(date);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        buffer.write(strDateTime.getBytes());
        buffer.write(body.getBytes());
        buffer.write(toBytes(iNonce));

        byte[] signatureBytes = new byte[Sign.BYTES];
        byte[] seed = Base64.getDecoder().decode(ownPrivateKey);
        KeyPair signKeyPair = sodium.cryptoSignSeedKeypair(seed);
        sodium.cryptoSignDetached(signatureBytes, buffer.toByteArray(), buffer.toByteArray().length,
                signKeyPair.getSecretKey().getAsBytes());

        String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);

        return new SignatureContainer(iNonce, signatureBase64, strDateTime);
    }

    public static String getPublicKey(String privateKey) throws SodiumException {
        byte[] seed = Base64.getDecoder().decode(privateKey);
        KeyPair signKeyPair = sodium.cryptoSignSeedKeypair(seed);
        return Base64.getEncoder().encodeToString(signKeyPair.getPublicKey().getAsBytes());
    }

    private static byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }
}