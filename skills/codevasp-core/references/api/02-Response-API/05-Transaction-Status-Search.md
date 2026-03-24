# 05-Transaction-Status-Search

Sometimes, a VASP who has been transferred an asset needs to know the status of the blockchain transaction of the VASP who transferred the asset.
This API is not an API to look up the asset transfer details of the counterpart VASP, but the API for the beneficiary VASP to know the blockchain transaction status of the originating VASP. This can be used in the followings.

* When the beneficiary VASP wants to check if the txid has been updated in the asset transfer details before an asset transfer transaction occurs on the blockchain and is reflected in the user's asset information, but if the status of its own asset transfer information is not confirmed, or wants to request the transaction status of an asset transfer to the originating VASP.
* When a response for an asset transfer authorization request has been sent, but the asset was not transferred, or the VASP wants to request the transaction status of the corresponding asset transfer to the originating VASP.

## Endpoint

`POST` `/v1/vasp/transfer/status`

## API action specification

1. In the DB, find asset transfer information corresponding to the transferId, which has been received as a request, and then find the `status` value, `txid`, and `vout` to respond.
2. Add the txid and vout values to the message only if they exist.

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | UUID assigned to the asset transfer authorization request. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| transferId | string | The transfer ID. |
| status | string | Status of the transfer (`pending`, `processing`, `wait-confirm`, `confirmed`, `canceled`). |
| txid | string | Transaction ID (Required when status is `confirmed`). |
| vout | string | Output index (Optional). |

**transferId**: This is an ID to distinguish asset transfer transactions in all APIs.

***

**status**: This refers to the status of the 'transferId', please refer to the <Anchor label="Asset Transfer Status Management" target="_blank" href="https://alliances.codevasp.com/board/318">Asset Transfer Status Management</Anchor> page for further explanation.

-`pending`: This is a status in which, for some reason, a blockchain transaction has not yet been sent and is waiting.

-`processing`: A transaction was sent to the blockchain, but it is waiting for mining.

-`wait-confirm`: It has been checked that the blockchain transactions had been mined, but finality has not yet been obtained.

-`confirmed`: The blockchain transactions have been mined and finality has been obtained.

-`canceled`: This is a status where a blockchain transaction has not been sent or canceled after being sent. (If canceled permanently)

***

**txid**: This is a transaction ID generated for virtual asset transfer. This is the information generated on each blockchain. This is required when status is confirmed.

***

**vout**: For the utxo type coin, multiple txids can be created so that vout is required to distinguish unique txids.

## Examples

### Request
```json
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002"
}
```

### Response
```json
{
  "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
  "status": "confirmed",
  "txid": "311BFF73D9B7969CCF1042186180159C724FAB59013A7A034A93E5FB9D6BAFE6",
  "vout": ""
}
```
