# 02_Comprehensive_Guide

## Originator: Sender's Data Type
```mermaid
graph LR
%% Scenario 1: Natural Person
	subgraph Originator1 [Originator1 - Sender is a natural person]
		direction LR
		O1((Originator)) -- "Sender account data bundle" --> OP1[originatorPersons]
		O1 -- "Sender account wallet address" --> AN1[accountNumber]
		OP1 -- "Individual user (KYC Info)" --> NP1(naturalPerson)
	end

%% Scenario 2: Legal Entity (Single CEO)
	subgraph Originator2 [Originator2 - Sender is a legal entity Single CEO]
		direction LR
		O2((Originator)) -- "Sender account data bundle" --> OP2[originatorPersons]
		O2 -- "Sender account wallet address" --> AN2[accountNumber]
		OP2 -- "Legal user (KYB info)" --> LP2(legalPerson)
		OP2 -- "CEO of the entity (KYB info)" --> NP2(naturalPerson)
	end

%% Scenario 3: Legal Entity (Multi CEO)
	subgraph Originator3 [Originator3 - Sender is a legal entity Multi CEO]
		direction LR
		O3((Originator)) -- "Sender account data bundle" --> OP3[originatorPersons]
		O3 -- "Sender account wallet address" --> AN3[accountNumber]
		OP3 -- "Legal user (KYB info)" --> LP3(legalPerson)
		OP3 -- "CEO 1 of the entity (KYB info)" --> NP3a(naturalPerson)
		OP3 -- "CEO 2 of the entity (KYB info)" --> NP3b(naturalPerson)
		NP3b -.-> Extra["Add as many 'naturalPerson' as needed"]
	end

%% Styling to match the original image colors
	style NP1 fill:#f8d7da,stroke:#842029
	style NP2 fill:#f8d7da,stroke:#842029
	style NP3a fill:#f8d7da,stroke:#842029
	style NP3b fill:#f8d7da,stroke:#842029
	style LP2 fill:#d1ecf1,stroke:#0c5460
	style LP3 fill:#d1ecf1,stroke:#0c5460
```
##  Beneficiary: Recipient's Data Type
```mermaid
graph LR
%% Scenario 1: Natural Person Recipient
	subgraph Beneficiary1 [Beneficiary1 - Recipient is a natural person]
		direction LR
		B1((Beneficiary)) -- "Recipient account data bundle" --> BP1[beneficiaryPersons]
		B1 -- "Recipient account wallet address" --> AN1[accountNumber]
		BP1 -- "User-entered info" --> NP1(naturalPerson)
	end

%% Scenario 2: Legal Entity Recipient (Single CEO)
	subgraph Beneficiary2 [Beneficiary2 - Recipient is a legal entity Single CEO]
		direction LR
		B2((Beneficiary)) -- "Recipient account data bundle" --> BP2[beneficiaryPersons]
		B2 -- "Recipient account wallet address" --> AN2[accountNumber]
		BP2 -- "User-entered info" --> LP2(legalPerson)
		BP2 -- "User-entered info" --> NP2(naturalPerson)
	end

%% Scenario 3: Legal Entity Recipient (Multi CEO)
	subgraph Beneficiary3 [Beneficiary3 - Recipient is a legal entity Multi CEO]
		direction LR
		B3((Beneficiary)) -- "Recipient account data bundle" --> BP3[beneficiaryPersons]
		B3 -- "Recipient account wallet address" --> AN3[accountNumber]
		BP3 -- "User-entered info" --> LP3(legalPerson)
		BP3 -- "User-entered info" --> NP3a(naturalPerson)
		BP3 -- "User-entered info" --> NP3b(naturalPerson)
		NP3b -.-> Extra["Add as many 'naturalPerson' as needed"]
	end

%% Styling to match the visual cues
	style NP1 fill:#f8d7da,stroke:#842029
	style NP2 fill:#f8d7da,stroke:#842029
	style NP3a fill:#f8d7da,stroke:#842029
	style NP3b fill:#f8d7da,stroke:#842029
	style LP2 fill:#d1ecf1,stroke:#0c5460
	style LP3 fill:#d1ecf1,stroke:#0c5460
```
## 'Legal to Legal' Deposit/Withdraw Guide
```mermaid
graph LR
    %% Main Bundle
    ivms101(["ivms101<br/>(Travel Rule messaging bundle)"])

    %% Originator Path (01)
    ivms101 --- Originator["Originator<br/>(Sender account data bundle)"]
    Originator --- originatorPersons["originatorPersons<br/>(Sender account info)"]
    originatorPersons --- OP_LP["legalPerson<br/>(Legal entity (KYB info))"]
    originatorPersons --- OP_NP1["naturalPerson<br/>(CEO 1 of the entity(KYB info))"]
    originatorPersons --- OP_NP2["naturalPerson<br/>(CEO 2 of the entity(KYB info))"]
    Originator --- OP_AN["accountNumber<br/>(Sender account wallet address)"]

    %% Beneficiary Path (02)
    ivms101 --- Beneficiary["Beneficiary<br/>(Recipient account data bundle)"]
    Beneficiary --- beneficiaryPersons["beneficiaryPersons<br/>(Recipient account info)"]
    beneficiaryPersons --- BP_LP["legalPerson<br/>(User-entered info)"]
    beneficiaryPersons --- BP_NP1["naturalPerson<br/>(User-entered info)"]
    beneficiaryPersons --- BP_NP2["naturalPerson<br/>(User-entered info)"]
    Beneficiary --- BP_AN["accountNumber<br/>(Recipient account wallet address)"]

    %% VASP Info
    ivms101 --- OriginatingVASP["OriginatingVASP<br/>(Origin VASP legal entity info)"]
    ivms101 --- BeneficiaryVASP["BeneficiaryVASP<br/>(Beneficiary VASP legal entity info)"]

    %% Styling to match image
    style ivms101 fill:#fff,stroke:#333,stroke-width:2px
    style OP_LP fill:#e0f7fa,stroke:#00acc1
    style BP_LP fill:#e0f7fa,stroke:#00acc1
    style OP_NP1 fill:#fce4ec,stroke:#d81b60
    style OP_NP2 fill:#fce4ec,stroke:#d81b60
    style BP_NP1 fill:#fce4ec,stroke:#d81b60
    style BP_NP2 fill:#fce4ec,stroke:#d81b60

```
### To do when withdraw
1. Acquire needed information
	1. The originator information is populated using the data stored in our database, which was obtained during the KYB process.
	2. The 'dateOfBirth' field under the 'naturalPerson' of the originator entity is not required. The recipient information is obtained from the customer.
	3. The 'dateOfBirth' field under the recipient entity's 'naturalPerson' is not required.
	4. Required data may differ by the counterparty VASP's policy. Check their policy before sending legal entity Travel Rule information.
