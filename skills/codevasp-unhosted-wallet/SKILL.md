---
name: codevasp-unhosted-wallet-guide
description: Expert guidance on CodeVASP Unhosted-Wallet Verification API integration. Use for wallet ownership verification via signature proof, widget rendering, and verification result retrieval.
metadata: 
  tags: ["codevasp", "unhosted-wallet", "compliance"]
  author: "CodeVASP"
  version: "1.0.0"
  
---

# CodeVASP Unhosted Wallet Integration Guide & Compliance Guide

## Overview

This skill provides the AI agent with the necessary procedures, references, and validation instructions to assist developers integrating with the CodeVASP Unhosted Wallet Verification system. It validates wallet ownership based on a **signature proof** from a user's personal (unhosted) wallet, using ECDSA (secp256k1) and keccak256 hashing to ensure cryptographically secure ownership claims.

## Documentation Source

API references are **not bundled** with this skill. They live at [docs.codevasp.com](https://docs.codevasp.com), which is the single source of truth. Fetch only the page(s) needed for the current question.

- **How to fetch**: Prefer `curl -s <url>` so you get the raw Markdown verbatim. If only a web-fetch tool is available, ask it to return the page content verbatim, not a summary — field names, required flags, and enum values must be exact.
- **Page not listed below?** Fetch `https://docs.codevasp.com/llms.txt` (index of every page) and use the entries under `## English` → `Unhosted Wallet`.
- **Fetch failed** (no network, blocked, 404): tell the user you could not load the official documentation and name the page you tried. Do not answer from memory as if it were verified.

## Resource Map

Be explicit in your responses about which page you are referencing.

### 1. API Reference (remote)
- [`01-Unhosted-Wallet-Introduction`](https://docs.codevasp.com/api/markdown/en/unhosted-wallet/api-reference/01-Unhosted-Wallet-Introduction): Overview of Unhosted Wallet Verification, integration workflow, required headers, host URLs, webhook setup, and supported networks.
- [`02-Issue-Token`](https://docs.codevasp.com/api/markdown/en/unhosted-wallet/api-reference/02-Issue-Token): API for issuing a one-time verification token and walletVerificationId.
- [`03-Render-Widget`](https://docs.codevasp.com/api/markdown/en/unhosted-wallet/api-reference/03-Render-Widget): Widget script loading, rendering instructions (Vanilla JS & React examples), and client event handling (success/error).
- [`04-Get-Result`](https://docs.codevasp.com/api/markdown/en/unhosted-wallet/api-reference/04-Get-Result): API for retrieving verification results using the Verification Session ID.

---

## Instructions

When the user requests assistance with CodeVASP Unhosted Wallet integrations, adhere rigorously to the following workflows:

### Workflow 1: Answering FAQ & Conceptual Questions
1. Always start by fetching `01-Unhosted-Wallet-Introduction` to find authoritative answers about the verification concept, prerequisites, supported networks, and required headers.
2. If the user asks about the verification flow, explain the three-step process: Token Issuance → Widget Execution → Result Processing.
3. If the user asks about supported networks, refer to the network list in `01-Unhosted-Wallet-Introduction` (ETH, ARBITRUM, BASE, KAIA, MATIC, SOL, BSC).
4. Keep answers concise. If a topic has an example, point the developer to the relevant API reference page instead of explaining extensively.

### Workflow 2: Implementing the Unhosted Wallet Verification Flow
If a developer asks how to implement unhosted wallet verification:
This skill supports VASP developers working on existing projects. The AI agent must maintain consistency with the existing codebase. Guide the developer through the following steps:

1. **Prerequisites & Setup** (Ref: `01-Unhosted-Wallet-Introduction`):
   - Ensure the Unhosted Wallet feature is activated by contacting the CodeVASP team.
   - Set up the appropriate host URL (Dev: `https://trapi-dev.codevasp.com`, Prod: `https://trapi.codevasp.com`).
   - Configure mandatory headers: `X-Code-Req-PubKey`, `X-Code-Req-Signature`, `X-Code-Req-Datetime`, `X-Code-Req-Nonce`, `X-Request-Origin`.

2. **Step 1 — Issue Token** (Ref: `02-Issue-Token`):
   - Call `POST /v1/code/unhosted-wallet-verification/widget/token` with required body parameters: `blockchain`, `asset`, `address`, `customerIdentification`, `widgetRenderingOrigin`, and optionally `callbackUrl`.
   - Store the returned `token` (one-time use, valid 24 hours) and `walletVerificationId` (unique session identifier).

3. **Step 2 — Render Widget** (Ref: `03-Render-Widget`):
   - Load the widget script (Dev: `https://wallet-verifier-dev.codevasp.com/widget/wallet-verifier.js`, Prod: `https://wallet-verifier.codevasp.com/widget/wallet-verifier.js`).
   - Create a `<wallet-verifier>` element with `data-token` and `data-language` attributes.
   - Identify the user's preferred framework and provide the corresponding example (Vanilla JS or React) from `03-Render-Widget`.

4. **Step 3 — Handle Client Events** (Ref: `03-Render-Widget`):
   - Listen for `verification-complete` event on success — contains `id`, `status`, `flow`, `address`, `asset`, `blockchain`.
   - Listen for `verification-error` event on failure — contains `errorCode` and `message`.
   - Handle error codes: `GENERAL_API_ERROR` (recommend refreshing widget or starting new session) and `CRYPTOGRAPHIC_SIGNATURE_FLOW_ERROR` (user cancelled).

5. **Step 4 — Get Verification Result** (Ref: `04-Get-Result`):
   - If a `callbackUrl` was configured, results are delivered automatically upon successful verification.
   - Alternatively, call `GET /v1/code/unhosted-wallet-verification/{verificationId}` to poll for results.
   - The response includes: `verificationId`, `status`, `blockchain`, `asset`, `address`, `signature`, `signedMessage`, `verifiedAt`.

### Workflow 3: JSON Payload Validation
If the user provides a request payload to validate for the Issue Token API:
1. **Field Check**: Verify all required fields are present (`blockchain`, `asset`, `address`, `customerIdentification`, `widgetRenderingOrigin`) against `02-Issue-Token`.
2. **Network Check**: Confirm the `blockchain` value matches one of the supported networks listed in `01-Unhosted-Wallet-Introduction`.
3. **Provide Feedback**: Identify errors with line-item precision, including missing required fields, invalid network values, or malformed URLs.

## Compliance Constraints
- **Do not invent instructions.** If something is not covered in the CodeVASP documentation (docs.codevasp.com), inform the user that you cannot verify that specific detail and they should check the official CodeVASP Alliance documentation.
- Always refer to the network strictly as **CodeVASP**.
