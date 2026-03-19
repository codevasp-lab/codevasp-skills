# 04 - IVMS101-part3

# 3. IVMS101 Required Fields
```mermaid
graph LR
    %% Root Node
    root(( ))

    %% Originator Branch
    root --- Originator["Originator"]
    Originator --- originatorPersons["originatorPersons"]
    originatorPersons --- OP_NP["naturalPerson"]
    
    OP_NP --- OP_CI["customerIdentification: 'customernumber in Max 50 Text'"]
    OP_NP --- OP_COR["countryOfResidence: 'US'"]
    
    OP_NP --- OP_NAME["name"]
    OP_NAME --- OP_NI["nameIdentifier"]
    OP_NI --- OP_NIV["primaryIdentifier: 'Barnes'<br/>secondaryIdentifier: 'Robert'<br/>nameIdentifierType: 'LEGL'"]
    OP_NAME --- OP_LNI["localNameIdentifier"]
    OP_LNI --- OP_LNIV["primaryIdentifier: '바네스 번즈'<br/>secondaryIdentifier: ''<br/>nameIdentifierType: 'LEGL'"]
    
    OP_NP --- OP_DAPB["dateAndPlaceOfBirth"]
    OP_DAPB --- OP_DAPBV["dateOfBirth: '1990-01-01'<br/>placeOfBirth: 'LA'"]
    
    Originator --- OP_AN["accountNumber"]
    OP_AN --- OP_ANV["rj8nOkwt1ypcfkyZrtorzZw8hYabNMYKz:tag or memo"]

    %% Beneficiary Branch
    root --- Beneficiary["Beneficiary"]
    Beneficiary --- beneficiaryPersons["beneficiaryPersons"]
    beneficiaryPersons --- BP_NP["naturalPerson"]
    
    BP_NP --- BP_CI["customerIdentification: 'customernumber in Max 50 Text'"]
    BP_NP --- BP_COR["countryOfResidence: 'US'"]
    
    BP_NP --- BP_NAME["name"]
    BP_NAME --- BP_NI["nameIdentifier"]
    BP_NI --- BP_NIV["primaryIdentifier: 'Smith'<br/>secondaryIdentifier: 'Alice'<br/>nameIdentifierType: 'LEGL'"]
    BP_NAME --- BP_LNI["localNameIdentifier"]
    BP_LNI --- BP_LNIV["primaryIdentifier: '엘리스 스미스'<br/>secondaryIdentifier: ''<br/>nameIdentifierType: 'LEGL'"]
    
    BP_NP --- BP_DAPB["dateAndPlaceOfBirth"]
    BP_DAPB --- BP_DAPBV["dateOfBirth: '1990-01-01'<br/>placeOfBirth: 'LA'"]
    
    Beneficiary --- BP_AN["accountNumber"]
    BP_AN --- BP_ANV["HPxPau6whpf18R2V4iZ7NQ8xXnscuYYCVh6:tag or memo"]

    %% OriginatingVASP Branch
    root --- OriginatingVASP["OriginatingVASP"]
    OriginatingVASP --- originatingVASP_inner["originatingVASP"]
    originatingVASP_inner --- OV_LP["legalPerson"]
    
    OV_LP --- OV_COR["countryOfRegistration: 'KR'"]
    OV_LP --- OV_NAME["name"]
    OV_NAME --- OV_NI["nameIdentifier"]
    OV_NI --- OV_NIV["legalPersonName: 'Gorbit Inc.'<br/>legalPersonNameIdentifierType: 'LEGL'"]
    
    OV_LP --- OV_GA["geographicAddress"]
    OV_GA --- OV_GAV["addressType: 'GEOG'<br/>streetName: 'Example Street'<br/>buildingNumber: '123'<br/>buildingName: 'Example Building'<br/>postcode: '00000'<br/>townName: 'Seoul'<br/>countrySubDivision: 'N/A'<br/>country: 'KR'"]
    OV_GAV --- OV_AL["addressLine"]
    OV_AL --- OV_AL1["104 Teheran-ro 1-gil, Gangnam-gu"]
    OV_AL --- OV_AL2["10th floor"]
    
    OV_LP --- OV_NI_ID["nationalIdentification"]
    OV_NI_ID --- OV_NI_V["nationalIdentifier: 'EXAMPLE-TAX-ID'<br/>nationalIdentifierType: 'RAID'<br/>registrationAuthority: 'RA000657'"]

    %% BeneficiaryVASP Branch
    root --- BeneficiaryVASP["BeneficiaryVASP"]
    BeneficiaryVASP --- beneficiaryVASP_inner["beneficiaryVASP"]
    beneficiaryVASP_inner --- BV_LP["legalPerson"]
    
    BV_LP --- BV_COR["countryOfRegistration: 'KR'"]
    BV_LP --- BV_NAME["name"]
    BV_NAME --- BV_NI["nameIdentifier"]
    BV_NI --- BV_NIV["legalPersonName: 'Coinone Inc.'<br/>legalPersonNameIdentifierType: 'LEGL'"]
    
    BV_LP --- BV_GA["geographicAddress"]
    BV_GA --- BV_GAV["addressType: 'GEOG'<br/>streetName: 'Example Street'<br/>buildingNumber: '456'<br/>buildingName: 'Example Building'<br/>postcode: '00000'<br/>townName: 'Seoul'<br/>countrySubDivision: 'N/A'<br/>country: 'KR'"]
    BV_GAV --- BV_AL["addressLine"]
    BV_AL --- BV_AL1["104 Teheran-ro 1-gil, Gangnam-gu"]
    BV_AL --- BV_AL2["10th floor"]
    
    BV_LP --- BV_NI_ID["nationalIdentification"]
    BV_NI_ID --- BV_NI_V["nationalIdentifier: '0104824434'<br/>nationalIdentifierType: 'RAID'<br/>registrationAuthority: 'RA000657'"]

```
IVMS101 features a complex structure, as illustrated above. The provided diagram is merely one example; scenarios vary depending on the classification as 'naturalPerson'/'legalPerson' and the use of 'localNameIdentifier'.