2. Check for counterparty VASP's policy
	1. Transfers involving legal entity may differ by country regulations and each VASP's internal policy.
		1. e.g) Korean regulation: Beneficiary  CEO('naturalPerson') info under 'legalPerson' of Beneficiary is also required  
		2.  e.g) VASP policy: Some VASPs allow transfers between 1st-party only. 
		3.  e.g) VASP policy: Some VASPs may require all information of CEO for multi-CEO entity
### To do when deposit
1. Save sender account data
	1. Please store it to comply with the Travel Rule.
	2. Make sure it is properly mapped to data such as TxID or Transfer ID.
2. Legal entity check
	1. Verify if 'legalPerson' exists in the originator or beneficiary data.
3. Query user data
	1. Based on the accountNumber (recipient wallet address), retrieve from our legal entity customer database.  
	2. The data obtained through KYB during the customer's onboarding process must be stored in advance.
4. Check internal policy
	1. Verify whether our policy allows deposits for the 'legal to legal ' type.  
	2. Review which data fields must match (entity info, representative info, or both).  
	3. If multiple representatives exist, check whether all or at least one must match.
5. Verify beneficiary data and our user data
	1. Representative information of a legal entity is often obtained during the KYB process rather than through a separate KYC procedure.  
	2. Text mismatches may occur due to naming format differences
