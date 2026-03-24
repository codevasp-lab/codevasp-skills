# 02-Uppsala-TXID-Screening

By providing a blockchain transaction id(hash), this API detects the risks related to the wallet addresses that are related to the txid.

The risk detection results for the TXID are provided as "BLACK/GRAY/WHITE/UNKNOWN," and if the detection results correspond to "BLACK" and "GRAY," additional information on the type of criminal damage is provided.

The Uppsala Wallet Screening API does not support development environment. Please use production environment.

> **Note**: This API is jointly operated by CodeVASP and Uppsala Security. For inquiries or access requests, please contact [partnership@codevasp.com](mailto:partnership@codevasp.com).

## Endpoint

`POST` `/v1/code/uppsala/tx-hash`

## Request Parameters

### Body Parameters
| Name | Type | Required | Description |
| :--- | :--- | :------- | :---------- |
| txid | string | Required | Blockchain transaction id. |
| chain | string | Required | Symbol for the virtual asset (e.g., "ETH"). Supported: BTC, ETH, SOL, LTC, TRX, EOS, XLM, ADA, BNB, BCH, XRP, BSC, KLAY, DASH, DOGE, ZEC, FTM, MATIC, AVAX. |

## Response

### Fields
| Name | Type | Description |
| :--- | :--- | :---------- |
| result | string | Result (`NORMAL`, `ERROR`). |
| id | string | Traffic ID. |
| senderWallets | array | List of sender wallet info. |
| receiverWallets | array | List of receiver wallet info. |
| reasonMsg | string | Detailed message if error. |

**WalletInfo Object:**
* `walletAddress`: Wallet address.
* `securityCategory`: Risk level (`BLACK`, `GRAY`, `WHITE`, `UNKNOWN`).
* `securityTags`: List of tags.
* `detailedDescription`: Description.

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
     --url https://trapi-dev.codevasp.com/v1/code/uppsala/tx-hash \
     --header 'X-Code-Req-Datetime: datetime' \
     --header 'X-Code-Req-Nonce: nonce' \
     --header 'X-Code-Req-PubKey: Your key' \
     --header 'X-Code-Req-Signature: Sig' \
     --header 'X-Request-Origin: code:yourVaspEntityId' \
     --header 'accept: application/json' \
     --header 'content-type: application/json' \
     --data '
{
  "txid": "0x655dd40d5919d01d7d6a84c8d0fb125552bd3be23eee0750f440d98783908344",
  "chain": "eth"
}
'
```

### Response
```json
{
  "id": "test5-0327042656-7ed4fd5a",
  "senderWallets": [
    {
      "walletAddress": "0x098b716b8aaf21512996dc57eb0615e2383e2f96",
      "securityCategory": "BLACK",
      "securityTags": [
        "Exploits",
        "Lazarus",
        "OFAC Sanctions"
      ],
      "detailedDescription": "Main wallet of Ronin Bridge Exploiter"
    }
  ],
  "receiverWallets": [
    {
      "walletAddress": "0x665660f65e94454a64b96693a67a41d440155617",
      "securityCategory": "BLACK",
      "securityTags": [
        "Exploits",
        "Lazarus"
      ],
      "detailedDescription": "Exploiter's intermediary wallet. Exploiter sends USDC here before swapping on Uniswap"
    }
  ],
  "result": "NORMAL",
  "statusCode": 200
}
```
