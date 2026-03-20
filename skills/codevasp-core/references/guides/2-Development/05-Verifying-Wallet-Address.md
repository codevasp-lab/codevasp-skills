# 05 - Verifying Wallet Address

A wallet address may have various formats, combining the address itself, a tag or memo, and a delimiter. It's crucial to note that in the IVMS101 protocol, this entire combination is treated as a single string. Thus, there are 4 possible formats of wallet addresses.

| No | Description                                       | Example Assets                                                      | Address Format             |
| :- | :------------------------------------------------ | :------------------------------------------------------------------ | :------------------------- |
| 1  | Address only                                      | BTC , ETH…                                                          | address                    |
| 2  | Combination of address and tag or memo            | EOS,  XRP…                                                          | address:memo or tag        |
| 3  | ':' included in the address                       | BCH, Kaspa…                                                         | prefix:address             |
| 4  | ':' included in the address and tag or memo added | Not existing at the moment, but potentially possible in the future. | prefix:address:memo or tag |

## 1. Verify Address First
```mermaid
graph TD
    %% Nodes
    A["Raw address<br/>1. address<br/>2. address:memo or Tag<br/>3. currency:address<br/>4. currency:address:memo or tag"]
    B["Check for a match"]
    C["Check for ':'"]
    D["Match"]
    E["':' Right split"]
    F["Mismatch"]
    G["Check for a match"]
    H["Match"]
    I["Mismatch"]

    %% Flow logic
    A --> B
    B -- "Yes" --> D
    B -- "No" --> C
    C -- "Yes" --> E
    C -- "No" --> F
    E --> G
    G -- "Yes" --> H
    G -- "No" --> I

    %% Styling
    classDef match fill:#d2e9e9,stroke:#5fb8b8,color:#333
    classDef mismatch fill:#d9d9d9,stroke:#999,color:#333
    classDef process fill:#f4f4f4,stroke:#ccc,color:#333

    class D,H match
    class F,I mismatch
    class A,B,C,E,G process

    %% Link Styling (matching blue/red arrows)
    linkStyle 1 stroke:#aed6f1,stroke-width:2px,color:#5dade2
    linkStyle 2 stroke:#f5b7b1,stroke-width:2px,color:#ec7063
    linkStyle 3 stroke:#aed6f1,stroke-width:2px,color:#5dade2
    linkStyle 4 stroke:#f5b7b1,stroke-width:2px,color:#ec7063
    linkStyle 6 stroke:#aed6f1,stroke-width:2px,color:#5dade2
    linkStyle 7 stroke:#f5b7b1,stroke-width:2px,color:#ec7063

```
1. Verify address with the received string as it is.
2. If it fails, check if the string contains a ':'. If so, split the string at the rightmost colon.
3. Re-verify with the first segment of the splitted string.

> **📌Tips**
> * When splitting, regardless of colon count, use the **rightmost colon** as the basis.
> * You must first try **verifying the received string**, irrespective of the presence of a colon.

## 2. Verify ':' first

```mermaid
flowchart TD
    %% Main Nodes
    Raw1["Raw address<br/>1. address<br/>2. address:memo or Tag<br/>3. currency:address<br/>4. currency:address:memo or tag"]
    CheckColon["Check for ':'"]
    CheckMatch1["Check for a match"]
    Match1["Match"]
    Mismatch1["Mismatch"]
    RightSplit["':' Right split"]
    CheckMatch2["Check for a match"]
    Match2["Match"]

    %% Nested Fallback Logic
    subgraph Fallback_Logic ["Fallback Verification"]
        Raw2["Raw address"]
        Mismatch2["Mismatch"]
        Match3["Match"]
    end

    %% Flow Connections
    Raw1 --> CheckColon
    CheckColon -- "No" --> CheckMatch1
    CheckColon -- "Yes" --> RightSplit
    
    CheckMatch1 -- "Yes" --> Match1
    CheckMatch1 -- "No" --> Mismatch1
    
    RightSplit --> CheckMatch2
    CheckMatch2 -- "Yes" --> Match2
    CheckMatch2 -- "No" --> Raw2
    
    Raw2 -- "No" --> Mismatch2
    Raw2 -- "Yes" --> Match3

    %% Global Styling
    classDef match fill:#d2e9e9,stroke:#5fb8b8,color:#333
    classDef mismatch fill:#d9d9d9,stroke:#999,color:#333
    classDef step fill:#f4f4f4,stroke:#ccc,color:#333

    class Match1,Match2,Match3 match
    class Mismatch1,Mismatch2 mismatch
    class Raw1,CheckColon,CheckMatch1,RightSplit,CheckMatch2,Raw2 step

    %% Link Styling (Yes = Blue, No = Red)
    %% Calculated indices based on connection order
    linkStyle 1,4,7,8 stroke:#f5b7b1,stroke-width:2px,color:#ec7063 %% Red/No Path
    linkStyle 2,3,6,9 stroke:#aed6f1,stroke-width:2px,color:#5dade2 %% Blue/Yes Path
    
    %% Subgraph Box Styling
    style Fallback_Logic stroke-dasharray: 5 5, fill:transparent, stroke:#5dade2
```


1. Verify the address directly if no colon (':') is present.
2. If a colon is present, split the string at the rightmost colon and verify the address using the first segment.
3. If it fails, verify the existence of the address using the received string as it is, including the colon.