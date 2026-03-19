# 03-06. Virtual Asset Address Search

A VASP who wants to transfer assets need to know which VASP owns the address (address + tag(optional)) of a virtual asset to which a user wants to transfer his or her asset by using this API in the first step of the entire process.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

## Endpoint

`POST` `/v1/code/VerifyAddress/{BeneficiaryVaspEntityId}`

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
| BeneficiaryVaspEntityId | string | Required | EntityID of the VASP which owns the address. |

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| currency | string | Required | Symbol of the virtual asset (case insensitive). |
| payload | string | Required | Encrypted ivms101 payload. `Beneficiary.accountNumber` is required. |
| address | string | Optional | Wallet address (for interoperability). |
| tag | string | Optional | Tag or Memo (for interoperability). |
| network | string | Optional | Network name (for interoperability). |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Verification result (`valid` or `invalid`). |
| reasonType | string | Reason code if invalid (e.g., `NOT_FOUND_ADDRESS`). |
| reasonMsg | string | Detailed message if invalid. |
| beneficiaryVaspEntityId | string | Entity ID of the corresponding VASP. |

## Examples

### Request
```bash
curl --request POST \
     --url https://trapi-dev.codevasp.com/v1/code/VerifyAddress/codexchange \
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
  "currency": "BTC",
  "payload": {
    "ivms101": {
      "Beneficiary": {
        "accountNumber": [
          "1KzHK8WMRHRCvRjUV5PFny3v6fqT3UAY5K"
        ]
      }
    }
  }
}
'
```

### Response
```json
{
  "result": "invalid",
  "reasonType": "NOT_FOUND_ADDRESS",
  "reasonMsg": "",
  "beneficiaryVaspEntityId": "string"
}
```
