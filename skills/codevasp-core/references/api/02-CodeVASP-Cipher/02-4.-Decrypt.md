# 02-4. Decrypt

This decryption function can be used universally. It decrypts with an algorithm that meets the requirements of CodeVASP travel rule.

`remotePublicKey` is the value of the header's `X-Code-Req-PubKey`.

## Endpoint

`POST` `/api/v1/code/decrypt`

## Request Parameters

### Headers
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| accept | string | Optional | `application/json` |
| content-type | string | Required | `application/json` |

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| allianceName | string | Required | Travel Rule protocol code (default: `CODE`) |
| data | string | Required | Any data you want to decrypt |
| remotePublicKey | string | Required | Counter VASP's public key |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| bodyString | string | Decrypted string |
| body | object | Decrypted ivms101 object |

## Examples

### Request
```bash
curl --request POST \
     --url https://your-server-host.com/api/v1/code/decrypt \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "allianceName": "CODE",
  "remotePublicKey": "8DJomhrvr0zD11+m3nwG3ZeO9OsSY8uLHdA7WgoaX0g=",
  "data": "jf6V5hL6VxjYD6+RdMm7NIRKCrFAn4HQ5ZumHgBS7ywcczAB0jK7/hMKHQBFm1RECbKOYGXroA8h+cSvMUABof+dIzqt3intiS9qfXe2bYfq9fdpicrcyhZOHnJT3tOz3HLM1SuxXSFNHrcmrIRE4KL80uV3gRcH7Z6gtbgFhlNDzzL/"
}
'
```

### Response
```json
{
  "body": {
    "ivms101": {
      "Beneficiary": {
        "beneficiaryPersons": [],
        "accountNumber": [
          "3432fwsf3q4qweqwef"
        ]
      }
    }
  },
  "bodyString": "{\"ivms101\":{\"Beneficiary\":{\"beneficiaryPersons\":[],\"accountNumber\":[\"3432fwsf3q4qweqwef\"]}}}"
}
```
