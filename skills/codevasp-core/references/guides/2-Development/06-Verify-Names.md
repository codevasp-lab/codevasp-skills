# 06 - Verify Names

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
graph TD
%% Global Styling
    classDef Array fill:#d1ecf1,stroke:#0c5460,color:#000;
    classDef String fill:#f8d7da,stroke:#842029,color:#000;
    classDef Decision fill:#eeeeee,stroke:#999999,color:#000,border-radius:20px;

    Root["User Name"]
    MainDecision["Is Beneficiary VASP Registered in Korea?<br/>('countryOfRegistration' = 'KR')"]

    Root --> MainDecision

%% Left Branch: Not Registered in Korea (No)
    subgraph Not_In_Korea [Beneficiary VASP Not in Korea]
        L_NameId["'nameIdentifier'<br/>= English Name"]

        L_Req["'primaryIdentifier': '',<br/>'secondaryIdentifier': ''"]

        L_Dec1["Is the surname separated from the given name?"]
        L_Opt1_No["'primaryIdentifier': 'Full Name',<br/>'secondaryIdentifier': ''"]
        L_Opt1_Yes["'primaryIdentifier': 'Last Name',<br/>'secondaryIdentifier': 'First Name'"]

        L_LocalId["'localNameIdentifier'<br/>= Local Name"]

        L_Dec2["Is the surname separated from the given name?"]
        L_Opt2_No["'primaryIdentifier': 'Full Name',<br/>'secondaryIdentifier': ''"]
        L_Opt2_Yes["'primaryIdentifier': 'Last Name',<br/>'secondaryIdentifier': 'First Name'"]
    end

%% Right Branch: Registered in Korea (Yes)
    subgraph In_Korea [Beneficiary VASP In Korea]
        R_NameId["'nameIdentifier'<br/>= Local Name"]

        R_Req["'primaryIdentifier': '',<br/>'secondaryIdentifier': ''"]

        R_Dec1["Is the surname separated from the given name?"]
        R_Opt1_No["'primaryIdentifier': 'Full Name',<br/>'secondaryIdentifier': ''"]
        R_Opt1_Yes["'primaryIdentifier': 'Last Name',<br/>'secondaryIdentifier': 'First Name'"]

        R_LocalId["'localNameIdentifier'<br/>= English Name"]

        R_Dec2["Is the surname separated from the given name?"]
        R_Opt2_No["'primaryIdentifier': 'Full Name',<br/>'secondaryIdentifier': ''"]
        R_Opt2_Yes["'primaryIdentifier': 'Last Name',<br/>'secondaryIdentifier': 'First Name'"]
    end

%% Flow Connections
    MainDecision -- "No (Red Path)" --> L_NameId
    MainDecision -- "Yes (Blue Path)" --> R_NameId

%% Not In Korea Flow
    L_NameId -- "No" --> L_Req
    L_NameId -- "Yes" --> L_Dec1
    L_Dec1 -- "No" --> L_Opt1_No
    L_Dec1 -- "Yes" --> L_Opt1_Yes

    L_Req -- "Required" --> L_LocalId
    L_Opt1_No -- "Optional" --> L_LocalId
    L_Opt1_Yes -- "Optional" --> L_LocalId

    L_LocalId --> L_Dec2
    L_Dec2 -- "No" --> L_Opt2_No
    L_Dec2 -- "Yes" --> L_Opt2_Yes

%% In Korea Flow
    R_NameId -- "No" --> R_Req
    R_NameId -- "Yes" --> R_Dec1
    R_Dec1 -- "No" --> R_Opt1_No
    R_Dec1 -- "Yes" --> R_Opt1_Yes

    R_Req -- "Required" --> R_LocalId
    R_Opt1_No -- "Optional" --> R_LocalId
    R_Opt1_Yes -- "Optional" --> R_LocalId

    R_LocalId --> R_Dec2
    R_Dec2 -- "No" --> R_Opt2_No
    R_Dec2 -- "Yes" --> R_Opt2_Yes

