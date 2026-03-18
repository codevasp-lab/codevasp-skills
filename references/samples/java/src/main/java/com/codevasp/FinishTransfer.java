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
import java.util.ArrayList;

@Slf4j
public class FinishTransfer {

  public static void main(String[] args) {
    try {
      log.info("Finish Transfer example.");
      // https://code-docs-en.readme.io/reference/request-finish-transfer

      String base64OwnPrivateKey = SampleData.BASE64_OWN_PRIVATE_KEY;
      String base64RemotePublicKey = SampleData.BASE64_REMOTE_PUBLIC_KEY;

      // Generating message to send
      JSONObject codeMessage = new JSONObject();
      codeMessage.put("transferId", "e9f59e8a-5fe5-47e9-b13a-c020c700ab6c");
      codeMessage.put("status", "canceled");
      codeMessage.put("reasonType", "LACK_OF_INFORMATION");

      String body = codeMessage.toString();
      CryptoUtils.SignatureContainer signature = CryptoUtils.generateSignature(body, base64OwnPrivateKey);

      log.info("pubkey: " + CryptoUtils.getPublicKey(base64OwnPrivateKey));
      log.info("signature: " + signature.getSignatureBase64());
      log.info("date time: " + signature.getStrDateTime());
      log.info("nonce: " + signature.getINonce());
      log.info("body: " + body);

      URL url = new URL("https://trapi-dev.codevasp.com/v1/code/transfer/"+SampleData.EXAMPLE_BENEFICIARY_ENTITY_ID+"/status");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("PUT");
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
          log.info("Message: " + receivedMessage);
        } catch (JSONException e) {
          log.info(e.getMessage());
          log.info(strCurrentLine);
        }
      }
    } catch (JSONException | SodiumException | IOException e) {
      log.error("Error occurred during asset address search: ", e);
    }
  }

}