## 'Legal to Natural' Deposit/Withdraw Guide
```mermaid
graph LR
    %% Root Node
    ivms101(["ivms101<br/>(Travel Rule messaging bundle)"])

    %% Originator Branch (Legal Entity)
    ivms101 --- Originator["Originator<br/>(Sender account data bundle)"]
    Originator --- originatorPersons["originatorPersons<br/>(Sender account info)"]
    originatorPersons --- OP_LP["legalPerson<br/>(Legal user (KYB info))"]
    originatorPersons --- OP_NP1["naturalPerson<br/>(CEO 1 of the entity(KYB info))"]
    originatorPersons --- OP_NP2["naturalPerson<br/>(CEO 2 of the entity(KYB info))"]
    Originator --- OP_AN["accountNumber<br/>(Sender account wallet address)"]

    %% Beneficiary Branch (Natural Person)
    ivms101 --- Beneficiary["Beneficiary<br/>(Recipient account data bundle)"]
    Beneficiary --- beneficiaryPersons["beneficiaryPersons<br/>(Recipient account info)"]
    beneficiaryPersons --- BP_NP["naturalPerson<br/>(User-entered info)"]
    Beneficiary --- BP_AN["accountNumber<br/>(Recipient account wallet address)"]

    %% VASP Info
    ivms101 --- OriginatingVASP["OriginatingVASP<br/>(Origin VASP legal entity info)"]
    ivms101 --- BeneficiaryVASP["BeneficiaryVASP<br/>(Beneficiary VASP legal entity info)"]

    %% Styling
    style ivms101 fill:#fff,stroke:#333,stroke-width:2px
    style OP_LP fill:#e0f7fa,stroke:#00acc1
    style OP_NP1 fill:#fce4ec,stroke:#d81b60
    style OP_NP2 fill:#fce4ec,stroke:#d81b60
    style BP_NP fill:#fce4ec,stroke:#d81b60
```
### To do when withdraw
 1. Acquire needed information
	 1. The originator information is populated using the data stored in our database, which was obtained during the KYB process.
	 2. The 'dateOfBirth' field under the 'naturalPerson' of the originator entity is not required.
     The recipient information is obtained from the customer.
	 3. The 'dateOfBirth' field under the recipient entity's 'naturalPerson' is not required.
2. Check for counterparty VASP's policy
	1. Check if the counterparty accepts deposits from legal entities or only allows first-party transfers.
### To do when deposit
1. Save sender account data
	1. Please store it to comply with the Travel Rule.   
	2. Make sure it is properly mapped to data such as TxID or Transfer ID.
2. Legal entity check
	1. Verify if 'legalPerson' exists in the originator or beneficiary data.
3. Query user data
	1. Based on the accountNumber (recipient wallet address), retrieve from our legal entity customer database.
4. Check internal policy
	1. Verify whether our policy allows deposits for the 'legal to natural ' type.
5. Verify beneficiary data and our user data
	1. Verify if the Travel Rule recipient data matches our customer's KYC data.
	2. Adding a name-order switching logic (first ↔ last) can increase match accuracy.
