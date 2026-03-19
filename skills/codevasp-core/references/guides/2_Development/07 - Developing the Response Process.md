# 07 - Developing the Response Process

## 1. Before You Start 
* We recommend developing the receiving first to better understand the encryption and decryption process.
* Receiving refers to depositing assets into our customer's wallet.
* With Travel Rule APIs, responding properly is as important as requesting.
* To ensure consistent communication between VASPs, please read the API guide carefully and develop according to the specifications. (All HTTP responses must return status 200.)
* The scope of incoming transfers and the receiving process may vary depending on your internal intake control policies.
* We recommend reviewing your internal policies first.
* Save all received 'transferId's.

## 2. On Dashboard
You can use the dashboard to test and verify the code you implemented in your development environment. Make sure to complete all the test before moving on to the next step. To get started, please enter the required information.

---

### **Navigation & Menus**
* **Main Tabs:** **Development**
* **Dropdown:** 
    * **Response Test** (Select)
    * Request Test
    * Environment Info
    * IP Whitelist
* **Guide:**
    * Enter a wallet address for testing that is managed by VASP.
    * Stored test information will be disclosed to other VASPs and may be used for testing purposes.

---

### **Example Test Data**
Provided your own test data on dashboard and save:

| Type1 | Type2 | Type3 | No Tag (Ticker/Wallet) | With Tag (Ticker/Wallet/Tag) | Legal Name |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Originator** | Individual | Normal | BTC / 1A1zP1eP5... | XRP / rEb8TK3gBg... / 123456789 | |
| | Legal | Normal (1 representative) | SOL / 6Ue2k1f7R... | TON / UQCCX_nk8Q2... / 112233445 | Code |
| | Legal | Normal (2 representatives) | ETH / 0x742d35C... | ATOM / cosmos1jp9... / 998877665 | CodeVASP |
| **Beneficiary** | Individual | Normal | BTC / 1KXRWxCiR... | BTC / 1KXRWxCiR... / 123 | - |
| | Individual | Sanction | ETH / 0xSanction | BTC / 3AtPZrpc4I... / 123 | - |
| | Legal | Normal (1 representative) | BTC / 3AtPZrpc4I... | BTC / 3AtPZrpc4I... / 123 | codevasp |
| | Legal | Normal (2 representatives) | BTC / 1BvBMSEYs... | BTC / 1BvBMSEYs... / 12345 | codevasp2 |

---

### **Notes**
* The default Originator test data is provided by CodeVASP.
* If you need to modify it for self-transfer tests or other purposes, click the edit button on the right.
* Wallet addresses should be entered with and without a tag.
* Test data for legal entities should be entered separately for those with one representative (CEO) and two representatives.
* Beneficiary information must also be categorized as either an individual or a legal entity, and all required fields must be completed accordingly.
* Please enter the TXID, VOUT as well!
* Other VASPs may use the information stored here to send test requests in their development environments. Please ensure it remains available at all times. 🤗🤗🤗

---

### **Main Test Case List**
* **TC-A** Virtual Asset Address Search
* **TC-B** Asset Transfer Authorization
* **TC-C** Report Transfer Result (TX Hash)
* **TC-D** Finish Transfer
* **TC-E** Health Check
* **TC-F** Verify Wallet Address that contain :
* Search VASP By TXID

---

### **Expanded Section: Asset Transfer Data Request**
| ID | Scenario Description  |
| :--- | :--- | 
| **TC-TX-01** | Not found TXID 
| **TC-TX-02** | Missing Required Information for IVMS101 Data Configuration
| **TC-TX-03** | When the passed beneficiary name is English 
| **TC-TX-04** | When the passed beneficiary name is a local name 
| **TC-TX-05** | When the passed beneficiary name is English (legal) 
| **TC-TX-06** | When the passed beneficiary name is a local name (legal)

---

* Click the arrow to expand the toggle.
* Click the button to test.
* **Guidelines**
  * Please run the entire tests.
  * CodeVASP team will provide feedback on the completed tests.
  * Make sure to follow the guide.