As required fields differ across sending and receiving cases, understanding each scenario thoroughly and entering the necessary details is required.

Although the structure appears complex, effectively handling the four essential elements—'Originator', 'Beneficiary', 'OriginatingVASP', and 'BeneficiaryVASP'—ensures a smooth process. The originating VASP shall incorporate 'Originator', 'Beneficiary', and 'OriginatingVASP' information into the 'payload' as per the IVMS101 standard and dispatch the request. The beneficiary VASP then finalizes the process by adding 'BeneficiaryVASP' details to the received data and issuing a response.

We will now review the key IVMS101 objects for common cases.

## 3-1. As an Originator
### 3-1-1. 'Originator': 'naturalPerson'
```mermaid
graph LR
    %% Root Node
    root(( ))

    %% Main Branches
    root --> Originator["Originator"]
    root --> Beneficiary["Beneficiary"]
    root --> OriginatingVASP["OriginatingVASP"]

    %% Originator Path
    Originator --> originatorPersons["originatorPersons [3]"]
    Originator --> accountNumber["accountNumber [1]"]

    originatorPersons --> naturalPerson["naturalPerson {3}"]
    accountNumber --> accountValue["rjChk8e71gxVhyJSr1srzZxWhVisWMMYKz:tag or memo"]

    %% Natural Person Details
    naturalPerson --> idInfo["customerIdentification: 'customernumber in Max 50 Text'<br/>countryOfResidence: 'US'"]
    naturalPerson --> name["name {1}"]
    naturalPerson --> dateAndPlaceOfBirth["dateAndPlaceOfBirth {1}"]

    %% Name Details
    name --> nameIdentifier["nameIdentifier [1]"]
    name --> localNameIdentifier["localNameIdentifier [1]"]

    nameIdentifier --> niValue["primaryIdentifier: ''<br/>secondaryIdentifier: ''<br/>nameIdentifierType: ''"]
    localNameIdentifier --> lniValue["primaryIdentifier: '로버트 반스'<br/>secondaryIdentifier: ''<br/>nameIdentifierType: 'LEGL'"]

    %% Birth Details
    dateAndPlaceOfBirth --> dobValue["dateOfBirth: '1990-01-01'<br/>placeOfBirth: 'LA'"]

    %% Beneficiary/VASP Details (To be provided)
    Beneficiary --> B_detail["detail: 'To be provided'"]
    OriginatingVASP --> V_detail["detail: 'To be provided'"]

    %% Styling for "Required" Fields (Red Border)
    classDef required fill:#fff,stroke:#ff0000,stroke-width:2px;
    class Originator,Beneficiary,OriginatingVASP required;
    class originatorPersons,accountNumber,accountValue required;
    class naturalPerson,name,dateAndPlaceOfBirth required;
    class nameIdentifier,niValue required;
    class dobValue required;

    %% Styling for "To be provided" and local info
    classDef optional fill:#f9f9f9,stroke:#999,stroke-width:1px;
    class idInfo,localNameIdentifier,lniValue,B_detail,V_detail optional;

```
* When the originator is an individual, under the 'name' object, 'nameIdentifier' is required, whereas 'localNameIdentifier' is optional.
* Since **the 'nameIdentifier' is required, enter blank** if there is no matching value.
* But when communicating **between Korean VASPs**, it is agreed that the 'nameIdentifier' will contain the Korean name and the 'localNameIdentifier' will contain the English name.
 