## 'Natural to Legal' Deposit/Withdraw Guide
```mermaid
graph LR
    %% Main Root
    ivms101(["ivms101<br/>Travel Rule messaging bundle"])

    %% Originator Section (01)
    subgraph sg01 ["01"]
        Originator["Originator<br/>(Sender account data bundle)"]
        originatorPersons["originatorPersons<br/>(Sender account info)"]
        orig_naturalPerson["naturalPerson<br/>(Individual user (KYC info))"]
        orig_accountNumber["accountNumber<br/>(Sender account wallet address)"]

        Originator --> originatorPersons
        Originator --> orig_accountNumber
        originatorPersons --> orig_naturalPerson
    end

    %% Beneficiary Section (02)
    subgraph sg02 ["02"]
        Beneficiary["Beneficiary<br/>(Recipient account data bundle)"]
        beneficiaryPersons["beneficiaryPersons<br/>(Recipient account info)"]
        bene_legalPerson["legalPerson<br/>(User-entered info)"]
        bene_naturalPerson1["naturalPerson<br/>(User-entered info)"]
        bene_naturalPerson2["naturalPerson<br/>(User-entered info)"]
        bene_accountNumber["accountNumber<br/>(Recipient account wallet address)"]

        Beneficiary --> beneficiaryPersons
        Beneficiary --> bene_accountNumber
        beneficiaryPersons --> bene_legalPerson
        beneficiaryPersons --> bene_naturalPerson1
        beneficiaryPersons --> bene_naturalPerson2
    end

    %% VASP Info
    OriginatingVASP["OriginatingVASP<br/>(Origin VASP legal entity info)"]
    BeneficiaryVASP["BeneficiaryVASP<br/>(Beneficiary VASP legal entity info)"]

    %% Central Connections
    ivms101 --- Originator
    ivms101 --- Beneficiary
    ivms101 --- OriginatingVASP
    ivms101 --- BeneficiaryVASP

    %% Styling
    style ivms101 fill:#fff,stroke:#333,stroke-width:2px
    style orig_naturalPerson fill:#fce4ec,stroke:#d81b60
    style bene_naturalPerson1 fill:#fce4ec,stroke:#d81b60
    style bene_naturalPerson2 fill:#fce4ec,stroke:#d81b60
    style bene_legalPerson fill:#e0f7fa,stroke:#00acc1
    style OriginatingVASP fill:#e0f7fa,stroke:#00acc1
    style BeneficiaryVASP fill:#e0f7fa,stroke:#00acc1
    
    %% Subgraph Styling
    style sg01 fill:#fafafa,stroke:#999,stroke-width:1px
    style sg02 fill:#fafafa,stroke:#999,stroke-dasharray: 5 5
```
### To do when withdraw
1. Acquire needed information
	1. The originator information is populated using the data stored in our database, which was obtained during the KYB process.
	2. The 'dateOfBirth' field under the 'naturalPerson' of the originator entity is not required. The recipient information is obtained from the customer.
	3. The 'dateOfBirth' field under the recipient entity's 'naturalPerson' is not required.
	4. Required data may differ by the counterparty VASP's policy. Check their policy before sending legal entity Travel Rule information.
2. Check for counterparty VASP's policy
	1. Transfers involving legal entity may differ by country regulations and each VASP's internal policy.
		1. e.g) Korean regulation: Beneficiary  CEO('naturalPerson') info under 'legalPerson' of Beneficiary is also required  
		2. e.g)VASP policy: Some VASPs allow transfers between 1st-party only. 
		3. e.g)VASP policy: Some VASPs may require all information of CEO for multi-CEO entity
## Tips
### 'naturalPerson' Data Source
```mermaid
graph LR
    %% Scenario 1: Individual Originator
    subgraph S1 ["Individual Originator (Sender)"]
        direction LR
        O1["Originator<br/>(Sender account data bundle)"] --> OP1["originatorPersons<br/>(Sender account info)"]
        O1 --> AN1["accountNumber<br/>(Sender account wallet address)"]
        OP1 --> NP1["naturalPerson<br/>(Individual user (KYC Info))"]
        
        %% Note for S1
        Note1[KYC completed]
        NP1 -.-> Note1
    end

    %% Scenario 2: Legal Entity Originator
    subgraph S2 ["Legal Entity Originator (Sender)"]
        direction LR
        O2["Originator<br/>(Sender account data bundle)"] --> OP2["originatorPersons<br/>(Sender account info)"]
        O2 --> AN2["accountNumber<br/>(Sender account wallet address)"]
        OP2 --> LP2["legalPerson<br/>(Legal user (KYB info))"]
        OP2 --> NP2["naturalPerson<br/>(CEO of the entity (KYB info))"]
        
        %% Note for S2
        Note2[KYB completed]
        NP2 -.-> Note2
    end

    %% Scenario 3: Individual Beneficiary
    subgraph S3 ["Individual Beneficiary (Recipient)"]
        direction LR
        B3["Beneficiary<br/>(Recipient account data bundle)"] --> BP3["beneficiaryPersons<br/>(Recipient account info)"]
        B3 --> AN3["accountNumber<br/>(Recipient account wallet address)"]
        BP3 --> NP3["naturalPerson<br/>(User-entered info)"]
        
        %% Note for S3
        Note3[From the origin VASP's withdrawal UI]
        NP3 -.-> Note3
    end

    %% Scenario 4: Legal Entity Beneficiary
    subgraph S4 ["Legal Entity Beneficiary (Recipient)"]
        direction LR
        B4["Beneficiary<br/>(Recipient account data bundle)"] --> BP4["beneficiaryPersons<br/>(Recipient account info)"]
        B4 --> AN4["accountNumber<br/>(Recipient account wallet address)"]
        BP4 --> LP4["legalPerson<br/>(User-entered info)"]
        BP4 --> NP4["naturalPerson<br/>(User-entered info)"]
        
        %% Note for S4
        Note4[From the origin VASP's withdrawal UI]
        NP4 -.-> Note4
    end

    %% Global Styling
    classDef natural fill:#fce4ec,stroke:#d81b60,stroke-width:1px
    classDef legal fill:#e0f7fa,stroke:#00acc1,stroke-width:1px
    classDef bundle fill:#f5f5f5,stroke:#333,stroke-width:2px
    classDef note fill:none,stroke:none,font-style:italic

    class NP1,NP2,NP3,NP4 natural
    class LP2,LP4 legal
    class O1,O2,B3,B4 bundle
    class Note1,Note2,Note3,Note4 note

```
#### When the Originator is a 'naturlaPerson'
- The 'naturlaPerson' information comes from the KYC database
#### When the Originator is a 'legalPerson'
- The 'naturlaPerson' information comes from the KYB database
- When processing and saving the data: 
	- Collect both local and English names.
	- Store first name and last name separately.
	- Design a refined fallback process for name verification.
