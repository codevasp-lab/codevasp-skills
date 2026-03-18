---
name: codevasp-travel-rule-guide
description: Expert guidance on Travel Rule compliance, CodeVASP API integration, and IVMS101 data structures. Use for VASP discovery, transfer authorization, FAQ, and IVMS101 payload generation/validation.
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
- **1_General/**
  - `01_Integration_Process.md`: Step-by-step CodeVASP onboarding.
  - `02_Transaction_Flow.md`: Detailed visual and textual transaction flows.
  - `03_FAQ.md`: Comprehensive list of common integration and policy questions.
- **2_Development/**
  - `Dev 00 - Communication Scenarios.md`: High-level interaction diagrams.
  - `Dev 01 - Dev Environment Setup.md`: Initial technical configuration.
  - `Dev 02 - Encryption & Decryption.md`: Detailed logic for secured payloads.
  - `Dev 03 - Header Parameter.md`: Reference for mandatory HTTP headers.
  - `Dev 04 - IVMS101-part1.md`: IVMS101 base objects and structures.
  - `Dev 04 - IVMS101-part2.md`: Advanced IVMS101 fields and validation rules.
  - `Dev 05 - Verifying Wallet Address.md`: Logic for address validation.
  - `Dev 06 - Verify Names.md`: Guidelines for KYC name matching.
  - `Dev 07 - Developing the Response Process.md`: VASP-side response implementation.
  - `Dev 08 - Developing the Request Process.md`: VASP-side request implementation.
  - `Dev 09 - Asset Transfer Status Management.md`: State machine for transfers.
  - `Dev 10 - Returning Errors.md`: Error code reference and handling.
  - `Dev 11 - Interoperability with Other Protocols.md`: Cross-protocol (GTR, etc.) guidance.
  - `Dev 12 - Go-Live Preparation.md`: Final checklist for production deployment.
- **3_Corporate_Travel_Rule/**
  - `01_Policy.md`: Policy framework for corporate accounts.
  - `02_Comprehensive_Guide.md`: End-to-end integration for Legal Entities.
  - `03_Creating_Travel_Rule_Objects.md`: Constructing corporate IVMS101 objects.
  - `04_Communicating_With_Other_Protocols.md`: Corporate interoperability details.

### 2. API Reference (`references/api/`)
- **API 01 - Intro/**
  - `CodeVASP Introduction.md`
- **API 02 - CodeVASP-Cipher/**
  - `02-1. CodeVASP-Cipher Server Module Guide.md`
  - `02-2. Create Header, Payload.md`
  - `02-3. Encrypt.md`
  - `02-4. Decrypt.md`
  - `02-5. Health Check.md`
- **API 03 - Request API/**
  - `03-1. VASP List Search.md`
  - `03-2. Public Key Search.md`
  - `03-3. Networks by Coin.md`
  - `03-4. Search VASP by Wallet Request.md`
  - `03-5. Search VASP by Wallet Result.md`
  - `03-6. Virtual Asset Address Search.md`
  - `03-7. Asset Transfer Authorization.md`
  - `03-8. Report Transfer Result (TX Hash).md`
  - `03-9. Transaction Status Search.md`
  - `03-10. Finish Transfer.md`
  - `03-11. Search VASP by TXID Request.md`
  - `03-12. Search VASP by TXID Result.md`
  - `03-13. Asset Transfer Data Request.md`
- **API 04 - Response API/**
  - `04-1. Getting Started.md`
  - `04-2. Virtual Asset Address Search.md`
  - `04-3. Asset Transfer Authorization.md`
  - `04-4. Report Transfer Result (TX Hash).md`
  - `04-5. Transaction Status Search.md`
  - `04-6. Finish Transfer.md`
  - `04-7. Search VASP by TXID.md`
  - `04-8. Asset Transfer Data Request.md`
  - `04-9. Health Check.md`
- **API 05 - More Services/**
  - `05-1. Uppsala Wallet Screening.md`
  - `05-2. Uppsala TXID Screening.md`
- **API 06 - Unhosted Wallet/**
  - `06-1. Intro.md`
  - `06-2. Issue Token.md`
  - `06-3. Render Widget.md`
  - `06-4. Get Result.md`

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
1. Always start by scanning `references/guides/1_General/03_FAQ.md` and the IVMS101 guides in `2_Development/` to find authoritative answers.
2. If the user asks about rules, alliances, or standards, retrieve the exact details (and any provided URLs) from the guides.
3. Keep answers concise. If a topic has an example, point the developer to it instead of explaining extensively.

### Workflow 2: Implementing the CodeVASP Protocol
If a developer asks how to implement a specific request (e.g., "How do I authorize a transfer from a Legal Person to a Natural Person?"):
1. **Explain the Flow**: Detail the conceptual flow using the relevant guides in `2_Development/`. Note specific caveats (e.g., Originator VASPs may only have partial info about Beneficiaries initially).
2. **Provide Templates**: Pull the exact, corresponding JSON template from the `references/examples/` directory.
3. **Step-by-Step Instructions**: Guide the developer on populating mandatory fields accurately using the API reference in `references/api/`.
4. **Provide Code Samples**: Identify the user's preferred language and pull the corresponding boilerplate from `references/samples/` (e.g., provide Node.js snippets if requested).

### Workflow 3: JSON Payload Validation
If the user provides a JSON payload to validate:
1. **Schema Check**: Compare the payload deterministically against `references/schemas/json-schema.json`.
2. **Rule Check**: Verify specific fields against the CodeVASP rules in the IVMS101 guides (e.g., string encoding must be UTF-8, case-insensitive values, etc.).
3. **Provide Feedback**: Identify errors with line-item precision.

### Workflow 4: Scripts & Automation
1. If the user provides a JSON payload and automation scripts exist in the `scripts/` directory, utilize them to guarantee 100% verification accuracy.

## Compliance Constraints
- **Do not invent instructions.** If something is not covered in the `references/` directories, inform the user that you cannot verify that specific detail and they should check the official CodeVASP Alliance documentation.
- Always refer to the network strictly as **CodeVASP**.
