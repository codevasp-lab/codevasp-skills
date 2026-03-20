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

## Folder Architecture & Resource Map

Before responding to user queries, consult the appropriate resources in the `references/` directory. Be explicit in your responses about which file or standard you are referencing.

### 1. Guides (`references/guides/`)
- **1-General/**
  - `01-Integration-Process.md`: Step-by-step CodeVASP onboarding.
  - `02-Transaction-Flow.md`: Detailed visual and textual transaction flows.
  - `03-General-FAQ.md`: Comprehensive list of common integration and policy questions.
  - `04_Technical-FAQ.md`: Technical FAQ.
- **2-Development/**
  - `00-Communication-Scenarios.md`: High-level interaction diagrams.
  - `01-Dev-Environment-Setup.md`: Initial technical configuration.
  - `02-Encryption-&-Decryption.md`: Detailed logic for secured payloads.
  - `03-Header-Parameter.md`: Reference for mandatory HTTP headers.
  - `04-IVMS101-part1.md`: IVMS101 base objects and structures.
  - `04-IVMS101-part2.md`: Advanced IVMS101 fields and validation rules.
  - `04-IVMS101-part3.md`: Additional IVMS101 details.
  - `05-Verifying-Wallet-Address.md`: Logic for address validation.
  - `06-Verify-Names.md`: Guidelines for KYC name matching.
  - `07-Developing-the-Response-Process.md`: VASP-side response implementation.
  - `08-Developing-the-Request-Process.md`: VASP-side request implementation.
  - `09-Asset-Transfer-Status-Management.md`: State machine for transfers.
  - `10-Returning-Errors.md`: Error code reference and handling.
  - `11-Interoperability-with-Other-Protocols.md`: Cross-protocol (GTR, etc.) guidance.
  - `12-Go-Live-Preparation.md`: Final checklist for production deployment.
- **3-Corporate-Travel-Rule/**
  - `01-Policy.md`: Policy framework for corporate accounts.
  - `02-Comprehensive-Guide.md`: End-to-end integration for Legal Entities.
  - `03-Creating-Travel-Rule-Objects.md`: Constructing corporate IVMS101 objects.
  - `04-Communicating-With-Other-Protocols.md`: Corporate interoperability details.

### 2. API Reference (`references/api/`)
- **01-Intro/**
  - `CodeVASP-Introduction.md`
- **02-CodeVASP-Cipher/**
  - `02-1.-CodeVASP-Cipher-Server-Module-Guide.md`
  - `02-2.-Create-Header-Payload-1.Core.md`
  - `02-2.-Create-Header-Payload-2.Addon.md`
  - `02-3.-Encrypt.md`
  - `02-4.-Decrypt.md`
  - `02-5.-Health-Check.md`
- **03-Request-API/**
  - `03-01.-VASP-List-Search.md`
  - `03-02.-Public-Key-Search.md`
  - `03-03.-Networks-by-Coin.md`
  - `03-04.-Search-VASP-by-Wallet-Request.md`
  - `03-05.-Search-VASP-by-Wallet-Result.md`
  - `03-06.-Virtual-Asset-Address-Search.md`
  - `03-07.-Asset-Transfer-Authorization.md`
  - `03-08.-Report-Transfer-Result.md`
  - `03-09.-Transaction-Status-Search.md`
  - `03-10.-Finish-Transfer.md`
  - `03-11.-Search-VASP-by-TXID-Request.md`
  - `03-12.-Search-VASP-by-TXID-Result.md`
  - `03-13.-Asset-Transfer-Data-Request.md`
- **04-Response-API/**
  - `04-1.-Getting-Started.md`
  - `04-2.-Virtual-Asset-Address-Search.md`
  - `04-3.-Asset-Transfer-Authorization.md`
  - `04-4.-Report-Transfer-Result.md`
  - `04-5.-Transaction-Status-Search.md`
  - `04-6.-Finish-Transfer.md`
  - `04-7.-Search-VASP-by-TXID.md`
  - `04-8.-Asset-Transfer-Data-Request.md`
  - `04-9.-Health-Check.md`

### 3. Examples (`references/examples/`)
- `asset-transfer-authorization-request-legal-2-legal-example.json`
- `asset-transfer-authorization-request-legal-2-natural-example.json`
- `asset-transfer-authorization-request-natural-2-legal-example.json`
- `asset-transfer-authorization-request-natural-2-natural-example.json`
- `asset-transfer-authorization-response-legal-2-legal-example.json`
- `asset-transfer-authorization-response-legal-2-natural-example.json`
- `asset-transfer-authorization-response-natural-2-legal-example.json`
- `asset-transfer-authorization-response-natural-2-natural-example.json`
- `complete-example-legal-person.json`
- `complete-example.json`

### 4. Implementation Samples (`references/samples/`)
- **nodejs/**: Example code for authentication, encryption, and API calls in JavaScript.
- **python/**: Example code for authentication, encryption, and API calls in Python.
- **java/**: Example code for authentication, encryption, and API calls in Java.
- **go/**: Example code for authentication, encryption, and API calls in Go.

### 5. Schemas (`references/schemas/`)
- `json-schema.json`: The definitive JSON schema for IVMS101 validation.

---

## Instructions

When the user requests assistance with CodeVASP integrations, adhere rigorously to the following workflows:

### Workflow 1: Answering FAQ & Conceptual Questions
1. Always start by scanning `references/guides/1-General/03_General-FAQ.md` and the IVMS101 guides in `2-Development/` to find authoritative answers.
2. If the user asks about rules, alliances, or standards, retrieve the exact details (and any provided URLs) from the guides.
3. The basic implementation for CodeVASP Travel rule standard process is with natural person (KYC) and legal person (KYB, Corporate) accounts.
4. Keep answers concise. If a topic has an example, point the developer to it instead of explaining extensively.

### Workflow 2: Implementing the CodeVASP Protocol
If a developer asks how to implement a specific request (e.g., "How do I authorize a transfer from a Legal Person to a Natural Person?"):
This skill supports VASP developers working on existing projects. As this will become part of their current software stack, the AI agent must maintain consistency with the existing codebase. Its goal is to implement the CodeVASP protocol within the VASP's deposit and withdrawal modules.
1. **Explain the Flow**: Detail the conceptual flow using the relevant guides in `2-Development/`. Note specific caveats (e.g., Originator VASPs may only have partial info about Beneficiaries initially).
2. **Provide Templates**: Pull the exact, corresponding JSON template from the `references/examples/` directory.
3. **Step-by-Step Instructions**: Guide the developer on populating mandatory fields accurately using the API reference in `references/api/`.
4. **Provide Code Samples**: Identify the user's preferred language and pull the corresponding boilerplate from `references/samples/` (e.g., provide Node.js snippets if requested).

### Workflow 3: JSON Payload Validation
If the user provides a JSON payload to validate:
1. **Schema Check**: Compare the payload deterministically against `references/schemas/json-schema.json`.
2. **Rule Check**: Verify specific fields against the CodeVASP rules in the IVMS101 guides (e.g., string encoding must be UTF-8, case-insensitive values, etc.).
3. **Provide Feedback**: Identify errors with line-item precision.

## Compliance Constraints
- **Do not invent instructions.** If something is not covered in the `references/` directories, inform the user that you cannot verify that specific detail and they should check the official CodeVASP Alliance documentation.
- Always refer to the network strictly as **CodeVASP**.
