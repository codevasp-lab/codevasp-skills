# 04-Uppsala-KYT-Development-Environment

This guide explains how to validate the Uppsala KYT integration in the development environment before going live.

The development environment provides fixed test cases, allowing users to verify cache-hit logic, asynchronous completion, callback delivery, and error handling.

* **Scope**: This document covers development-only supported networks, preset test cases, request retention policy, response behaviors, processing delays, callback timing, and test-environment-specific errors.
* **Related APIs**: 
  - KYT Search ([05-Uppsala-KYT-Search.md](04-Uppsala-KYT-Search.md)): Create requests. 
  - KYT Report ([06-Uppsala-KYT-Report.md](05-Uppsala-KYT-Report.md)): Poll analysis results. 
  - KYT Callback ([07-Uppsala-KYT-Callback.md](06-Uppsala-KYT-Callback.md)): Reference for when callbackUrl is enabled.

## Supported Networks

The development environment only supports the following networks:

| | |
| :--- | :--- |
| ETH | BTC |

Any other `blockchain` value will return a 400 UNSUPPORTED_BLOCKCHAIN error.

## Test Cases

Submit one of the following `(blockchain, txHash)` pairs to trigger a specific analysis result:

| Case | blockchain | txHash |
| :--- | :--- | :--- |
| Clean | `ETH` | `0x5028a06f62bd79cd44d96779c5d1db68882ede90c3cbf62b6cc11a6218984063` |
| Suspicious | `BTC` | `6db6a29832732e9c6f19ea8f85150ffe1deafa8412654b0be4f4a532a876fff6` |
| Malicious | `ETH` | `0x091d6e123c64e8ce9c0f39a9085d6adcf29cf8f49c888a95c128566550765298` |

Any pair not listed above will simulate a failed analysis, returning a `FAILED` status with the reason `"Tx hash not found"`.

The dummy report bodies for each preset follow the schema documented in [06-Uppsala-KYT-Report.md](05-Uppsala-KYT-Report.md).

## Response Behavior

The development environment follows these deterministic behaviors for KYT operations:

- Standard Search: If force is disabled and the request matches a preset, KYT Search returns a cached result path immediately.

- Forced Analysis: If force is enabled, KYT Search returns `PENDING`. The final result becomes available via KYT Report after a 3-second delay.

- Callback Flow: If `callbackUrl` is provided with `force=true`, the callback is dispatched approximately 3 seconds after the initial request.

- Error Simulation: If a `(blockchain, txHash)` pair is unsupported, KYT Search still initiates an asynchronous flow, but the status will update to `FAILED` (Reason: `"Tx hash not found"`) after 3 seconds.

## Request Retention

The requestId issued in the development environment is valid for 5 minutes.

Integrators must poll the result or verify the callback within this window. After 5 minutes, the requestId may no longer be available for status checks.

## Callback Timing

When `callbackUrl` is configured:

- Cached Results: Callbacks for cached paths follow the flow defined in [07-Uppsala-KYT-Callback.md](06-Uppsala-KYT-Callback.md).

- Asynchronous Results: For `force=true` or Unsupported cases, the callback is sent after a 3-second processing delay.