# Dev 03 - Header Parameter

## X-Code-Req-Nonce (Required, Unsigned Integer)
* This is a serial number set on the request to avoid duplicate requests.
* The request sending side sets a monotonically increasing value, or a random value which is not duplicated at each request.
* The CodeVASP server returns an error if the same value comes in within 100 seconds.

## X-Code-Req-PubKey (Required, Base64 String)
* This is a public key of the originating VASP and is used to encrypt or decrypt a message.
* The PubKey used in the CodeVASP is clearly VerifyKey that verifies the signature. But this is called PubKey because the public key used for encryption/decryption can be calculated from it.

## X-Code-Req-Signature (Required, String)
* This is a value obtained by concatenating fields in the order of (`X-Code-Req-Datetime`, `body`, `X-Code-Req-Nonce`) to generate a byte sequence, then signing it using the Private Key of the sending host with EdDSA (Ed25519).
* For details, please refer to the example which is distributed separately.\
  (Ed25519 [https://pynacl.readthedocs.io/en/latest/signing/?highlight=Ed25519#ed25519](https://pynacl.readthedocs.io/en/latest/signing/?highlight=Ed25519#ed25519))
* It is used to authenticate the sending VASP by the CodeVASP server, and to verify that the message has not been tampered with.
* It is only sent when the originating VASP sends a request and is not forwarded to the receiving VASP.
* It is not included when the receiving VASP sends a response message.

## X-Code-Req-Datetime (Required, String)
* This is the timestamp of when the request was sent, expressed in UTC time using ISO 8601 format. (e.g., `2022-06-31T23:59:59Z`)

## X-Request-Origin (Required, String)
* This is an identifier formed by joining the travel rule solution alliance name of the sending VASP and its VASP identifier within the alliance, separated by `:`.
  - e.g., `code:coinone`, `verifyvasp:15952089931162060684`

## X-Code-Req-Remote-PubKey (Optional, Base64 String)
* This is the public key of the receiving VASP, used for message encryption. Omit this header for APIs that do not encrypt the request body, or for messages sent directly to the CodeVASP server.
* An error is returned if the header value does not match the public key of the destination VASP registered in the CodeVASP Central Server. If the header is absent, verification is skipped.
* Since the receiving VASP's public key may be rotated (renewed), a sending VASP that receives a key mismatch error should re-fetch the public key and retry.