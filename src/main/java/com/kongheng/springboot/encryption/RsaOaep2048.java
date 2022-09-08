package com.kongheng.springboot.encryption;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class RsaOaep2048 {

  private static final String ALGORITHMS = "RSA";
  private static final int KEY_SIZE = 2048;
  private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
  private static final String HASH = "SHA-256";
  private static final String MASK_GENERATION_FUNCTION = "MGF1";


  private PrivateKey privateKey;
  private PublicKey publicKey;

  public RsaOaep2048() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHMS);
      keyPairGenerator.initialize(KEY_SIZE);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      privateKey = keyPair.getPrivate();
      publicKey = keyPair.getPublic();
    } catch (Exception e) {
      log.error("method=RsaOaep2048", e);
    }
  }

  public RsaOaep2048(String base64PrivateKey, String base64PublicKey) {
    if (!Objects.isNull(base64PrivateKey)) {
      privateKey = importPrivateKey(base64PrivateKey);
    }
    if (!Objects.isNull(base64PublicKey)) {
      publicKey = importPublicKey(base64PublicKey);
    }
  }

  private PublicKey importPublicKey(String base64PublicKey) {
    try {
      X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
          Base64.getDecoder().decode(base64PublicKey));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
      return keyFactory.generatePublic(publicKeySpec);
    } catch (Exception e) {
      log.error("method=importPublicKey", e);
    }
    return null;
  }

  private PrivateKey importPrivateKey(String base64PrivateKey) {
    try {
      PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
          Base64.getDecoder().decode(base64PrivateKey));
      KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHMS);
      return keyFactory.generatePrivate(privateKeySpec);
    } catch (Exception e) {
      log.error("method=importPrivateKey", e);
    }
    return null;
  }

  public String encrypt(String plainText) throws Exception {
    if (Objects.isNull(publicKey)) {
      throw new Exception("Public Key is null");
    }
    try {
      Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, publicKey);
      byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(cipherText);
    } catch (Exception e) {
      log.error("method=encrypt", e);
    }
    return "";
  }

  public String decrypt(String cipherBase64) throws Exception {
    if (Objects.isNull(privateKey)) {
      throw new Exception("Private Key is null");
    }
    try {
      Cipher cipher = initCipher(Cipher.DECRYPT_MODE, privateKey);
      byte[] plainByte = cipher.doFinal(Base64.getDecoder().decode(cipherBase64));
      return new String(plainByte, StandardCharsets.UTF_8);
    } catch (Exception e) {
      log.error("method=decrypt", e);
    }
    return "";
  }

  private Cipher initCipher(int operationMode, Key key)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    OAEPParameterSpec parameterSpec = new OAEPParameterSpec(HASH, MASK_GENERATION_FUNCTION,
        MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
    cipher.init(operationMode, key, parameterSpec);
    return cipher;
  }
}
