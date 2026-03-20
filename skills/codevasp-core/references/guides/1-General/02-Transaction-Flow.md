# 02_Transaction_Flow

> **Note:** The flows below represent a simplified version of a VASP's deposit and withdrawal processes with Travel Rule requirements applied. The actual implementation and sequential steps may vary depending on each individual VASP's internal policies and system architecture.

```mermaid
graph TD
    WithdrawalBegins("Withdrawal begins - User enters required information")
    VaspListSearch("[API Call] VASP List Search")
    ProvideVaspList("Provide the VASP list to user")
    UserSelectsBeneficiary("User selects beneficiary VASP")
    SearchVaspByWalletReq("[API Call] Search VASP by Wallet Request")
    SearchVaspByWalletRes("[API Call] Search VASP by Wallet Result")
    AcquisitionVaspInfo("Acquisition of beneficiary VASP info")
    VirtualAssetAddressSearch("[API Call] Virtual Asset Address Search")
    AssetTransferAuth("[API Call] Asset Transfer Authorization")
    GuideUser1("[Termination] Guide user")
    GuideUser2("[Termination] Guide user")
    Verified("Verified")
    Pending("Pending")
    Processing("Processing")
    WaitConfirmed("Wait-Confirmed")
    Confirmed("Confirmed")
    OnChainTransaction("On-Chain Transaction")
    ReportTransferResult("[API Call] Report Transfer Result")
    SaveData("Save Data")
    Canceled("Canceled")
    FinishTransfer("[API Call] Finish Transfer")
    GuideUser3("[Termination] Guide user")

    WithdrawalBegins -->|Option 1| VaspListSearch
    WithdrawalBegins -->|Option 2| SearchVaspByWalletReq

    VaspListSearch --> ProvideVaspList
    ProvideVaspList --> UserSelectsBeneficiary
    UserSelectsBeneficiary --> AcquisitionVaspInfo

    SearchVaspByWalletReq --> SearchVaspByWalletRes
    SearchVaspByWalletRes --> AcquisitionVaspInfo

    AcquisitionVaspInfo --> VirtualAssetAddressSearch

    VirtualAssetAddressSearch -->|No| GuideUser1
    VirtualAssetAddressSearch -.->|No: When option 1 preceded| SearchVaspByWalletReq
    VirtualAssetAddressSearch -->|Yes| AssetTransferAuth

    AssetTransferAuth -->|No| GuideUser2
    AssetTransferAuth -->|Yes| Verified

    Verified -->|Yes| Pending
    Verified -->|No: Rejected due to internal policy| Canceled

    Pending -->|Yes| Processing
    Pending -->|No| Canceled

    Processing -->|Yes| WaitConfirmed
    Processing -->|No| Canceled

    WaitConfirmed -->|Yes| Confirmed
    WaitConfirmed -->|No| Canceled

    Confirmed -->|Yes| OnChainTransaction
    OnChainTransaction -->|Yes| ReportTransferResult
    ReportTransferResult -->|Yes| SaveData

    Canceled -->|Yes| FinishTransfer
    FinishTransfer -->|Yes| GuideUser3

    subgraph "Process on the blockchain"
        WaitConfirmed
        Confirmed
        OnChainTransaction
    end
```
### 2. Deposit 
```mermaid
graph TD
    OnChainMonitoring("On-Chain transaction monitoring")
    TransactionDetected("Transaction detected")
    DataMapping("Transaction & Travel Rule Data Mapping")
    PolicyOption("Option: Depends on YOUR policy")
    WaitPending("Wait for confirm(Pending)")
    
    TransactionStatusSearch("[API Call] Transaction Status Search")
    ReportTransferConfirmed("[API Call] Report Transfer Result: Confirmed")
    FinishTransferCanceled("[API Call] Finish Transfer: Canceled")
    
    TransferVerification("Transfer confirmation verification")
    GuideUser("[Termination] Guide user")
    
    SearchVaspReq("[API Call] Search VASP by TXID Request")
    SearchVaspRes("[API Call] Search VASP by TXID Result")
    SearchVaspResponse("[API Response] Search VASP by TXID Result Response")
    AssetTransferReq("[API Call] Asset Transfer Data Request")
    AssetTransferRes("[API Response] Asset Transfer Data Request Response")
    
    SaveTravelRuleData("Save Travel Rule Data")
    UpdateBalance("Update user balance")

    OnChainMonitoring --> TransactionDetected
    TransactionDetected --> DataMapping
    
    DataMapping -->|Yes| WaitPending
    DataMapping -->|No| PolicyOption
    
    PolicyOption --> SearchVaspReq
    
    WaitPending --> TransactionStatusSearch
    WaitPending --> ReportTransferConfirmed
    WaitPending --> FinishTransferCanceled
    
    TransactionStatusSearch --> TransferVerification
    ReportTransferConfirmed --> TransferVerification
    
    FinishTransferCanceled --> GuideUser
    
    SearchVaspReq --> SearchVaspRes
    SearchVaspRes --> SearchVaspResponse
    SearchVaspResponse --> AssetTransferReq
    AssetTransferReq --> AssetTransferRes
    
    TransferVerification --> SaveTravelRuleData
    AssetTransferRes --> SaveTravelRuleData
    
    SaveTravelRuleData --> UpdateBalance

```
