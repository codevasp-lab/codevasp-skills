# Dev 04 - IVMS101-part2

# 1. IVMS101
- IVMS101 is a standardized Travel Rule data format for secure and interoperable exchange between VASPs.
## 1-1. Reference Materials
* Please find the official IVMS101 guide by InterVASP [Here](https://www.intervasp.org/).
* Please refer to the json schema and examples [here](https://github.com/codevasp-lab/ivms101/blob/main/README.md) .
* [CodeVASP Github](https://github.com/codevasp-lab/ivms101)
## 1-2. Formatting Rules
* Field names in the message should be written in camelCase, starting with a lowercase letter.
* However, entities defined in IVMS101—namely Originator, Beneficiary, OriginatorVASP, and BeneficiaryVASP—should be written in PascalCase.
* All field values are case-insensitive unless otherwise specified.
* All field values must be expressed as UTF-8 encoded strings, including booleans, integers, and floats.
* English should be used for all field values, unless Korean is explicitly allowed.
## 1-3. IVMS101 Version
IVMS101 has been updated once since its release, resulting in two versions:
* IVMS101.2020: Initial version
* IVMS101.2023: Updated in 2024
Currently, all global Travel Rule solutions universally adopt IVMS101.2020, and all of CodeVASP's guide content is based on this version.
Using different versions of the IVMS101 protocol between VASPs can disrupt communication. To ensure proper functionality, always adhere to the instructions outlined in the documentation when implementing.
# 2. IVMS101 Type
- IVMS101 is described object by object to guide the construction of the payload.
### IVMS101 as an originator VASP
As an originator VASP, you need to send following to beneficiary VSAP. You should know the entityId of beneficiary from CodeVASP, however, you still do not know their VASP information, thus, only send following objects.
```
{ 
  "ivms101" : {  
    "Originator": {...},  
    "Beneficiary": {...},  
    "OriginatingVASP": {...}  
  }
}
```
### IVMS101 as a beneficiary VASP
When beneficiary VASP response to originator, it should complete the IVMS101 format as following.
* You may also include more Beneficiary information in Beneficiary Object such as `customerIdentification`.
```
{ 
  "ivms101" : {  
    "Originator": {...},  
    "Beneficiary": {...},  
    "OriginatingVASP": {...},
    "BeneficiaryVASP": {...}                   
  }
}
```
## 2-0. ivms101 
| Name            | Required | Type                                | Description                                                                      |
| :-------------- | :------- | :---------------------------------- | :------------------------------------------------------------------------------- |
| Originator      | Required | Originator      | An object that contains the information of the user sending the virtual asset.   |
| Beneficiary     | Required | Beneficiary       | An object that contains the information of the user receiving the virtual asset. |
| OriginatingVASP | Required | OriginatingVASP| An object that contains the information of the VASP sending the virtual asset.   |
| BeneficiaryVASP | Optional | BeneficiaryVASP | An object that contains the information of the VASP receiving the virtual asset. |

## 2-1. Originator
-  Details associated with the identity information of the sender (the person sending the virtual asset).

| Name              | Required | Type                      | Description                                                                                                                           |
| :---------------- | :------- | :------------------------ | :------------------------------------------------------------------------------------------------------------------------------------ |
| originatorPersons | Required | array\<Person> | An object designed to hold the information of the sender.                                                                             |
| accountNumber     | Required | array\<String>            | The wallet address of the user sending the asset. If a tag or memo value is required, it should be appended using ':' as a separator. |
## 2-2. Beneficiary
- Details associated with the identity information of the recipient (the person receiving the virtual asset).

| Name               | Required | Type                      | Description                                                                                                                 |
| :----------------- | :------- | :------------------------ | :-------------------------------------------------------------------------------------------------------------------------- |
| beneficiaryPersons | Required | array\<Person> | An object designed to contain the information of the recipient.                                                             |
| accountNumber      | Required | array\<String>            | The wallet address of the user receiving the asset, with a tag or memo value appended using ':' as a separator if required. |

## 2-3. OriginatingVASP
- Information about the Virtual Asset Service Provider (VASP) on the sender's side.

| Name            | Required | Type              | Description                                                     |
| :-------------- | :------- | :---------------- | :-------------------------------------------------------------- |
| originatingVASP | Optional | Person | An object designed to hold the information of the sending VASP. |

## 2-4. BeneficiaryVASP
- Information about the Virtual Asset Service Provider (VASP) on the recipient's side.

| Name            | Required | Type              | Description                                                          |
| :-------------- | :------- | :---------------- | :------------------------------------------------------------------- |
| beneficiaryVASP | Optional | Person | An object designed to contain the information of the receiving VASP. |

### 2-1-1. Address
- Information representing the geographical address of an individual or a corporation.

| Name               | Required | Type                                | Description                                        |
| :----------------- | :------- | :---------------------------------- | :------------------------------------------------- |
| addressType        | Required | AddressTypeCode | An object that represents the type of address.     |
| townName           | Required | String                              | City name                                          |
| country            | Required | CountryCode        | Country of residence                               |
| department         | Optional | String                              | Department of a large organization or building     |
| subDepartment      | Optional | String                              | Sub-department of a large organization or building |
| streetName         | Optional | String                              | Street name                                        |
| buildingNumber     | Optional | String                              | Building number                                    |
| buildingName       | Optional | String                              | Building name                                      |
| floor              | Optional | String                              | Floor                                              |
| postBox            | Optional | String                              | Post box                                           |
| room               | Optional | String                              | Room number                                        |
| postcode           | Optional | String                              | Zip code, Postal code                              |
| townLocationName   | Optional | String                              | Name of a specific location within a city          |
| districtName       | Optional | String                              | District                                           |
| countrySubDivision | Optional | String                              | Division within a country                          |
| addressLine        | Optional | array\<String>                      | Detailed address                                   |
### 2-1-2. AddressTypeCode
- A code used to identify the classification or type of an address.

| Code | Name        | Description                                                                                                           |
| :--- | :---------- | :-------------------------------------------------------------------------------------------------------------------- |
| HOME | Residential | Address is the home address.                                                                                          |
| BIZZ | Business    | Address is the business address.                                                                                      |
| GEOG | Geographic  | Address is the unspecified physical(geographical) address suitable for identification of the natural or legal person. |

### 2-1-3. CountryCode
* Two-letter country code determined by ISO-3166-1 alpha-2. e.g.) `KR`, `JP`, `US`, etc.
* [https://www.iso.org/obp/ui/#search](https://www.iso.org/obp/ui/#search)

### 2-1-4. DateAndPlaceOfBirth
- Information regarding the date of birth and place of birth.


| Name         | Required | Type   | Description    |
| :----------- | :------- | :----- | :------------- |
| dateOfBirth  | Required | Date   | Date of birth  |
| placeOfBirth | Required | String | Place of birth |

### 2-1-5. LegalPerson
- Information about a corporation.
- Either `geographicAddress` or `nationalIdentification` is mandatory.

| Name                   | Required | Type                                              | Description                                                                                                                                              |
| :--------------------- | :------- | :------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------- |
| name                   | Required | LegalPersonName              | An object containing the official name information of a corporation.                                                                                     |
| geographicAddress      | Optional | array\<Address>                       | An object containing the address information of a corporation.                                                                                           |
| customerIdentification | Optional | String                                            | A unique number assigned by a VASP to identify a corporation as a customer.                                                                              |
| nationalIdentification | Optional | NationalIdentification | An object containing numbers such as the corporate registration number and tax identification number, used for official identification of a corporation. |
| countryOfRegistration  | Required | CoununtryCode     | Country of registration                                                                                                                                  |
### 2-1-6. LegalPersonName
- Information related to the name of the corporation.

| Name                   | Required | Type                                                      | Description                                                                          |
| :--------------------- | :------- | :-------------------------------------------------------- | :----------------------------------------------------------------------------------- |
| nameIdentifier         | Required | array\<LegalPersonNameID>           | An object that can include one or more names, such as legal names, trade names, etc. |
| localNameIdentifier    | Optional | array\<LocalLegalPersonNameID> | An object containing the name of the corporation in the local language.              |
| phoneticNameIdentifier | Optional | array\<LocalLegalPersonNameID> | An object containing phonetic names based on pronunciation.                          |

### 2-1-7. LegalPersonNameID
- Information for specifically identifying the name of the corporation.

| Name                          | Required | Type                                                | Description                                                                       |
| :---------------------------- | :------- | :-------------------------------------------------- | :-------------------------------------------------------------------------------- |
| legalPersonName               | Required | String                                              | The name of the corporation as used in legal documents or official registrations. |
| legalPersonNameIdentifierType | Required |LegalPersonNameTypeCode | An object representing the type of corporation name.                              |

### 2-1-8. LegalPersonNameTypeCode
- A code used to distinguish the types of corporation names.

| Code | Name         | Description                                                                                                                                          |
| :--- | :----------- | :--------------------------------------------------------------------------------------------------------------------------------------------------- |
| LEGL | Legal name   | Official name under which an organisation is registered.                                                                                             |
| SHRT | Short name   | Specifies the short name of the organisation.                                                                                                        |
| TRAD | Trading name | Name used by a business for commercial purposes, although its registered legal name, used for contracts and other formal situations, may be another. |

### 2-1-9. LocalLegalPersonNameID
- Information expressing the name of the corporation in the local language of the region or country where the corporation is located.

| Name                          | Required | Type                                                | Description                                                |
| :---------------------------- | :------- | :-------------------------------------------------- | :--------------------------------------------------------- |
| legalPersonName               | Required | String                                              | The name of the corporation in the local language.         |
| legalPersonNameIdentifierType | Required | LegalPersonNameTypeCode| An object representing the type of the corporation's name. |

### 2-1-10. LocalNaturalPersonNameID
- Information for identifying an individual's (natural person's) name according to the local region or language.


| Name                | Required | Type                                                    | Description                                                                                                          |
| :------------------ | :------- | :------------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------- |
| primaryIdentifier   | Required | String                                                  | Enter the last name (surname), and if it cannot be separated, indicate the surname and first name in order together. |
| secondaryIdentifier | Optional | String                                                  | Enter the first name, and if it cannot be separated, omit it.                                                        |
| nameIdentifierType  | Required | NaturalPersonNameTypeCode| An object that represents the type of name., Default: 'LEGL'(=Legal)                                                 |

### 2-1-11. NationalIdentification
- Information about a unique identification number or code used to identify an individual.

| Name | Required | Type | Description |
| --- | --- | --- | --- |
| nationalIdentifier | Required | String | A unique identification number for an individual or corporation.|
| nationalIdentifierType | Required| NationalIdentifierTypeCode | An object representing the type of identification number. |
| countryOfIssue | Optional | CountryCode | The country where the identification number was issued. (only used with 'naturalPerson') |
| registrationAuthority | Optional | RegistrationAuthority | - An object containing information about the institution that issued the identification number. <br /> -Used only when the value of 'nationalIdentifierType' is not 'LEIX'. |

### 2-1-12. NationalIdentifierTypeCode
- A code used to distinguish the types of an individual's national identification number.

| Code | Name                               | Description                                                                                                                                    |
| :--- | :--------------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------- |
| ARNU | Alien registration number          | Number assigned by a government agency to identify foreign nationals.                                                                          |
| CCPT | Passport number                    | Number assigned by a passport authority.                                                                                                       |
| RAID | Registration authority identifier  | Identifier of a legal entity as maintained by a registration authority.                                                                        |
| DRLC | Driver license number              | Number assigned to a driver's license.                                                                                                         |
| FIIN | Foreign investment identity number | Number assigned to a foreign investor(other than the alien number).                                                                            |
| TXID | Tax identification number          | Number assigned by a tax authority to an entity.                                                                                               |
| SOCS | Social security number             | Number assigned by a social security agency.                                                                                                   |
| IDCD | Identity card number               | Number assigned by a national authority to an identity card.                                                                                   |
| LEIX | Legal Entity Identifier            | Legal Entity Identifier (LEI) assigned in accordance with ISO 174421                                                                           |
| MISC | Unspecified                        | A national identifier which may be known but which cannot otherwise be categorized or the category of which the sender is unable to determine. |


### 2-1-13. NaturalPerson
- Information that can clearly identify an individual (natural person), such as identification information, address, national identification number, etc.

| Name                   | Required | Type                                        | Description                                                                           |
| :--------------------- | :------- | :------------------------------------------ | :------------------------------------------------------------------------------------ |
| name                  | Required | NaturalPersonName   | An object designed to contain name information.                                       |
| dateAndPlaceOfBirth    | Optional | DateAndPlaceOfBirth | An object designed to contain information about the date of birth and place of birth. |
| customerIdentification | Optional | String                                      | An identifier (UID or IDX) assigned by a VASP to distinguish users.                   |
| countryOfResidence     | Optional | CountryCode                 | Information about the country of residence.                                           |

### 2-1-14. NaturalPersonName
- Information regarding the name of an individual (natural person).

| Name                   | Required | Type                                                 | Description                                                                                                                                                 |
| :--------------------- | :------- | :--------------------------------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| nameIdentifier         | Required | array\<NaturalPersonNameID>  | An object for entering the legal name. When transacting between domestic VASPs, enter in Korean, and when transacting with foreign VASPs, enter in English. |
| localNameIdentifier    | Optional | array\<NaturalPersonNameID>  | An object for providing the Local name additionally when transacting with foreign VASPs.                                                                    |
| phoneticNameIdentifier | Optional | array\<NaturalPersonNameID> | An object containing phonetic names based on pronunciation.|

### 2-1-15. NaturalPersonNameID
- Specific identification information regarding the name of an individual (natural person).

| Name                | Required | Type                                                    | Description                                                                                                      |
| :------------------ | :------- | :------------------------------------------------------ | :--------------------------------------------------------------------------------------------------------------- |
| primaryIdentifier   | Required | string                                                  | Enter the last name (surname), and if it cannot be separated, list the surname and first name together in order. |
| nameIdentifierType  | Required | NaturalPersonNameTypeCode | An object that represents the type of name., Default: 'LEGL'(=Legal)                                             |
| secondaryIdentifier | Optional | string                                                  | Enter the first name, and if it cannot be separated, omit it.                                                    |


### 2-1-16. NaturalPersonNameTypeCode 
- A code used to distinguish the types of an individual's (natural person's) name.


| Code | Name          | Description                                                                                                                                        |
| :--- | :------------ | :------------------------------------------------------------------------------------------------------------------------------------------------- |
| ALIA | Alias name    | A name other than the legal name by which a natural person is also known.                                                                          |
| BIRT | Name at birth | The name given to a natural person at birth.                                                                                                       |
| MAID | Maiden name   | The original name of a natural person who has changed their name after marriage.                                                                   |
| LEGL | Legal name    | The name that identifies a natural person for legal, official or administrative purposes.                                                          |
| MISC | Unspecified   | A name by which a natural person maybe known but which cannot otherwise be categorized or the category of which the sender is unable to determine. |

### 2-1-17. Person

- Information used to distinguish individuals or natural persons involved in a transaction.**Either `naturalPerson` or `legalPerson` is mandatory.**

| Name          | Required | Type                            | Description                                                |
| :------------ | :------- | :------------------------------ | :--------------------------------------------------------- |
| naturalPerson | Optional | NaturalPerson | An object designed to set information about an individual. |
| legalPerson   | Optional | LegalPerson     | An object designed to set information about a corporation. |

### 2-1-18. RegistrationAuthority
* An 8-digit code representing the corporate registration authority.
* Type: Text
* Format: RA000099
* Entire list of code can be found here: [https://www.gleif.org/en/about-lei/code-lists/gleif-registration-authorities-list](https://www.gleif.org/en/about-lei/code-lists/gleif-registration-authorities-list) (There is Excel file at the end of the page.)

## 2-5. Example 
* [Json Schema](https://github.com/codevasp-lab/ivms101/blob/main/json-schema.json)
* [Complete Example - Natural Person](https://github.com/codevasp-lab/ivms101/blob/main/complete-example.json)
* [Complete Example - Legal Person](https://github.com/codevasp-lab/ivms101/blob/main/complete-example-legal-person.json)
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
```
{
  "Originator": {
    "originatorPersons": [
      {
        "naturalPerson": {
          "name": {
            "nameIdentifier": [
              {
                "primaryIdentifier": "Doe",
                "secondaryIdentifier": "John",
                "nameIdentifierType": "LEGL"
              }
            ],
          },
          "dateAndPlaceOfBirth": {
            "dateOfBirth": "1990-01-01",
          },
          "customerIdentification":"UID"
        }
      }
    ],
    "accountNumber": [
      "rJChk8e71gxVhyJSr1srzZxWhVisWMMYKZ:tag or memo"
    ]
  },
  "Beneficiary": {
  },
  "OriginatingVASP": {
  }
}
```

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

```
{
  "Originator": {
    "originatorPersons": [
      {
        "legalPerson": {
          "name": {
            "nameIdentifier": [
              {
                "legalPersonName": "CodeVASP Inc.",
                "legalPersonNameIdentifierType": "LEGL"
              }
            ]
          },
          "countryOfRegistration": "KR",
          "customerIdentification":"UID"
        }
      },
      {
        "naturalPerson": {
          "name": {
            "nameIdentifier": [
              {
                "primaryIdentifier": "Doe",
                "secondaryIdentifier": "John",
                "nameIdentifierType": "LEGL"
              }
            ],
          }
        }
      },
    ],
    "accountNumber": [
      "rJChk8e71gxVhyJSr1srzZxWhVisWMMYKZ:tag or memo"
    ]
  },
  "Beneficiary": {
  },
  "OriginatingVASP": {
    
  }
}
```

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

```
{
  "Originator": {
  },
  "Beneficiary": {
    "beneficiaryPersons": [
      {
        "naturalPerson": {
          "name": {
            "nameIdentifier": [
              {
                "primaryIdentifier": "Doe",
                "secondaryIdentifier": "John",
                "nameIdentifierType": "LEGL"
              }
            ],
          },
        }
      }
    ],
    "accountNumber": [
      "rHcFoo6a9qT5NHiVn1THQRhsEGcxtYCV4d:tag or memo"
    ]
  },
  "OriginatingVASP": {
    
  }
}
```

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
```
{
  "Originator": {
  },
  "Beneficiary": {
    "beneficiaryPersons": [
      {
        "legalPerson": {
          "name": {
            "nameIdentifier": [
              {
                "legalPersonName": "CodeVASP Inc.",
                "legalPersonNameIdentifierType": "LEGL"
              }
            ]
          }
        }
      },
      {
        "naturalPerson": {
          "name": {
            "nameIdentifier": [
              {
                "primaryIdentifier": "Doe",
                "secondaryIdentifier": "John",
                "nameIdentifierType": "LEGL"
              }
            ],
          }
        }
      },
    ],
    "accountNumber": [
      "rHcFoo6a9qT5NHiVn1THQRhsEGcxtYCV4d:tag or memo"
    ]
  },
  "OriginatingVASP": {

  }
}
```

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

  

```

{

"Originator": {

},

"Beneficiary": {

},

"OriginatingVASP": {

"originatingVASP": {

"legalPerson": {

"name": {

"nameIdentifier": [

{

"legalPersonName": "CodeVASP Inc.",

"legalPersonNameIdentifierType": "LEGL"

}

]

},

"geographicAddress": [

{

"addressType": "GEOG",

"townName": "Seoul",

"country": "KR"

}

],

"nationalIdentification": {

"nationalIdentifier": "EXAMPLE-TAX-ID",

"nationalIdentifierType": "RAID",

},

"countryOfRegistration": "KR"

}

}

}

}

```

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