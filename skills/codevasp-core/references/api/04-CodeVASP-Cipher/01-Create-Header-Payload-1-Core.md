# 01-Create-Header-Payload-1-Core

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

* `SEARCH_VASP_LIST`: VASP List Search
* `SEARCH_PUBKEY_LIST`: Public Key Search
* `SEARCH_ADDRESS`: Virtual Asset Address Search
* `TRANSFER_AUTHORIZATION`: Asset Transfer Authorization
* `REPORT_TRANSFER_RESULT`: Report Transfer Result (TX Hash)
* `SEARCH_TRANSACTION_STATUS`: Transaction Status Search
* `FINISH_TRANSFER`: Finish Transfer

---

# Scenarios

## VASP List Search

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SEARCH_VASP_LIST` |

```json
{
    "allianceName": "CODE",
    "apiType": "SEARCH_VASP_LIST"
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
  "signature": "siBkH9hov/7yfU8bedguB0mMIdt04DQUnB85mVPpzrMr8i+SYl3Pn46U2kvjrxEzwCSaH5E5W0nFsPyRfHbNCw==",
  "nonce": 803400752,
  "dateTime": "2023-06-21T05:25:52Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo="
}
```

## Public Key Search

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SEARCH_PUBKEY_LIST` |

```json
{
    "allianceName": "CODE",
    "apiType": "SEARCH_PUBKEY_LIST"
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
  "signature": "ouCcxqhDtsU4N9nN05QP6J6gJoJsZqilsj296C2bRNAMB1MCmVVChJ7OkhU0SVdQWaDMg3YMLZmgMr2yJfpMDQ==",
  "nonce": 1727097200,
  "dateTime": "2023-06-21T05:25:28Z",
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

## Asset Transfer Authorization

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `TRANSFER_AUTHORIZATION` |
| remotePublicKey | Required | string | beneficiary VASP's Public Key |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "remotePublicKey": "8DJomhrvr0zD11+m3nwG3ZeO9OsSY8uLHdA7WgoaX0g=",
    "apiType": "TRANSFER_AUTHORIZATION",
    "request": {
        "amount": "0.00001",
        "tradePrice": "55555",
        "transferId": "qwewq51312",
        "tradeCurrency": "KRW",
        "historicalCost": "",
        "currency": "BTC",
        "originatingVasp": {},
        "isExceedingThreshold": true,
      
        "address": "",
        "tag":"",
        "network":"",
      
        "originatorNaturalPersonLastName": "Barnes",
        "originatorNaturalPersonFirstName": "Robert",
        "originatorNaturalPersonLocalLastName": "반스",
        "originatorNaturalPersonLocalFirstName": "로버트",
        "originatorNaturalPersonDob": "1990-01-01", 
        "originatorNaturalPersonPob": "Seoul", 
      
        "originatorLegalName": "Coinone., Ltd.",
        "originatorLegalLocalName": "(주)코인원",
        "originatorLegalAddressType": "GEOG",
        "originatorLegalTownName": "TownName",
        "originatorLegalAddressLine":  [
           "Gangnam-gu",
           "1234"
        ],
        "originatorLegalCountry": "KR",
        "originatorLegalNationalIdentifier": "1234567890",
        "originatorLegalNationalIdentifierType": "RAID",
        "originatorLegalRegistrationAuthority":  "RA000657",
        "originatorLegalCountryOfRegistration": "KR",
        "originatorLegalPersonList" : [
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
        "nameIdentifierType": "LEGL",
        "customerIdentification": "3213213qweqwe213312",
        "originatorWalletAddress": "012345678900",
      
        "originatingVaspCountryOfRegistration": "KR",
        "originatingVaspLegalName": "Korbit Inc.",
        "originatingVaspNameIdentifierType" : "LEGL",
        "originatingVaspAddressType": "GEOG",
        "originatingVaspTownName": "Seoul",
        "originatingVaspAddressLine": [
            "14 Teheran-ro 4-gil, Gangnam-gu",
            "4th floor"
        ],
        "originatingVaspCountry": "KR",
        "originatingVaspNationalIdentifier": "1234567890",
        "originatingVaspNationalIdentifierType": "RAID",
        "originatingVaspRegistrationAuthority": "RA000657",
        "beneficiaryWalletAddress": "012345678901",
        "beneficiaryNameIdentifierType": "LEGL",
        "beneficiaryCustomerIdentification": "3213213qweqwe213312",
        "beneficiaryNaturalPersonLastName": "스미스",
        "beneficiaryNaturalPersonFirstName": "앨리스",
        "beneficiaryNaturalPersonLocalLastName": "Smith",
        "beneficiaryNaturalPersonLocalFirstName": "Alice",
        "beneficiaryNaturalPersonDob": "1990-01-01", 
        "beneficiaryNaturalPersonPob": "Seoul", 
        
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
        ]
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
  "signature": "pOCyDvkDsPGq5oU4Of7C2IwFv09Oovbp0tKU2mFoxtXuPPWsTJ/UOUGkebvskTx4PV1z9VFNazmDPcI9A6EFDA==",
  "nonce": 1131014480,
  "dateTime": "2023-06-21T05:25:07Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo=",
  "body": "{\"transferId\":\"qwewq51312\",\"currency\":\"BTC\",\"amount\":\"0.00001\",\"historicalCost\":\"\",\"tradePrice\":\"55555\",\"tradeCurrency\":\"KRW\",\"isExceedingThreshold\":true,\"originatingVasp\":{},\"payload\":\"...\"}"
}
```

