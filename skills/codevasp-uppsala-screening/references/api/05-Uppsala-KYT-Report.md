# 06-Uppsala-KYT-Report

Retrieves the KYT analysis report using the `requestId` returned by the KYT Search API ([05-Uppsala-KYT-Search.md](04-Uppsala-KYT-Search.md)).

The response structure varies depending on the analysis status: `PENDING` (still processing), `RELEASED` (completed), or `FAILED` (analysis failed).

> **Note**: This API is jointly operated by CodeVASP and Uppsala Security. For inquiries or access requests, please contact [partnership@codevasp.com](mailto:partnership@codevasp.com).

## Endpoint

`GET` `/v1/code/uppsala/kytreport`

## Request Parameters

### Query Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| requestId | integer | Required | Request ID received from the KYT Search API response. |

No request body is required.

## Response

### Fields
| Name | Type | Nullable | Description |
| :--- | :--- | :------- | :---------- |
| requestId | integer | No | The search request ID. |
| status | string | No | `PENDING`, `RELEASED`, or `FAILED`. |
| error | string | Yes | Error description. Present only when status is `FAILED`. |
| submittedAt | datetime | No | Timestamp when the analysis was requested. |
| report | object | Yes | Full analysis report. Present only when status is `RELEASED`. |

### Status Values

| Status | Meaning |
| :--- | :--- |
| `PENDING` | Analysis is still in progress. Retry after a short interval. |
| `RELEASED` | Analysis completed. The `report` field contains the full result. |
| `FAILED` | Analysis failed. The `error` field contains the reason. |

> **Note**: Analysis is performed asynchronously. When the status is `PENDING`, it is recommended to poll at intervals of 5–10 seconds until the status changes to `RELEASED` or `FAILED`.

### Error Values (when `FAILED`)

| error | Meaning |
| :--- | :--- |
| `Tx hash not found` | The transaction hash could not be found on the blockchain. |
| `Transaction analysis failed` | The analysis process encountered a failure. |

> **Troubleshooting `Tx hash not found`**: This error occurs when:
> 1. The transaction hash does not exist on the specified blockchain.
> 2. The transaction is unconfirmed (pending in mempool) — retry after block confirmation.
> 3. The `blockchain` value submitted in [05-Uppsala-KYT-Search.md](04-Uppsala-KYT-Search.md) does not match the actual chain of the transaction (e.g., an EVM hash submitted with `blockchain: "TRX"`).

---

## Report

When `status` is `RELEASED`, the `report` field contains the full analysis result.

### Top-Level Report Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| reportId | integer | Unique report identifier. |
| verdict | string | Risk verdict: `Clean`, `Suspicious`, or `Malicious`. |
| totalAmount | number | Total amount traced in the transaction graph. |
| maliciousAmount | number | Amount traced to blacklisted sources. Basis for `Malicious` verdict. |
| suspiciousAmount | number | Amount traced to suspicious sources (mixers, gamblers, etc.). Basis for `Suspicious` verdict. |
| receiver | string | Receiving address of the analyzed transaction. |
| generatedAt | datetime | Report generation timestamp. |
| senders | string[] | List of sender addresses. UTXO chains (BTC/LTC) may have multiple senders. |
| riskIndicators | object | Detected risk indicators. See Risk Indicators section below. |
| annotations | `map<string, object>` | Address-to-label mapping for addresses found in the transaction path. |
| byToken | `map<string, object>` | Amount breakdown by token symbol (e.g., ETH, USDT, BTC). Separates risk amounts per token for EVM multi-token transactions. |

### Verdict Logic

| Verdict | Trigger Condition |
| :--- | :--- |
| `Malicious` | `blacklistedAddresses` contains 1 or more entries. |
| `Suspicious` | No blacklisted addresses, but `suspiciousServices`, `behavioralPatterns`, `transactionPatterns`, or `mlFeatures` has at least one detection. |
| `Clean` | All risk indicators are empty. |

---

### Risk Indicators (`riskIndicators`)

