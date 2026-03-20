# 02-2. Create Header, Payload (Add-On)

For Travel Rule API requests, the data to be entered in the Header and Body is encrypted and provided as a response. It is convenient to use the values received as is for the corresponding Travel Rule API according to each API type.

## Endpoint

`POST` `/api/v1/code/api-payloads`

## Request Parameters

### Headers
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| accept | string | Optional | `application/json` |
| content-type | string | Required | `application/json` |

### Body Parameters (General)
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| allianceName | string | Required | Travel Rule protocol code (default: `CODE`) |
| apiType | string | Required | The type of API action (see below) |
| request | object | Optional | Specific request payload depending on `apiType` |
| remotePublicKey | string | Optional | Counterparty Public Key (required for some types) |

## Supported API Types
 
* `SEARCH_ADDRESS_REQUEST`: Search VASP by Wallet Request
* `SEARCH_ADDRESS_RESULT`: Search VASP by Wallet Result
* `SEARCH_TRANSACTION_ID`: Search VASP by TXID Request
* `FETCH_TRANSACTION_ID`: Search VASP by TXID Result
* `VERIFICATION_TRANSACTION_ID`: Asset Transfer Data Request
* `SCREENING`: Screening

---

# Scenarios

## Search VASP by Wallet Request

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SEARCH_ADDRESS_REQUEST` |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "apiType": "SEARCH_ADDRESS_REQUEST",
    "request": {
        "currency": "XRP",
        "beneficiaryWalletAddress": "0x1234567890123456789012345678901234567890",
        "requestId": "fafd59e2-aff7-4cff-b7d8-9caa0cda70da"
    }
}
```

### Response

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| signature | Required | string | Generated Signature |
| nonce | Required | number | Generated Nonce |
| dateTime | Required | string | Current datetime for request |
| publicKey | Required | string | Your Public Key |
| body | Required | string | Generated payload |

```json
{
  "signature": "iDONJT6ck7bZD2++nGrknOR+o+aultZpwvWGRzabOePQvylN93A/9Zs6g+9mD7cnSMkE7NpuAKtKHNZvWP0RDA==",
  "nonce": 1048167312,
  "dateTime": "2023-06-21T05:25:16Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo=",
  "body": "{\"currency\":\"XRP\",\"addressNumber\":\"rHcFoo6a9qT5NHiVn1THQRhsEGcxtYCV4d:memo or tag\",\"requestId\":\"fafd59e2-aff7-4cff-b7d8-9caa0cda70da\"}"
}
```

