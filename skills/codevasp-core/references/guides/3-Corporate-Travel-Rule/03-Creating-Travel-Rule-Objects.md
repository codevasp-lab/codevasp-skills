# 03_Creating_Travel_Rule_Objects

* This guide is intended for developers handling Travel Rule data.
## 1. Getting Started
When a legal entity is involved as either the originator or the beneficiary, the structure of the Travel Rule data (messaging protocol IVMS101) changes accordingly.

Before implementing these changes, please review your internal policies related to legal transactions. At a minimum, ensure that your team is fully aligned on the points outlined in [Corporate Travel Rule Policy].

From here, we will go through the process in two parts — handling withdrawals and deposits.

## 2. As an originator
### 2-1. How to build originator object('originatorPersons')
#### Individual (KYC)
```mermaid
graph LR
    A("'originatorPersons'") --> B("'naturalPerson' info")
    
```
#### Legal Entity (KYB)
```mermaid
graph LR
    %% Scenario 1
    subgraph S1 ["For 1 representative"]
        direction LR
        O1["'originatorPersons'"] --> LP1["'legalPerson' info"]
        O1 --> NP1_1["'naturalPerson' info"]
        
        %% Annotations
        LP1 --- C1[corporate details]
        NP1_1 --- R1_1[rep1 details]
    end

    %% Scenario 2
    subgraph S2 ["For 2 representatives"]
        direction LR
        O2["'originatorPersons'"] --> LP2["'legalPerson' info"]
        O2 --> NP2_1["'naturalPerson' info"]
        O2 --> NP2_2["'naturalPerson' info"]
        
        %% Annotations
        LP2 --- C2[corporate details]
        NP2_1 --- R2_1[rep1 details]
        NP2_2 --- R2_2[rep2 details]
    end

    %% Scenario 3
    subgraph S3 ["For 3 representatives"]
        direction LR
        O3["'originatorPersons'"] --> LP3["'legalPerson' info"]
        O3 --> NP3_1["'naturalPerson' info"]
        O3 --> NP3_2["'naturalPerson' info"]
        O3 --> NP3_3["'naturalPerson' info"]
        
        %% Annotations
        LP3 --- C3[corporate details]
        NP3_1 --- R3_1[rep1 details]
        NP3_2 --- R3_2[rep2 details]
        NP3_3 --- R3_3[rep3 details]
    end

    %% Global Styling
    classDef originator fill:#eeeeee,stroke:#dddddd,stroke-width:1px
    classDef legal fill:#e0f7fa,stroke:#00acc1,stroke-width:1px
    classDef natural fill:#f1d3d3,stroke:#e57373,stroke-width:1px
    classDef annotation fill:none,stroke:none,font-style:italic,color:#666

    class O1,O2,O3 originator
    class LP1,LP2,LP3 legal
    class NP1_1,NP2_1,NP2_2,NP3_1,NP3_2,NP3_3 natural
    class C1,C2,C3,R1_1,R2_1,R2_2,R3_1,R3_2,R3_3 annotation

```

> [!NOTE] Name field: allowed characters
> - 'legalPerson': special characters and numbers are allowed  
> - 'naturalPerson': special characters and numbers are **not** allowed in names
* The 'originatorPersons' object is built using the corporate information collected during KYB.
* Under 'originatorPersons', include 'legalPerson' to hold corporate information and 'naturalPerson' for the representative's details.
* In cases where there are multiple representatives, include a separate 'naturalPerson' for each individual.
### 2-2. How to build beneficiary object('beneficiaryPersons')
#### Individual(User Input)
```mermaid
graph LR
    A("'beneficiaryPersons'") --> B("'naturalPerson' info")
    
```
#### Legal Entity (User Input)
```mermaid
graph LR
    %% Scenario 1
    subgraph S1 ["For 1 representative"]
        direction LR
        B1["'beneficiaryPersons'"] --> LP1["'legalPerson' info"]
        B1 --> NP1_1["'naturalPerson' info"]
        
        %% Annotations
        LP1 --- C1[corporate details]
        NP1_1 --- R1_1[rep1 details]
    end

    %% Scenario 2
    subgraph S2 ["For 2 representatives"]
        direction LR
        B2["'beneficiaryPersons'"] --> LP2["'legalPerson' info"]
        B2 --> NP2_1["'naturalPerson' info"]
        B2 --> NP2_2["'naturalPerson' info"]
        
        %% Annotations
        LP2 --- C2[corporate details]
        NP2_1 --- R2_1[rep1 details]
        NP2_2 --- R2_2[rep2 details]
    end

    %% Scenario 3
    subgraph S3 ["For 3 representatives"]
        direction LR
        B3["'beneficiaryPersons'"] --> LP3["'legalPerson' info"]
        B3 --> NP3_1["'naturalPerson' info"]
        B3 --> NP3_2["'naturalPerson' info"]
        B3 --> NP3_3["'naturalPerson' info"]
        
        %% Annotations
        LP3 --- C3[corporate details]
        NP3_1 --- R3_1[rep1 details]
        NP3_2 --- R3_2[rep2 details]
        NP3_3 --- R3_3[rep3 details]
    end

    %% Global Styling
    classDef beneficiary fill:#eeeeee,stroke:#dddddd,stroke-width:1px
    classDef legal fill:#e0f7fa,stroke:#00acc1,stroke-width:1px
    classDef natural fill:#f1d3d3,stroke:#e57373,stroke-width:1px
    classDef annotation fill:none,stroke:none,font-style:italic,color:#666

    class B1,B2,B3 beneficiary
    class LP1,LP2,LP3 legal
    class NP1_1,NP2_1,NP2_2,NP3_1,NP3_2,NP3_3 natural
    class C1,C2,C3,R1_1,R2_1,R2_2,R3_1,R3_2,R3_3 annotation
```
> [!NOTE] Name field: allowed characters
> - 'legalPerson': special characters and numbers are allowed  
> - 'naturalPerson': special characters and numbers are **not** allowed in names

- The beneficiary object is generally built using user-provided input.
- Please update the withdrawal UI to allow input of multiple representatives, in case there is more than one.
- Under beneficiaryPersons, include 'legalPerson' to store corporate information and 'naturalPerson' to capture the representative's details.
- Add a separate naturalPerson for each representative if there is more than one.

## 3. As a beneficiary
* Check your internal policy on whether transfers between natural persons and legal entities are allowed.
* The format of the originator and beneficiary objects may vary depending on the policy.
* Verify that the legal entity and representative information on 'your' platform matches the Beneficiary data for the Travel Rule before proceeding.
* If your platform has n representatives registered, the Travel Rule data must contain the same number of representative entries, and a full match is required. This is CodeVASP's recommended guideline.