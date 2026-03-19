# 03-03. Networks by Coin

Returns network information for each coin ticker (currency). The network value returned by this API can be used in operations such as 'Search VASP by Wallet', 'Search VASP by TXID', and ID Connect requests.

Since the network field is required in certain cases (e.g., search requests sent to GTR), it is strongly recommended to provide this value whenever possible.

## Endpoint

`GET` `/v1/code/connect/coin-network`

## Request Parameters

### Headers
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| X-Code-Req-Datetime | string | Required | ISO8601 UTC datetime |
| X-Code-Req-Nonce | string | Required | Random nonce |
| X-Code-Req-PubKey | string | Required | Your Public Key |
| X-Code-Req-Signature | string | Required | Signature |
| X-Request-Origin | string | Required | `code:{yourVaspEntityId}` |
| accept | string | Optional | `application/json` |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| updatedDateTime | string | Updated time |
| coinNetworks | array | An array of network objects by coin |

**coinNetworks object fields:**

| Name | Type | Description |
| :--- | :--- | :---------- |
| coin | string | Coin symbol |
| networkList | array | List of networks supported for this coin |

**networkList object fields:**

| Name | Type | Description |
| :--- | :--- | :---------- |
| network | string | Network name |
| addressExample | string | Address example |
| txIdExample | string | TXID example |

## Examples

### Request
```bash
curl --request GET \
     --url https://trapi-dev.codevasp.com/v1/code/connect/coin-network \
     --header 'X-Code-Req-Datetime: 2024-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json'
```

### Response
```json
{
    "updatedDateTime": "2025-02-13T00:57:51",
    "coinNetworks": [
        {
            "coin": "BTC",
            "networkList": [
                {
                    "network": "BSC",
                    "addressExample": "0xffe699847beffcb5396d90321064417e91c407ba",
                    "txIdExample": "0x17137e2c88feaa7b22dc099bf799eb482f4cdbd3931f9684ebf665d1d49fbe17"
                },
                {
                    "network": "BTC",
                    "addressExample": "1zzYZRybLWfBFyit7Jqm8osnVtKHtkKkW",
                    "txIdExample": "797be944db44c5ddfdaebc61ac617f164708c329c57da514a3dc0bca8940e34a"
                }
            ]
        }
    ]
}
```
