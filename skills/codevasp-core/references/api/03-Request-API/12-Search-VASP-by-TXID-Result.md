# 12-Search-VASP-by-TXID-Result

This API retrieves the result of a previous 'Search VASP by TXID' request.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

## Endpoint

`GET` `/v1/code/verification/tx/{requestId}`

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
| requestId | string | Required | `requestId` sent when requesting 'Search VASP by TXID'. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Search result (`valid`, `invalid`). |
| reasonType | string | Reason code if invalid (e.g., `NOT_FOUND_TXID`). |
| reasonMsg | string | Detailed message if invalid. |
| vaspEntityId | string | VASP's `entityId` identified in the search results. |
| requestId | string | `requestId` used during the request. |
| analysisResult | array | Array of search results from individual VASPs. |

**result**: This is the request result.

-`valid`: When the TXID search results are returned successfully.

-`invalid`: When the TXID search result is invalid. The detailed information can be distinguished by the reasonType value.

***

**reasonType**: If the response is invalid, it explains the reason.

-`NOT_FOUND_TXID`: When the TXID cannot be found.

-`LACK_OF_INFORMATION`: When the requestId cannot be found.

-`PROCESSING`: The search has not been completed. Please wait and then request again. More than 10 seconds intervals are recommended.

-`ALLIANCE_DATA_NON_UNIQUE_RESULT`: Received 'valid' responses from two or more VASPs.

***

**reasonMsg**: Defines a detailed message in case of invalidity.

***

**vaspEntityId**: VASP's 'entityId' identified in the search results.

***

**requestId**: 'requestId' used during the request.

***

**analysisResult**: The details of the search results are as follows. The response comes as an array of objects in the form below.

**analysisResult object fields:**

| Name | Type | Description |
| :--- | :--- | :---------- |
| entityId | string | Entity ID of the searched VASP. |
| result | string | Search result (`NOT_FOUND_TXID`, `FOUND`, `SYSTEM_NOT_AVAILABLE`). |

## Examples

### Request
```bash
curl --request GET \
     --url https://trapi-dev.codevasp.com/v1/code/verification/tx/fafd59e2-aff7-4cff-b7d8-9caa0cda70da \
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
  "result": "valid",
  "beneficiaryVaspEntityId": "example-vasp",
  "requestId": "fafd59e2-aff7-4cff-b7d8-9caa0cda70da",
  "analysisResult": [
    {
      "entityId": "bithumb",
      "result": "NOT_FOUND_ADDRESS"
    },
    {
      "entityId": "coinone",
      "result": "NOT_FOUND_ADDRESS"
    },
    {
      "entityId": "example-vasp",
      "result": "FOUND"
    }
  ]
}
```
