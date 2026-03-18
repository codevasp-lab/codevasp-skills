import axios from "axios";

// VASP 조회 Example
(async () => {
    const cipherUrl = 'http://localhost:8787/api/v1/code/api-payloads';
    const payloadBody = Buffer.from('{\n' +
        '    "remotePublicKey": "",\n' +
        '    "apiType": "SEARCH_VASP_LIST"\n' +
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

    const URL = `https://trapi-dev.codevasp.com/v1/code/vasps`;

    const config = {
        headers: {
            'X-Code-Req-PubKey': cipherResponse.data.publicKey,
            'X-Code-Req-Signature': cipherResponse.data.signature,
            'X-Request-Origin': 'code:dummy',
            'X-Code-Req-Datetime': cipherResponse.data.dateTime,
            'X-Code-Req-Nonce': cipherResponse.data.nonce,
            'Content-Type': 'application/json'
        }
    };

    console.log('Request Body:', cipherResponse.data.body);

    try {
        const response = await axios.get(URL, config);
        console.log(JSON.stringify(response.data, null, 2));
    } catch (error) {
        console.log(error.response.data);
    }
})();