# 04-6. Finish Transfer

This API is used in the following cases:

* Originator VASP has to notify the status of on-chain transaction to the beneficiary VASP when the on-chain transaction has failed.
* When the originator VASP received 'verified' in 'Asset Transfer Authorization' stage from the beneficiary, VASP but it determined not to send the on-chain transaction.

A calling side, which call this API, is not compulsory but the implementation on VASP side is required to process a request.

## Endpoint

`PUT` `/v1/vasp/transfer/status`

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | UUID assigned to the asset transfer authorization request. |
| status | string | Required | Status of the transfer (`canceled`). |
| reasonType | string | Optional | Reason code if status is canceled. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| transferId | string | The transfer ID. |
| result | string | Result code (`normal`, `error`). |
| reasonType | string | Reason code if error (e.g., `ILLEGAL_STATEFLOW`). |

## Examples

### Request
```json
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
  "status": "canceled",
  "reasonType": "LACK_OF_INFORMATION"
}
```

### Response
```json
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
  "result": "error",
  "reasonType": "ILLEGAL_STATEFLOW"
}
```
