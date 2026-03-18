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
import uuid

import requests

beneficiary_vasp_entity_id = 'coinone'
beneficiary_vasp_public_key = 'P2lEVJ63ESshum0JavXufBA4WUbydnsZzVGFnCVWo/Y='

transfer_id = uuid.uuid4()

# Code-Cipher
url = "http://localhost:8787/api/v1/code/api-payloads"
data = f'{{"allianceName": "CODE", "remotePublicKey": "{beneficiary_vasp_public_key}", ' \
       f'"apiType": "TRANSFER_AUTHORIZATION", ' \
       f'"request": {{' \
       f'"amount": "0.00001",' \
       f'"tradePrice": "55555",' \
       f'"transferId": "{transfer_id}",' \
       f'"tradeCurrency": "KRW",' \
       f'"historicalCost": "",' \
       f'"currency": "BTC",' \
       f'"originatingVasp": {{}},' \
       f'"isExceedingThreshold": true,' \
       f'"originatorLegalName": "(주)코인원",' \
       f'"originatorLegalPersonLastName": "Kim",' \
       f'"originatorLegalPersonFirstName": "Chulsu",' \
       f'"originatorNaturalPersonLastName": "Barnes",' \
       f'"originatorNaturalPersonFirstName": "Robert",' \
       f'"originatorNaturalPersonLocalLastName": "로버트 반스",' \
       f'"originatorLegalPersonLocalLastName": "김",' \
       f'"originatorLegalPersonLocalFirstName": "철수",' \
       f'"nameIdentifierType": "LEGL",' \
       f'"customerIdentification": "3213213qweqwe213312",' \
       f'"originatorWalletAddress": "012345678900",' \
       f'"originatingVaspCountryOfRegistration": "KR",' \
       f'"originatingVaspLegalName": "Korbit Inc.",' \
       f'"originatingVaspAddressType": "GEOG",' \
       f'"originatingVaspTownName": "Seoul",' \
       f'"originatingVaspAddressLine": ["14 Teheran-ro 4-gil, Gangnam-gu","4th floor"],' \
       f'"originatingVaspCountry": "KR",' \
       f'"originatingVaspNationalIdentifier": "1234567890",' \
       f'"originatingVaspNationalIdentifierType": "RAID",' \
       f'"originatingVaspRegistrationAuthority": "RA000657",' \
       f'"beneficiaryWalletAddress": "tb1qu6luu67fnad5zw87swynd6drz2wyrrkxdmv60d",' \
       f'"beneficiaryNaturalPersonLastName": "스미스",' \
       f'"beneficiaryNaturalPersonFirstName": "앨리스",' \
       f'"beneficiaryNaturalPersonLocalLastName": "Smith",' \
       f'"beneficiaryNaturalPersonLocalFirstName": "Alice",' \
       f'"beneficiaryLegalName": "(주)코인원",' \
       f'"beneficiaryLegalPersonLastName": "KIM",' \
       f'"beneficiaryLegalPersonFirstName": "JIHO",' \
       f'"beneficiaryLegalPersonLocalLastName": "김",' \
       f'"beneficiaryLegalPersonLocalFirstName": "지호"' \
       f'}}}}'

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
url = f"https://trapi-dev.codevasp.com/v1/code/transfer/{beneficiary_vasp_entity_id}"

response = requests.post(url, data=data, headers=header)
print(response)
print(response.status_code)
print(response.content)
