# 04-Report-Transfer-Result

* This page explains how to respond to 'Report transfer Result(TX Hash)' API.
* In this step, the receiving VASP should:
    * detect the transaction on-chain
    * receive the txid from the originating VASP and return an appropriate response
    * save the txid
    * map the travel rule data(trasnfer id) with the txid
* The typical process is approval to 'Asset Transfer Authorization' -> on-chain detection -> txid receipt. However, depending on each VASP’s node operation method and circumstances, the order of on-chain detection and txid receipt may vary.
* Therefore, be aware that **you may receive a txid before the on-chain transaction is detected**.
* In such cases, it is recommended to first return a normal response to the 'Report Transfer Result (TX Hash)' request, then confirm the on-chain data and update the customer’s asset accordingly.
* This entire process assumes the integrity of the protocol and trusts that the counterparty VASP will not transmit an invalid txid.

## Endpoint

`POST` `/v1/beneficiary/transfer/txid`

## API action specification

1. Find asset transfer information with 'transferId'.
2. Update the 'txid' value of the asset transfer information found, and change the asset transfer status to `confirmed`.
3. If there already exists the 'txid' related to the 'transferId' provided, then beneficiary VASP should return normal when the both 'txid's are identical, otherwise returns an error.


## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | UUID assigned to the asset transfer authorization request. |
| txid | string | Required | Transaction ID generated on the blockchain. |
| vout | string | Optional | Output index (for UTXO coins). |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| transferId | string | The transfer ID. |
| result | string | Result code (`normal`, `error`). |
| reasonType | string | Reason code if error (e.g., `TXID_ALREADY_EXISTS`). |

**transferId**: This is an ID to distinguish asset transfer transactions in all APIs

***

**result**: This is the result of receiving originating information.

-`normal`: normal processing

-`error`: If status change is not possible

***

**reasonType**: If the result value is error, a value which identifies the detailed reason.

-`TXID_ALREADY_EXISTS`: You are trying to store a different TXID for an asset transfer that already has a TXID stored. Once a TXID is created, it may fail, but it cannot be changed.

-`TRANSFER_ALREADY_FAILED`: You cannot send a TXID if the blockchain transaction status of corresponding to the TransferId, already failed.

-`UNKNOWN_TRANSFER_ID`: TransferID cannot be found.

-`UNKNOWN`: Unkown error.

## Examples

### Request
```json
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
  "txid": "311BFF73D9B7969CCF1042186180159C724FAB59013A7A034A93E5FB9D6BAFE6",
  "vout": ""
}
```

### Response
```json
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
  "result": "normal",
  "reasonType": "TXID_ALREADY_EXISTS"
}
```
