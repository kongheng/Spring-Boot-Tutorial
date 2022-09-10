package com.kongheng.springboot.encryption;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AesGCM256 {

  private static final String ALGORITHMS = "AES";
  private static final int KEY_BIT_SIZE = 256;
  private static final int NONCE_BIT_SIZE = 128;
  private static final String TRANSFORMATION = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LENGTH = 16;
  private static final int VECTOR_BYTE_SIZE_AFTER_GENERATED = 16;
  private static final int CHUNK_BYTE_SIZE = 5 * 1024 * 1024;
  private final static SecureRandom SECURE_RANDOM = new SecureRandom();


  private byte[] secretKey;

  public AesGCM256(String base64Key) {
    secretKey = Base64.getDecoder().decode(base64Key);
  }

  public AesGCM256() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHMS);
      keyGenerator.init(KEY_BIT_SIZE);
      secretKey = keyGenerator.generateKey().getEncoded();
    } catch (Exception e) {
      log.error("method=AesGcm256", e);
    }
  }

  public String getSecretKey() {
    if (Objects.isNull(secretKey)) {
      log.info("method=getSecretKey msg=secretKey is null");
      return "";
    }
    return Base64.getEncoder().encodeToString(secretKey);
  }

  public String encrypt(String plainText) throws Exception {
    if (Objects.isNull(secretKey)) {
      throw new Exception("SecretKey is null");
    }
    try {
      byte[] initialVectorBitSize = generate128InitialVectorBitSize();
      Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, initialVectorBitSize);
      byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      byte[] result = new byte[cipherText.length + initialVectorBitSize.length];
      System.arraycopy(cipherText, 0, result, 0, cipherText.length);
      System.arraycopy(initialVectorBitSize, 0, result, cipherText.length,
          initialVectorBitSize.length);
      return Base64.getEncoder().encodeToString(result);
    } catch (Exception e) {
      log.error("method=encrypt", e);
    }
    return "";
  }

  public String decrypt(String cipherText) throws Exception {
    if (Objects.isNull(secretKey)) {
      throw new Exception("SecretKey is null");
    }
    try {
      byte[] cipherTextBytes = Base64.getDecoder()
          .decode(cipherText.getBytes(StandardCharsets.UTF_8));
      byte[] vector = getVectorFromBytes(cipherTextBytes);
      Cipher cipher = initCipher(Cipher.DECRYPT_MODE, vector);
      byte[] bytes = Arrays.copyOfRange(cipherTextBytes, 0, cipherTextBytes.length - vector.length);
      byte[] decryptedText = cipher.doFinal(bytes);
      return new String(decryptedText);
    } catch (Exception e) {
      log.error("method=decrypt", e);
    }
    return "";
  }

  private byte[] getVectorFromBytes(byte[] cipherTextBytes) {
    byte[] temp = new byte[VECTOR_BYTE_SIZE_AFTER_GENERATED];
    int tempIndex = 0;
    int start = cipherTextBytes.length - VECTOR_BYTE_SIZE_AFTER_GENERATED;
    for (int index = start; index < cipherTextBytes.length; index++) {
      temp[tempIndex] = cipherTextBytes[index];
      tempIndex++;
    }
    return temp;
  }

  private Cipher initCipher(int operationMode, byte[] initialVector)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHMS);
    GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, initialVector);
    cipher.init(operationMode, keySpec, gcmParameterSpec);
    return cipher;
  }

  private byte[] generate128InitialVectorBitSize() {
    byte[] initialVector = new byte[NONCE_BIT_SIZE / 8];
    SECURE_RANDOM.nextBytes(initialVector);
    return initialVector;
  }
}
