# CodeVASP Compliance AI Skill

This repository contains an AI Agent Skill specifically designed to assist developers integrating with the **CodeVASP** network. 

By adding this skill to your AI assistant (like Claude Code, Cursor, or other agentic AI environments), the AI gains expert domain knowledge on Travel Rule compliance, VASP discovery, transfer authorizations, and IVMS101 JSON payload validation.

## What This Skill Can Do

Once loaded, you can ask your AI assistant to:
- **Answer general conceptual questions** about the CodeVASP Protocol and IVMS101 standard.
- **Provide JSON payload templates** for asset transfer requests/responses between Natural and Legal persons.
- **Provide functional code samples** in Node.js and Python for common integration tasks like header generation and payload encryption.
- **Instruct you on how to authorize transfers** and implement the specific API flows.
- **Validate your JSON payloads** against the official IVMS101 JSON Schema to deterministically catch structural errors or missing mandatory fields.

## How to Use This Skill

**Option 1: Using Vercel's `npx skills` (Recommended)**
If you use the `vercel-labs/skills` CLI ecosystem for AI agents, you can install this immediately by running:
```bash
npx skills add https://github.com/codevasp-lab/codevasp-skill
```

**Option 2: Manual Installation**
1. **Clone or Download** this repository to your local machine.
2. **Add it to your AI Environment**:
    * **Claude Code**: You can usually reference the directory or start a session inside the directory. The AI will read the `SKILL.md` file as part of its system prompt initialization.
    * **Other AI Agents**: Import or upload the folder contents. As long as the agent is capable of reading directory structures and system prompt files, it will utilize the `SKILL.md` instructions and read the files in the `references/` directory.

3. **Ask your AI a question**: Try giving your AI a prompt like:
    > "I need to send an asset transfer authorization from a Legal Person to a Natural Person according to CodeVASP. Can you provide the JSON payload and a Node.js snippet for the headers?"

## Folder Architecture

The skill is built using the "Domain-specific intelligence" pattern. 
- **`SKILL.md`**: The brain of the skill. This contains the triggers and the workflow instructions for the AI on *how* to use the references below.
- **`references/`**: The knowledge base.
  - **`guides/`**: Human-readable documentation (e.g., FAQ, IVMS101 guides) outlining the protocol rules.
  - **`schemas/`**: (`json-schema.json`) The strict deterministic schema used by the AI to validate your payloads.
  - **`examples/`**: Ready-to-use JSON templates for every combination of Natural and Legal person transfers.
  - **`samples/`**: Functional code snippets in Node.js, Python, Java, Go for integration logic.
  - **`api/`**: Detailed API reference endpoints for integration.

## Safety & "IP Whitelisting" Note

Because this skill operates on a "Fundamentals" approach — loading static schemas, templates, and guides into the agent's context rather than giving the agent direct MCP (Model Context Protocol) API connectivity — your developers can test their code securely. The AI does not require internet access or direct connectivity to your protected APIs to serve as an expert validator.
