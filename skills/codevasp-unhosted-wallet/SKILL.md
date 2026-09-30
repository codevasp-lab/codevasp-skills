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

This skill provides the AI agent with the necessary procedures, references, and validation instructions to assist developers integrating with the CodeVASP Unhosted Wallet Verification system. It validates wallet ownership based on a cryptographic **signature proof** from a user's personal (unhosted) wallet. See `01-Unhosted-Wallet-Introduction` for how the proof works.

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

When the user requests assistance with CodeVASP Unhosted Wallet integrations, adhere rigorously to the following workflows. Concrete values — host URLs, endpoint paths, headers, field lists, widget script URLs, event names, error codes, token validity, supported networks — are defined only in the documentation pages above. Always fetch the referenced page and quote values from it; do not rely on values remembered from earlier conversations.

### Workflow 1: Answering FAQ & Conceptual Questions
1. Always start by fetching `01-Unhosted-Wallet-Introduction` to find authoritative answers about the verification concept, prerequisites, supported networks, and required headers.
2. If the user asks about the verification flow, explain the three-step process: Token Issuance → Widget Execution → Result Processing.
3. If the user asks about supported networks, quote the list from `01-Unhosted-Wallet-Introduction`, including how its values map to request fields.
4. Keep answers concise. If a topic has an example, point the developer to the relevant API reference page instead of explaining extensively.

### Workflow 2: Implementing the Unhosted Wallet Verification Flow
If a developer asks how to implement unhosted wallet verification:
This skill supports VASP developers working on existing projects. The AI agent must maintain consistency with the existing codebase. Guide the developer through the following steps:

1. **Prerequisites & Setup** (Ref: `01-Unhosted-Wallet-Introduction`):
   - Confirm the feature activation prerequisite described on the page.
   - Take the host URLs for each environment and the mandatory headers from the page.

2. **Step 1 — Issue Token** (Ref: `02-Issue-Token`):
   - Build the request from the page's endpoint and body parameters (required and optional).
   - Store the returned token and verification session identifier, respecting the token's usage and validity rules on the page.

3. **Step 2 — Render Widget** (Ref: `03-Render-Widget`):
   - Load the widget script for the target environment and create the widget element with the attributes the page specifies.
   - Identify the user's preferred framework and provide the corresponding example (Vanilla JS or React) from the page.

4. **Step 3 — Handle Client Events** (Ref: `03-Render-Widget`):
   - Handle the success and error events, their payload fields, and each error code as described on the page, including the recommended recovery for each error.

5. **Step 4 — Get Verification Result** (Ref: `04-Get-Result`):
   - Explain both delivery paths: the callback (if configured at token issuance) and the result-retrieval API.
   - Parse the response fields as defined on the page.

### Workflow 3: JSON Payload Validation
If the user provides a request payload to validate for the Issue Token API:
1. **Field Check**: Fetch `02-Issue-Token` and verify all required fields are present and correctly typed.
2. **Network Check**: Confirm the `blockchain` value matches one of the supported networks listed in `01-Unhosted-Wallet-Introduction`.
3. **Provide Feedback**: Identify errors with line-item precision, including missing required fields, invalid network values, or malformed URLs.

## Compliance Constraints
- **Do not invent instructions.** If something is not covered in the CodeVASP documentation (docs.codevasp.com), inform the user that you cannot verify that specific detail and they should check the official CodeVASP Alliance documentation.
- Always refer to the network strictly as **CodeVASP**.
