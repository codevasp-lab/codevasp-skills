# 07-Uppsala-KYT-Callback

When a `callbackUrl` is provided in the KYT Search request ([05-Uppsala-KYT-Search.md](04-Uppsala-KYT-Search.md)), the analysis result is delivered via POST to that URL upon completion or failure.

> **Note**: This API is jointly operated by CodeVASP and Uppsala Security. For inquiries or access requests, please contact [partnership@codevasp.com](mailto:partnership@codevasp.com).

> **Important**: To receive callbacks, the CodeVASP server IP must be whitelisted in your firewall or ACL:
> - Development: `3.35.100.55/32`, `13.209.222.19/32`, `211.245.36.156/32`
> - Production: `3.37.135.89/32`

## Callback Trigger Conditions

- A callback is sent **only** when `callbackUrl` was specified in the `/kytsearch` request.
- The callback fires once when the analysis status becomes `RELEASED` or `FAILED`.
- No callback is sent while the status is `PENDING`.

## Callback Request Spec

| Item | Value |
| :--- | :--- |
| Method | `POST` |
| Content-Type | `application/json` |
| URL | The `callbackUrl` specified in the `/kytsearch` request. |

## Callback Body

The callback body has the same structure as the KYT Report API ([06-Uppsala-KYT-Report.md](05-Uppsala-KYT-Report.md)) response.

### Fields
| Name | Type | Nullable | Description |
| :--- | :--- | :------- | :---------- |
| requestId | integer | No | The search request ID. |
| status | string | No | `RELEASED` or `FAILED`. |
| error | string | Yes | Error description. Present only when status is `FAILED`. |
| submittedAt | datetime | No | Timestamp when the analysis was requested. |
| report | object | Yes | Full analysis report. Present only when status is `RELEASED`. See [06-Uppsala-KYT-Report.md](05-Uppsala-KYT-Report.md) for the full report object specification. |

## Examples

### Callback Body (FAILED)
```json
{
  "requestId": 5713,
  "status": "FAILED",
  "error": "Tx hash not found",
  "submittedAt": "2026-03-24T09:02:52",
  "report": null
}
```

### Callback Body (RELEASED)
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
      }
    },
    "byToken": {
      "ETH": { "total": 0.01305, "malicious": 0, "suspicious": 0 }
    }
  }
}
```

## Notes

- Callbacks are sent asynchronously. There is no retry on failure — the callback is attempted once regardless of the receiver's response status.
- The callback endpoint must use HTTPS only.
- For the full `report` object field descriptions, refer to [06-Uppsala-KYT-Report.md](05-Uppsala-KYT-Report.md).
