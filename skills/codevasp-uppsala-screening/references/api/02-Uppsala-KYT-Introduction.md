# 03-Uppsala-KYT-Introduction

Uppsala KYT enables members to request transaction risk analysis from the Uppsala Sentinel Protocol through CodeVASP, then retrieve the result through the KYT Report API or an optional callback.

* **Principles**: The integration starts with KYT Search ([05-Uppsala-KYT-Search.md](04-Uppsala-KYT-Search.md)), which creates an analysis request for a confirmed blockchain transaction. Results are then retrieved either by polling KYT Report ([06-Uppsala-KYT-Report.md](05-Uppsala-KYT-Report.md)) or by receiving a callback from KYT Callback ([07-Uppsala-KYT-Callback.md](06-Uppsala-KYT-Callback.md)).
* **Prerequisites**: To enable the Uppsala KYT feature, please contact the CodeVASP team at [partnership@codevasp.com](mailto:partnership@codevasp.com) or through your designated Slack channel.

# Integration Workflow

1. **Submit KYT Search**: Call `POST /v1/code/uppsala/kytsearch` with a confirmed `txHash`, the correct `blockchain`, and optional `force` / `callbackUrl` values.
2. **Track Processing Status**: If the response is `PENDING`, continue checking the same request through `GET /v1/code/uppsala/kytreport?requestId=...`.
3. **Receive the Final Result**: Consume the final `RELEASED` or `FAILED` result either from KYT Report polling or from the optional callback delivered to your webhook endpoint.

# Before Integration

### Headers
Every API request must include the following mandatory headers. These values should be generated using the CodeVASP Cipher or an equivalent encryption/decryption module.

* `X-Code-Req-PubKey`
* `X-Code-Req-Signature`
* `X-Code-Req-Datetime`
* `X-Code-Req-Nonce`
* `X-Request-Origin`

### Host
* Dev: [https://trapi-dev.codevasp.com](https://trapi-dev.codevasp.com)
* Prod: [https://trapi.codevasp.com](https://trapi.codevasp.com)

### Callback Endpoint
If you plan to use `callbackUrl`, create an HTTPS endpoint on your server that can receive `POST` requests with a JSON body matching the KYT Report response format.

If you restrict inbound access, please whitelist the following CodeVASP server IPs:

* Development: `3.35.100.55/32`, `13.209.222.19/32`, `211.245.36.156/32`
* Production: `3.37.135.89/32`

Callbacks are sent only once and are not retried, so we recommend keeping KYT Report polling as a fallback even when callbacks are enabled.

### Environment Notes
* Request/response schemas, required headers, and error formats are the same in development and production.
* Production environment supports the full blockchain list documented in [05-Uppsala-KYT-Search.md](04-Uppsala-KYT-Search.md).
* Development environment supports restricted blockchains and test cases documented in [04-Uppsala-KYT-Development-Environment.md](03-Uppsala-KYT-Development-Environment.md).
