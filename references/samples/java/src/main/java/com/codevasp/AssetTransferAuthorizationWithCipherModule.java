package com.codevasp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.codevasp.data.SampleData;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is an example when you use the CODE Cipher module.
 */
@Slf4j
public class AssetTransferAuthorizationWithCipherModule {
  public static void main(String[] args) throws JSONException, IOException {
    log.info("Asset Transfer Authorization with CipherModule example.");

    String base64RemotePublicKey = SampleData.BASE64_REMOTE_PUBLIC_KEY;
    String beneficiaryVaspEntityId = SampleData.EXAMPLE_BENEFICIARY_ENTITY_ID;

    String cipherBody = """
        {
            "remotePublicKey": "$base64RemotePublicKey",
            "apiType": "TRANSFER_AUTHORIZATION",
            "request": {
                "amount": "0.00001",
                "tradePrice": "55555",
                "transferId": "$transferId",
                "tradeCurrency": "KRW",
                "historicalCost": "",
                "currency": "BTC",
                "originatingVasp": {},
                "isExceedingThreshold": true,
                "originatorNaturalPersonLastName": "Barnes",
                "originatorNaturalPersonFirstName": "Robert",
                "originatorNaturalPersonLocalLastName": "반스",
                "originatorNaturalPersonLocalFirstName": "로버트",
                "originatorLegalName": "(주)코인원",
                "originatorLegalPersonLastName": "Kim",
                "originatorLegalPersonFirstName": "Chulsu",
                "originatorLegalPersonLocalLastName": "김",
                "originatorLegalPersonLocalFirstName": "철수",
                "nameIdentifierType": "LEGL",
                "customerIdentification": "3213213qweqwe213312",
                "originatorWalletAddress": "012345678900",
                "originatingVaspCountryOfRegistration": "KR",
                "originatingVaspLegalName": "Korbit Inc.",
                "originatingVaspAddressType": "GEOG",
                "originatingVaspTownName": "Seoul",
                "originatingVaspAddressLine": [
                    "14 Teheran-ro 4-gil, Gangnam-gu",
                    "4th floor"
                ],
                "originatingVaspCountry": "KR",
                "originatingVaspNationalIdentifier": "1234567890",
                "originatingVaspNationalIdentifierType": "RAID",
                "originatingVaspRegistrationAuthority": "RA000657",
                "beneficiaryWalletAddress": "1KzHK8WMRHRCvRjUV5PFny3v6fqT3UAY5K",
                "beneficiaryNaturalPersonLastName": "Kim",
                "beneficiaryNaturalPersonFirstName": "Code"
            }
        }
        """;
    cipherBody =
        cipherBody.replace("$base64RemotePublicKey", base64RemotePublicKey).replace("$transferId",
            UUID.randomUUID().toString());

    String signature;
    String nonce;
    String dateTime;
    String publicKey;
    String body;

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create("http://localhost:8787/api/v1/code/api-payloads"))
        .POST(HttpRequest.BodyPublishers.ofString(cipherBody, StandardCharsets.UTF_8))
        .header("Content-Type", "application/json")
        .build();

    HttpResponse<String> httpResponse;

    try {
      httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

      // 응답 상태 코드 및 메시지 출력
      int cipherResponseCode = httpResponse.statusCode();
      log.info("Cipher Response Code: {}", cipherResponseCode);

      // 응답 본문 출력
      JSONObject jsonObject = new JSONObject(httpResponse.body());
      var hasBody = jsonObject.has("body");
      log.info("Signature: " + jsonObject.get("signature"));
      log.info("Nonce: " + jsonObject.get("nonce"));
      log.info("DateTime: " + jsonObject.get("dateTime"));
      log.info("PublicKey: " + jsonObject.get("publicKey"));
      log.info("Body: " + (hasBody ? jsonObject.getString("body") : null));

      signature = jsonObject.get("signature").toString();
      nonce = jsonObject.get("nonce").toString();
      dateTime = jsonObject.get("dateTime").toString();
      publicKey = jsonObject.get("publicKey").toString();
      body = (hasBody ? jsonObject.getString("body") : null);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    var bodyPublisher = body != null ?
        HttpRequest.BodyPublishers.ofByteArray(body.getBytes(StandardCharsets.UTF_8)) :
        HttpRequest.BodyPublishers.noBody();

    var builder = HttpRequest.newBuilder()
        .uri(URI.create("https://trapi-dev.codevasp.com/v1/code/transfer/" + beneficiaryVaspEntityId))
        .POST(bodyPublisher)
        .header("Content-Type", "application/json")
        .header("X-Code-Req-PubKey", publicKey)
        .header("X-Code-Req-Signature", signature)
        .header("X-Code-Req-Datetime", dateTime)
        .header("X-Code-Req-Nonce", nonce)
        .header("X-Request-Origin", "code:".concat(SampleData.OWN_ENTITY_ID)) // input your entityId
        .header("X-Code-Req-Remote-PubKey", base64RemotePublicKey);

    httpRequest = builder.build();

    try {
      httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

      int responseCode = httpResponse.statusCode();
      log.info("Response Code: {}", responseCode);
      log.info("Result: " + new JSONObject(httpResponse.body()));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
