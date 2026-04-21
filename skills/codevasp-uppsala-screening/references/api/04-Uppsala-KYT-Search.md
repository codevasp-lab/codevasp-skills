# 05-Uppsala-KYT-Search

By providing a blockchain transaction hash, this API submits a KYT (Know Your Transaction) analysis request to Uppsala Sentinel Protocol. If a cached result exists, it returns immediately; otherwise, the analysis runs asynchronously.

Use the KYT Report API ([06-Uppsala-KYT-Report.md](05-Uppsala-KYT-Report.md)) to retrieve the analysis result, or provide a `callbackUrl` to receive the result via webhook when the analysis completes.

> **Note**: This API is jointly operated by CodeVASP and Uppsala Security. For inquiries or access requests, please contact [partnership@codevasp.com](mailto:partnership@codevasp.com).

## Endpoint

`POST` `/v1/code/uppsala/kytsearch`

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| txHash | string | Required | Blockchain transaction hash to analyze. |
| blockchain | string | Required | Network code (e.g., "ETH", "BTC"). See supported networks below. |
| force | boolean | Optional | When `true`, ignores cached results and forces re-analysis. Default: `false`. |
| callbackUrl | string | Optional | HTTPS URL to receive the analysis result via POST callback when completed. |

### Supported Networks

| | | | |
| :--- | :--- | :--- | :--- |
| ETH | BTC | TRX | SOL |
| MATIC | BSC | BNB | BASE |
| ARBITRUM | OPTIMISM | SCROLL | LINEA |
| AVAXC | AVAX | OPBNB | BCH |
| SEI | SEIEVM | SONIC | ADA |
| XRP | XLM | LTC | EOS |
| KAIA | KLAY | CELO | GLMR |
| MOVR | WLD | BERA | FTM |
| ORDIBTC | SEGWITBTC | PLASMA | STABLE |
| FRAXTAL | KATANA | | |

### Validation Rules

- `txHash`: Must not be empty or null.
- `blockchain`: Must not be empty or null. Must be one of the supported network codes.
- `callbackUrl`: Must be a valid HTTPS URL if provided.

> **Important**: The `txHash` must be a **confirmed** transaction that has been included in a block. Unconfirmed (mempool-only) transactions are not supported. The `blockchain` must match the chain the transaction actually belongs to — for example, an ETH transaction must be submitted with `blockchain: "ETH"`, not `"TRX"`.

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| requestId | integer | Generated request ID. Used to retrieve the report via KYT Report API. |
| status | string | `PENDING` or `RELEASED`. |
| message | string | Descriptive message for the status. |

### Status Values

| Status | Meaning | Message |
| :--- | :--- | :--- |
| `PENDING` | New analysis started. Retrieve the result via KYT Report API or wait for callback. | `"Transaction analysis started."` |
| `RELEASED` | Cache hit. An existing analysis result was found and linked immediately. | `"Linked to existing report for this transaction."` |

## Callback

If `callbackUrl` is provided, the analysis result is delivered via `POST` to that URL when the analysis completes (status becomes `RELEASED` or `FAILED`). The callback body has the same structure as the KYT Report API response. See [07-Uppsala-KYT-Callback.md](06-Uppsala-KYT-Callback.md) for details.

## Errors

### Error Response Format
```json
{
  "errorType": "type",
  "errorMsg": "message"
}
```

### Error Codes
| HTTP | errorType | Condition |
| :--- | :--- | :--- |
| 400 | `BAD_REQUEST` | Request body JSON format error or validation failure. |
| 400 | `UNSUPPORTED_BLOCKCHAIN` | Unsupported `blockchain` value. |
| 422 | `INVALID_ORIGIN` | VASP KYT feature not registered, request limit exceeded, or feature disabled. |
| 503 | `SERVICE_UNAVAILABLE` | Temporary service disruption. |
| 500 | `INTERNAL_SERVER_ERROR` | Internal server error. |

## Examples

### Request
```bash
curl --request POST \
     --url https://trapi.codevasp.com/v1/code/uppsala/kytsearch \
     --header 'X-Code-Req-Datetime: datetime' \
     --header 'X-Code-Req-Nonce: nonce' \
     --header 'X-Code-Req-PubKey: Your key' \
     --header 'X-Code-Req-Signature: Sig' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "txHash": "0x5028a06f62bd79cd44d96779c5d1db68882ede90c3cbf62b6cc11a6218984063",
  "blockchain": "ETH",
  "force": false,
  "callbackUrl": "https://example.com/callback/kyt/uppsala"
}
'
```

### Response (PENDING)
```json
{
  "requestId": 5714,
  "status": "PENDING",
  "message": "Transaction analysis started."
}
```

### Response (RELEASED — cache hit)
```json
{
  "requestId": 5714,
  "status": "RELEASED",
  "message": "Linked to existing report for this transaction."
}
```
