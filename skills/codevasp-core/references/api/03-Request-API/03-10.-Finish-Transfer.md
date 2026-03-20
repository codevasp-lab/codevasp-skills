# 03-10. Finish Transfer

If the asset transfer process is interrupted or fails from the originator VASP, the beneficiary VASP may be stuck in an infinite wait state because it is not aware of this situation. To avoid this, the originator should inform the beneficiary VASP.

This API is used in the following cases:

1. When the originator VASP received an verified response for the asset transfer authorization request, but the originator VASP need to notify the beneficiary VASP that it terminates the process without transferring the assets by its internal judgment.
2. This is used to notify the beneficiary VASP when an asset transfer transaction fails on the blockchain.

VASPs calling this API must be able to determine whether the response from the target VASP is a success or failure. If an error is received or a request timeout occurs, caller should implement periodic retry logic to resend the request. For this purpose, an attempted change to the same state is considered a success.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

## Endpoint

`PUT` `/v1/code/transfer/{BeneficiaryVaspEntityId}/status`

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

### Path Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| BeneficiaryVaspEntityId | string | Required | EntityID of the VASP who owns the address. |

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | UUID assigned to the asset transfer authorization request. |
| status | string | Required | Status of the transfer (e.g., `canceled`). |
| reasonType | string | Optional | Reason code if status is canceled. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| transferId | string | The transfer ID. |
| result | string | Result code (`normal`, `error`). |
| reasonType | string | Reason code if error. |

## Examples

### Request
```bash
curl --request PUT \
     --url https://trapi-dev.codevasp.com/v1/code/transfer/codexchange/status \
     --header 'X-Code-Req-Datetime: 2024-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
  "status": "canceled",
  "reasonType": "SANCTION_LIST"
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
