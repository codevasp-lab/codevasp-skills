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
import json
import secrets
import struct
from base64 import b64encode, b64decode
from datetime import timezone, datetime

import nacl
import requests
from nacl.exceptions import BadSignatureError
from nacl.public import Box as NaClBox
from nacl.signing import SigningKey, VerifyKey

# 거래소의 public_key
public_key_str = ""  # Your Public Key
# 거래소의 private_key
private_key_str = ""  # Your Private Key

# Code-Cipher
url = "http://localhost:8787/api/v1/code/api-payloads"
data = "{\"allianceName\": \"CODE\", \"remotePublicKey\": \"\", \"apiType\": \"SEARCH_VASP_LIST\"}"

response = requests.post(url, data=bytes(data, encoding='utf-8'), headers={"Content-Type": "application/json"})
# 바이트 문자열을 문자열로 변환
response_str = response.content.decode('utf-8')
# JSON 파싱하여 딕셔너리로 변환
response_dict = json.loads(response_str)
signature = response_dict['signature']
nonce = response_dict['nonce']
date_time = response_dict['dateTime']
public_key = response_dict['publicKey']
print(response)
print(response.status_code)
print(response.content)
print("Signature: {0}".format(signature))
print("Nonce: {0}".format(nonce))
print("DateTime: {0}".format(date_time))
print("PublicKey: {0}".format(public_key))
print("###################################################")

header = {
    "X-Code-Req-PubKey": public_key
    , "X-Code-Req-Datetime": date_time
    , "X-Request-Origin": "code:dummy"
    , "X-Code-Req-Nonce": str(nonce)
    , "X-Code-Req-Signature": signature
}

# URL 주소
url = "https://trapi-dev.codevasp.com/v1/code/vasps"

response = requests.get(url, headers=header)
print(response)
print(response.status_code)
print(response.content)