| Field | Type | Description |
| :--- | :--- | :---------- |
| blacklistedAddresses | string[] | Uppsala blacklist matched addresses. |
| suspiciousServices | map<address, label> | High-risk services in the trace path. |
| behavioralPatterns | object | Behavioral pattern detections. |
| transactionPatterns | object | Transaction pattern detections. |
| mlFeatures | `map<string, integer>` | ML model laundering detections. Value is `1` (detected). |

#### `blacklistedAddresses` (string[])

Addresses matching the Uppsala blacklist. **If non-empty, the verdict is always `Malicious`.** These are wallets directly linked to hacking, scams, darknet, or theft.

#### `suspiciousServices` (map<address, label>)

Addresses identified as high-risk services in the trace path. Label examples: `Mixer`, `Tumbler`, `Gambling`, `Darknet Market`.

#### `behavioralPatterns`

| Key | Description |
| :--- | :---------- |
| swift_movement | Layering indicator. Rapid fund forwarding through intermediate wallets. |

##### `swift_movement`

Detects funds rapidly forwarded through intermediate wallets within a threshold time window.

| Field | Type | Description |
| :--- | :--- | :---------- |
| addresses_count | integer | Number of unique addresses that received and forwarded funds within the time window. |
| movement_count | integer | Total number of receive-then-forward movements detected. |
| threshold_seconds | integer | Time window threshold (movements within this time are flagged as swift). |
| path_count | integer | Number of forwarding chains found in the trace graph. |
| paths[].path_length | integer | Number of hops (intermediate wallets) in the chain. |
| paths[].movements[].address | string | Address that received then forwarded funds. |
| paths[].movements[].receive_tx | string | Receive transaction hash. |
| paths[].movements[].send_tx | string | Send transaction hash. |
| paths[].movements[].receive_time | datetime | Time funds were received. |
| paths[].movements[].send_time | datetime | Time funds were forwarded. |
| paths[].movements[].time_diff_seconds | integer | Seconds between receive and forward (key layering signal). |
| paths[].movements[].receive_amount | number | Amount received at this hop (native token). |
| paths[].summary.start_address | string | Forwarding chain start address. |
| paths[].summary.end_address | string | Forwarding chain end address. |
| paths[].summary.total_time_seconds | integer | Total elapsed time from start to end of chain. |
| paths[].summary.avg_hop_time_seconds | integer | Average time per hop. |

#### `transactionPatterns`

| Key | Description |
| :--- | :---------- |
| dormant status | Wallets suddenly active after long dormancy. |
| high_value_transaction | Transfers exceeding USD threshold. |
| high_gas_fee | Abnormally high gas fees relative to transfer amount. |

##### `dormant status`

Wallets that became suddenly active after a long dormant period.

| Field | Type | Description |
| :--- | :--- | :---------- |
| pattern | string | Pattern identifier (`"dormant_account"`). |
| is_suspicious | boolean | `true` if the dormant pattern is judged as an obfuscation signal. |
| addresses_count | integer | Number of wallets matching the pattern. |
| threshold_days | integer | Dormancy threshold in days. |
| wallets[].address | string | Wallet address matching the dormant pattern. |
| wallets[].holding_type | string | `long_hold_then_sent`, `still_holding`, or `never_sent`. |
| wallets[].holding_period_days | number | Actual holding period in days. |
| wallets[].hold_start | datetime | Holding start time. |
| wallets[].hold_end | datetime | Holding end time (may be `"still holding"`). |
| wallets[].hold_start_tx | string | Transaction hash when holding started. |
| wallets[].hold_end_tx | string | Transaction hash when holding ended (`null` if still holding). |

##### `high_value_transaction`

Transfers exceeding a configured USD threshold.

| Field | Type | Description |
| :--- | :--- | :---------- |
| pattern | string | Pattern identifier (`"high_value_transaction"`). |
| is_suspicious | boolean | `true` if the high-value pattern is judged as a laundering signal. |
| count | integer | Number of transactions exceeding the threshold. |
| total_usd | number | Total USD value of flagged transactions. |
| threshold_usd | number | USD threshold used for detection. |
| transactions[].tx_hash | string | Transaction hash. |
| transactions[].amount | number | Transfer amount (native token). |
| transactions[].amount_usd | number | USD value at time of transfer. |
| transactions[].symbol | string | Token symbol. |

