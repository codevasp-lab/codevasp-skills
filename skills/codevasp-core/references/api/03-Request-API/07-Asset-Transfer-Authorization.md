# 07-Asset-Transfer-Authorization

A VASP who wants to transfer an asset requests the VASP to whom the virtual asset is to be transferred for an authorization to transfer the asset. The VASP to transfer assets sends the sender's personal information at the request, and the VASP, to whom an asset is transferred, can check the sender's information and reject the transaction or authorize it with the beneficiary's personal information.
The VASP who sent a request updates the status of saved asset transfer list to verified or denied before sending the request or after receiving the response.

> ❗️Due to the complexity of the ivms101 standard objects, we recommend using CodeVASP-Cipher. If it is unavoidably impossible to use due to internal security reasons, please read the ivms101 standard section in the developer guide carefully.

> ❗️This API works in a **synchronous manner**, providing an immediate response upon request.

## Endpoint

`POST` `/v1/code/transfer/{BeneficiaryVaspEntityId}`

## Request Parameters

### Headers
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| X-Code-Req-Datetime | string | Required | ISO8601 UTC datetime |
| X-Code-Req-Nonce | string | Required | Random nonce |
| X-Code-Req-PubKey | string | Required | Your Public Key |
| X-Code-Req-Remote-PubKey | string | Required | Beneficiary Public Key |
| X-Code-Req-Signature | string | Required | Signature |
| X-Request-Origin | string | Required | `code:{yourVaspEntityId}` |
| accept | string | Optional | `application/json` |
| content-type | string | Required | `application/json` |

### Path Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| BeneficiaryVaspEntityId | string | Required | EntityID of the VASP which owns the address. |

### Body Parameters

> **Info**: For the originator's personal information, as of August 7 2024, **individual's name and date of birth** are to be sent for the individual, and only information on the legal person and representative name is to be sent for the legal person.

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

**transferId**: A unique ID is required to track the status of transaction from an asset transfer authorization request through subsequent processes.
The client who sends a request generates and sends a UUID v4 value.

***

**currency**: This is a symbol of the virtual asset you want to transfer. (This is case insensitive.)

***

**amount**: Enter the total number of virtual assets to be transferred, representing the amount actually sent through the blockchain, excluding fees.

***

**historicalCost**: This is an acquisition cost of the virtual asset to be transferred (The requirements of National Tax Service. However, it is not used yet.)

***

**tradePrice**: This is the amount of the virtual asset transfer converted to a type of legal tender. If there is no its own price information, convert this using the price API of other VASP. **Please refer to 'tradePrice' calculation below.**

***

**tradeCurrency**: This is a legal tender code, which follows the ISO 4217 standard used when converting to a legal tender. The following currencies can be entered: 'KRW', 'USD', 'EUR', 'JPY', 'CNY', 'GBP', 'CAD', 'AUD', 'HKD', 'SGD'. If you need to use any other currency code, please inform the CodeVASP team! For more details, please refer to the <Anchor label="Developer FAQ" target="_blank" href="https://alliances.codevasp.com/board/322">Developer FAQ</Anchor> page.

***

**isExceedingThreshold**: Indicates whether the tradePrice exceeds the Travel Rule threshold specified by law.
This field is input as true or false, and if the field value is true, the Beneficiary name in the request is compared with the actual name of the Beneficiary who owns the virtual asset address.
If the field value is true and the Beneficiary name is missing or different in the request, a 'denied' response is sent.

***

**originatingVasp**: Due to the difference from other solutions, an originatingVASP object may be included outside payload. In this case, please overwrite originatingVASP of payload -> ivms101.

***

**payload**: This is an object to contain an IVMS101 message. Please refer to [04-IVMS101].

***

**address**: Wallet address of the beneficiary. Since some VASPs integrated with other solutions may required this field, please refer to [12-Interoperability with Other Protocols] page.

***

**tag**: Include this if a Tag or Memo exists (e.g., XRP). Since some VASPs integrated with other solutions may required this field,  please refer to [12-Interoperability with Other Protocols] page.

***

**network**: This is included to distinguish when a single coin exists on multiple networks. Since some VASPs integrated with other solutions may required this field,  please refer to [12-Interoperability with Other Protocols] page.

> 📘 'tradePrice' calculation
>
> In the "tradePrice" field, you should enter the value calculated by multiplying the quantity of the asset by its price in fiat currency. In the "tradeCurrency" field, you should specify the type of fiat currency.
> For example, if you are transferring 2 BTC and the price at that moment is 42,708 USD, then the total price will be 42,708 USD * 2 = 85,416 USD. 
> In this case, your value should be as follows:
>
> "tradePrice": "85416",
> "tradeCurrency": "USD",


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

**result**: This is a result of the previous authorization of the virtual asset.

-`verified`: Authorized

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

**payload**(Required): This is an object to contain IVMS101 message. Please refer to [04-IVMS101].


## Examples

### Request
```bash
curl --request POST \
     --url https://trapi-dev.codevasp.com/v1/code/transfer/codexchange \
     --header 'X-Code-Req-Datetime: 2024-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Remote-PubKey: beneficiaryPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "transferId": "681f27dd-43e4-4ea3-9bcc-607426d6349f",
  "currency": "btc",
  "amount": "1.23",
  "historicalCost": "40000",
  "tradePrice": "125000",
  "tradeCurrency": "USD",
  "isExceedingThreshold": true,
  "originatingVasp": {},
  "payload": "encrypted ivms101 payload"
}'
```

### Response
```json
{
  "result": "verified",
  "reasonType": "",
  "reasonMsg": "",
  "transferId": "681f27dd-43e4-4ea3-9bcc-607426d6349f",
  "beneficiaryVasp": {},
  "payload": "encrypted ivms101 payload"
}
```
