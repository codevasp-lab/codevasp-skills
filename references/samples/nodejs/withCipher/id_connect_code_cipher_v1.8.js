import axios from "axios";
import { v4 as uuidv4 } from 'uuid';

(async () => {
    const vaspEntityId = 'UMAyjXzBdHpn1KoBkpJbm'; // 대상 VASP의 Entity ID
    const requestId = uuidv4();

    const cipherUrl = 'http://localhost:8787/api/v1/code/connect/hash';
    const payloadBody = Buffer.from('{\n' +
        '    "firstName": "Seonghoon",\n' +
        '    "lastName": "Ju",\n' +
        '    "dateOfBirth": "1981-04-11",\n' +
        '    "accountNumber": "0x35f11fd920fb010dd3cb5c19489a9149be164a55",\n' +
        '    "network": "ETH",\n' +
        '    "currency": "ETH",\n' +
        `    "requestId": "${requestId}",` +
        '    "tag": ""\n' +
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

    const URL = `https://trapi-dev.codevasp.com/v1/code/connect/${vaspEntityId}`;

    const config = {
        headers: {
            'X-Code-Req-PubKey': cipherResponse.data.publicKey,
            'X-Code-Req-Signature': cipherResponse.data.signature,
            'X-Request-Origin': 'code:dummy',
            'X-Code-Req-Datetime': cipherResponse.data.dateTime,
            'X-Code-Req-Nonce': cipherResponse.data.nonce,
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