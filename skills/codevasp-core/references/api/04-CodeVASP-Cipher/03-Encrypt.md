# 03-Encrypt

This is a universal encryption feature that you can use. It encrypts with an algorithm that meets the CodeVASP travel rule requirements.

## Endpoint

`POST` `/api/v1/code/encrypt`

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
| data | string | Required | Data to encrypt |
| remotePublicKey | string | Required | Counter VASP's public key |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| signature | string | Generated Signature |
| nonce | number | Generated Nonce |
| dateTime | string | Current datetime for request |
| body | string | Encrypted string |

## Examples

### Request
```bash
curl --request POST \
     --url https://your-server-host.com/api/v1/code/encrypt \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "allianceName": "CODE",
  "data": "{\\\"hi\\\": \\\"hello\\\"}",
  "remotePublicKey": "8DJomhrvr0zD11+m3nwG3ZeO9OsSY8uLHdA7WgoaX0g="
}'
```

### Response
```json
{
  "body": "CQZl5C7Pjx4osceIbLSQ3rGeJ8XdghN99nJHPs1ob964Fq0Thw5asAIJHctE+ElR+2JirluJhA==",
  "signature": "EQH6i+vraeJApDrl0kNFT5uLg6VxDVApq5oaNkbuqL/1JzTB9IcvyLKSDBgQC7xfxwRb1Pvp7XxPsjKEfA0kDg==",
  "nonce": 1504804560,
  "dateTime": "2023-06-21T05:26:02Z"
}
```
