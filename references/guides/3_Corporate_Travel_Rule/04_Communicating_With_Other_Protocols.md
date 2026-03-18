# Communicating With Other Protocols

## 1. To GTR(specific to Binance)
* The process differs when communicating with Binance, which is integrated with the GTR solution. 
* Unlike Travel Rule processing for natural persons, ID-Connect is not supported for legal entities.

### 1-1. As an originator
* The communication process remains the same, but additional required fields are introduced:
    1. ‘address’ 
    2. ‘tag’
    3. ‘network’

### 1-2. As an beneficiary
* As of now, Binance does not proactively send Travel Rule data.
* If it is confirmed—e.g., via ‘Search VASP by TXID’—that the transaction originated from Binance, please verify the originator and beneficiary information using a ‘Asset Transfer Data Request’.
* Note that the originator information must be provided when making the verification request.
* The originator information can be obtained in the following two ways.
    * Enter the same KYB information as the receiving wallet. 
      (Originator’s wallet address is optional) ※Note: This will only allow self-transfers.
    * Request an explanation from the customer
* If the information does not match, Binance will return an ‘error’ response.
* For a smoother workflow, we recommend verifying against the beneficiary information first and initiating an explanation request to the customer.
```mermaid
graph LR
    subgraph ATDR [" [Asset Transfer Data Request] API "]
        direction LR
        A["Binance TX confirmed"]
        B["beneficiary = originator"]
        C(("originator verification request"))
        D["Match"]
        E["Next Step"]
        F["Mismatch"]
        G["Customer Explanation"]
        A --> B
        B --> C
        C --> D
        C --> F
        D --> E
        F --> G
    end

    %% Styling
    classDef tealNode fill:#5cb8bc,stroke:#5cb8bc,color:#fff,rx:10,ry:10
    classDef darkNode fill:#0b3d52,stroke:#0b3d52,color:#fff
    
    class A,B,D,E,F,G tealNode
    class C darkNode

```
