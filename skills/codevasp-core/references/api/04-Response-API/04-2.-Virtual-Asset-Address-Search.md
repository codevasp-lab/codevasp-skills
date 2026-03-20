# 04-2. Virtual Asset Address Search

This is an API which shall be implemented to respond to a wallet address search request message sent by the VASP to transfer assets.
Through this API, all VASPs shall be able to respond to the address search API of the VASPs to whom they want to transfer their virtual assets.

## Endpoint

`POST` `/v1/beneficiary/VerifyAddress`

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| currency | string | Required | Symbol of the virtual asset (case insensitive). |
| payload | string | Required | Encrypted ivms101 payload. `Beneficiary.accountNumber` is required. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Verification result (`valid`, `invalid`). |
| reasonType | string | Reason code if invalid (e.g., `NOT_FOUND_ADDRESS`). |
| reasonMsg | string | Detailed message if invalid. |
| beneficiaryVaspEntityId | string | Entity ID of the corresponding VASP (if found). |

## Examples

### Request
```json
{
  "currency": "XRP",
  "payload": "encrypted ivms101 payload"
}
```

### Response
```json
{
  "result": "invalid",
  "reasonType": "NOT_FOUND_ADDRESS",
  "reasonMsg": "",
  "beneficiaryVaspEntityId": "string"
}
```
