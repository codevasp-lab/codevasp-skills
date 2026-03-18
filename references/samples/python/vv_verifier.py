from base64 import b64encode

import nacl.hash
from nacl.encoding import Base64Encoder
from nacl.hash import blake2b
from nacl.public import Box
from nacl.signing import SigningKey, VerifyKey

code_privkey_b64 = "YEIngcRkJYCy1zgf7DvsOm+LMVjD0U6uLhT/DjsWl0s="
vv_pubkey_b64 = "Jigv3O53E3DaYzOLDg1WHDVsjBqaCiKtQyXwaZtMLcA="

code_privkey = SigningKey(code_privkey_b64, encoder=nacl.encoding.Base64Encoder).to_curve25519_private_key()
vv_pubkey = VerifyKey(vv_pubkey_b64, encoder=nacl.encoding.Base64Encoder).to_curve25519_public_key()

origin = "verifyvasp:central"
datetime = "2022-04-13T08:22:29.510Z"
payload = "null"

hash_origin = 'code:central2022-04-14T11:10:20Z{"timestamp":1649934621}'

hash_key = Box(code_privkey, vv_pubkey).shared_key()
print(b64encode(hash_key).decode('ascii'))
signature = blake2b(bytes(hash_origin, 'ascii'), key=hash_key, encoder=nacl.encoding.RawEncoder)
print(b64encode(signature).decode('ascii'))

