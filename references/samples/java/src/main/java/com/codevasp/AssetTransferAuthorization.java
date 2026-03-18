package com.codevasp;

import com.codevasp.data.SampleData;
import com.codevasp.utils.CryptoUtils;
import com.goterl.lazysodium.exceptions.SodiumException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class AssetTransferAuthorization {

  public static void main(String[] args) {
    try {
      log.info("Asset Transfer Authorization example.");
      // https://code-docs-en.readme.io/reference/request-asset-transfer-authorization

      String base64OwnPrivateKey = SampleData.BASE64_OWN_PRIVATE_KEY;
      String base64RemotePublicKey = SampleData.BASE64_REMOTE_PUBLIC_KEY;

      // Start construction payload part of message
      JSONObject payload = generateIvmsPayload(
              "Robert",
              "Barnes",
              "",
              "로버트 반스",
              "UID1234567890",
              SampleData.EXAMPLE_ORIGINATOR_ADDRESS,
              "code",
              "kim",
              "",
              "김코드",
              "UID0987654321",
              SampleData.EXAMPLE_BENEFICIARY_ADDRESS
      );
      // End construction payload part of message

      UUID transferId = UUID.randomUUID();
      // Generating message to send
      JSONObject codeMessage = new JSONObject();
      generateRequestBody(
              codeMessage,
              transferId,
              SampleData.EXAMPLE_CURRENCY,
              "1",
              "65000",
              "USD",
              "true"
      );

      log.info("payload: {}", payload);
      String encrypted = CryptoUtils.encrypt(payload.toString(), base64OwnPrivateKey, base64RemotePublicKey);
      codeMessage.put("payload", encrypted);

      String body = codeMessage.toString();

      CryptoUtils.SignatureContainer signature = CryptoUtils.generateSignature(body, base64OwnPrivateKey);

      log.info("pubkey: " + CryptoUtils.getPublicKey(base64OwnPrivateKey));
      log.info("signature: " + signature.getSignatureBase64());
      log.info("date time: " + signature.getStrDateTime());
      log.info("nonce: " + signature.getINonce());
      log.info("body: " + body);

      URL url = new URL("https://trapi-dev.codevasp.com/v1/code/transfer/" + SampleData.EXAMPLE_BENEFICIARY_ENTITY_ID);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);

      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("X-Code-Req-PubKey", CryptoUtils.getPublicKey(base64OwnPrivateKey));
      conn.setRequestProperty("X-Code-Req-Signature", signature.getSignatureBase64());
      conn.setRequestProperty("X-Code-Req-Datetime", signature.getStrDateTime());
      conn.setRequestProperty("X-Code-Req-Nonce", String.valueOf(signature.getINonce()));
      conn.setRequestProperty("X-Request-Origin", "code:".concat(SampleData.OWN_ENTITY_ID));
      conn.setRequestProperty("X-Code-Req-Remote-PubKey", base64RemotePublicKey);

      if (body != null) {
        conn.setRequestProperty("Content-Length", Integer.toString(body.length()));
        conn.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
      }
      conn.connect();

      // send to CODE server and get response
      int responseCode = conn.getResponseCode();
      String responseMessage = conn.getResponseMessage();
      log.info("responseCode: " + responseCode + " " + responseMessage);

      BufferedReader br;
      String strCurrentLine;
      br = new BufferedReader(
              new InputStreamReader(responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()));
      while ((strCurrentLine = br.readLine()) != null) {
        try {
          JSONObject receivedMessage = new JSONObject(strCurrentLine);
          if (receivedMessage.has("payload") &&
                  !receivedMessage.get("payload").getClass().getName().equals("org.json.JSONObject")) {
            String encryptedPayload = receivedMessage.getString("payload");
            String decryptedString = CryptoUtils.decrypt(encryptedPayload, base64OwnPrivateKey, base64RemotePublicKey);
            JSONObject objPayload = new JSONObject(decryptedString);
            receivedMessage.put("payload", objPayload);
            log.info("response payload: " + objPayload);
          }
          log.info("Decrypted Message: " + receivedMessage);
        } catch (JSONException e) {
          log.info(e.getMessage());
          log.info(strCurrentLine);
        }
      }
    } catch (JSONException | SodiumException | IOException e) {
      log.error("Error occurred during asset address search: ", e);
    }
  }

  private static void generateRequestBody(
          JSONObject codeMessage,
          UUID uuid,
          String currency,
          String amount,
          String tradePrice,
          String tradeCurrency,
          String isExceedingThreshold
  ) {
    codeMessage.put("transferId", uuid);
    codeMessage.put("currency", currency);
    codeMessage.put("amount", amount);
    codeMessage.put("historicalCost", "");
    codeMessage.put("tradePrice", tradePrice);
    codeMessage.put("tradeCurrency", tradeCurrency);
    codeMessage.put("isExceedingThreshold", isExceedingThreshold);
  }

  private static JSONObject generateIvmsPayload(
          String originFirstName,
          String originLastName,
          String originLocalFirstName,
          String originLocalLastName,
          String originId,
          String originAddress, // enter your originator's deposit address here
          String beneficiaryFirstName,
          String beneficiaryLastName,
          String beneficiaryLocalFirstName,
          String beneficiaryLocalLastName,
          String beneficiaryId,
          String beneficiaryAddress
  ) {
    // Originator
    JSONObject originatorNameIdentifier = new JSONObject();
    originatorNameIdentifier.put("primaryIdentifier", originLastName);
    originatorNameIdentifier.put("secondaryIdentifier", originFirstName);
    originatorNameIdentifier.put("nameIdentifierType", "LEGL");

    JSONArray originatorNameIdentifierArray = new JSONArray();
    originatorNameIdentifierArray.put(originatorNameIdentifier);

    JSONObject originatorLocalNameIdentifier = new JSONObject();
    originatorLocalNameIdentifier.put("primaryIdentifier", originLocalLastName);
    originatorLocalNameIdentifier.put("secondaryIdentifier", originLocalFirstName);
    originatorLocalNameIdentifier.put("nameIdentifierType", "LEGL");

    JSONArray originatorLocalNameIdentifierArray = new JSONArray();
    originatorLocalNameIdentifierArray.put(originatorLocalNameIdentifier);

    JSONObject originatorName = new JSONObject();
    originatorName.put("nameIdentifier", originatorNameIdentifierArray);
    originatorName.put("localNameIdentifier", originatorLocalNameIdentifierArray);

    JSONObject originatorNaturalPerson = new JSONObject();
    originatorNaturalPerson.put("name", originatorName);
    originatorNaturalPerson.put("customerIdentification", originId);

    JSONObject originatorPersons = new JSONObject();
    originatorPersons.put("naturalPerson", originatorNaturalPerson);

    JSONArray originatorPersonsArray = new JSONArray();
    originatorPersonsArray.put(originatorPersons);

    JSONArray originatorAccountNumber = new JSONArray();
    originatorAccountNumber.put(originAddress);

    JSONObject Originator = new JSONObject();
    Originator.put("originatorPersons", originatorPersonsArray);
    Originator.put("accountNumber", originatorAccountNumber);

    // Beneficiary
    JSONObject beneficiaryNameIdentifier = new JSONObject();
    beneficiaryNameIdentifier.put("primaryIdentifier", beneficiaryLastName);
    beneficiaryNameIdentifier.put("secondaryIdentifier", beneficiaryFirstName);
    beneficiaryNameIdentifier.put("nameIdentifierType", "LEGL");

    JSONArray beneficiaryNameIdentifierArray = new JSONArray();
    beneficiaryNameIdentifierArray.put(beneficiaryNameIdentifier);

    JSONObject beneficiaryLocalNameIdentifier = new JSONObject();
    beneficiaryLocalNameIdentifier.put("primaryIdentifier", beneficiaryLocalLastName);
    beneficiaryLocalNameIdentifier.put("secondaryIdentifier", beneficiaryLocalFirstName);
    beneficiaryLocalNameIdentifier.put("nameIdentifierType", "LEGL");

    JSONArray beneficiaryLocalNameIdentifierArray = new JSONArray();
    beneficiaryLocalNameIdentifierArray.put(beneficiaryLocalNameIdentifier);

    JSONObject beneficiaryName = new JSONObject();
    beneficiaryName.put("nameIdentifier", beneficiaryNameIdentifierArray);
    beneficiaryName.put("localNameIdentifier", beneficiaryLocalNameIdentifierArray);

    JSONObject beneficiaryNaturalPerson = new JSONObject();
    beneficiaryNaturalPerson.put("name", beneficiaryName);
    beneficiaryNaturalPerson.put("customerIdentification", beneficiaryId);

    JSONObject beneficiaryPersons = new JSONObject();
    beneficiaryPersons.put("naturalPerson", beneficiaryNaturalPerson);

    JSONArray beneficiaryPersonsArray = new JSONArray();
    beneficiaryPersonsArray.put(beneficiaryPersons);

    JSONArray beneficiaryAccountNumber = new JSONArray();
    beneficiaryAccountNumber.put(beneficiaryAddress);

    JSONObject Beneficiary = new JSONObject();
    Beneficiary.put("beneficiaryPersons", beneficiaryPersonsArray);
    Beneficiary.put("accountNumber", beneficiaryAccountNumber);

    //Originating VASP
    JSONObject originVaspNameIdentifier = new JSONObject();
    originVaspNameIdentifier.put("legalPersonName", "Test 5 entity");
    originVaspNameIdentifier.put("legalPersonNameIdentifierType", "LEGL");

    JSONArray originVaspNameIdentifierArray = new JSONArray();
    originVaspNameIdentifierArray.put(originVaspNameIdentifier);

    JSONObject originVaspName = new JSONObject();
    originVaspName.put("nameIdentifier", originVaspNameIdentifierArray);

    JSONObject originVaspLegalPerson = new JSONObject();
    originVaspLegalPerson.put("name", originVaspName);

    JSONArray originVaspAddressLine = new JSONArray();
    originVaspAddressLine.put("100 Teheran-ro 1-gil");
    originVaspAddressLine.put("Gangnam-gu");
    originVaspAddressLine.put("10th floor");

    JSONObject originVaspGeographicAddress = new JSONObject();
    originVaspGeographicAddress.put("addressType", "GEOG");
    originVaspGeographicAddress.put("addressLine", originVaspAddressLine);
    originVaspGeographicAddress.put("country", "DZ");

    JSONArray originVaspGeographicAddressArray = new JSONArray();
    originVaspGeographicAddressArray.put(originVaspGeographicAddress);

    originVaspLegalPerson.put("geographicAddress", originVaspGeographicAddressArray);
    originVaspLegalPerson.put("countryOfRegistration", "DZ");

    JSONObject innerOriginatingVASP = new JSONObject();
    innerOriginatingVASP.put("legalPerson", originVaspLegalPerson);

    JSONObject OriginatingVASP = new JSONObject();
    OriginatingVASP.put("originatingVASP", innerOriginatingVASP);

    // put all to ivms
    JSONObject ivms = new JSONObject();
    ivms.put("Originator", Originator);
    ivms.put("Beneficiary", Beneficiary);
    ivms.put("OriginatingVASP", OriginatingVASP);

    JSONObject payload = new JSONObject();
    payload.put("ivms101", ivms);
    return payload;
  }

}