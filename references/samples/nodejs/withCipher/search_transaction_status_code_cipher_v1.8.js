import axios from "axios";
import { v4 as uuidv4 } from 'uuid';

// VerifyAddress Example
(async () => {
    const b64RemotePublicKey = 'yebws9G0efg2qxxJ3ob3SS6XC6xVvlOZw6PHqTfWdB4='; // 대상 VASP의 PublicKey
    const vaspEntityId = 'beeblock'; // 대상 VASP의 Entity ID
    const transferId = uuidv4();

    const cipherUrl = 'http://localhost:8787/api/v1/code/api-payloads';
    const payloadBody = Buffer.from('{\n' +
        `    "remotePublicKey": "${b64RemotePublicKey}",` +
        '    "apiType": "SEARCH_TRANSACTION_STATUS",\n' +
        '    "request": {\n' +
        `        "transferId": "${transferId}"\n` +
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

    const URL = `https://trapi-dev.codevasp.com/v1/code/transfer/${vaspEntityId}/status`;

    const config = {
        headers: {
            'X-Code-Req-PubKey': cipherResponse.data.publicKey,
            'X-Code-Req-Signature': cipherResponse.data.signature,
            'X-Request-Origin': 'code:dummy',
            'X-Code-Req-Datetime': cipherResponse.data.dateTime,
            'X-Code-Req-Nonce': cipherResponse.data.nonce,
            'X-Code-Req-Remote-PubKey': b64RemotePublicKey,
            'Content-Length': cipherResponse.data.body.length,
            'Content-Type': 'application/json'
        }
    };

    console.log('Request Body:', cipherResponse.data.body);

    try {
        const response = await axios.post(URL, cipherResponse.data.body, config);
        console.log(response.data);
    } catch (error) {
        console.log(error.response.data);
    }
})();