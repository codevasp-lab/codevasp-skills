import axios from "axios";

// VerifyAddress Example
(async () => {
    const b64RemotePublicKey = 'DJji2rbTNqI33S8kkHMEHQ23twnK9FaVPtrKUwLNkMs='; // 대상 VASP의 PublicKey
    const vaspEntityId = 'bithumb'; // 대상 VASP의 Entity ID

    const cipherUrl = 'http://localhost:8787/api/v1/code/api-payloads';
    const payloadBody = Buffer.from('{\n' +
        `    "remotePublicKey": "${b64RemotePublicKey}",` +
        '    "apiType": "SEARCH_ADDRESS",\n' +
        '    "request" : {\n' +
        '        "currency": "XRP",\n' +
        '        "beneficiaryWalletAddress": "rs6qtDrs8qp5qLkUxuzQdxFh5eGrQbL3Ee:1004829098"\n' +
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

    const URL = `https://trapi-dev.codevasp.com/v1/code/VerifyAddress/${vaspEntityId}`;

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