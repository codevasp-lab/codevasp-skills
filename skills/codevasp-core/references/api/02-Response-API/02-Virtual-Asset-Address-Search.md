# 02-Virtual-Asset-Address-Search

This is an API which shall be implemented to respond to a wallet address search request message sent by the VASP to transfer assets.
Through this API, all VASPs shall be able to respond to the address search API of the VASPs to whom they want to transfer their virtual assets.

## Endpoint

`POST` `/v1/beneficiary/VerifyAddress`

## API action specification

1. Search if the address within VASP is correct bases on `currency` and `accountNumber`.
    1. **\[Important!] Refer<Anchor label="Verifying Wallet Address" target="_blank" href="https://alliances.codevasp.com/board/316">Verifying Wallet Address</Anchor> for how to verify address.**
2. If the addressee’s name was transmitted, it is compared with the name on the asset transfer permission request. (This is for designated exchange inquiry only.)
3. Perform sanction screening of the `accountNumber` and the person of the beneficiary VASP and reflect the result in the response to reply.
4. All of these processes shall be implemented not to exceed 1 second.

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| currency | string | Required | Symbol of the virtual asset (case insensitive). |
| payload | string | Required | Encrypted ivms101 payload. `Beneficiary.accountNumber` is required. |

**currency**: This is a symbol of the virtual asset you want to transfer. (This is case insensitive.)

***

**payload**: It contains the IVMS101 message with personal information. payload encrypts and sends JSON String. Because the wallet address is verified within the IVMS101 object encrypted into a string, the entire IVMS101 object may not have been fully structured. As in the sample payload below, within the `Beneficiary` object, the `array<accountNumber>` is mandatory, while `beneficiaryPersons` is an empty array.

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Verification result (`valid`, `invalid`). |
| reasonType | string | Reason code if invalid (e.g., `NOT_FOUND_ADDRESS`). |
| reasonMsg | string | Detailed message if invalid. |
| beneficiaryVaspEntityId | string | Entity ID of the corresponding VASP (if found). |

**result**: This is a virtual asset address verification result.

-`valid`: This is a normal address

-`invalid`: This is the result of virtual asset address search. You can classify the details by the reasonType value.

***

**reasonType**: In cases where the result field value is 'invalid', this field should be added.

-`NOT_FOUND_ADDRESS`: This is a case where a virtual asset address cannot be found.

-`NOT_SUPPORTED_SYMBOL`: This is a currency symbol which cannot be traded.

-`NOT_KYC_USER`: This is a case where the owner did not process KYC verification.

-`SANCTION_LIST`: Virtual asset addresses or owners are subject to the sanction of the beneficiary VASP.

-`LACK_OF_INFORMATION`: This is a case where there is no the information necessary to make an asset transfer decision.

-`UNKNOWN`: This refers to other reasons.

***

**reasonMsg**: It defines a detailed message if invalid.

***

**beneficiaryVaspEntityId**: If there is a VASP that owns a virtual asset address to be looked up, this is the Entity ID of the corresponding VASP.


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
