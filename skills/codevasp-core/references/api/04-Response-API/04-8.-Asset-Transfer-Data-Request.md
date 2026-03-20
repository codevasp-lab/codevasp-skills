# 04-8. Asset Transfer Data Request

In cases where a transfer with TXID lacks Travel Rule data, preventing the beneficiary VASP from processing the deposit, the beneficiary VASP will call 'Asset Transfer Data Request' API to get the Travel Rule data. Implement the API to handle this request.

> **When the originator's KYC information is not available**
>
> If the originator has not completed KYC and identity information is not available, set the 'reasonType' to 'LACK_OF_INFORMATION' and provide a detailed reason in 'reasonMsg' in the response to the 'asset transfer data request'. (e.g., 'The user did not finish KYC.')

## Endpoint

`PUT` `/v1/verification/tx`

## Request Parameters

### Body Parameters

> **Info**: For the originator's personal information, as of March 25, 2022, only an individual's name is to be sent for the individual, and only information on the legal person and representative name is to be sent for the legal person.

| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | UUID sent by the Beneficiary VASP requesting the data. |
| txid | string | Required | Transaction ID (TXID). |
| beneficiaryAddress | string | Required | Beneficiary's wallet address. |
| payload | string | Required | Encrypted ivms101 payload. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Request result (`normal`, `error`). |
| reasonType | string | Reason code if error (e.g., `NOT_FOUND_TXID`). |
| reasonMsg | string | Detailed message. |
| transferId | string | The `transferId`. |
| currency | string | Currency symbol. |
| amount | string | Transferred amount. |
| historicalCost | string | Acquisition cost. |
| tradePrice | string | Value in fiat currency. |
| tradeCurrency | string | Fiat currency code. |
| isExceedingThreshold | string | `true` if exceeds Travel Rule threshold. |
| payload | string | Encrypted ivms101 response message. |

## Examples

### Request
```json
{
    "txid" : "311BFF73D9B7969CCF1042186180159C724FAB59013A7A034A93E5FB9D6BAFE6",
    "beneficiaryAddress" : "address:tag",
    "transferId" : "b09c8d00-8da9-11ec-b909-0242ac120002",
    "payload": "encrypted ivms101 payload(Beneficiary.Object,BeneficiaryVASP.Object)"
}
```

### Response
```json
{
    "result" : "normal",
    "transferId" : "b09c8d00-8da9-11ec-b909-0242ac120002",
    "currency": "BTC",
    "amount": "0.1",
    "historicalCost": "",
    "tradePrice": "10000",
    "tradeCurrency": "KRW",
    "isExceedingThreshold": "true",
    "payload": "encrypted ivms101 payload"
}
```
