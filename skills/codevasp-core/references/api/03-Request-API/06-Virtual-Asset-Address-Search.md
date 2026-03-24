# 06-Virtual-Asset-Address-Search

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

**currency**: This is a symbol of the virtual asset you want to transfer. (This is case insensitive.)

***

**payload**: Since only the wallet address is verified at this point as an encrypted IVMS101 object, it is not necessary to input the entire IVMS101 object. **The`array<accountNumber>` within the 'Beneficiary' object is required, while beneficiaryPersons should be empty array**.

***

**address**: Wallet address of the beneficiary. Since some VASPs integrated with other solutions may require this field, please refer to [12-Interoperability with Other Protocols] page.

***

**tag**: Include this if a Tag or Memo exists (e.g., XRP). Since some VASPs integrated with other solutions may require this field, please refer to [12-Interoperability with Other Protocols] page.

***

**network**: This is included to distinguish when a single coin exists on multiple networks. Since some VASPs integrated with other solutions may require this field, please refer to [12-Interoperability with Other Protocols] page.


## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Verification result (`valid` or `invalid`). |
| reasonType | string | Reason code if invalid (e.g., `NOT_FOUND_ADDRESS`). |
| reasonMsg | string | Detailed message if invalid. |
| beneficiaryVaspEntityId | string | Entity ID of the corresponding VASP. |

**result**: This is a virtual asset address verification result.

-`valid`: This is a normal address

-`invalid`: This is the result of virtual asset address search. You can classify the details by the reasonType value.

***

**reasonType**: If the result field value is invalid, you need to add this field to send error details.

-`NOT_FOUND_ADDRESS`: This is a case where a virtual asset address cannot be found.

-`NOT_SUPPORTED_SYMBOL`: This is a currency symbol which cannot be traded.

-`NOT_KYC_USER`: This is a case where the owner did not process KYC verification.

-`SANCTION_LIST`: Virtual asset addresses or owners are subject to the sanction of the beneficiary VASP.

-`LACK_OF_INFORMATION`: This is a case where there is no the information necessary to make an asset transfer decision.

-`UNKNOWN`: This refers to other reasons.

***

**reasonMsg**: It defines a detailed message if invalid.

***

**beneficiaryVaspEntityId**: If there is a VASP that owns a virtual asset address to be looked up, this is the Entity ID of the corresponding VASP.

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
}'
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