### 3-1-2. 'Originator': 'legalPerson'
```mermaid
graph LR
    %% Root Node
    root(( ))

    %% Primary Branches
    root --- Originator["Originator"]
    root --- Beneficiary["Beneficiary"]
    root --- OriginatingVASP["OriginatingVASP"]

    %% Originator Path
    Originator --- originatorPersons["originatorPersons [2]"]
    Originator --- accountNumber["accountNumber [1]"]

    %% originatorPersons Branches
    originatorPersons --- legalPerson["legalPerson {1}<br/>(Corporate Info)"]
    originatorPersons --- CEO1["naturalPerson {1}<br/>(CEO1 Info)"]
    originatorPersons --- CEO2["naturalPerson {1}<br/>(CEO2 Info (Optional))"]

    %% Corporate Info Details
    legalPerson --- LP_ID["customerIdentification: 'customernumber in Max 50 Text'<br/>countryOfRegistration: 'KR'"]
    legalPerson --- LP_name["name {1}"]
    legalPerson --- LP_natID["nationalIdentification {1}"]

    LP_name --- LP_nameID["nameIdentifier {1}"]
    LP_nameID --- LP_nameVal["legalPersonName: 'Coinone Inc.'<br/>legalPersonNameIdentifierType: 'LEGL'"]

    %% CEO1 Info Details
    CEO1 --- CEO1_name["name {2}"]
    CEO1_name --- CEO1_nameID["nameIdentifier [1]"]
    CEO1_name --- CEO1_localName["localNameIdentifier [1]"]

    CEO1_nameID --- CEO1_val["primaryIdentifier: ''<br/>secondaryIdentifier: ''<br/>nameIdentifierType: ''<br/>(Enter blank if no value)"]

    %% Account Details
    accountNumber --- accountVal["rjChk8e71gxVhyJSr1srzZxWhVisWMMYKz:tag or memo"]

    %% Beneficiary & VASP Details
    Beneficiary --- B_detail["detail: 'To be provided'"]
    OriginatingVASP --- V_detail["detail: 'To be provided'"]

    %% Styling for "Required" Fields (Red Border)
    classDef required fill:#fff,stroke:#ff0000,stroke-width:2px;
    class Originator,Beneficiary,OriginatingVASP required;
    class originatorPersons,accountNumber,accountVal required;
    class legalPerson,CEO1 required;
    class LP_name,LP_nameID,LP_nameVal required;
    class CEO1_name,CEO1_nameID,CEO1_val required;

    %% Styling for Optional/Placeholders
    classDef optional fill:#f9f9f9,stroke:#999,stroke-width:1px;
    class CEO2,LP_ID,LP_natID,CEO1_localName,B_detail,V_detail optional;

```
* When the originator is a corporate entity,  under the 'originatorPersons' object, **both a 'legalPerson' and at least one 'naturalPerson' are required**.
* The 'legalPerson' object contains corporate details, while 'naturalPerson' includes the corporate representative(CEO)'s information.
* Under the 'name' object, **'nameIdentifier' is required**, whereas 'localNameIdentifier' is optional.
* Since **the 'nameIdentifier' is required, enter blank** if there is no matching value.
* If there are multiple corporate representatives, add as many 'naturalPerson' objects as needed to the 'beneficiaryPersons' array.
* The 'nameIdentifier' contains the English name, while the 'localNameIdentifier' holds the Korean name (or other local language names).
* But when communicating **between Korean VASPs**, it is agreed that the 'nameIdentifier' will contain the Korean name and the 'localNameIdentifier' will contain the English name.
 

