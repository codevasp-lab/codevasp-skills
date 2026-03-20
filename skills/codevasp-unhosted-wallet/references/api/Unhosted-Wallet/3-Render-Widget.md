# 3. Render Widget

* Dev: [https://wallet-verifier-dev.codevasp.com/widget/wallet-verifier.js](https://wallet-verifier-dev.codevasp.com/widget/wallet-verifier.js)
* Prod: [https://wallet-verifier.codevasp.com/widget/wallet-verifier.js](https://wallet-verifier.codevasp.com/widget/wallet-verifier.js)

***

# Render Widget

Render the widget using the token issued through the 'Issue Token' API. The widget automatically provides screens for wallet connection and message signing (Signature Proof).

1. Load the component
   ```html
   <script type="module" src={Widget Script}></script>
   ```
2. Add the element
   ```html
   <wallet-verifier
     id="wv"
     data-token={token}
     data-language="en"
   >
   </wallet-verifier>
   ```

### Vanilla JS Example
```html
<div id="wallet-verifier"></div>
<script type="module" 
  src="WIDGET_SCRIPT_URL"
></script>
<script>
  const el = document.createElement('wallet-verifier')

  el.setAttribute('data-token', 'YOUR_TOKEN')
  // Supported languages: en, et, cs, hu, pl, ru, uk, es, ko
  el.setAttribute('data-language', 'en')

  el.addEventListener('verification-complete', (event) => {
    console.log('complete', event.detail)
  })
  el.addEventListener('verification-error', (event) => {
    console.log('error', event.detail)
  })

  document.getElementById('wallet-verifier').appendChild(el)
</script>
```

### React Example
```jsx
import { useEffect, useRef } from 'react'

export default function WalletVerifierWidget({ token }) {
  const elRef = useRef(null)

  useEffect(() => {
    const script = document.createElement('script')
    script.type = 'module'
    script.src = 'WIDGET_SCRIPT_URL'
    document.body.appendChild(script)

    return () => {
      document.body.removeChild(script)
    }
  }, [])

  useEffect(() => {
    const el = elRef.current

    if (!el) return

    const onComplete = (event) => {
      const detail = event.detail
      console.log('complete', detail)
    }

    const onError = (event) => {
      const detail = event.detail
      console.log('error', detail)
    }

    el.addEventListener('verification-complete', onComplete)
    el.addEventListener('verification-error', onError)

    return () => {
      el.removeEventListener('verification-complete', onComplete)
      el.removeEventListener('verification-error', onError)
    }
  }, [])

  return (
    <wallet-verifier
      ref={elRef}
      data-token={token}
      // Supported languages: en, et, cs, hu, pl, ru, uk, es, ko
      data-language="en"
    />
  )
}
```

***

# Client Event Handling

Client events consist of `verification-complete` for success and `verification-error` for failure. Upon success, results are delivered via callback and can also be verified through the 'Get Verification Result' API. No callback is sent for failures.

## Verification Success Event

Upon successful verification, the widget emits a `verification-complete` event. The success event includes minimal result data, such as the unique Verification Session ID, status, and address.

```javascript
<script>
  document.getElementById('wv')
    .addEventListener('verification-complete', (event) => {
      // event.detail contains:
      // id, status, flow, address, asset, blockchain
      console.log('Verification result:', event.detail);
    });
</script>
```

| Name       | Type   | Description                                     |
| :--------- | :----- | :---------------------------------------------- |
| address    | string | user's wallet address                           |
| asset      | string | token ticker(coin)                              |
| blockchain | string | blockchain(network)                             |
| flow       | string | verification methods and flow (SIGNATURE_PROOF) |
| id         | string | unique identifying ID for verification session  |
| status     | string | verification result ("PENDING", "VERIFIED")     |

## Verification Failure Event

When verification fails, the browser triggers a `verification-error` event within the widget. The failure event includes an error code and a message.

```javascript
<script>
  document.getElementById('wv')
    .addEventListener('verification-error', (event) => {
      // event.detail contains: errorCode & message
      console.log('Verification error:', event.detail);
    });
</script>
```

| Name      | Type   | Description   |
| :-------- | :----- | :------------ |
| errorCode | string | error code    |
| message   | string | error message |

**errorCode**: Error code
* `GENERAL_API_ERROR`: Response Errors (Network, API Failures, etc.)
* `CRYPTOGRAPHIC_SIGNATURE_FLOW_ERROR`: Error during authentication cancellation, typically occurs when the user cancels the process.

> **Note**: The widget is designed to handle most errors internally by default. Even if an error occurs, users can typically retry the current step or restart the verification process from the beginning. If necessary, you can handle error events directly to apply custom logic or implement alternative behaviors. In particular, when a `GENERAL_API_ERROR` occurs, it is recommended to refresh the widget or initiate a new verification session for the user.
