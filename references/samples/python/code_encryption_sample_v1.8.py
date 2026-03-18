import base64
import struct
from base64 import b64encode

import nacl.hash
from nacl.encoding import Base64Encoder
from nacl.public import Box
from nacl.signing import SigningKey, VerifyKey

sender_privkey_b64 = "kFW2RN0w/4hp/HJRRdVyzwCjxRncCnrsUn1a/7fxTRc="
sender_pubkey_b64 = "wR1Gtiixh2GuBQuki01oJuZ4znqwK/MgXYFwt8g+I+s="

receiver_privkey_b64 = "YSHamhe1y27aMt1aXl30mvlqlzI/TRScOrBy8vnNdUY="
receiver_pubkey_b64 = "wbFoqLVxayF7Uz+YSsixnEJr88ll/J+wOhRsbAQ/pO4="

# Encryption example uses this key pair.
# The code was changed as follows.
sender_privkey = SigningKey(sender_privkey_b64.encode('utf-8'),
                            encoder=nacl.encoding.Base64Encoder).to_curve25519_private_key()
sender_pubkey = VerifyKey(sender_pubkey_b64.encode('utf-8'),
                          encoder=nacl.encoding.Base64Encoder).to_curve25519_public_key()

# The code was changed as follows.
receiver_privkey = SigningKey(receiver_privkey_b64.encode('utf-8'),
                              encoder=nacl.encoding.Base64Encoder).to_curve25519_private_key()
receiver_pubkey = VerifyKey(receiver_pubkey_b64.encode('utf-8'),
                            encoder=nacl.encoding.Base64Encoder).to_curve25519_public_key()
# Generating Request Hash
origin = 'code:dummy'
datetime = '2022-04-06T18:25:07.512Z'
message = '{"ivms101":{"Beneficiary":{"accountNumber":["3AtPZrpc4iTHLBwsWJJAkzZqeL8L3axRbX"]}}}'
nonce = 8963

datetime_bytes = datetime.encode('utf-8')
message_bytes = message.encode('utf-8')
signing_target = struct.pack(f'!{len(datetime_bytes)}s{len(message_bytes)}sL',
                             datetime_bytes,
                             message_bytes,
                             nonce)

signature = SigningKey(sender_privkey_b64.encode('utf-8'),
                 encoder=nacl.encoding.Base64Encoder).sign(signing_target).signature

print("Signature: " + b64encode(signature).decode('utf-8'))

# Headers when VASP sends a request.
X_Request_Pubkey = sender_pubkey_b64  # caution: sender's public key
X_Request_Signature = signature
X_Request_Origin = origin
X_Request_Datetime = datetime

sender_box = Box(sender_privkey, receiver_pubkey)

encrypted = sender_box.encrypt(bytes(message, 'utf-8'))
print("Encrypted message: " + b64encode(encrypted).decode('utf-8'))
print("---------------------------------->>")

# and when VASP receives message.
# X-Request-Signature will be removed by CODE Central Server.
# So, VASP does not verify signature.

received_encrypted_message = 'fD3dxtfDtVoVHkoZJpmDrJRqG2fISRfQrOjvNDSACE2FVyL954LTTNxLUOE1zkBt4j0q6+630zPtIpr1nE4vs9YLhe0CZ3JKaKG/zdkrgBGOYINqf+brM6pwfXpX3CgF5JvgU+CNkUwjxeTnGUH2foQrtxljhJaWa9Uh7w=='

# Initialize code Box
receiver_box = Box(receiver_privkey, sender_pubkey)
decrypted = receiver_box.decrypt(base64.b64decode(received_encrypted_message.encode('utf-8')))

print("Decrypted message: " + decrypted.decode('utf-8'))
