import base64
from base64 import b64encode

import nacl.hash
from nacl.encoding import Base64Encoder
from nacl.public import Box
from nacl.signing import SigningKey, VerifyKey

code_privkey_b64 = "6S1xXrPgW8pJtM3fPN0xLf5cGp8usE5Hdo19zDTffeo="
vv_pubkey_b64 = "YMiX8d/f/rAsdM5scvx0CAuJnc2LFpVmaPA+JW5p5hQ="

code_privkey = SigningKey(code_privkey_b64, encoder=nacl.encoding.Base64Encoder).to_curve25519_private_key()
vv_pubkey = VerifyKey(vv_pubkey_b64, encoder=nacl.encoding.Base64Encoder).to_curve25519_public_key()

orig_message = 'test'

received_encrypted_message = "nP2MGbEi1yWyLZyFkAWPyytkZr4C+7GgRit0a+BfwtkOrN8/HWY+Oh0skU5W7Gdo1NgVMMHkbVSQAzAukU9mQwN0e0C2jcBYwdxF79Q5gXgG9t/u+Zxi9Jcfymj2Zq+T7L12pRsvQqH0sPKo9Lw0cg1tkGOoH0bpCH34pXucc336zyrwwSmo"

# Initialize code Box
receiver_box = Box(code_privkey, vv_pubkey)
encrypted = receiver_box.encrypt(bytes(orig_message, 'ascii'))
print("encrypted message: " + b64encode(encrypted).decode('ascii'))

encrypted = base64.b64decode(received_encrypted_message.encode('ascii'))
decrypted = receiver_box.decrypt(encrypted)

print("decrypted message: " + decrypted.decode('utf-8'))
