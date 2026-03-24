# 02-Issue-Token

This API issues a token and a `walletVerificationId` for Unhosted Wallet verification.

The `token` is for **one-time use and valid for 24 hours** to execute the verification widget.

The `walletVerificationId` is a unique identifier for the verification session that is newly issued for each request and can be used to retrieve verification results.

## Endpoint

`POST` `/v1/code/unhosted-wallet-verification/widget/token`

## Request Parameters

### Body Parameters
| Name | Type | Required | Description                                                                                      |
| :--- | :--- | :------- |:-------------------------------------------------------------------------------------------------|
| blockchain | string | Required | Blockchain network (e.g., `ETH`).                                                                |
| asset | string | Required | Token ticker (e.g., `ETH`).                                                                      |
| address | string | Required | User's wallet address.                                                                           |
| customerIdentification | string | Required | User identification number.                                                                      |
| widgetRenderingOrigin | string | Required | The origin where the widget is hosted (protocol + domain + port). e.g https://your.app.domain:80 |
| callbackUrl | string | Optional | Callback URL for verification results. e.g https://your.domain/{endpoint}                       |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| token | string | Verification widget token. |
| walletVerificationId | string | Verification session ID. |

## Examples

### Request
```bash
curl --request POST \
     --url https://trapi-dev.codevasp.com/v1/code/unhosted-wallet-verification/widget/token \
     --header 'X-Code-Req-Datetime: 2026-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "blockchain": "ETH",
  "asset": "ETH",
  "address": "0x...",
  "customerIdentification": "codevasp-user-1",
  "widgetRenderingOrigin": "https://widget.codevasp.com",
  "callbackUrl": "https://widget.codevasp.com/callback"
}'
```

### Response
```json
{
  "token": "widget-token",
  "walletVerificationId": "7c7cb929-b274-4841-9476-5dd43630a08a"
}
```
