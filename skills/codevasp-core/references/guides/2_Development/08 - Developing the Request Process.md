# 08 - Developing the Request Process

## 1. Before You Start 
* After 'Asset Transfer Authorization', you must complete the process with either  'Report Transfer Result (TX Hash)' or 'Finish Transfer'.
* If the asset transfer is canceled, the status cannot be changed and you must restart from 'Asset Transfer Authorization'.
* Treat any response outside of the API specifications as an error and contact the CodeVASP team if it persists.
* Always save the generated 'transferId'.
* The receiving VASP may manage a list of VASPs it accepts assets from (especially Korean VASPs).
    * A VASP outside of the list will get a rejection or error at the 'Virtual Asset Address Search' or 'Asset Transfer Authorization' process.
 
## 2. On Dashboard
* **Main Tabs**: **Development**  
* **Dropdown:** **Request Test**
  * Response Test
  * **Request Test** (Select)
  * Environment Info
  * IP Whitelist
 
### **Development Environment Request Test**
* Displays only data confirmed by CodeVASP from requests to `CODExchange-kor` and `CODExchange-non-kor`.
* This test is conducted only in the development environment.

### **Test Case List**
* **TC-A** Virtual Asset Address Search
* **TC-B** Asset Transfer Authorization
* **TC-C** Report Transfer Result (TX Hash)
* **TC-D** Finish Transfer 

There are two sets of test data tables for development purposes.

### **Section 1: CodeVASP TEST VASP**
[1] Data you can use when sending requests to CodeVASP.

| # | Protocol | VASP Name | Entity ID | Test Type | Account Type | Symbol | Wallet Address | ... |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |:----|
| 1 | code | CODExchange-kor | codexchange-kor | NORMAL | Individual | BTC | test wallet | ... |
| 2 | code | CODExchange-non-kor | codexchange-non-kor | NOT KYC | Individual | BTC | 3Hb4C5kQznvq8B4E3LWj2V7xgZu43VjVWC | ... |
| 3 | code | CODExchange-non-kor | codexchange-non-kor | SANCTION | Individual | XRP | 3Hb4C5kQznvq8B4E3LWj2V7xgZu43VjVWCDDS | ... |

### **Section 2: VASP TEST DATA**
[2] Data you can use when sending requests to other VASPs. Followings are some of examples.

| # | Protocol | VASP Name | Entity ID | Test Type | Account Type | Symbol | Wallet Address | ... |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |:----|
| 1 | code | bithumb | bithumb | NORMAL | Individual | BTC | 12b5GwvDXZMLjjtHSm tDc1djttWpKG1anj | ... |
| 2 | code | bithumb | bithumb | NORMAL | Corporate | BTC | 0xbB622Fe5B25cA92382FD61212B88f1283f20f6bA | ... |
| 3 | code | bithumb | bithumb | SANCTION | Individual | btc | 3AZcqNQ1NzDNPLfsmJ5FRTuj8Td1FY7kvB | ... |
| 4 | code | Bybit | bybit | NORMAL | Individual | ETH | 0x7eb2777d91df3e7fb1aa2376194df40d6257c34a | ... |
| 5 | code | codevasp | codevasp | NORMAL | Corporate | BTC | n3DnigSF1Q61SP98oyLRapryX4KWkFrZkT | ... |
| 6 | code | codevasp | codevasp | SANCTION | Corporate | BTC | mwF1Xa4hAXp76ZVsoNqedkz8GN2YSdERvs | ... |
| 7 | code | gateio | gateio | NORMAL | Individual | USDT | QV6AYH67QIUBT7BTSTUGZJQZLTKT36IR5XXCE3OBET4VNOUCJ2PXGGKMKE | ... |
| 8 | code | hanbitco | hanbitco | NOT KYC | Individual | BTC | 3Hb4C5kQznvq8B4E3LWj2V7xgZu43VjVWCdasd | ... |
| 9 | code | InfiniteBlock Corp | infiniteblock-corp | NORMAL | Corporate | BTC | n3DnigSF1Q61SP98oyLRapryX4KWkFrZkT | ... |

