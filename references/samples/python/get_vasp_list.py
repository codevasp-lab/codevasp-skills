# 연동해야 하는 API는 총 7개 입니다.
# 1. VASP 목록 조회 / VASP List Search
# 2. 특정 VASP의 공개키 목록 얻어오기 / PublicKey List Search
# 3. 가상 자산 주소 조회 / Virtual Asset Address Search
# 4. 자산 이전 허가 요청 / Asset Transfer Authorization
# 5. 자산 이전 결과 전송 / Report Transfer Result(TX Hash)
# 6. 트랜잭션 상태 조회 / Transaction Status Search
# 7. 자산 이전 종료 / Finish Transfer
# 해당 코드는 1번 API 호출을 위한 코드입니다.

import base64
import secrets
import struct
from base64 import b64encode, b64decode
from datetime import timezone, datetime

import nacl
import requests
from nacl.exceptions import BadSignatureError
from nacl.public import Box as NaClBox
from nacl.signing import SigningKey, VerifyKey


class InvalidSignature(Exception):
    pass


class PrivateKey:
    def __init__(self, impl) -> None:
        self._impl: SigningKey = impl

    @classmethod
    def generate(cls):
        impl = SigningKey.generate()
        return cls(impl)

    @classmethod
    def from_private_str(cls, data: str):
        return cls.from_private_bytes(b64decode(data))

    @classmethod
    def from_private_bytes(cls, data: bytes):
        impl = SigningKey(data)
        return cls(impl)

    def public_key(self):
        return PublicKey(self._impl.verify_key)

    def private_bytes(self):
        return bytes(self._impl)

    def sign(self, data: bytes):
        return self._impl.sign(data).signature

    def to_base64str(self):
        return b64encode(self.private_bytes()).decode('utf-8')

    @property
    def nacl_private_key(self):
        return self._impl.to_curve25519_private_key()


class PublicKey:
    def __init__(self, impl) -> None:
        self._impl: VerifyKey = impl

    @classmethod
    def from_public_str(cls, data: str):
        return cls.from_public_bytes(b64decode(data))

    @classmethod
    def from_public_bytes(cls, data: bytes):
        impl = VerifyKey(data)
        return cls(impl)

    def public_bytes(self):
        return bytes(self._impl)

    def verify(self, signature: bytes, data: bytes):
        try:
            return self._impl.verify(data, signature)
        except BadSignatureError:
            raise InvalidSignature
        except Exception:
            raise

    def to_base64str(self):
        return b64encode(self.public_bytes()).decode('utf-8')

    @property
    def nacl_public_key(self):
        return self._impl.to_curve25519_public_key()


class Box:
    def __init__(self, self_privkey: PrivateKey,
                 other_pubkey: PublicKey) -> None:
        self._privkey = self_privkey
        self._pubkey = other_pubkey
        self._box = NaClBox(self_privkey.nacl_private_key,
                            other_pubkey.nacl_public_key)

    def encrypt_body(self, body) -> bytes:
        return self._box.encrypt(body)

    def decrypt_body(self, body) -> bytes:
        if body == b'':
            return body
        return self._box.decrypt(body)


# 헤더를 만들기 위해 필요한 변수는 3개입니다.
# 1번 API는 GET 방식이라 body가 "" 임

body = ""
nonce = abs(secrets.randbits(16) * 10000)
datetime = datetime.now().strftime("%Y-%m-%dT%H:%M:%S.%fZ")

# 거래소의 public_key
public_key_str = ""  # Your Public Key
# 거래소의 private_key
private_key_str = ""  # Your Private Key
private_key = PrivateKey.from_private_bytes(base64.b64decode(private_key_str))

print("datetime : {0}".format(datetime))
print("body : {0}".format(body))
print("nonce : {0}".format(nonce))

print("###################################################")

datetime_bytes = bytes(datetime, encoding='utf-8')
body_bytes = bytes(body, encoding='utf-8')

# 3개의 값을 조합하여 byte sequence로 만듬 : 빅엔디언 방식(파라미터 순서가 꼭 맞아야 함)
# f'!{len(datetime_bytes)}s{len(body_bytes)}sL' 이 부분이 결과적으로 !L00s0sL
# 00s --> 00개의 문자열 ==> url_path_bytes (datetime_bytes의 length)
# 0s --> 0개의 문자열 ==> body는 없었으므로
# L --> 부호 없는 정수 ==> nonce
# 이렇게 각각 의미하는 것임

signing_target = struct.pack(f'!{len(datetime_bytes)}s{len(body_bytes)}sL',
                             datetime_bytes,
                             body_bytes,
                             nonce)

# private_key로 사인
signature = SigningKey(private_key_str.encode('utf-8'),
                       encoder=nacl.encoding.Base64Encoder).sign(signing_target).signature

header = {
    "X-Code-Req-PubKey": public_key_str
    , "X-Code-Req-Datetime": str(datetime)
    , "X-Request-Origin": "code:dummy"
    , "X-Code-Req-Nonce": str(nonce)
    , "X-Code-Req-Signature": b64encode(signature).decode('ascii')
}

# URL 주소
url = "https://trapi-dev.codevasp.com/v1/code/vasps"

res = requests.get(url, headers=header)
print(res)
print(res.status_code)
print(res.content)
