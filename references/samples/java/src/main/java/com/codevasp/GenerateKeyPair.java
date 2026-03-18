package com.codevasp;

import com.goterl.lazysodium.exceptions.SodiumException;
import lombok.extern.slf4j.Slf4j;
import com.codevasp.utils.CryptoUtils;


@Slf4j
public class GenerateKeyPair {
  public static void main(String[] args) throws SodiumException {
    String newKey = CryptoUtils.generateSignKey();
    log.info("new Private Key: {}", newKey);
    String newPubKey = CryptoUtils.getPublicKey(newKey);
    log.info("new Public Key: {}", newPubKey);
  }
}