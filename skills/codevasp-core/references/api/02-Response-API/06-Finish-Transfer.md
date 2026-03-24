# 06-Finish-Transfer

This API is used in the following cases:

* Originator VASP has to notify the status of an on-chain transaction to the beneficiary VASP when the on-chain transaction has failed.
* When the originator VASP received 'verified' in 'Asset Transfer Authorization' stage from the beneficiary, VASP but it determined not to send the on-chain transaction.

A calling side, which call this API, is not compulsory, but the implementation on VASP side is required to process a request.

## Endpoint

`PUT` `/v1/vasp/transfer/status`

## API action specification

1. Finds the asset transfer information corresponding to the request's `transferId` and updates the `status`.
2. `canceled` `status` can be changed when the previous status of `denied` or not fully terminated and change it to terminated.
3. When the originating VASP requests to finish(cancel) a `transferId` that has become immutable after block creation and confirmation, return an `ILLEGAL_STATEFLOW` error.
4. Allows changing the status of an asset transfer to `canceled` if it is already `canceled`.

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | UUID assigned to the asset transfer authorization request. |
| status | string | Required | Status of the transfer (`canceled`). |
| reasonType | string | Optional | Reason code if status is canceled. |

**transferId**: This is UUID assigned to the an asset transfer authorization request. It notifies the result of asset reflection for the corresponding transaction.

***

**status**: It indicates the status of whether the virtual asset has been reflected in the user account or not.

-`canceled`: This is a status where a blockchain transaction has not been sent or canceled after being sent. (If canceled permanently)
***

**reasonType**: This code indicates the reason if status is beneficiary-denied or canceled. (reasonType is the same as address search and asset transfer request API. This may contain unnecessary items. Please do not use this.)

-`NOT_FOUND_ADDRESS`: This is a case where a virtual asset address cannot be found.

-`NOT_SUPPORTED_SYMBOL`: This is a currency symbol which cannot be traded.

-`NOT_KYC_USER`: This is a case where the owner did not process KYC verification.

-`SANCTION_LIST`: Virtual asset addresses or owners are subject to the sanction of the beneficiary VASP.

-`LACK_OF_INFORMATION`: This is a case where there is no the information necessary to make an asset transfer decision.

-`UNKNOWN`: This refers to other reasons.

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| transferId | string | The transfer ID. |
| result | string | Result code (`normal`, `error`). |
| reasonType | string | Reason code if error (e.g., `ILLEGAL_STATEFLOW`). |

**transferId**: This is an ID to distinguish asset transfer transactions in all APIs (Created by CodeVASP)

***

**result**: This is the result of receiving originating information.

-`normal`: The request was processed correctly, the transaction status is marked as closed.

-`error`: If status change is not possible

***

**reasonType**: If the result value is error, a value which identifies the detailed reason.

-`UNKNOWN_TRANSFER_ID`: If the Transfer ID cannot be found

-`ILLEGAL_STATEFLOW`: This is a state flow which is not allowed. This is a case where change is not allowed from the current state to the update state.

-`UNKNOWN`: This is another error. Other solutions sometimes do not return errors, and in this case, this error is returned.

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
