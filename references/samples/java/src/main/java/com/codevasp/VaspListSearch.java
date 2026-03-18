package com.codevasp;

import com.codevasp.data.SampleData;
import com.codevasp.utils.CryptoUtils;
import com.goterl.lazysodium.exceptions.SodiumException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class VaspListSearch {
  public static void main(String[] args) throws JSONException, IOException, SodiumException {
    log.info("Vasp List Search example.");
    // https://code-docs-en.readme.io/reference/request-vasp-list-search

    String base64OwnPrivateKey = SampleData.BASE64_OWN_PRIVATE_KEY;
    String base64RemotePublicKey = SampleData.BASE64_REMOTE_PUBLIC_KEY;

    CryptoUtils.SignatureContainer signatureContainer = CryptoUtils.generateSignature("", base64OwnPrivateKey);

    HttpResponse<String> httpResponse;
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create("https://trapi-dev.codevasp.com/v1/code/vasps"))
        .GET()
        .header("Content-Type", "application/json")
        .header("X-Code-Req-PubKey", CryptoUtils.getPublicKey(base64OwnPrivateKey))
        .header("X-Code-Req-Signature", signatureContainer.getSignatureBase64())
        .header("X-Code-Req-Datetime", signatureContainer.getStrDateTime())
        .header("X-Code-Req-Nonce", signatureContainer.getINonce().toString())
        .header("X-Request-Origin", "code:".concat(SampleData.OWN_ENTITY_ID)).build();

    try {
      HttpClient httpClient = HttpClient.newHttpClient();
      httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

      int responseCode = httpResponse.statusCode();
      log.info("Response Code: {}", responseCode);
      log.info("Result: " + new JSONObject(httpResponse.body()));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
