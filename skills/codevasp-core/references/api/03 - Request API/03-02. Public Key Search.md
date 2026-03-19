# 03-02. Public Key Search

You can use this API to query the Public Key of a specific VASP. While the results from 'VASP List Search' may include some cached data, this API retrieves current data.

For example, when you receive `422 INVALID_RECEIVER_PUBLIC_KEY` error, you can call this API to check if the Public Key has been changed.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

## Endpoint

`GET` `/v1/code/Vasp/{VaspEntityId}/pubkey`

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

### Path Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| VaspEntityId | string | Required | The VaspEntityId of the VASP to query. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| pubkeys | array | Array of pubkey objects. |

**pubkey object fields:**

| Name | Type | Description |
| :--- | :--- | :---------- |
| pubkey | string | base64-encoded pubkey |
| expiresAt | string | ISO 8601 UTC expiration time |

## Examples

### Request
```bash
curl --request GET \
     --url https://trapi-dev.codevasp.com/v1/code/Vasp/codexchange/pubkey \
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
  "pubkeys": [
    {
      "pubkey": "base64 encoded ascii",
      "expiresAt": "2022-06-31T23:59:59Z"
    }
  ]
}
```