### 3-1-3. 'Beneficiary': 'naturalPerson'
```mermaid
graph LR
    %% Root Node
    root(( ))

    %% Primary Branches
    root --- Originator["Originator"]
    root --- Beneficiary["Beneficiary {2}"]
    root --- OriginatingVASP["OriginatingVASP"]

    %% Beneficiary Path
    Beneficiary --- beneficiaryPersons["beneficiaryPersons [1]"]
    Beneficiary --- accountNumber["accountNumber [1]"]

    %% Persons and Account Details
    beneficiaryPersons --- naturalPerson["naturalPerson {3}"]
    accountNumber --- accountVal["rHcFoo6a9qT5NHiVn1THQRhsEGcxtYCV4d:tag or memo"]

    %% Natural Person Details
    naturalPerson --- idInfo["customerIdentification: 'customernumber in Max 50 Text'<br/>countryOfResidence: 'US'"]
    naturalPerson --- name["name {2}"]
    naturalPerson --- dateAndPlaceOfBirth["dateAndPlaceOfBirth {1}"]

    %% Name Details
    name --- nameIdentifier["nameIdentifier [1]"]
    name --- localNameIdentifier["localNameIdentifier [1]"]

    %% Values
    nameIdentifier --- niVal["primaryIdentifier: ''<br/>secondaryIdentifier: ''<br/>nameIdentifierType: ''<br/>(Enter blank if no value)"]
    localNameIdentifier --- lniVal["primaryIdentifier: '엘리스 스미스'<br/>secondaryIdentifier: ''<br/>nameIdentifierType: 'LEGL'"]
    dateAndPlaceOfBirth --- dobVal["dateOfBirth: '1990-01-01'<br/>placeOfBirth: 'LA'"]

    %% Placeholder details
    Originator --- O_detail["detail: 'To be provided'"]
    OriginatingVASP --- V_detail["detail: 'To be provided'"]

    %% Styling for "Required" Fields (Red Border)
    classDef required fill:#fff,stroke:#ff0000,stroke-width:2px;
    class Originator,Beneficiary,OriginatingVASP required;
    class beneficiaryPersons,accountNumber,accountVal required;
    class naturalPerson,name,nameIdentifier,niVal required;

    %% Styling for Optional/Secondary Fields
    classDef optional fill:#f9f9f9,stroke:#999,stroke-width:1px;
    class idInfo,dateAndPlaceOfBirth,localNameIdentifier,lniVal,dobVal,O_detail,V_detail optional;

```
- When the beneficiary is an individual, under the 'name' object, **'nameIdentifier' is required**,  whereas 'localNameIdentifier' is optional.
- Since **the 'nameIdentifier' is required, enter blank** if there is no matching value.
- The 'nameIdentifier' contains the English name, while the 'localNameIdentifier' holds the Korean name (or other local language names).
- But when communicating **between Korean VASPs**, it is agreed that the 'nameIdentifier' will contain the Korean name and the 'localNameIdentifier' will contain the English name.
 

