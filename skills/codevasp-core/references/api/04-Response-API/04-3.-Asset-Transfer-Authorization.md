# 04-3. Asset Transfer Authorization

A VASP who wants to transfer an asset requests the beneficiary VASP for asset transfer authorization prior to transferring an asset. You shall implement an API to process a request for authorization to transfer this asset.

> **Name Validation Logic**
>
> We recommend designing the **name validation logic to default to False, and return True only when all valid conditions are explicitly met**. Setting the default to True and rejecting only under specific conditions can make it difficult to build a defensive design, as it may unintentionally allow all inputs when exceptions occur.

## Endpoint

`POST` `/v1/beneficiary/transfer`

## Request Parameters

### Body Parameters

> **Info**: For the originator's personal information, as of March 25, 2022, only an individual's name is to be sent for the individual, and only information on the legal person and representative name is to be sent for the legal person.

| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| transferId | string | Required | Unique ID (UUID v4) to track the transaction. |
| currency | string | Required | Symbol of the virtual asset (case insensitive). |
| amount | string | Required | Total number of virtual assets to be transferred. |
| historicalCost | string | Optional | Acquisition cost (NTS requirement, not used yet). |
| tradePrice | string | Required | Value in fiat currency (quantity * price). |
| tradeCurrency | string | Required | Fiat currency code (ISO 4217, e.g., KRW, USD). |
| isExceedingThreshold | string | Required | `true` if exceeds Travel Rule threshold. |
| originatingVasp | string | Optional | Overwrites payload's originatingVASP if included. |
| payload | string | Required | Encrypted ivms101 message. |
| address | string | Optional | Wallet address (for interoperability). |
| tag | string | Optional | Tag or Memo (for interoperability). |
| network | string | Optional | Network name (for interoperability). |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Authorization result (`verified`, `denied`). |
| reasonType | string | Reason code if denied (e.g., `NOT_FOUND_ADDRESS`). |
| reasonMsg | string | Detailed message. |
| transferId | string | The transfer ID. |
| beneficiaryVasp | string | Beneficiary VASP information. |
| payload | string | Encrypted ivms101 response message. |

## Examples

### Request
```json
{
    "transferId": "string",
    "currency": "string",
    "amount": "string",
    "historicalCost": "",
    "tradePrice": "1000001",
    "tradeCurrency": "KRW",
    "isExceedingThreshold": "true",
    "originatingVasp": {},
    "payload": "encrypted ivms101 payload"
}
```

### Response
```json
{
    "result": "string",
    "reasonType": "",
    "reasonMsg": "",
    "transferId": "string",
    "beneficiaryVasp": {},
    "payload": "encrypted ivms101 payload"
}
```
