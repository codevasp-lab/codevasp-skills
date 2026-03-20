# 04-4. Report Transfer Result (TX Hash)

In this step, the receiving VASP should:
* detect the transaction on-chain
* receive the txid from the originating VASP and return an appropriate response
* save the txid
* map the travel rule data(transfer id) with the txid

The typical process is approval to 'Asset Transfer Authorization' -> on-chain detection -> txid receipt. However, depending on each VASP's node operation method and circumstances, the order of on-chain detection and txid receipt may vary. Therefore, be aware that **you may receive a txid before the on-chain transaction is detected**.

## Endpoint

`POST` `/v1/beneficiary/transfer/txid`

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