### 3-1-4. 'Beneficiary': 'legalPerson'
```mermaid
graph LR
    %% Root Node
    root(( ))

    %% Primary Branches
    root --- Originator["Originator"]
    root --- Beneficiary["Beneficiary {2}"]
    root --- OriginatingVASP["OriginatingVASP"]

    %% Beneficiary Path
    Beneficiary --- beneficiaryPersons["beneficiaryPersons [2]"]
    Beneficiary --- accountNumber["accountNumber [1]"]

    %% beneficiaryPersons Branches
    beneficiaryPersons --- legalPerson["legalPerson {1}<br/>(Corporate Info)"]
    beneficiaryPersons --- CEO1["naturalPerson {1}<br/>(CEO1 Info)"]
    beneficiaryPersons --- CEO2["naturalPerson {1}<br/>(CEO2 Info (Optional))"]

    %% Corporate Info Details
    legalPerson --- LP_ID["customerIdentification: 'customernumber in Max 50 Text'<br/>countryOfRegistration: 'KR'"]
    legalPerson --- LP_name["name {1}"]
    legalPerson --- LP_natID["nationalIdentification {1}"]

    LP_name --- LP_nameID["nameIdentifier {1}"]
    LP_nameID --- LP_nameVal["legalPersonName: 'Korbit Inc.'<br/>legalPersonNameIdentifierType: 'LEGL'"]

    %% CEO1 Info Details
    CEO1 --- CEO1_name["name {2}"]
    CEO1_name --- CEO1_nameID["nameIdentifier [1]"]
    CEO1_name --- CEO1_localName["localNameIdentifier [1]"]

    CEO1_nameID --- CEO1_val["primaryIdentifier: ''<br/>secondaryIdentifier: ''<br/>nameIdentifierType: ''<br/>(Enter blank if no value)"]

    %% Account Details
    accountNumber --- accountVal["HPxPau6whpf18R2V4iZ7NQ8xXnscuYYCVh6:tag or memo"]

    %% Placeholder details
    Originator --- O_detail["detail: 'To be provided'"]
    OriginatingVASP --- V_detail["detail: 'To be provided'"]

    %% Styling for "Required" Fields (Red Border)
    classDef required fill:#fff,stroke:#ff0000,stroke-width:2px;
    class Originator,Beneficiary,OriginatingVASP required;
    class beneficiaryPersons,accountNumber,accountVal required;
    class legalPerson,CEO1 required;
    class LP_name,LP_nameID,LP_nameVal required;
    class CEO1_name,CEO1_nameID,CEO1_val required;

    %% Styling for Optional/Secondary Fields
    classDef optional fill:#f9f9f9,stroke:#999,stroke-width:1px;
    class CEO2,LP_ID,LP_natID,CEO1_localName,O_detail,V_detail optional;

```
* Under the 'originatorPersons' object, both a 'legalPerson' and at least one 'naturalPerson' are required.
* The 'legalPerson' object contains corporate details, while 'naturalPerson' includes the corporate representative(CEO)'s information.
* Under the 'name' object, **'nameIdentifier' is required**,  whereas 'localNameIdentifier' is optional.
* Since **the 'nameIdentifier' is required, enter blank** if there is no matching value.
* If there are multiple corporate representatives, add as many 'naturalPerson' objects as needed to the 'beneficiaryPersons' array.
* The 'nameIdentifier' contains the English name, while the 'localNameIdentifier' holds the Korean name (or other local language names).
* - But when communicating **between Korean VASPs**, it is agreed that the 'nameIdentifier' will contain the Korean name and the 'localNameIdentifier' will contain the English name.
 

### 3-1-5. 'OriginatingVASP'
```mermaid
graph LR
    %% Root Node
    root(( ))

    %% Primary Branches
    root --- Originator["Originator {2}"]
    root --- Beneficiary["Beneficiary"]
    root --- OriginatingVASP["OriginatingVASP {1}"]

    %% Detail placeholders
    Originator --- O_detail["detail: 'To be provided'"]
    Beneficiary --- B_detail["detail: 'To be provided'"]

    %% OriginatingVASP Path
    OriginatingVASP --- originatingVASP_inner["originatingVASP {1}"]
    originatingVASP_inner --- legalPerson["legalPerson {4}"]

    %% legalPerson Branches
    legalPerson --- countryReg["countryOfRegistration: 'KR'"]
    legalPerson --- name["name {1}"]
    legalPerson --- geographicAddress["geographicAddress [1]"]
    legalPerson --- nationalIdentification["nationalIdentification {1}"]

    %% Name Details
    name --- nameIdentifier["nameIdentifier [1]"]
    nameIdentifier --- nameVal["legalPersonName: 'Korbit Inc.'<br/>legalPersonNameIdentifierType: 'LEGL'"]

    %% Geographic Address Details
    geographicAddress --- geoVal["addressType: 'GEOG'<br/>streetName: 'Example Street'<br/>buildingNumber: '123'<br/>buildingName: 'Example Building'<br/>postcode: '00000'<br/>townName: 'Seoul'<br/>countrySubDivision: 'N/A'<br/>country: 'KR'"]
    geoVal --- addressLine["addressLine [2]"]
    addressLine --- al1["104 Teheran-ro 1-gil, Gangnam-gu"]
    addressLine --- al2["10th floor"]

    %% National Identification Details
    nationalIdentification --- natVal["nationalIdentifier: 'EXAMPLE-TAX-ID'<br/>nationalIdentifierType: 'RAID'<br/>registrationAuthority: 'RA000657'"]

    %% Conditional Requirement Logic
    Note["★One of two required"]
    Note -.-> geographicAddress
    Note -.-> nationalIdentification

    %% Styling for "Required" Fields (Red Border)
    classDef required fill:#fff,stroke:#ff0000,stroke-width:2px;
    class Originator,OriginatingVASP,originatingVASP_inner,legalPerson required;
    class countryReg,name,nameIdentifier,nameVal required;
    class geographicAddress,nationalIdentification,natVal required;

    %% Styling for "To be provided"
    classDef optional fill:#f9f9f9,stroke:#999,stroke-width:1px;
    class O_detail,B_detail,addressLine,al1,al2 optional;

```
- The 'OriginatingVASP' object contains information about the sending VASP.
- Under 'legalPerson', both 'name' and 'countryOfRegistration' are required, and either 'geographicAddress' or 'nationalIdentification' should also be entered.
- When using 'nationalIdentification', it's recommended to include 'registrationAuthority', the details of the issuing body. Download the 'GLEIF Registration Authorities List' from the bottom of the [GLEIF website](https://www.gleif.org/en/about-lei/code-lists/gleif-registration-authorities-list), locate the Authority Code that corresponds with your country and registration type.
  

