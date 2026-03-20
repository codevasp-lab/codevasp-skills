---
name: codevasp-uppsala-screening-guide
description: Expert guidance on CodeVASP Uppsala Screening API integration. Use for wallet address risk detection and transaction ID (TXID) risk screening via Uppsala Security.
metadata: 
  tags: ["codevasp", "uppsala", "screening", "compliance", "AML"]
  author: "CodeVASP"
  version: "1.0.0"
  
---

# CodeVASP Uppsala Screening Integration Guide

## Overview

This skill provides the AI agent with the necessary procedures, references, and validation instructions to assist developers integrating with the CodeVASP Uppsala Screening APIs. These APIs, jointly operated by CodeVASP and Uppsala Security, detect risks associated with **wallet addresses** and **transaction IDs (TXIDs)**, returning risk levels (`BLACK`, `GRAY`, `WHITE`, `UNKNOWN`) and security tags for flagged items.

> **Important**: The Uppsala Screening APIs do **not** support the development environment. Only the production environment is available.

## Folder Architecture & Resource Map

Before responding to user queries, consult the appropriate resources in the `references/` directory. Be explicit in your responses about which file or standard you are referencing.

### 1. Guides (`references/guides/`)
- **Development/**
  - `01-Dev-Environment-Setup.md`: Dashboard onboarding, dev server registration (key pair generation), and IP whitelisting procedures.
  - `03-Header-Parameter.md`: Detailed specification of mandatory HTTP headers (`X-Code-Req-Nonce`, `X-Code-Req-PubKey`, `X-Code-Req-Signature`, `X-Code-Req-Datetime`, `X-Request-Origin`).

### 2. API Reference (`references/api/`)

- **Uppsala-Screening/**
  - `1-Uppsala-Wallet-Screening.md`: API for screening a wallet address for risk. Returns `securityCategory` and `securityTags`.
  - `2-Uppsala-TXID-Screening.md`: API for screening a blockchain transaction ID for risk. Returns sender/receiver wallet risk info with `securityCategory`, `securityTags`, and `detailedDescription`.

---

## Instructions

When the user requests assistance with CodeVASP Uppsala Screening integrations, adhere rigorously to the following workflows:

### Workflow 1: Answering FAQ & Conceptual Questions
1. Always start by scanning `references/api/Uppsala-Screening/1-Uppsala-Wallet-Screening.md` and `2-Uppsala-TXID-Screening.md`, as well as the guides in `references/guides/Development/`, to find authoritative answers.
2. If the user asks about risk levels, explain the four security categories: `BLACK` (highly suspicious), `GRAY` (suspicious), `WHITE` (normal), `UNKNOWN` (unknown).
3. If the user asks about supported chains, refer to the chain list: BTC, ETH, SOL, LTC, TRX, EOS, XLM, ADA, BNB, BCH, XRP, BSC, KLAY, DASH, DOGE, ZEC, FTM, MATIC, AVAX.
4. If the user asks about environment availability, clarify that Uppsala Screening APIs are **production only** — no dev environment is supported.
5. For access requests or inquiries, direct the user to [partnership@codevasp.com](mailto:partnership@codevasp.com).
6. Keep answers concise. Point the developer to the relevant API reference file instead of explaining extensively.

### Workflow 2: Implementing Uppsala Screening
If a developer asks how to implement wallet or TXID screening:
This skill supports VASP developers working on existing projects. The AI agent must maintain consistency with the existing codebase. Guide the developer through the following steps:

1. **Prerequisites & Setup** (Ref: `01-Dev-Environment-Setup.md`, `03-Header-Parameter.md`):
   - Follow the dashboard onboarding and dev server registration steps in `01-Dev-Environment-Setup.md` (account creation, server URL registration, key pair generation).
   - Configure mandatory headers per `03-Header-Parameter.md`: `X-Code-Req-PubKey`, `X-Code-Req-Signature`, `X-Code-Req-Datetime`, `X-Code-Req-Nonce`, `X-Request-Origin`.
   - Use the **production** host URL only: `https://trapi.codevasp.com`.
   - Whitelist IP addresses (both directions) as described in `01-Dev-Environment-Setup.md`.

2. **Wallet Screening** (Ref: `1.-Uppsala-Wallet-Screening.md`):
   - Call `POST /v1/code/uppsala/wallet` with body parameters: `walletAddress` (string, required) and `chain` (string, required — e.g., `"ETH"`).
   - Parse the response: check `result` (`NORMAL` or `ERROR`), read `securityCategory` for risk level, and inspect `securityTags` array for specific threat labels (e.g., "Gambling", "Malware", "Darknet").

3. **TXID Screening** (Ref: `2.-Uppsala-TXID-Screening.md`):
   - Call `POST /v1/code/uppsala/tx-hash` with body parameters: `txid` (string, required) and `chain` (string, required — e.g., `"ETH"`).
   - Parse the response: check `result`, then iterate over `senderWallets` and `receiverWallets` arrays. Each wallet object contains `walletAddress`, `securityCategory`, `securityTags`, and `detailedDescription`.

### Workflow 3: Request Payload Validation
If the user provides a request payload to validate for either screening API:
1. **Field Check**: Verify all required fields are present:
   - Wallet Screening: `walletAddress`, `chain` (per `1.-Uppsala-Wallet-Screening.md`).
   - TXID Screening: `txid`, `chain` (per `2.-Uppsala-TXID-Screening.md`).
2. **Chain Check**: Confirm the `chain` value matches one of the supported chains: BTC, ETH, SOL, LTC, TRX, EOS, XLM, ADA, BNB, BCH, XRP, BSC, KLAY, DASH, DOGE, ZEC, FTM, MATIC, AVAX.
3. **Provide Feedback**: Identify errors with line-item precision, including missing required fields or unsupported chain values.

## Compliance Constraints
- **Do not invent instructions.** If something is not covered in the `references/` directories, inform the user that you cannot verify that specific detail and they should check the official CodeVASP Alliance documentation.
- Always refer to the network strictly as **CodeVASP**.