##### `high_gas_fee`

Transactions with abnormally high gas fees relative to the transfer amount.

| Field | Type | Description |
| :--- | :--- | :---------- |
| pattern | string | Pattern identifier (`"high_gas_fee"`). |
| is_suspicious | boolean | `true` if the gas fee pattern is judged as a laundering signal. |
| count | integer | Number of flagged transactions. |
| total_gas_cost | number | Total gas cost of flagged transactions (native token). |
| avg_gas_percentage | number | Average gas cost as percentage of transfer amount. |
| transactions[].tx_hash | string | Transaction hash. |
| transactions[].gas_used | integer | Gas units consumed. |
| transactions[].gas_cost | number | Gas cost (native token). |
| transactions[].gas_cost_symbol | string | Gas payment token symbol. |
| transactions[].tx_amount | number | Transfer amount (native token). |
| transactions[].gas_percentage | number | Gas cost as percentage of transfer amount. |
| transactions[].exceeds_absolute_threshold | boolean | Whether gas cost exceeds the absolute threshold. |
| transactions[].exceeds_ratio_threshold | boolean | Whether gas cost exceeds the ratio threshold. |
| transactions[].threshold_used | number | Absolute threshold applied. |

#### `mlFeatures` (`map<string, integer>`)

FATF guideline-based ML model detections for money laundering behaviors. Non-empty values contribute to `Suspicious` or `Malicious` verdicts.

Known feature keys: `Blacklisted wallet in trail`, `Relaying and Mixing`, `Tumbling`, `Abnormal Mixing`, `Abnormal Relaying`.

---

### Annotations (`annotations`)

Map where each key is a **wallet address** (string) and the value is:

| Field | Type | Description |
| :--- | :--- | :---------- |
| annotation | string | Human-readable label (e.g., `"Binance"`, `"Wrapped Ether, Token Contract"`, `"Huobi"`, `"OKEx, Exchange"`). May be `null` for blacklisted addresses. |
| securityCategory | string | Risk grade: `whitelist` (safe), `graylist` (neutral — exchanges, contracts), `blacklist` (malicious). |

### By Token (`byToken`)

Map where each key is a **token symbol** (e.g., `"ETH"`, `"USDT"`, `"BTC"`) and the value is:

| Field | Type | Description |
| :--- | :--- | :---------- |
| total | number | Total amount traced for this token. |
| malicious | number | Amount traced to blacklisted sources. |
| suspicious | number | Amount traced to suspicious sources. |

---

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
| 403 | `ACCESS_DENIED` | No access permission for this resource. |
| 404 | `NOT_FOUND` | The requestId does not exist. |
| 422 | `INVALID_ORIGIN` | VASP KYT feature not registered, request limit exceeded, or feature disabled. |
| 503 | `SERVICE_UNAVAILABLE` | Temporary service disruption. |
| 500 | `INTERNAL_SERVER_ERROR` | Internal server error. |

## Examples

### Request
```bash
curl --request GET \
     --url 'https://trapi.codevasp.com/v1/code/uppsala/kytreport?requestId=5714' \
     --header 'X-Code-Req-Datetime: datetime' \
     --header 'X-Code-Req-Nonce: nonce' \
     --header 'X-Code-Req-PubKey: Your key' \
     --header 'X-Code-Req-Signature: Sig' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json'
```

### Response (PENDING)
```json
{
  "requestId": 5714,
  "status": "PENDING",
  "error": null,
  "submittedAt": "2026-03-24T09:05:43",
  "report": null
}
```

### Response (FAILED)
```json
{
  "requestId": 5713,
  "status": "FAILED",
  "error": "Tx hash not found",
  "submittedAt": "2026-03-24T09:02:52",
  "report": null
}
```