### 3-1-6. 'BeneficiaryVASP'
```mermaid
graph LR
    %% Root Node
    root(( ))

    %% Primary Branches
    root --- Originator["Originator {2}"]
    root --- Beneficiary["Beneficiary"]
    root --- OriginatingVASP["OriginatingVASP"]
    root --- BeneficiaryVASP["BeneficiaryVASP {1}"]

    %% Detail placeholders
    Originator --- O_detail["detail: 'To be provided'"]
    Beneficiary --- B_detail["detail: 'To be provided'"]
    OriginatingVASP --- V_detail["detail: 'To be provided'"]

    %% BeneficiaryVASP Path
    BeneficiaryVASP --- beneficiaryVASP_inner["beneficiaryVASP {1}"]
    beneficiaryVASP_inner --- legalPerson["legalPerson {4}"]

    %% legalPerson Branches
    legalPerson --- countryReg["countryOfRegistration: 'KR'"]
    legalPerson --- name["name {1}"]
    legalPerson --- geographicAddress["geographicAddress [1]"]
    legalPerson --- nationalIdentification["nationalIdentification {1}"]

    %% Name Details
    name --- nameIdentifier["nameIdentifier [1]"]
    nameIdentifier --- nameVal["legalPersonName: 'Coinone Inc.'<br/>legalPersonNameIdentifierType: 'LEGL'"]

    %% Geographic Address Details
    geographicAddress --- geoVal["addressType: 'GEOG'<br/>streetName: 'Example Street'<br/>buildingNumber: '456'<br/>buildingName: 'Example Building'<br/>postcode: '00000'<br/>townName: 'Seoul'<br/>countrySubDivision: 'N/A'<br/>country: 'KR'"]
    geoVal --- addressLine["addressLine [2]"]
    addressLine --- al1["104 Teheran-ro 1-gil, Gangnam-gu"]
    addressLine --- al2["10th floor"]

    %% National Identification Details
    nationalIdentification --- natVal["nationalIdentifier: '6948624434'<br/>nationalIdentifierType: 'RAID'<br/>registrationAuthority: 'RA000657'"]

    %% Conditional Requirement Logic
    Note["★One of two required"]
    Note -.-> geographicAddress
    Note -.-> nationalIdentification

    %% Styling for "Required" Fields (Red Border)
    classDef required fill:#fff,stroke:#ff0000,stroke-width:2px;
    class Originator,Beneficiary,OriginatingVASP,BeneficiaryVASP required;
    class beneficiaryVASP_inner,legalPerson required;
    class countryReg,name,nameIdentifier,nameVal required;
    class geographicAddress,nationalIdentification,natVal required;

    %% Styling for Optional/Placeholder
    classDef optional fill:#f9f9f9,stroke:#999,stroke-width:1px;
    class O_detail,B_detail,V_detail,addressLine,al1,al2 optional;

```
* The receiving VASP adds their 'BeneficiaryVASP' information to the 'Originator', 'Beneficiary', and 'OriginatingVASP' information contained in the 'Asset Transfer Authorization Request' and sends it back to the originating VASP.
* Under 'legalPerson', both 'name' and 'countryOfRegistration' are required, and either 'geographicAddress' or 'nationalIdentification' should also be entered.
* When using 'nationalIdentification', it's recommended to include 'registrationAuthority', the details of the issuing body. Download the 'GLEIF Registration Authorities List' from the bottom of the [GLEIF website](https://www.gleif.org/en/about-lei/code-lists/gleif-registration-authorities-list), locate the Authority Code that corresponds with your country and registration type..