#### When the Beneficiary is a 'naturlaPerson'
- The 'naturlaPerson' information comes from the origin VASP's withdrawal UI
- It was entered by the user. 
#### When the Beneficiary is a 'legalPerson'
- The 'naturlaPerson' information comes from the origin VASP's withdrawal UI
- May require multiple name input fields
### 'naturalPerson' Date of Birth
```mermaid
graph LR
    %% Scenario 1: Individual Originator
    subgraph S1 ["Individual Originator (Sender)"]
        direction LR
        O1["Originator<br/>(Sender account data bundle)"] --> OP1["originatorPersons<br/>(Sender account info)"]
        O1 --> AN1["accountNumber<br/>(Sender account wallet address)"]
        OP1 --> NP1["naturalPerson<br/>(Individual user (KYC Info))"]
        NP1 --> DOB1["'dateOfBirth' <b style='color:red'>Required</b>"]
    end

    %% Scenario 2: Legal Entity Originator
    subgraph S2 ["Legal Entity Originator (Sender)"]
        direction LR
        O2["Originator<br/>(Sender account data bundle)"] --> OP2["originatorPersons<br/>(Sender account info)"]
        O2 --> AN2["accountNumber<br/>(Sender account wallet address)"]
        OP2 --> LP2["legalPerson<br/>(Legal user (KYB info))"]
        OP2 --> NP2["naturalPerson<br/>(CEO of the entity (KYB info))"]
        NP2 --> DOB2["'dateOfBirth' Optional"]
    end

    %% Scenario 3: Individual Beneficiary
    subgraph S3 ["Individual Beneficiary (Recipient)"]
        direction LR
        B3["Beneficiary<br/>(Recipient account data bundle)"] --> BP3["beneficiaryPersons<br/>(Recipient account info)"]
        B3 --> AN3["accountNumber<br/>(Recipient account wallet address)"]
        BP3 --> NP3["naturalPerson<br/>(User-entered info)"]
        NP3 --> DOB3["'dateOfBirth' Optional"]
    end

    %% Scenario 4: Legal Entity Beneficiary
    subgraph S4 ["Legal Entity Beneficiary (Recipient)"]
        direction LR
        B4["Beneficiary<br/>(Recipient account data bundle)"] --> BP4["beneficiaryPersons<br/>(Recipient account info)"]
        B4 --> AN4["accountNumber<br/>(Recipient account wallet address)"]
        BP4 --> LP4["legalPerson<br/>(User-entered info)"]
        BP4 --> NP4["naturalPerson<br/>(User-entered info)"]
        NP4 --> DOB4["'dateOfBirth' Optional"]
    end

    %% Styling to match the visual cues
    classDef natural fill:#fce4ec,stroke:#d81b60,stroke-width:1px
    classDef legal fill:#e0f7fa,stroke:#00acc1,stroke-width:1px
    classDef bundle fill:#f5f5f5,stroke:#333,stroke-width:2px
    classDef dob fill:#333,color:#fff,stroke:none

    class NP1,NP2,NP3,NP4 natural
    class LP2,LP4 legal
    class O1,O2,B3,B4 bundle
    class DOB1,DOB2,DOB3,DOB4 dob

```
