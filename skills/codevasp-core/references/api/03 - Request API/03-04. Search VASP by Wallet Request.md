# 03-04. Search VASP by Wallet Request

A VASP intending to transfer virtual assets might integrate this API at the initial step of the process. It will help identify the VASP to which the beneficiary's wallet address (address + tag(optional)) belongs.

This API allows the elimination of a process where users select an exchange on the UI, enabling more streamlined flow. Even more, it proves highly beneficial when the user is unaware of the counterpart VASP.

Only data from VASPs affiliated with CodeVASP and certain other supported protocols can be queried. A '`NOT_FOUND_ADDRESS`' response indicates that the queried address either does not belong to a currently searchable VASP or could not be retrieved due to a temporary server issue on the member VASP's side.

> ❗️This search targets only the **user wallets(capable of receiving deposits)** associated with VASPs. It does not include the cold wallets of VASPs.

> ❗️After making a request to an API that operates **asynchronously**, you can either **receive the result using a Callback URL or check the result via the 'Search VASP by Wallet Result' API**. The result data is the same for both methods.

## Endpoint

`POST` `/v2/code/VerifyAddress`

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
| content-type | string | Required | `application/json` |

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| currency | string | Required | The symbol of the virtual asset you wish to transfer. (Case insensitive) |
| addressNumber | string | Required | Beneficiary's wallet address. Attach secondary addresses like tag or memo after a ':' delimiter. |
| network | string | Optional | Distinguishes coins on multiple networks. Use `all` to search all available networks. |
| requestId | string | Required | Unique request ID for querying the result. |
| callbackUrl | string | Optional | Callback URL in `https://` format to receive the result. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Result for a request (`SUCCESS` if valid). |
| requestId | string | The request ID provided. |

## Examples

### Request
```bash
curl --request POST \
     --url https://trapi-dev.codevasp.com/v2/code/VerifyAddress \
     --header 'X-Code-Req-Datetime: 2024-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "currency": "XRP",
  "addressNumber": "rHcFoo6a9qT5NHiVn1THQRhsEGcxtYCV4d:memo or tag",
  "network": "XRP",
  "requestId": "fafd59e2-aff7-4cff-b7d8-9caa0cda70da",
  "callbackUrl": "https://endpoint.com"
}
'
```

### Response
```json
{
  "result": "SUCCESS",
  "requestId": "fafd59e2-aff7-4cff-b7d8-9caa0cda70da"
}
```