## Report Transfer Result (TX Hash)

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `REPORT_TRANSFER_RESULT` |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "apiType": "REPORT_TRANSFER_RESULT",
    "request": {
        "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
        "txid": "311BFF73D9B7969CCF1042186180159C724FAB59013A7A034A93E5FB9D6BAFE6",
        "beneficiaryAddress":"address:tag"
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

## Transaction Status Search

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `SEARCH_TRANSACTION_STATUS` |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "apiType": "SEARCH_TRANSACTION_STATUS",
    "request": {
        "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002"
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
  "signature": "5SvoIHj5+sY6vXGVs2RBzVriTvYjhyPMJhzSq4/hJdpv6nC4WCswj7j31arl6BoTuhCa7eaudtyp2JpVVoLGDA==",
  "nonce": 1459904464,
  "dateTime": "2023-06-21T05:24:40Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo=",
  "body": "{\"transferId\":\"b09c8d00-8da9-11ec-b909-0242ac120002\"}"
}
```

## Finish Transfer

### Request

| Name | Required | Type | Description |
| :--- | :------- | :--- | :---------- |
| allianceName | Required | string | Default: `CODE` |
| apiType | Required | string | `FINISH_TRANSFER` |
| request | Required | object | Request Body |

```json
{
    "allianceName": "CODE",
    "apiType": "FINISH_TRANSFER",
    "request": {
        "transferId": "b09c8d00-8da9-11ec-b909-0242ac120002",
        "status": "canceled",
        "reasonType": "SANCTION_LIST"
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
  "signature": "3zfR+ONXteTcs3o7MMT69QyCYbBpXJFhXXqRAo99UxrpAOTQUEpUVffTZ2wRKPSkfxdqEvoNHJlA2PShZBVgBA==",
  "nonce": 1593438672,
  "dateTime": "2023-06-21T05:24:09Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo=",
  "body": "{\"transferId\":\"b09c8d00-8da9-11ec-b909-0242ac120002\",\"status\":\"canceled\",\"reasonType\":\"SANCTION_LIST\"}"
}
```

## Example (Generic)

### Request
```bash
curl --request POST \
     --url https://your-server-host.com/api/v1/code/api-payloads \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "allianceName": "code",
  "apiType": "Types",
  "remotePublicKey": "remotePublicKey",
  "request": "request"
}'
```

### Response
```json
{
  "signature": "siBkH9hov/7yfU8bedguB0mMIdt04DQUnB85mVPpzrMr8i+SYl3Pn46U2kvjrxEzwCSaH5E5W0nFsPyRfHbNCw==",
  "nonce": 803400752,
  "dateTime": "2023-06-21T05:25:52Z",
  "publicKey": "LOpv3Vd7PKLrlDmk/MFi6mc2rPWhi3G0H3D74dayxSo="
}
```
