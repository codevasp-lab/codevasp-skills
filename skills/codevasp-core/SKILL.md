---
name: codevasp-travel-rule-guide
description: Expert guidance on Travel Rule compliance, CodeVASP Travel rule API integration, and IVMS101 data structures. Use for VASP discovery, transfer authorization, FAQ, and IVMS101 payload generation/validation.
metadata: 
  tags: ["codevasp", "travel-rule", "IVMS101", "compliance"]
  author: "CodeVASP"
  version: "1.0.0"
  
---

# CodeVASP Travel Rule Integration & Compliance Guide

## Overview
This skill provides the AI agent with the necessary procedures, references, and validation instructions to assist developers integrating with the CodeVASP network. It handles Travel Rule compliance, API implementation, FAQ responses, and detailed IVMS101 payload structuring.

## Documentation Source

Guides and API references are **not bundled** with this skill. They live at [docs.codevasp.com](https://docs.codevasp.com), which is the single source of truth. Fetch only the page(s) needed for the current question.

- **How to fetch**: Prefer `curl -s <url>` so you get the raw Markdown verbatim. If only a web-fetch tool is available, ask it to return the page content verbatim, not a summary — field names, required flags, and enum values must be exact.
- **Page not listed below?** Fetch `https://docs.codevasp.com/llms.txt` (index of every page) and use the entries under `## English` → `Travel Rule`.
- **Fetch failed** (no network, blocked, 404): tell the user you could not load the official documentation and name the page you tried. Do not answer from memory as if it were verified.

## Resource Map

Be explicit in your responses about which page or file you are referencing.

### 1. Guides (remote)

- **01-General/**
  - [`01-Integration-Process`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/01-General/01-Integration-Process): Step-by-step CodeVASP onboarding.
  - [`02-Communication-Scenarios`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/01-General/02-Communication-Scenarios): High-level interaction diagrams.
  - [`03-Transaction-Flow`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/01-General/03-Transaction-Flow): Detailed visual and textual transaction flows.
  - [`04-General-FAQ`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/01-General/04-General-FAQ): Common integration and policy questions.
  - [`05-Technical-FAQ`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/01-General/05-Technical-FAQ): Technical FAQ.
- **02-Development/**
  - [`01-Dev-Environment-Setup`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/01-Dev-Environment-Setup): Initial technical configuration.
  - [`02-Encryption-Decryption`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/02-Encryption-Decryption): Detailed logic for secured payloads.
  - [`03-Header-Parameter`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/03-Header-Parameter): Mandatory HTTP headers.
  - [`04-IVMS101-part1`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/04-IVMS101-part1): IVMS101 base objects and structures.
  - [`04-IVMS101-part2`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/04-IVMS101-part2): Advanced IVMS101 fields and validation rules.
  - [`04-IVMS101-part3`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/04-IVMS101-part3): Additional IVMS101 details.
  - [`05-Verifying-Wallet-Address`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/05-Verifying-Wallet-Address): Logic for address validation.
  - [`06-Verify-Names`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/06-Verify-Names): Guidelines for KYC name matching.
  - [`07-Developing-the-Response-Process`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/07-Developing-the-Response-Process): VASP-side response implementation.
  - [`08-Developing-the-Request-Process`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/08-Developing-the-Request-Process): VASP-side request implementation.
  - [`09-Asset-Transfer-Status-Management`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/09-Asset-Transfer-Status-Management): State machine for transfers.
  - [`10-Returning-Errors`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/10-Returning-Errors): Error code reference and handling.
  - [`11-CodeVASP-Cipher-Server-Module-Guide`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/11-CodeVASP-Cipher-Server-Module-Guide): Cipher Server module integration.
  - [`12-Interoperability-with-Other-Protocols`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/12-Interoperability-with-Other-Protocols): Cross-protocol (GTR, etc.) guidance.
  - [`13-Go-Live-Preparation`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/02-Development/13-Go-Live-Preparation): Final checklist for production deployment.
- **03-Corporate-Travel-Rule/**
  - [`01-Policy`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/03-Corporate-Travel-Rule/01-Policy): Policy framework for corporate accounts.
  - [`02-Comprehensive-Guide`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/03-Corporate-Travel-Rule/02-Comprehensive-Guide): End-to-end integration for Legal Entities.
  - [`03-Creating-Travel-Rule-Objects`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/03-Corporate-Travel-Rule/03-Creating-Travel-Rule-Objects): Constructing corporate IVMS101 objects.
  - [`04-Communicating-With-Other-Protocols`](https://docs.codevasp.com/api/markdown/en/travel-rule/guides/03-Corporate-Travel-Rule/04-Communicating-With-Other-Protocols): Corporate interoperability details.

### 2. API Reference (remote)
- **01-Intro/**
  - [`01-CodeVASP-Introduction`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/01-Intro/01-CodeVASP-Introduction)
- **02-Response-API/**
  - [`01-Getting-Started`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/01-Getting-Started)
  - [`02-Virtual-Asset-Address-Search`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/02-Virtual-Asset-Address-Search)
  - [`03-Asset-Transfer-Authorization`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/03-Asset-Transfer-Authorization)
  - [`04-Report-Transfer-Result`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/04-Report-Transfer-Result)
  - [`05-Transaction-Status-Search`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/05-Transaction-Status-Search)
  - [`06-Finish-Transfer`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/06-Finish-Transfer)
  - [`07-Search-VASP-by-TXID`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/07-Search-VASP-by-TXID)
  - [`08-Asset-Transfer-Data-Request`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/08-Asset-Transfer-Data-Request)
  - [`09-Health-Check`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/02-Response-API/09-Health-Check)
- **03-Request-API/**
  - [`01-VASP-List-Search`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/01-VASP-List-Search)
  - [`02-Public-Key-Search`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/02-Public-Key-Search)
  - [`03-Networks-by-Coin`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/03-Networks-by-Coin)
  - [`04-Search-VASP-by-Wallet-Request`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/04-Search-VASP-by-Wallet-Request)
  - [`05-Search-VASP-by-Wallet-Result`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/05-Search-VASP-by-Wallet-Result)
  - [`06-Virtual-Asset-Address-Search`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/06-Virtual-Asset-Address-Search)
  - [`07-Asset-Transfer-Authorization`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/07-Asset-Transfer-Authorization)
  - [`08-Report-Transfer-Result`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/08-Report-Transfer-Result)
  - [`09-Transaction-Status-Search`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/09-Transaction-Status-Search)
  - [`10-Finish-Transfer`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/10-Finish-Transfer)
  - [`11-Search-VASP-by-TXID-Request`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/11-Search-VASP-by-TXID-Request)
  - [`12-Search-VASP-by-TXID-Result`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/12-Search-VASP-by-TXID-Result)
  - [`13-Asset-Transfer-Data-Request`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/03-Request-API/13-Asset-Transfer-Data-Request)
- **04-CodeVASP-Cipher/**
  - [`01-Create-Header-Payload-1-Core`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/04-CodeVASP-Cipher/01-Create-Header-Payload-1-Core)
  - [`02-Create-Header-Payload-2-Addon`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/04-CodeVASP-Cipher/02-Create-Header-Payload-2-Addon)
  - [`03-Encrypt`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/04-CodeVASP-Cipher/03-Encrypt)
  - [`04-Decrypt`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/04-CodeVASP-Cipher/04-Decrypt)
  - [`05-Health-Check`](https://docs.codevasp.com/api/markdown/en/travel-rule/api-reference/04-CodeVASP-Cipher/05-Health-Check)

### 3. IVMS101 Examples (remote, [codevasp-lab/IVMS101](https://github.com/codevasp-lab/IVMS101))
Fetch these JSON files the same way as the docs pages. For IVMS101 concepts and message rules, also see [`README.md`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/README.md).
- [`asset-transfer-authorization-request-legal-2-legal-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-request-legal-2-legal-example.json)
- [`asset-transfer-authorization-request-legal-2-natural-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-request-legal-2-natural-example.json)
- [`asset-transfer-authorization-request-natural-2-legal-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-request-natural-2-legal-example.json)
- [`asset-transfer-authorization-request-natural-2-natural-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-request-natural-2-natural-example.json)
- [`asset-transfer-authorization-response-legal-2-legal-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-response-legal-2-legal-example.json)
- [`asset-transfer-authorization-response-legal-2-natural-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-response-legal-2-natural-example.json)
- [`asset-transfer-authorization-response-natural-2-legal-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-response-natural-2-legal-example.json)
- [`asset-transfer-authorization-response-natural-2-natural-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/asset-transfer-authorization-response-natural-2-natural-example.json)
- [`complete-example-legal-person.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/complete-example-legal-person.json)
- [`complete-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/complete-example.json)
- [`virtual-asset-address-search-request-example.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/virtual-asset-address-search-request-example.json)

### 4. Implementation Samples (local, `references/samples/`)
- **nodejs/**: Example code for authentication, encryption, and API calls in JavaScript.
- **python/**: Example code for authentication, encryption, and API calls in Python.
- **java/**: Example code for authentication, encryption, and API calls in Java.
- **go/**: Example code for authentication, encryption, and API calls in Go.

### 5. IVMS101 Schema (remote)
- [`json-schema.json`](https://raw.githubusercontent.com/codevasp-lab/IVMS101/main/json-schema.json): The definitive JSON schema for IVMS101 validation.

---

## Instructions

When the user requests assistance with CodeVASP integrations, adhere rigorously to the following workflows:

### Workflow 1: Answering FAQ & Conceptual Questions
1. Always start by fetching `04-General-FAQ` (and `05-Technical-FAQ` for technical questions) and, where relevant, the IVMS101 guides in `02-Development/` to find authoritative answers.
2. If the user asks about rules, alliances, or standards, retrieve the exact details (and any provided URLs) from the guides.
3. The basic implementation for CodeVASP Travel rule standard process is with natural person (KYC) and legal person (KYB, Corporate) accounts.
4. Keep answers concise. If a topic has an example, point the developer to it instead of explaining extensively.

### Workflow 2: Implementing the CodeVASP Protocol
If a developer asks how to implement a specific request (e.g., "How do I authorize a transfer from a Legal Person to a Natural Person?"):
This skill supports VASP developers working on existing projects. As this will become part of their current software stack, the AI agent must maintain consistency with the existing codebase. Its goal is to implement the CodeVASP protocol within the VASP's deposit and withdrawal modules.
1. **Explain the Flow**: Detail the conceptual flow using the relevant guides in `02-Development/`. Note specific caveats (e.g., Originator VASPs may only have partial info about Beneficiaries initially).
2. **Provide Templates**: Fetch the exact, corresponding JSON template from the IVMS101 examples listed above.
3. **Step-by-Step Instructions**: Guide the developer on populating mandatory fields accurately using the relevant API reference page.
4. **Provide Code Samples**: Identify the user's preferred language and pull the corresponding boilerplate from `references/samples/` (e.g., provide Node.js snippets if requested).

### Workflow 3: JSON Payload Validation
If the user provides a JSON payload to validate:
1. **Schema Check**: Fetch `json-schema.json` and validate the payload against it. If a JSON Schema validator is available (e.g. `python -m jsonschema`, `npx ajv-cli`), run it for a deterministic result; otherwise compare field by field.
2. **Rule Check**: Verify specific fields against the CodeVASP rules in the IVMS101 guides `04-IVMS101-part1`–`part3` (e.g., string encoding must be UTF-8, case-insensitive values, etc.).
3. **Provide Feedback**: Identify errors with line-item precision.

## Compliance Constraints
- **Do not invent instructions.** If something is not covered in the CodeVASP documentation (docs.codevasp.com), the IVMS101 repository, or the local `references/samples/`, inform the user that you cannot verify that specific detail and they should check the official CodeVASP Alliance documentation.
- Always refer to the network strictly as **CodeVASP**.
