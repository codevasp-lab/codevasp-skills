# 연동해야 하는 API는 총 7개 입니다.
# 1. VASP 목록 조회 / VASP List Search
# 2. 특정 VASP의 공개키 목록 얻어오기 / PublicKey List Search
# 3. 가상 자산 주소 조회 / Virtual Asset Address Search
# 4. 자산 이전 허가 요청 / Asset Transfer Authorization
# 5. 자산 이전 결과 전송 / Report Transfer Result(TX Hash)
# 6. 트랜잭션 상태 조회 / Transaction Status Search
# 7. 자산 이전 종료 / Finish Transfer
# 해당 코드는 3번 API 호출을 위한 코드입니다.

import json

import requests

beneficiary_vasp_entity_id = 'coinone'
beneficiary_vasp_public_key = 'P2lEVJ63ESshum0JavXufBA4WUbydnsZzVGFnCVWo/Y='

# Code-Cipher
url = "http://localhost:8787/api/v1/code/api-payloads"
data = f'{{"allianceName": "CODE", "remotePublicKey": "{beneficiary_vasp_public_key}", "apiType": "SEARCH_ADDRESS", "request": {{"currency": "XRP", "beneficiaryWalletAddress": "0x1234567890123456789012345678901234567890"}}}}'

print(data)
response = requests.post(url, data=bytes(data, encoding='utf-8'), headers={"Content-Type": "application/json"})
# 바이트 문자열을 문자열로 변환
response_str = response.content.decode('utf-8')
# JSON 파싱하여 딕셔너리로 변환
response_dict = json.loads(response_str)
signature = response_dict['signature']
nonce = response_dict['nonce']
date_time = response_dict['dateTime']
public_key = response_dict['publicKey']
data = bytes(response_dict['body'], encoding='utf-8')

print(response)
print(response.status_code)
print("Signature: {0}".format(signature))
print("Nonce: {0}".format(nonce))
print("DateTime: {0}".format(date_time))
print("PublicKey: {0}".format(public_key))
print("Data: {0}".format(data))
print("###################################################")

header = {
    "X-Code-Req-PubKey": public_key
    , "X-Code-Req-Signature": signature
    , "X-Request-Origin": "code:dummy"
    , "X-Code-Req-Datetime": date_time
    , "X-Code-Req-Nonce": str(nonce)
    , "X-Code-Req-Remote-PubKey": beneficiary_vasp_public_key
    , "Content-Length": str(len(data))
    , "Content-Type": "application/json"
}

# URL 주소
url = f"https://trapi-dev.codevasp.com/v1/code/VerifyAddress/{beneficiary_vasp_entity_id}"

response = requests.post(url, data=data, headers=header)
print(response)
print(response.status_code)
print(response.content)
