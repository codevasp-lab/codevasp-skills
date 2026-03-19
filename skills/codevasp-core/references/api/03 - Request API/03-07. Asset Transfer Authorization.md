# 03-07. Asset Transfer Authorization

A VASP who wants to transfer an asset requests the VASP to whom the virtual asset is to be transferred for an authorization to transfer the asset. The VASP to transfer assets sends the sender's personal information at the request, and the VASP, to whom an asset is transferred, can check the sender's information and reject the transaction or authorize it with the beneficiary's personal information.
The VASP who sent a request updates the status of saved asset transfer list to verified or denied before sending the request or after receiving the response.

> ❗️Due to the complexity of the ivms101 standard objects, we recommend using CodeVASP-Cipher. If it is unavoidably impossible to use due to internal security reasons, please read the ivms101 standard section in the developer guide carefully.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

## Endpoint

`POST` `/v1/code/transfer/{BeneficiaryVaspEntityId}`

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

> **Info**: For the originator's personal information, as of August 7 2024, **individual's name and date of birth** are to be sent for the individual, and only information on the legal person and representative name is to be sent for the legal person.

| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | Unique ID (UUID v4) to track the transaction. |
| currency | string | Required | Symbol of the virtual asset (case insensitive). |
| amount | string | Required | Total number of virtual assets to be transferred. |
| historicalCost | string | Optional | Acquisition cost (NTS requirement, not used yet). |
| tradePrice | string | Required | Value in fiat currency (quantity * price). |
| tradeCurrency | string | Required | Fiat currency code (ISO 4217, e.g., KRW, USD). |
| isExceedingThreshold | string | Required | `true` if exceeds Travel Rule threshold. |
| originatingVasp | string | Optional | Overwrites payload's originatingVASP if included. |
| payload | string | Required | Encrypted ivms101 message. |
| address | string | Optional | Wallet address (for interoperability). |
| tag | string | Optional | Tag or Memo (for interoperability). |
| network | string | Optional | Network name (for interoperability). |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Authorization result (`verified`, `denied`). |
| reasonType | string | Reason code if denied (e.g., `NOT_FOUND_ADDRESS`). |
| reasonMsg | string | Detailed message. |
| transferId | string | The transfer ID. |
| beneficiaryVasp | string | Beneficiary VASP information. |
| payload | string | Encrypted ivms101 response message. |

## Examples

### Request
```bash
curl --request POST \
     --url https://trapi-dev.codevasp.com/v1/code/transfer/codexchange \
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
  "currency": "btc",
  "amount": "1.23",
  "historicalCost": "40000",
  "tradePrice": "125000",
  "tradeCurrency": "USD",
  "isExceedingThreshold": true,
  "payload": {
    "ivms101": {
      "OriginatingVASP": {
        "originatingVASP": {
          "legalPerson": {
            "nationalIdentification": {
              "nationalIdentifier": "Business registration number",
              "nationalIdentifierType": "RAID",
              "registrationAuthority": "RA000657"
            },
            "countryOfRegistration": "KR"
          }
        }
      },
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
  "result": "verified",
  "reasonType": "",
  "reasonMsg": "",
  "transferId": "681f27dd-43e4-4ea3-9bcc-607426d6349f",
  "beneficiaryVasp": {},
  "payload": "encrypted ivms101 payload"
}
```