%% Applying Classes
    class L_NameId,L_LocalId,R_NameId,R_LocalId Array;
    class L_Req,L_Opt1_No,L_Opt1_Yes,L_Opt2_No,L_Opt2_Yes String;
    class R_Req,R_Opt1_No,R_Opt1_Yes,R_Opt2_No,R_Opt2_Yes String;
    class MainDecision,L_Dec1,L_Dec2,R_Dec1,R_Dec2 Decision;
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
    Start["'nameIdentifier' of 'Beneficiary'"]
    Retrieve["Retrieve Users Data"]

    Step1["❶ Concatenate 'primaryIdentifier' & 'secondaryIdentifier'"]
    Step2["❷ Concatenate User's First Name & Last Name"]

    Comp1["Compare ❶ & ❷"]

    Step3["❸ Reverse User's First Name & Last Name"]
    Comp2["Compare ❶ & ❸"]

    LocalID["'localNameIdentifier' of 'Beneficiary'"]
    Step4["❹ Concatenate 'primaryIdentifier' & 'secondaryIdentifier'"]

    Comp3["Compare ❹ & ❷"]
    Comp4["Compare ❹ & ❸"]

    Proceed["Proceed to next step"]
    Mismatch["Name Mismatch"]

%% Flow Connections
    Start --> Retrieve
    Retrieve --> Step1
    Step1 --> Step2
    Step2 --> Comp1

    Comp1 -- "Yes" --> Proceed
    Comp1 -- "No" --> Step3

    Step3 --> Comp2
    Comp2 -- "Yes" --> Proceed
    Comp2 -- "No" --> LocalID

    LocalID --> Step4
    Step4 --> Comp3

    Comp3 -- "Yes" --> Proceed
    Comp3 -- "No" --> Comp4

    Comp4 -- "Yes" --> Proceed
    Comp4 -- "No" --> Mismatch

%% Styling based on original image legend
    classDef requestData fill:#d1ecf1,stroke:#0c5460,color:#000;
    classDef databaseData fill:#f8d7da,stroke:#842029,color:#000;
    classDef logicStep fill:#e9ecef,stroke:#495057,color:#000,border-radius:20px;

    class Start,Step1,LocalID,Step4 requestData;
    class Retrieve,Step2,Step3 databaseData;
    class Comp1,Comp2,Comp3,Comp4 logicStep;

%% Explanatory Comments
%% Blue Arrows represent the 'Yes' path in the original image.
%% Red Arrows represent the 'No' path in the original image.
```

When verifying an individual's name, both the 'nameIdentifier' and 'localNameIdentifier' objects must be validated. First, compare the 'nameIdentifier'. If it does not match, then compare the 'localNameIdentifier'.
※Note: The 'localNameIdentifier' may contain user information, so there could be cases where the 'nameIdentifier' is blank.

The verifying VASP queries the user information in its database based on the wallet address provided in the request and compares it with the name of the actual owner of that address.

For accurate comparison, combining the 'primaryIdentifier' and 'secondaryIdentifier' (first name and last name) and comparing all possible combinations are recommended. In some cases, the counterpart VASP may not separate the name into first name and last name, and might send the entire name in the 'primaryIdentifier'. In this scenario, since the counterpart's data will not change regardless of the order, 'we' should separate the user's name into first name and last name and compare it in different orders. This comparison should be performed as a single UTF-8 string, ignoring spaces (' ') and case differences.
※If the names are not separated, assume the order is surname first and given name second for Korean names, and given name first and surname second for English names.

### 4-2. Comparing Legal Person Names
The originating VASP sends both the legal entity's name and the name of one representative. Therefore, the beneficiary VASP must compare both the legal entity's name and the individual's name, ensuring that both match.

The legal entity's name should be entered as it appears in official registration. Using a service name or any other name might result in verification(A legal name mismatch stored in beneficiary's database can lead to rejection). In case of failure, adding a process to remove specific strings like "Inc." or "Ltd." and retry verification is recommended.

The process of comparing the representative's name is just the same as comparing natural person's name.