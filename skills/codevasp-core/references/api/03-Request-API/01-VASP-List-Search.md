# 01-VASP-List-Search

Returns a list of VASPs that have completed integration with CodeVASP. This refers to a list of exchanges (VASPs) that comply with the Travel Rule and are capable of deposits and withdrawals. It may be provided to users as a 'List of Exchanges Available for Deposit and Withdrawal'. The actual possibility of deposits and withdrawals can vary according to the internal policies of the counterpart VASP. It is recommended to call this at regular intervals to update the information.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

## Endpoint

`GET` `/v1/code/vasps`

## Request Parameters

### Headers
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| X-Code-Req-Datetime | string | Required | ISO8601 UTC datetime |
| X-Code-Req-Nonce | string | Required | Random nonce |
| X-Code-Req-PubKey | string | Required | Your Public Key |
| X-Code-Req-Signature | string | Required | Signature |
| X-Request-Origin | string | Required | `code:{yourVaspEntityId}` |
| accept | string | Optional | `application/json` |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| vasps | array | List of VASPs |

**vasps object fields:**

| Name | Type | Description |
| :--- | :--- | :---------- |
| health | string | Health check result of VASP's API server. (`up`/`down`) |
| vaspEntityId | string | A unique ID assigned to a VASP within the CodeVASP system. |
| vaspName | string | VASP notation name (e.g., coinone, bithumb). |
| vaspLegalName | string | VASP's legal name on registration paper. |
| countryOfRegistration | string | Two-letter country code (ISO-3166-1 alpha-2). |
| allianceName | string | The name of the Travel Rule Solution used by the VASP. |
| pubkeys | array | Array of pubkey objects. |

**health**: Health check result of VASP's API server. If the server is 'up', it is in service; if it is 'down', it is out of service and should not send any requests.

***

**vaspEntityId**: A unique ID assigned to a VASP within the CodeVASP system, ensuring no overlap with other VASPs. This is the same as 'vaspName'.

***

**vaspName**: VASP notation name. It is used as a key to find a specific VASP from the list.\
(e.g. coinone, bithumb, korbit)

***

**vaspLegalName**: VASP's legal name on registration paper.\
(e.g. Bithumb Korea Co.,Ltd., Coinone Inc., Korbit Inc.)

***

**countryOfRegistration**: Country code where VASP is registered. This is a two-letter country code determined by ISO-3166-1 alpha-2. Ex) KR, JP, US, etc.

***

**allianceName**: The name of the Travel Rule Solution used by the VASP.

***

**pubkeys**: an array of pubkey objects registered to VASP. Each object consists of pubkey and ExpiresAt fields.

-`pubkey`: base64-encoded public key.

-`expiresAt`: ISO8601 UTC notation of the expiration time of the pubkey.


## Examples

### Request
```json
curl --request GET \
     --url https://trapi-dev.codevasp.com/v1/code/vasps \
     --header 'X-Code-Req-Datetime: 2024-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json'
```

### Response
```json
{
  "vasps": [
    {
      "vaspName": "coinone",
      "vaspEntityId": "coinone",
      "vaspLegalName": "Coinone Inc.",
      "health": "up",
      "allianceName": "code",
      "pubkeys": [
        {
          "expiresAt": "2025-03-22T15:49:32",
          "pubkey": "P2lEVJ63ESshum0JavXufBA4WUbydnsZzVGFnCVWo/Y="
        }
      ],
      "countryOfRegistration": "KR"
    }
  ]
}
```