## Search VASP by Wallet Result

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SEARCH_ADDRESS_RESULT` |

```json
{
    "allianceName": "CODE",
    "apiType": "SEARCH_ADDRESS_RESULT"
}
```

### Response

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| signature | Required | string | Generated Signature |
| nonce | Required | number | Generated Nonce |
| dateTime | Required | string | Current datetime for request |
| publicKey | Required | string | Your Public Key |

```json
{
  "signature": "iDONJT6ck7bZD2++nGrknOR+o+aultZpwvWGRzabOePQvylN93A/9Zs6g+9mD7cnSMkE7NpuAKtKHNZvWP0RDA==",
  "nonce": 1048167312,
  "dateTime": "2023-06-21T05:25:16Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo="
}
```

## Virtual Asset Address Search

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SEARCH_ADDRESS` |
| remotePublicKey | Required | string | beneficiary VASP's Public Key |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "remotePublicKey": "8DJomhrvr0zD11+m3nwG3ZeO9OsSY8uLHdA7WgoaX0g=",
    "apiType": "SEARCH_ADDRESS",
    "request": {
        "currency": "USDT",
        "beneficiaryWalletAddress": "0x1234567890123456789012345678901234567890", 
        "address": "0x1234567890123456789012345678901234567890",
        "network": "ETH",
        "tag": "tag if needed"
    }
}
```

### Response

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| signature | Required | string | Generated Signature |
| nonce | Required | number | Generated Nonce |
| dateTime | Required | string | Current datetime for request |
| publicKey | Required | string | Your Public Key |
| body | Required | string | Generated payload |

```json
{
  "signature": "iDONJT6ck7bZD2++nGrknOR+o+aultZpwvWGRzabOePQvylN93A/9Zs6g+9mD7cnSMkE7NpuAKtKHNZvWP0RDA==",
  "nonce": 1048167312,
  "dateTime": "2023-06-21T05:25:16Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo=",
  "body": "{\"currency\":\"XRP\",\"payload\":\"7UGcPV9aZ+NGZNdeV31eXyuEpQKHPA0kjaFvcUt9f8BIXG7IrkqiG2sXBqF9KZC5CZBAZ4xqnlHl2+ZSjqNSw9lm08y55NQTkIPNMHjdpsWCOItg4j5G+HDy2k8pZxmRZP7nVI6OkquJ7oB0jz31GL5uIPZd76NaBzOVy9dXe/Kp/lvTqGQj+TdD+v9C7Ez0fkOY+VPvQkzzBRlb\"}"
}
```

## Search VASP by TXID Request

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SEARCH_TRANSACTION_ID` |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "apiType": "SEARCH_TRANSACTION_ID",
    "request": {
        "txid": "0xac52211cafdbfe2b59571eed29bd22608984df5e11d771bb620a2213a333a674",
        "beneficiaryAddress":"address:tag",
        "requestId" : "fafd59e2-aff7-4cff-b7d8-9caa0cda70da"
    }
}
```

### Response

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| signature | Required | string | Generated Signature |
| nonce | Required | number | Generated Nonce |
| dateTime | Required | string | Current datetime for request |
| publicKey | Required | string | Your Public Key |
| body | Required | string | Generated payload |

```json
{
    "signature": "JY+mP1kj2zIQAzxUiQtwRrFmt0Rs+DRxYXHdCkpImC3qiPWEqploS8zM6s8r1AX8iiy+gBrcOaC3EdooR+VgBQ==",
    "nonce": 1324920960,
    "dateTime": "2024-05-23T05:16:18Z",
    "publicKey": "sG65Uno5V+Vm6XOXuwSuM5XMn+HgpqOZi+8PGaTIAMM=",
    "body": "{\"txid\":\"0xac52211cafdbfe2b59571eed29bd22608984df5e11d771bb620a2213a333a674\",\"requestId\":\"fafd59e2-aff7-4cff-b7d8-9caa0cda70da\",\"beneficiaryAddress\":\"address:tag\"}"
}
```

## Search VASP by TXID Result

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `FETCH_TRANSACTION_ID` |

```json
{
    "allianceName": "CODE",
    "apiType": "FETCH_TRANSACTION_ID"
}
```

### Response

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| signature | Required | string | Generated Signature |
| nonce | Required | number | Generated Nonce |
| dateTime | Required | string | Current datetime for request |
| publicKey | Required | string | Your Public Key |

```json
{
    "signature": "GNAy7n2KrjRiV1Cgbi/ifVbQipchsqFGS5DULqxeYZDgiV+uy3IDpbD5PrwFyUJkDUeq+3O5VbY8m6uAjIfpDA==",
    "nonce": 501286944,
    "dateTime": "2024-05-23T05:16:49Z",
    "publicKey": "sG65Uno5V+Vm6XOXuwSuM5XMn+HgpqOZi+8PGaTIAMM="
}
```

## Asset Transfer Data Request

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `VERIFICATION_TRANSACTION_ID` |
| remotePublicKey | Required | string | originator VASP's Public Key |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "remotePublicKey": "8DJomhrvr0zD11+m3nwG3ZeO9OsSY8uLHdA7WgoaX0g=",
    "apiType": "VERIFICATION_TRANSACTION_ID",
    "request": {
        "txid":"0xac52211cafdbfe2b59571eed29bd22608984df5e11d771bb620a2213a333a674",
        "beneficiaryAddress":"address:tag",
        "transferId" : "fafd59e2-aff7-4cff-b7d8-9caa0cda70da",
        "beneficiaryWalletAddress": "012345678901",
        "beneficiaryNameIdentifierType": "LEGL",
        "beneficiaryCustomerIdentification": "3213213qweqwe213312",
        "beneficiaryNaturalPersonLastName": "스미스",
        "beneficiaryNaturalPersonFirstName": "앨리스",
        "beneficiaryNaturalPersonLocalLastName": "Smith",
        "beneficiaryNaturalPersonLocalFirstName": "Alice",
        "beneficiaryLegalName": "Coinone., Ltd.",
        "beneficiaryLegalLocalName": "(주)코인원",
        "beneficiaryLegalPersonList" : [
          {
            "firstName": "Minsu",
            "lastName": "Kim",
            "localFirstName":"민수",
            "localLastName":"김"
          },
          {
            "firstName": "Soonhee",
            "lastName": "Lee",
            "localFirstName":"순희",
            "localLastName":"이"}
        ],
        "beneficiaryVaspCountryOfRegistration": "KR",
        "beneficiaryVaspLegalName": "Korbit Inc. beneficiaryVaspLegalName",
        "beneficiaryVaspNameIdentifierType": "LEGL",
        "beneficiaryVaspAddressType": "GEOG",
        "beneficiaryVaspTownName": "Seoul",
        "beneficiaryVaspAddressLine": [
            "14 Teheran-ro 4-gil, Gangnam-gu",
            "4th floor"
        ],
        "beneficiaryVaspCountry": "KR",
        "beneficiaryVaspNationalIdentifier": "1234567890",
        "beneficiaryVaspNationalIdentifierType": "RAID",
        "beneficiaryVaspRegistrationAuthority": "RA000657"
    }
}
```

