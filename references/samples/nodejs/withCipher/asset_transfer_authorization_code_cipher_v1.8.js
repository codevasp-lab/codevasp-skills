import axios from "axios";
import { v4 as uuidv4 } from 'uuid';

// Example
(async () => {
    const b64RemotePublicKey = 'DJji2rbTNqI33S8kkHMEHQ23twnK9FaVPtrKUwLNkMs='; // 대상 VASP의 PublicKey
    const vaspEntityId = 'bithumb'; // 대상 VASP의 Entity ID
    const transferId = uuidv4();

    const cipherUrl = 'http://localhost:8787/api/v1/code/api-payloads';
    const payloadBody = Buffer.from('{\n' +
        '    "allianceType": "CODE",\n' +
        `    "remotePublicKey": "${b64RemotePublicKey}",` +
        '    "apiType": "TRANSFER_AUTHORIZATION",\n' +
        '    "request" : {\n' +
        '        "amount": "0.00001",\n' +
        '        "tradePrice": "55555",\n' +
        `        "transferId": "${transferId}",` +
        '        "tradeCurrency": "KRW",\n' +
        '        "currency": "BTC",\n' +
        '        "isExceedingThreshold": true,\n' +
        '        "originatorLegalName": "(주)코인원",\n' +
        '        "originatorLegalPersonLastName": "Kim",\n' +
        '        "originatorLegalPersonFirstName": "Chulsu",\n' +
        '        "originatorNaturalPersonLastName": "Barnes",\n' +
        '        "originatorNaturalPersonFirstName": "Robert",\n' +
        '        "originatorNaturalPersonLocalLastName": "로버트 반스",\n' +
        '        "customerIdentification": "3213213qweqwe213312",\n' +
        '        "originatingVaspCountryOfRegistration": "US",\n' +
        '        "originatingVaspLegalName": "Korbit Inc.",\n' +
        '        "originatorWalletAddress": "tb1q8jwxl6sf9jucpct2lfcdw2a0qz34sdg",\n' +
        '        "beneficiaryWalletAddress": "1BgBvV9x46zYVuwERHZr81nDeZ19qjUrjE",\n' +
        '        "beneficiaryNaturalPersonLastName": "김",\n' +
        '        "beneficiaryNaturalPersonFirstName": "패스"\n' +
        '    }\n' +
        '}')

    const cipherHeader = {
        headers: {
            'Content-Length': payloadBody.length,
            'Content-Type': 'application/json'
        }
    };

    let cipherResponse;

    try {
        cipherResponse = await axios.post(cipherUrl, payloadBody.toString('utf8'), cipherHeader);
        console.log(cipherResponse.data);
        console.log(cipherResponse.data.signature);
        console.log(cipherResponse.data.nonce);
        console.log(cipherResponse.data.dateTime);
    } catch (error) {
        console.log(error.response.data);
    }

    console.log('Signature:', cipherResponse.data.signature);
    console.log('Verify Key:', cipherResponse.data.publicKey);

    const URL = `https://trapi-dev.codevasp.com/v1/code/transfer/${vaspEntityId}`;

    const config = {
        headers: {
            'X-Code-Req-PubKey': cipherResponse.data.publicKey,
            'X-Code-Req-Signature': cipherResponse.data.signature,
            'X-Request-Origin': 'code:dummy', // change to your entity id
            'X-Code-Req-Datetime': cipherResponse.data.dateTime,
            'X-Code-Req-Nonce': cipherResponse.data.nonce,
            'X-Code-Req-Remote-PubKey': b64RemotePublicKey,
            'Content-Length': cipherResponse.data.body.length,
            'Content-Type': 'application/json'
        }
    };

    try {
        const response = await axios.post(URL, cipherResponse.data.body, config);
        console.log(response.data);
    } catch (error) {
        console.log(error.response.data);
    }
})();