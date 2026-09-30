---
name: codevasp-uppsala-screening-guide
description: Expert guidance on CodeVASP Uppsala Screening API integration. Use for wallet address risk detection and KYT (Know Your Transaction) analysis via Uppsala Security.
metadata: 
  tags: ["codevasp", "uppsala", "screening", "compliance", "AML", "KYT"]
  author: "CodeVASP"
  version: "2.0.0"
  
---

# CodeVASP Uppsala Screening Integration Guide

## Overview

This skill provides the AI agent with the necessary procedures, references, and validation instructions to assist developers integrating with the CodeVASP Uppsala Screening APIs. These APIs, jointly operated by CodeVASP and Uppsala Security, detect risks associated with **wallet addresses** and **blockchain transactions (KYT)**.

- **Wallet Screening**: Synchronous API returning risk levels (`BLACK`, `GRAY`, `WHITE`, `UNKNOWN`) and security tags.
- **KYT (Know Your Transaction)**: Asynchronous API returning a detailed analysis report with a verdict of `Clean`, `Suspicious`, or `Malicious`.

> **Important**: The Wallet Screening API does **not** support the development environment. The KYT API supports both development and production environments.

## Documentation Source

API references are **not bundled** with this skill. They live at [docs.codevasp.com](https://docs.codevasp.com), which is the single source of truth. Fetch only the page(s) needed for the current question.

- **How to fetch**: Prefer `curl -s <url>` so you get the raw Markdown verbatim. If only a web-fetch tool is available, ask it to return the page content verbatim, not a summary — field names, required flags, and enum values must be exact.
- **Page not listed below?** Fetch `https://docs.codevasp.com/llms.txt` (index of every page) and use the entries under `## English` → `Uppsala Screening`.
- **Fetch failed** (no network, blocked, 404): tell the user you could not load the official documentation and name the page you tried. Do not answer from memory as if it were verified.

## Resource Map

Be explicit in your responses about which page you are referencing.

### 1. API Reference (remote)
- [`01-Uppsala-Wallet-Screening`](https://docs.codevasp.com/api/markdown/en/uppsala-screening/api-reference/01-Uppsala-Wallet-Screening): API for screening a wallet address for risk. Returns `securityCategory` and `securityTags`.
- [`02-Uppsala-KYT-Introduction`](https://docs.codevasp.com/api/markdown/en/uppsala-screening/api-reference/02-Uppsala-KYT-Introduction): Overview of the KYT integration workflow, prerequisites, headers, host URLs, and callback IP whitelist.
- [`03-Uppsala-KYT-Development-Environment`](https://docs.codevasp.com/api/markdown/en/uppsala-screening/api-reference/03-Uppsala-KYT-Development-Environment): Supported networks, preset test cases, and response behaviors for the KYT dev environment.
- [`04-Uppsala-KYT-Search`](https://docs.codevasp.com/api/markdown/en/uppsala-screening/api-reference/04-Uppsala-KYT-Search): API for submitting a KYT analysis request. Returns `requestId` and initial `status`.
- [`05-Uppsala-KYT-Report`](https://docs.codevasp.com/api/markdown/en/uppsala-screening/api-reference/05-Uppsala-KYT-Report): API for polling the KYT analysis result. Returns full report with `verdict`, `riskIndicators`, `annotations`, and `byToken`.
- [`06-Uppsala-KYT-Callback`](https://docs.codevasp.com/api/markdown/en/uppsala-screening/api-reference/06-Uppsala-KYT-Callback): Callback specification delivered to `callbackUrl` when KYT analysis completes.

---

## Instructions

When the user requests assistance with CodeVASP Uppsala Screening integrations, adhere rigorously to the following workflows. Concrete values — host URLs, endpoint paths, field lists, supported chains, IP addresses, test data — are defined only in the documentation pages above. Always fetch the referenced page and quote values from it; do not rely on values remembered from earlier conversations.

### Workflow 1: Answering FAQ & Conceptual Questions
1. Always start by fetching the relevant page(s) to find authoritative answers.
2. **Wallet Screening risk levels** (Ref: `01-Uppsala-Wallet-Screening`): Explain the security categories and security tags as defined on the page.
3. **KYT verdicts** (Ref: `05-Uppsala-KYT-Report`): Explain the verdict values and risk indicators as defined on the page.
4. **Environment availability**: Wallet Screening and KYT differ in which environments they support. Check `01-Uppsala-Wallet-Screening` and `02-Uppsala-KYT-Introduction` for the supported environments and host URLs.
5. **KYT flow**: KYT is asynchronous — a search request returns a `requestId`, and the result is obtained by polling KYT Report or via an optional callback. See `02-Uppsala-KYT-Introduction` for the overview.
6. For access requests or inquiries, direct the user to [partnership@codevasp.com](mailto:partnership@codevasp.com).
7. Keep answers concise. Point the developer to the relevant API reference page instead of explaining extensively.

### Workflow 2: Implementing Uppsala Screening
This skill supports VASP developers working on existing projects. The AI agent must maintain consistency with the existing codebase.

#### 2a. Wallet Screening (Ref: `01-Uppsala-Wallet-Screening`)
1. Take the host URL, endpoint, mandatory headers, and request body fields from the page. Note the page's environment restriction before suggesting a host.
2. Parse the response as described on the page: the result code, the security category (risk level), and the security tags.

#### 2b. KYT (Know Your Transaction) (Ref: `02-Uppsala-KYT-Introduction`, `04-Uppsala-KYT-Search`, `05-Uppsala-KYT-Report`, `06-Uppsala-KYT-Callback`)
Guide the developer through the following three-step flow:

1. **Submit KYT Search** (Ref: `04-Uppsala-KYT-Search`):
   - Build the request from the page's body parameters, including its requirements on the transaction (e.g. confirmation state, matching chain).
   - Handle both initial statuses the page describes — including the case where a cached result is returned immediately and the report can be fetched right away.

2. **Poll KYT Report** (Ref: `05-Uppsala-KYT-Report`):
   - Poll with the `requestId` at the interval the page recommends until a terminal status is reached.
   - Parse the report fields (verdict, risk indicators, etc.) as defined on the page.

3. **Receive Callback (optional)** (Ref: `06-Uppsala-KYT-Callback`, `02-Uppsala-KYT-Introduction`):
   - Implement the callback receiver per the page's payload format.
   - Follow the page's delivery/retry behavior; if callbacks are not retried, keep KYT Report polling as a fallback.
   - Take the CodeVASP server IP addresses to whitelist from `02-Uppsala-KYT-Introduction`.

### Workflow 3: Request Payload Validation
If the user provides a request payload to validate:
1. Fetch the page for the API being called (`01-Uppsala-Wallet-Screening` or `04-Uppsala-KYT-Search`).
2. Verify all required fields are present and correctly typed.
3. Confirm the chain / blockchain value is in the supported list on that page (for KYT in the dev environment, also check `03-Uppsala-KYT-Development-Environment`).
4. Validate any URL fields (e.g. `callbackUrl`) against the page's requirements.
5. Provide feedback with line-item precision on missing fields, unsupported values, or invalid URLs.

### Workflow 4: KYT Development Environment Testing (Ref: `03-Uppsala-KYT-Development-Environment`)
If the user asks about testing KYT in the development environment:
1. Fetch the page and use its dev host, supported networks, and preset test cases (`blockchain` + `txHash` pairs) for each verdict.
2. Explain the page's behavior for inputs that are not preset test cases, and any dev-only limits such as `requestId` validity.

## Compliance Constraints
- **Do not invent instructions.** If something is not covered in the CodeVASP documentation (docs.codevasp.com), inform the user that you cannot verify that specific detail and they should check the official CodeVASP Alliance documentation.
- Always refer to the network strictly as **CodeVASP**.