### Response

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| signature | Required | string | Generated Signature |
| nonce | Required | number | Generated Nonce |
| dateTime | Required | string | Current datetime for request |
| publicKey | Required | string | Your Public Key |
| body | Required | string | Generated payload |

```json
{
    "signature": "6kfex70cuBgi6yYqRIFmDubuQavgARLjOv2aCmVWBAJ7ENAxumzKtoVNZ/MvoOcYkfkEJd1VTdfDSsG1LFZDCg==",
    "nonce": 1513115728,
    "dateTime": "2024-05-23T05:20:19Z",
    "publicKey": "sG65Uno5V+Vm6XOXuwSuM5XMn+HgpqOZi+8PGaTIAMM=",
    "body": "{\"transferId\":\"fafd59e2-aff7-4cff-b7d8-9caa0cda70da\",\"txid\":\"...\"}"
}
```

## Screening

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SCREENING` |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE", 
    "apiType": "SCREENING",
    "request": {
        "walletAddress": "** WALLET ADDRESS **",
        "chain": "** CHAIN TYPE **"
    }
}
```

### Response

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| signature | Required | string | Generated Signature |
| nonce | Required | number | Generated Nonce |
| dateTime | Required | string | Current datetime for request |
| publicKey | Required | string | Your Public Key |
| body | Required | string | Generated payload |

```json
{
    "signature": "rjhM+wJ2kzanjpTi7BAM5Lv7TsbYe+Qs+eTirbulbUNNvKWMPj76Lhpdutq8AP/wKItMKgT4jHh94x/S3YeLBA==",
    "nonce": 1694362784,
    "dateTime": "2025-08-12T07:33:25Z",
    "publicKey": "ILIszcJToqIA7DxHWMHLd5bhUx0QUV7m3KzWRX5sJ34=",
    "body": "{\"walletAddress\":\"sampleWallet\",\"chain\":\"ETH\"}"
}
```
 