# Integration Process

```mermaid
flowchart TD
    Phase1["1.CodeVASP DD"] -.-> Phase2["2.CodeVASP Dev Integration"]
    Phase2 -.-> Phase3["3.Member VASP Review"]

    Phase1 --> Step1_1["CodeVASP DD Assessment Complete"]

    Phase2 --> Step2_1["API Integration"]
    Step2_1 --> Step2_2["Dev env Tx/Rx Test"]
    Step2_2 --> Step2_3["Checklist Review"]
    Step2_3 --> Step2_4["Dev Integration Done (Prod Deployment)"]

    Phase3 --> Step3_1["VASP DD Assessment"]
    Step3_1 --> Step3_2["Assessment Passed"]
    Step3_2 --> Step3_3["Integration Complete🎉"]
```

## 1. CodeVASP DD
CodeVASP conducts our own due diligence on VASPs prior to integration to ensure regulatory compliance and the establishment of a reliable environment. The document submission process for DD is managed through CodeVASP's dashboard. Additionally, we support our member VASPs to carry DD Assessments and document sharing via the CodeVASP Dashboard.

## 2. CodeVASP Dev Integration
Once integration is complete, a transmission and reception test will be conducted in the development environment. For these tests, we verify that the API traffic is functioning properly and ensure that all conditions for Travel Rule compliance are met. Before going live, please review the Integration Checklist. Upon completion of these steps, the system will be deployed to the production environment, making it technically capable of communicating with the CodeVASP Travel Rule Alliance.

## 3. Member VASP Review
Completing the API integration process with CodeVASP does not automatically enable transactions with all member VASPs. For actual transactions to be enabled, each member VASP typically has an internal review process for connecting with newly onboarded VASPs. This process may include AML/CFT risk assessments, evaluations of business considerations, and reviews of development readiness and operational stability.

VASP entities operating under licensing regimes in regulated jurisdictions generally have their own due diligence procedures. The review process and timeline vary depending on each VASP’s internal policies, and the decision to enable transaction integration—along with its timing—is determined based on the outcome of each VASP’s due diligence.

```mermaid
graph LR
    %% Stage 1: CodeVASP DD
    subgraph S1 ["1.CodeVASP DD"]
        direction TB
        A1["CodeVASP DD<br/>Assessment Complete"]
    end

    %% Stage 2: CodeVASP Dev Integration
    subgraph S2 ["2.CodeVASP Dev Integration"]
        direction TB
        B1["API Integration"] --> B2["Dev env<br/>Tx/Rx Test"]
        B2 --> B3["Checklist Review"]
        B3 --> B4["Dev Integration Done<br/>(Prod Deployment)"]
    end

    %% Stage 3: Member VASP Review
    subgraph S3 ["3.Member VASP Review"]
        direction TB
        C1["VASP DD Assessment"] --> C2["Assessment Passed"]
        C2 --> C3["Integration Complete 🎉"]
    end

    %% Transitions between stages
    S1 -.-> S2
    S2 -.-> S3

    %% Text Mapping & Explanations
    %% Node A1: Initial due diligence (DD) assessment phase.
    %% Node B1-B4: Technical integration steps including API setup, testing, and production deployment.
    %% Node C1-C3: Final review and validation by the Member VASP to close the process.
```
