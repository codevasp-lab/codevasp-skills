package com.codevasp;

import com.codevasp.utils.CryptoUtils;
import com.goterl.lazysodium.exceptions.SodiumException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;



@Slf4j
public class DecryptPayload {
  public static void main(String[] args) throws SodiumException {

    // test key
    //    own-private-key: HaFGZdcmhEWrbDO+jq1lrWvMwBLoiJf99Bm7FfMkad0=
    //    own-public-key: gGvXiR/ne8pRr89DLqifw/cGtj1sBNC8yhQFhKZvD6Q=

    // Verify Wallet Address Example
    // https://code-docs-en.readme.io/reference/response-virtual-asset-address-search
    // assume that you received HTTP request from originator
    // you retrieve headers to get the key
    String ownPrivateKey = "HaFGZdcmhEWrbDO+jq1lrWvMwBLoiJf99Bm7FfMkad0=";
    String reqPubkey = "3sR8zw8hPsedB1u4lj0qStyvboFAltf9DakLL68CTIw=";
    String remotePubkey = "gGvXiR/ne8pRr89DLqifw/cGtj1sBNC8yhQFhKZvD6Q=";
    String origin = "test5";
    String payload = "XUpdV8WnvjqoHEFe3COtIM+muV19aGaOetaM5BEFrTFHkEgO5ZKLZDno+RC8XVfvfhabS92RzQIMMDblI2f9Kk0Ktq5qXGnS/coHghx8JZi+lI211sAdiIZGJZgHHJZdXh6vYIQW2tg7s/WA9qDZsfYDCQ8/k9voMvWBfUI7EPRTXE3JPJMlsroEEj5Yw1y4wcg1cQ==";
    String currency = "BTC";

    String decrypted = CryptoUtils.decrypt(payload, ownPrivateKey, reqPubkey);
    log.info("decrypted {}", decrypted);

    // get AccountNumber
    JSONObject jObject = new JSONObject(decrypted);
    JSONObject ivms101 = jObject.getJSONObject("ivms101");
    JSONObject Beneficiary = ivms101.getJSONObject("Beneficiary");
    JSONArray accountNumber = Beneficiary.getJSONArray("accountNumber");
    String accountNumberStr = accountNumber.getString(0);
    log.info("accountNumberStr {}", accountNumberStr);
    // Follow this rule and check the address in your database
    // https://code-docs-en.readme.io/reference/verify-wallet-address

    // Asset Transfer Authorization Example
    // https://code-docs-en.readme.io/reference/response-asset-transfer-authorization
    // assume that you received HTTP request from originator
    // you retrieve headers to get the key
    String payload2 = "dlVPnuyub17mXlQyAJVh7CrgVQGNoRvrvpVobLowmcqRSfjN02cJnkQ+6jAHuW+sCji7CRw4ts+oQXlDVsPqNDI4rA57jwhd+5HXe+At4dODF2G3KR5sVSOtRIGBdcxTLlbOtrCUpYuAZ+ezmYkMQ9SiqZsvnR+FoJPJ1TbyY7RRoVXg3P6SqPwS+388NludDCe4jAo/+WoRQ/Z8ogSi07cgtxn6bzCZd6X6zU89n8l0eba9uYpIzGho3/ph8tdk6T4QQKPwrP1kdYn9STWzY3xAM1NsADFwyz5rad+iltHEriGstd55iabKBoj2OfbcPJEM52tLkh+jDhKVtsU20PjU3GFekFUN2k3yE+kCrhvuo7/wRbPam1yakZ7XjjDchYyR5UXmr03HGdLGCtAlCUiSi+i5CCxWC4HrjI87bcdq1x0eH9k7HtF3DBjqAbGAWUawJNfV2wsrCvaef+t7y++P00u8Mhxs1+UzHn7x3azIwyXDEPTGf0oSv58Mgen5u5QyOyTyGV1ooHYH7k0SxmmyQRcFfpbhgEz2ZJTGSZbJ/wxPLbELllO2sWItCt9WYXq+gfgKugFYvivkYreJTkvYUzz+b4yChhwJbVzkD7QHdkN4Wn07h/i8sBpcm2ueMnDeTkgjyTQTCTrdkFRFrX/dSHy6PAyNo6CBwMEIwMHdY0XDGeYhK4C1k77AuBGdtTZSbD7BE6AaOLcMqpLIUkzZ8ggh8K1o41rN+G5IHYfqNQWnN+VV4sL1399hBijv6dPt4fuIqYGAHFL5WH/pkBpCCILEtrP3te+Wf2yqtVvy959iYbscZeLWMXvxRat1NCPL0Jh2RZMUrBIPivq4rL18KjEcEQPWHmKPYn00a/OApFARi64+2z9Tj7m55pgnsPYO3sAhigkvcd+dijmFr6OzZXW6zSmlmmStDuAjTp56JM6+gnjWE9EgDIlJw647YKvE/Eaw36cBp1MGgPlaYsorOeX/KqP9WTplKYlYw8Kt2T7iWbNszXqOlv2R3TdkPSHMwJC7ul7xVLw8ALorwZ0OZSWF5A0mgKwWhJfOIzJFAxb35DXiROgOtzsTPZAj84EFL0CCqHwOZ8SZUnFQuNkiTwhh3NpnZ7fEbVBPVSUyXq2V9oW3bOxU8z9wUO7x3PYdVbgDwUphNunVn7f9mv0QG8ksMPs27Xh39/kvzWwlw4wT6dHSv14h4e/0qu+59znpHNbj5UUafodaEnrsnvrVZq+IQu/DXpgk6UM7BExuNdTGijEHr79ixYAax0ytgJ8zHsgsL/kDHoQA9XV+dDTPl4armvD36UmCKK/8yR/ue7hNgZWaNGiLzdkyDoThNku+xk0xsRZES838kt+XefyURr/I+v90azrQTHTLiUWI36v6Nv6u+xZb68fdcCPrCDw6E/6ivjM7O4sSd85CxbZGkYqqH21f2+RVs3uGVudJ359r+U6+5ZpW3PQ5kl8YJayd299l3P8zsU4TXZerqCB2+4PMKkkIoK2xUz91";

    String decrypted2 = CryptoUtils.decrypt(payload2, ownPrivateKey, reqPubkey);
    log.info("decrypted2 {}", decrypted2);

    // get AccountNumber
    JSONObject jObject2 = new JSONObject(decrypted2);
    JSONObject ivms1012 = jObject2.getJSONObject("ivms101");
    JSONObject Beneficiary2 = ivms1012.getJSONObject("Beneficiary");
    JSONArray accountNumber2 = Beneficiary2.getJSONArray("accountNumber");
    String accountNumberStr2 = accountNumber2.getString(0);
    log.info("accountNumberStr {}", accountNumberStr2);
    // Follow this rule and check the address in your database
    // https://code-docs-en.readme.io/reference/verify-wallet-address

    JSONArray beneficiaryPersons = Beneficiary2.getJSONArray("beneficiaryPersons");
    JSONObject naturalPerson = beneficiaryPersons.getJSONObject(0).getJSONObject("naturalPerson");
    JSONObject name = naturalPerson.getJSONObject("name");
    log.info("name {}", name);
    JSONArray nameIdentifier = name.getJSONArray("nameIdentifier");
    log.info("nameIdentifier {}", nameIdentifier);
    String primaryIdentifier = nameIdentifier.getJSONObject(0).getString("primaryIdentifier");
    String secondaryIdentifier = nameIdentifier.getJSONObject(0).getString("secondaryIdentifier");
    log.info("primaryIdentifier {}", primaryIdentifier);
    log.info("secondaryIdentifier {}", secondaryIdentifier);
    // Now, you have basic information to verify the beneficiary

  }
}