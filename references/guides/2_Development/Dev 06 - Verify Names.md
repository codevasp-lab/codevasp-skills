# Dev 06 - Verify Names

## General Rules

* General rule for name is to separate the surname and given name when entering the name of a natural person.
* Use PascalCase for the following objects: `Originator`, `Beneficiary`, `OriginatorVASP`, and `BeneficiaryVASP`. All other fields should follow camelCase.
* The field `primaryIdentifier` must always be present with a value, whereas `secondaryIdentifier` must always be present but may be left empty.
* Unless otherwise specified, all field values are case-insensitive.
* When validating names, whitespace should be removed before comparison.
* All field values must be expressed as UTF-8 encoded strings, including booleans, integers, and floats.
* Unless Korean is explicitly allowed, all values must be entered in English.
* If there is no official English name, the Korean name must be transliterated according to the official [Korean Romanization](https://www.korean.go.kr/front_eng/roman/roman_01.do).

## 1. Separating the First Name & Last Name
```mermaid
flowchart TD
    subgraph Example ["Example: Eugene Lee"]
        direction LR
        User((User Icon))
        Data["<b>Name: Eugene Lee</b><br/>VASP A: first name: 'LeeEugene', last name: ''<br/>VASP B: first name: 'EugeneLee', last name: ''<br/>VASP C: first name: 'Eugene', last name: 'Lee'"]
        User --- Data
    end

    subgraph Comparison ["Comparison Results"]
        direction TB
        
        %% Table Headers
        H_Empty[" "]
        H_FL["<b>First → Last</b>"]
        H_LF["<b>Last → First</b>"]

        %% Row 1: A vs B
        Row1_Label["<b>Fix A examine B</b>"]
        R1C1["LeeEugene vs EugeneLee<br/>(🔴 Mismatch)"]
        R1C2["LeeEugene vs EugeneLee<br/>(🔴 Mismatch)"]

        %% Row 2: A vs C
        Row2_Label["<b>Fix A examine C</b>"]
        R2C1["LeeEugene vs EugeneLee<br/>(🔴 Mismatch)"]
        R2C2["LeeEugene vs LeeEugene<br/>(🟢 Match)"]

        %% Layout Connections (Grid simulation)
        H_FL --- R1C1 --- R2C1
        H_LF --- R1C2 --- R2C2
        Row1_Label --- R1C1
        Row2_Label --- R2C1
    end

    %% Styling
    style R2C2 fill:#d2e9e9,stroke:#5fb8b8,stroke-width:2px
    style R1C1 fill:#f9d7d7,stroke:#f0a8a8
    style R1C2 fill:#f9d7d7,stroke:#f0a8a8
    style R2C1 fill:#f9d7d7,stroke:#f0a8a8
    style Comparison fill:#fcfcfc,stroke:#333
    style Example fill:#fcfcfc,stroke:#333

```
Since the components and order of names vary by country, VASPs (Virtual Asset Service Providers) often have different verification policies. Therefore, separating the surname and given name allows for flexibility in combining them in different orders, which can improve verification accuracy. If the surname and given name are treated as a single field, it becomes difficult to verify the name in cases where the order differs, as in the example above.

Therefore, if you are using an external KYC solution, it is recommended to verify how the user's name data is being handled, ensure that the surname and given name are separated, and, if applicable, also store the user's Local name.
## 2. Name Normalization
1. Split surname and given name 
2. Convert all alphabetic characters to lowercase
3. Remove special characters, numbers, and whitespace
4. Reverse the order of surname and given name

> [!NOTE]
> **Example** — Name: `Jean-Luc O'Connor`
> 
> | Step | Surname | Given Name |
> |------|---------|------------|
> | Original | `O'Connor` | `Jean-Luc` |
> | Lowercase | `o'connor` | `jean-luc` |
> | Remove special chars | `oconnor` | `jeanluc` |
> | Combined (both orders) | `jeanluc` + `oconnor` → **`jeanlucoconnor`** & **`oconnorjeanluc`** | |

Name verification may fail when communicating with certain Korean exchanges if names contain special characters, numbers, or spaces. To avoid any potential issues, it is recommended to remove all special characters, numbers, and spaces from names before processing.

## 3. Name Notation as an Originating VASP
### 3-1. Natural Person 
```mermaid
flowchart TD
    %% Root Decision
    Start([User Name]) --> D1{"Is Beneficiary VASP<br/>Registered in Korea? (KR)"}

    %% NON-KR PATH (Priority: English Name)
    D1 -- "No" --> N1_NI["'nameIdentifier'<br/>= English Name"]
    
    subgraph Non_KR ["Non-KR Destination Logic"]
        N1_NI -- "No Name" --> N1_NI_Req["'primaryIdentifier': '',<br/>'secondaryIdentifier': ''<br/>(Mandatory Blank)"]
        N1_NI_Req --> N1_LNI["'localNameIdentifier'<br/>= Local Name"]
        
        N1_NI -- "Has Name" --> D2{"Can split<br/>Surname/Given Name?"}
        D2 -- "No" --> N1_NI_Full["'primaryIdentifier': 'Full Name'"]
        D2 -- "Yes" --> N1_NI_Sep["'primaryIdentifier': 'Surname',<br/>'secondaryIdentifier': 'Given Name'"]
        
        N1_NI_Full & N1_NI_Sep --> N1_LNI
        
        N1_LNI --> D3{"Can split<br/>Surname/Given Name?"}
        D3 -- "No" --> N1_LNI_Full["'primaryIdentifier': 'Full Name'"]
        D3 -- "Yes" --> N1_LNI_Sep["'primaryIdentifier': 'Surname',<br/>'secondaryIdentifier': 'Given Name'"]
    end

    %% KR PATH (Priority: Local Name)
    D1 -- "Yes" --> N2_NI["'nameIdentifier'<br/>= Local Name"]

    subgraph KR_Destination ["KR Destination Logic"]
        N2_NI -- "No Name" --> N2_NI_Req["'primaryIdentifier': '',<br/>'secondaryIdentifier': ''<br/>(Mandatory Blank)"]
        N2_NI_Req --> N2_LNI["'localNameIdentifier'<br/>= English Name"]
        
        N2_NI -- "Has Name" --> D4{"Can split<br/>Surname/Given Name?"}
        D4 -- "No" --> N2_NI_Full["'primaryIdentifier': 'Full Name'"]
        D4 -- "Yes" --> N2_NI_Sep["'primaryIdentifier': 'Surname',<br/>'secondaryIdentifier': 'Given Name'"]
        
        N2_NI_Full & N2_NI_Sep --> N2_LNI
        
        N2_LNI --> D5{"Can split<br/>Surname/Given Name?"}
        D5 -- "No" --> N2_LNI_Full["'primaryIdentifier': 'Full Name'"]
        D5 -- "Yes" --> N2_LNI_Sep["'primaryIdentifier': 'Surname',<br/>'secondaryIdentifier': 'Given Name'"]
    end

    %% Styling Definitions (Safe Classing)
    classDef array fill:#d2e9e9,stroke:#5fb8b8,rx:10,ry:10
    classDef str fill:#f9d7d7,stroke:#f0a8a8,rx:5,ry:5
    classDef decision fill:#f4f4f4,stroke:#ccc
    
    class N1_NI,N1_LNI,N2_NI,N2_LNI array
    class N1_NI_Req,N1_NI_Full,N1_NI_Sep,N1_LNI_Full,N1_LNI_Sep str
    class N2_NI_Req,N2_NI_Full,N2_NI_Sep,N2_LNI_Full,N2_LNI_Sep str
    class D1,D2,D3,D4,D5 decision
```

#### 3-1-1. Language Rule
* Enter 'nameIdentifier' in English.
* If the beneficiary's name is in Korean(other than English) enter the name in the 'localNameIdentifier' element. However, even in this case, it is recommended to provide a 'nameIdentifier' with a legal English name.
* If there is no English name, enter `""` for `nameIdentifier` as shown below.
```json
{
  "name": {
    "nameIdentifier": [
      {
        "primaryIdentifier": "",
        "secondaryIdentifier": "",
        "nameIdentifierType": "LEGL"
      }
    ],
    "localNameIdentifier": [
      {
        "primaryIdentifier": "로버트 반스",
        "secondaryIdentifier": "",
        "nameIdentifierType": "LEGL"
      }
    ]
  }
}
```

#### 3-1-2. When Names can be Separated
* Enter the last name in 'primaryIdentifier'.
* Enter the first name in 'secondaryIdentifier'.

#### 3-1-3. When Names can NOT be Separated
* Enter the full name in 'primaryIdentifier' based on VASP DB. For English, enter the name in the order of first name and last name. For Korean, enter the name in the order of last name and first name. If you cannot identify first name and last name, then enter the same value as the value in the DB.
* Do not enter anything in 'secondaryIdentifier'.

### 3-2. Legal Person
For legal entities, it is necessary to provide both the entity's name and the name of its representative(The CEO). This requirement applies to both the sending and receiving of virtual assets.

The IVMS101 standard does not include a dedicated element for the representative's information within the 'legalPerson'. Therefore, the representative's information should be recorded as follows:

* The 'originatorPersons' or 'beneficiaryPersons' fields are arrays of the Person type. The first element of the array must always contain the information of the legal person.
* From the second element onwards, enter the personal information of the representative ('naturalPerson').
* Even if there are multiple representatives, all the information of representatives should be entered.

## 4. Verifying Names as a Beneficiary VASP
### 4-1. Comparing Natural Person Names
```mermaid
graph TD
    %% Node Definitions
    B_Name["'nameIdentifier' of 'Beneficiary'"]
    Retrieve["Retrieve Users Data"]
    
    %% Counterparty Logic
    Step1["(1) Concatenate 'primaryIdentifier' & 'secondaryIdentifier'"]
    Step4["(4) Concatenate 'local primary' & 'local secondary'"]
    
    %% User Logic
    Step2["(2) Concatenate User's First Name & Last Name"]
    Step3["(3) Reverse User's First Name & Last Name"]
    
    %% Comparison Steps
    Comp12["Compare (1) & (2)"]
    Comp13["Compare (1) & (3)"]
    Comp42["Compare (4) & (2)"]
    Comp43["Compare (4) & (3)"]
    
    %% Outcomes
    Next["Proceed to next step"]
    LocalID["'localNameIdentifier' of 'Beneficiary'"]
    Mismatch["Name Mismatch"]

    %% Flow Connections
    B_Name --> Retrieve
    Retrieve --> Step1
    Retrieve --> Step2
    
    Step1 --> Comp12
    Step2 --> Comp12
    
    Comp12 -- "Yes" --> Next
    Comp12 -- "No" --> Step3
    
    Step3 --> Comp13
    Step1 --> Comp13
    
    Comp13 -- "Yes" --> Next
    Comp13 -- "No" --> LocalID
    
    LocalID --> Step4
    Step4 --> Comp42
    Step2 --> Comp42
    
    Comp42 -- "Yes" --> Next
    Comp42 -- "No" --> Comp43
    
    Step4 --> Comp43
    Step3 --> Comp43
    
    Comp43 -- "Yes" --> Next
    Comp43 -- "No" --> Mismatch

    %% Styling Definitions
    classDef counterparty fill:#cfecec,stroke:#cfecec,rx:10,ry:10
    classDef you fill:#f6d2d2,stroke:#f6d2d2,rx:10,ry:10
    classDef neutral fill:#f4f4f4,stroke:#ccc,rx:10,ry:10

    %% Data from Counterparty (Teal)
    class B_Name,Step1,LocalID,Step4 counterparty
    %% Data from your DB (Pink)
    class Retrieve,Step2,Step3 you
    %% Comparisons and Outcomes (Gray)
    class Comp12,Comp13,Comp42,Comp43,Next,Mismatch neutral
```
