import struct
from base64 import b64decode

from nacl.encoding import Base64Encoder
from nacl.exceptions import BadSignatureError
from nacl.signing import VerifyKey

class InvalidSignature(Exception):
    pass


# signing data
# body = '{"ivms101":{"Beneficiary":{"accountNumber":["3AtPZrpc4iTHLBwsWJJAkzZqeL8L3axRbX"]}}}'
# datetime_str = datetime.utcnow().replace(tzinfo=timezone.utc).isoformat()

body = '{"payload":"21gCaspC2z2SVlLm7fTKoZHFjHFOi17ecom6pt5Ds2O4hlxbCxj37q30SwELAqNT7ynQGAAeSjZ0JUuC+BoKqfbiDuqq/XsM+ooJUP5vqBUFfJTp20CT6BShpM87sWOdBrBC0nJHHIU3t6TBrJuKLxqoExwa+4xQyqFsfw==","currency":"XRP"}'
nonce = 8963
datetime_str = '2022-05-19T04:28:28+0000'

signature = 'HOVYjR8/8ZCituF03u1Q0heYy/UmGutVqg/Xu4u+7mTOv0XJQcNvm28SEokgRtvi3bdPewA08PMB653F1AA1BQ=='

datetime_bytes = bytes(datetime_str, encoding='utf-8')
body_bytes = bytes(body, encoding='utf-8')

# 4개의 값을 조합하여 byte sequence로 만듬 : 빅엔디언 방식(파라미터 순서가 꼭 맞아야 함)
# f'!{len(datetime_bytes)}s{len(body_bytes)}s{len(nonce_bytes)}s' 처럼 bytes 를 묶으면서
# 각각의 길이로 포맷을 만들어줘야 함.
# {len(datetime_bytes)}s --> datetime_bytes 의 길이
# {len(body_bytes)}s --> body_bytes 의 길이
# {len(nonce_bytes)}s --> nonce 의 길이
# 이렇게 각각 의미하는 것임

signing_target = struct.pack(
    f'!{len(datetime_bytes)}s{len(body_bytes)}sL',
    datetime_bytes,
    body_bytes,
    nonce)

signature_bytes = bytes(b64decode(signature.encode('utf-8')))
verify_key_b64 = 'wR1Gtiixh2GuBQuki01oJuZ4znqwK/MgXYFwt8g+I+s='
verify_key = VerifyKey(verify_key_b64.encode('utf-8'), encoder=Base64Encoder)

# Check the validity of a message's signature
# The message and the signature can either be passed together, or
# separately if the signature is decoded to raw bytes.
# These are equivalent:
try:
    if verify_key.verify(signing_target, signature_bytes):
        print("Verifying is success.")
except BadSignatureError:
    raise InvalidSignature
except Exception:
    raise