### Response (RELEASED — Clean)
```json
{
  "requestId": 5714,
  "status": "RELEASED",
  "error": null,
  "submittedAt": "2026-03-24T09:05:43",
  "report": {
    "reportId": 4154,
    "verdict": "Clean",
    "totalAmount": 0.01305,
    "maliciousAmount": 0,
    "suspiciousAmount": 0,
    "receiver": "0x4675c7e5baafbffbca748158becba61ef3b0a263",
    "generatedAt": "2026-03-24T09:08:02",
    "senders": ["0x4838b106fce9647bdf1e7877bf73ce8b0bad5f97"],
    "riskIndicators": {
      "blacklistedAddresses": [],
      "suspiciousServices": {},
      "behavioralPatterns": {},
      "transactionPatterns": {},
      "mlFeatures": {}
    },
    "annotations": {
      "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2": {
        "annotation": "Wrapped Ether, Token Contract",
        "securityCategory": "graylist"
      },
      "0x51c72848c68a965f66fa7a88855f9f7784502a7f": {
        "annotation": "Wintermute, Market Maker",
        "securityCategory": "graylist"
      }
    },
    "byToken": {
      "ETH": { "total": 0.01305, "malicious": 0, "suspicious": 0 }
    }
  }
}
```

### Response (RELEASED — Malicious)
```json
{
  "requestId": 5715,
  "status": "RELEASED",
  "error": null,
  "submittedAt": "2026-03-04T06:20:00",
  "report": {
    "reportId": 2934,
    "verdict": "Malicious",
    "totalAmount": 27.71263268,
    "maliciousAmount": 27.71263268,
    "suspiciousAmount": 0,
    "receiver": "0xeaf19109fd5fb9cffff7d602e1bf35203afd443a",
    "generatedAt": "2026-03-04T06:22:55",
    "senders": ["0xda39b8f19ae780aa07ff512a79f03df52c1c7e43"],
    "riskIndicators": {
      "blacklistedAddresses": [
        "0xc5d431ee2470484b94ce5660aa6ae835346abb19"
      ],
      "suspiciousServices": {},
      "behavioralPatterns": {},
      "transactionPatterns": {
        "dormant status": {
          "pattern": "dormant_account",
          "is_suspicious": false,
          "addresses_count": 1,
          "threshold_days": 365,
          "wallets": [
            {
              "address": "0xda39b8f19ae780aa07ff512a79f03df52c1c7e43",
              "holding_type": "never_sent",
              "holding_period_days": 3498.32,
              "hold_start": "2016-08-04 22:41:16",
              "hold_end": "still holding",
              "hold_start_tx": "0x2ea57f652edb269495...",
              "hold_end_tx": null
            }
          ]
        }
      },
      "mlFeatures": {
        "Blacklisted wallet in trail": 1
      }
    },
    "annotations": {
      "0xc5d431ee2470484b94ce5660aa6ae835346abb19": {
        "annotation": null,
        "securityCategory": "blacklist"
      }
    },
    "byToken": {
      "ETH": { "total": 27.712632679, "malicious": 27.712632679, "suspicious": 0 }
    }
  }
}
```

### Response (RELEASED — Suspicious)
```json
{
  "requestId": 5716,
  "status": "RELEASED",
  "error": null,
  "submittedAt": "2026-03-04T09:10:00",
  "report": {
    "reportId": 2942,
    "verdict": "Suspicious",
    "totalAmount": 30000,
    "maliciousAmount": 0,
    "suspiciousAmount": 1700,
    "receiver": "TEkS7DeWpHmy6Wty3nLhKmcgCvbWZ1oyAa",
    "generatedAt": "2026-03-04T09:12:04",
    "senders": ["TRPS8prmSJrGcv8K6NUhDzMG8tQ8pu1GPA"],
    "riskIndicators": {
      "blacklistedAddresses": [],
      "suspiciousServices": {},
      "behavioralPatterns": {},
      "transactionPatterns": {},
      "mlFeatures": {
        "Abnormal Relaying": 1
      }
    },
    "annotations": {
      "TMqDrZa5kEebg5wi3W3wusVZ6ZF2w6JczH": {
        "annotation": "Huione",
        "securityCategory": "graylist"
      },
      "TLaGjwhvA8XQYSkFAcAXyYDvuue9eGYitv": {
        "annotation": "OKEx, Exchange",
        "securityCategory": "graylist"
      }
    },
    "byToken": {
      "USDT": { "total": 30000, "malicious": 0, "suspicious": 1700 }
    }
  }
}
```
