# 01-Uppsala-Wallet-Screening

By providing a virtual asset wallet address, this API detects the risks related to the wallet address.

The risk detection results for the wallet address are provided as "BLACK/GRAY/WHITE/UNKNOWN," and if the detection results correspond to "BLACK" and "GRAY," additional information on the type of criminal damage is provided.

The Uppsala Wallet Screening API does not support development environment. Please use production environment.

> **Note**: This API is jointly operated by CodeVASP and Uppsala Security. For inquiries or access requests, please contact [partnership@codevasp.com](mailto:partnership@codevasp.com).

## Endpoint

`POST` `/v1/code/uppsala/wallet`

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| walletAddress | string | Required | Virtual asset wallet address. |
| chain | string | Required | Symbol for the virtual asset (e.g., "ETH"). Supported: BTC, ETH, SOL, LTC, TRX, EOS, XLM, ADA, BNB, BCH, XRP, BSC, KLAY, DASH, DOGE, ZEC, FTM, MATIC, AVAX. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Result (`NORMAL`, `ERROR`). |
| id | string | Traffic ID. |
| walletAddress | string | Requested wallet address. |
| securityCategory | string | Risk level (`BLACK`, `GRAY`, `WHITE`, `UNKNOWN`). |
| reasonMsg | string | Detailed message if error. |
| securityTags | array | List of tags if BLACK/GRAY (e.g., "Gambling"). |

**Security Categories:**
* `BLACK`: Highly suspicious
* `GRAY`: Suspicious
* `WHITE`: Normal
* `UNKNOWN`: Unknown (Can't verify the risk assessment of the wallet address)

**securityTags**: If the requested virtual asset wallet address is "BLACK" or "GRAY," information on the type of criminal damage is provided.

| Category | TAG                         |
| :------- | :-------------------------- |
| BLACK    | Adware                      |
| BLACK    | Black Market                |
| BLACK    | Child Sexual Exploitation   |
| BLACK    | Counterfeit Money           |
| BLACK    | Credit Card Fraud           |
| BLACK    | Cryptocurrency Laundering   |
| BLACK    | Cryptojacking               |
| BLACK    | Double Spend Attack         |
| BLACK    | Drug                        |
| BLACK    | Exploits                    |
| BLACK    | Fake ICO                    |
| BLACK    | Hack                        |
| BLACK    | Impersonation               |
| BLACK    | Information Leakage         |
| BLACK    | Lazarus                     |
| BLACK    | Malware                     |
| BLACK    | Murder                      |
| BLACK    | Patchwork                   |
| BLACK    | Phishing                    |
| BLACK    | Ponzi                       |
| BLACK    | Ransomware                  |
| BLACK    | Scam                        |
| BLACK    | Spam                        |
| BLACK    | Terrorism                   |
| BLACK    | Vishing                     |
| BLACK    | Weapon                      |
| BLACK    | Spyware                     |
| BLACK    | TeamTNT                     |
| BLACK    | Botnet                      |
| BLACK    | Vulnerability               |
| GRAY     | Community                   |
| GRAY     | Covid19                     |
| GRAY     | Cryptocurrency              |
| GRAY     | Darknet                     |
| GRAY     | Escrow                      |
| GRAY     | Gambling                    |
| GRAY     | Hosting                     |
| GRAY     | Locked                      |
| GRAY     | Pixel Tracker               |
| GRAY     | Pornography                 |
| GRAY     | Wiki                        |
| GRAY     | Domain Generation Algorithm |
| GRAY     | Non-Compliant VASP (KR)     |

## Examples

### Request
```bash
curl --request POST \
     --url https://trapi-dev.codevasp.com/v1/code/uppsala/wallet \
     --header 'X-Code-Req-Datetime: 2024-03-04T15:10Z' \
     --header 'X-Code-Req-Nonce: 989166249' \
     --header 'X-Code-Req-PubKey: YourPubkey' \
     --header 'X-Code-Req-Signature: Signature' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "walletAddress": "0x67c9c03dC2aa8C7EcB6b30494aA757367ed2981C",
  "chain": "ETH"
}'
```

### Response
```json
{
  "result": "NORMAL",
  "id": "dummy-0313013706-57359efa",
  "walletAddress": "0x67c9c03dC2aa8C7EcB6b30494aA757367ed2981",
  "securityCategory": "BLACK",
  "securityTags": [
    "Gambling",
    "Malware",
    "Darknet"
  ]
}
```
