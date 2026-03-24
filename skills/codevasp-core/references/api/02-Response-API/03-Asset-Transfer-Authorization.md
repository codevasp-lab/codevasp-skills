# 03-Asset-Transfer-Authorization

A VASP who wants to transfer an asset requests the beneficiary VASP for asset transfer authorization prior to transferring an asset. You shall implement an API to process a request for authorization to transfer this asset.

> **Name Validation Logic**
>
> We recommend designing the **name validation logic to default to False, and return True only when all valid conditions are explicitly met**. Setting the default to True and rejecting only under specific conditions can make it difficult to build a defensive design, as it may unintentionally allow all inputs when exceptions occur.

## Endpoint

`POST` `/v1/beneficiary/transfer`

## API Specification

1. Decrypt the encrypted payload with its own private key.
2. You can check the information of an asset to be transferred, the `Originator` information, and the `OriginatingVASP` information, and decide whether to allow the transfer of this asset. Requests from VerifyVASP, the `originatingVASP` object is outside payload. Please overwrite inside payload -> ivms101.
3. Check that the beneficiary's address information contained in `Beneficiary` is an address that can actually be received.
    1. ❗️**Refer [05-Verifying Wallet Address] page for how to verify address.**
4. If there is the beneficiary name in `Beneficiary`, check that it matches the owner of the corresponding address as well. Name information includes both first name and last name only if `primaryIdentifier` has been defined.
    1. ❗️**Refer [06-Verify-Names] for how to verify names.**
5. If necessary, perform sanction screening for the beneficiary address as well.
6. Based on this content, store the asset transfer information in DB and assign a `status` value. `verified` or `denied`
7. Create a response based on the result of checking the content of a request message. At this time, use `Originator`, `OriginatingVASP` and `Beneficiary` object by copying the objects from the request, and create the `BeneficiaryVASP` object based on the information of the beneficiary VASP.

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

**transferId**: This is a unique identification number for the asset transfer details created by a VASP which wants to transfer an asset. The client who sends a request generates and sends a UUID v4 value.

***

**currency**: This is a symbol of the virtual asset you want to transfer. (This is case insensitive.)

***

**amount**: This is the total volume of virtual assets you want to transfer

***

**historicalCost**: This is an acquisition cost of the virtual asset to be transferred (The requirements of Korea National Tax Service. However, it is not used yet.)

***

**tradePrice**: This is the amount of the virtual asset transfer converted to a type of legal tender. If there is no its own price information, convert this using the price API of other VASP.

***

**tradeCurrency**: This is a legal tender code, which follows the ISO 4217 standard used when converting to a legal tender. The following currencies can be entered: 'KRW', 'USD', 'EUR', 'JPY', 'CNY', 'GBP', 'CAD', 'AUD', 'HKD', 'SGD'. If you need to use any other currency code, please inform the CodeVASP team! For more details, please refer to the FAQ page.

***

**isExceedingThreshold**: Indicates whether the `tradePrice` exceeds the Travel Rule threshold specified by law.
This field is input as `true` or `false`, and if the field value is `true`, the `Beneficiary` name in the request is compared with the actual name of the `Beneficiary` who owns the virtual asset address.
If the field value is true and the `Beneficiary` name is missing or different in the request, a 'denied' response is sent.

***

**originatingVasp**: For requests from other solutions, an originatingVASP object may be outside payload. In this case, please overwrite the `originatingVASP` object outside payload to the ivms101 message of the payload area.

***

**payload**: This is an object to contain IVMS101 message. Please refer to [IVMS101] page, Type section.

***

**address**: Wallet address of the beneficiary. Since some VASPs integrated with other solutions may required this field, please refer to [Interoperability with Other Protocols] page.

***

**tag**: Include this if a Tag or Memo exists (e.g., XRP). Since some VASPs integrated with other solutions may required this field, please refer to [Interoperability with Other Protocols] page.

***

**network**: This is included to distinguish when a single coin exists on multiple networks. Since some VASPs integrated with other solutions may required this field, please refer to [Interoperability with Other Protocols] page.


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

**result**: This is a result derived from the previous authorization of the virtual asset.

-`verified`: Authorize.

-`denied`: Deny to transfer an asset

***

**reasonType**: For denied, the type corresponding to the reason.

-`NOT_FOUND_ADDRESS`: This is a case where a virtual asset address cannot be found.

-`NOT_SUPPORTED_SYMBOL`: This is a currency symbol which cannot be traded.

-`NOT_KYC_USER`: This is a case where the owner did not process KYC verification.

-`INPUT_NAME_MISMATCHED`: The beneficiary name sent in the request message does not match the actual owner's name.

-`DOB_MISMATCHED`: The beneficiary DOB sent in the request message does not match the actual owner's DOB. (Optional)

-`SANCTION_LIST`: Virtual asset addresses or owners are subject to the sanction of the beneficiary VASP.

-`LACK_OF_INFORMATION`: This is a case where there is no the information necessary to make an asset transfer decision.

-`UNKNOWN`: This refers to other reasons.

***

**reasonMsg**: message describing the reasonType

***

**transferId**: A unique ID is required to track the status of a transaction from an asset transfer authorization request through subsequent processes. The client who sends a request generates and sends UUID.

***

**payload**(Required): This is an object to contain IVMS101 message. Please refer to [04-IVMS101-part1] page.

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
