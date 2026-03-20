# 03-13. Asset Transfer Data Request

This API works in the reverse way of 'Asset Transfer Authorization'. So when an anonymous transaction is transferred, it will request for the Travel Rule data to make it identified.

> ❗️Due to the complexity of the ivms101 standard, we recommend using CodeVASP-Cipher. If, for internal security reasons, you are unable to use it, please carefully read the ivms101 standard section in the developer guide.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

> ❗️For GTR-affiliated member companies, the `currency` and `network` parameters are required. For coin-specific network details, please refer to [interoperability page with other protocols].

## Endpoint

`PUT` `/v1/code/verification/tx/{vaspEntityId}`

## Request Parameters

### Headers
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| X-Code-Req-Datetime | string | Required | ISO8601 UTC datetime |
| X-Code-Req-Nonce | string | Required | Random nonce |
| X-Code-Req-PubKey | string | Required | Your Public Key |
| X-Code-Req-Remote-PubKey | string | Required | Beneficiary Public Key |
| X-Code-Req-Signature | string | Required | Signature |
| X-Request-Origin | string | Required | `code:{yourVaspEntityId}` |
| accept | string | Optional | `application/json` |
| content-type | string | Required | `application/json` |

### Path Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| vaspEntityId | string | Required | Entity ID of originating VASP returned from 'Search VASP by TXID Result'. |

### Body Parameters

> **Info**: For the originator's personal information, as of August 7 2024, **individual's name and date of birth** are to be sent for the individual, and only information on the legal person and representative name is to be sent for the legal person.

| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | Use the 'requestId' from 'Search VASP by TXID'. |
| txid | string | Required | Transaction ID (TXID). |
| beneficiaryAddress | string | Required | Beneficiary's wallet address. |
| currency | string | Optional | Currency symbol (mandatory for GTR). |
| network | string | Optional | Network name (mandatory for GTR). |
| payload | string | Required | Encrypted ivms101 payload. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Request result (`normal`, `error`). |
| reasonType | string | Reason code if error (e.g., `NOT_FOUND_TXID`). |
| reasonMsg | string | Detailed message. |
| transferId | string | The `transferId`. |
| currency | string | Currency symbol. |
| amount | string | Transferred amount. |
| historicalCost | string | Acquisition cost. |
| tradePrice | string | Value in fiat currency. |
| tradeCurrency | string | Fiat currency code. |
| isExceedingThreshold | string | `true` if exceeds Travel Rule threshold. |
| payload | string | Encrypted ivms101 response message. |

## Examples

### Request
```bash
curl --request PUT \
     --url https://trapi-dev.codevasp.com/v1/code/verification/tx/vaspEntityId \
     --header 'X-Code-Req-Datetime: 2024-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Remote-PubKey: beneficiaryPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "transferId": "681f27dd-43e4-4ea3-9bcc-607426d6349f",
  "txid": "311BFF73D9B7969CCF1042186180159C724FAB59013A7A034A93E5FB9D6BAFE6",
  "beneficiaryAddress": "address:tag",
  "payload": "encrypted ivms101 payload"
}
'
```

### Response
```json
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
  "result": "error",
  "reasonType": "UNKNOWN_TRANSFER_ID"
}
```
