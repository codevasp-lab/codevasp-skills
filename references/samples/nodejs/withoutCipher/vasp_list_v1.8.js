import CodeCrypto from '../util/codeCrypto.js';
import axios from "axios";

(async () => {
    const yourOriginEntityId = "Your Own Entity Id";
    const b64YourPrivateKey = 'Your Own Private Key';
    const b64RemotePublicKey = ''; // Vasp 조회 시에는 PublicKey는 빈 문자열

    const codeCrypto = new CodeCrypto(b64YourPrivateKey, b64RemotePublicKey);
    await codeCrypto.init();

    const datetime = new Date().toISOString();
    let nonce = Math.floor(Math.random() * 10000);
    const datetimeArray = new TextEncoder().encode(datetime);
    const nonceArray = new Uint8Array(new Uint32Array([nonce]).buffer).reverse();

    const data = new Uint8Array(datetimeArray.length + nonceArray.length);
    data.set(datetimeArray, 0);
    data.set(nonceArray, datetimeArray.length);

    console.log(nonceArray);
    console.log(data.toString('base64'));

    const signatureBytes = await codeCrypto.sign(data);
    const verifyKey = await codeCrypto.getVerifyKey();

    console.log('Signature:', Buffer.from(signatureBytes).toString('base64'));
    console.log('Verify Key:', verifyKey);

    const URL = 'https://trapi-dev.codevasp.com/v1/code/vasps';

    const config = {
        headers: {
            'X-Code-Req-PubKey': verifyKey,
            'X-Code-Req-Signature': Buffer.from(signatureBytes).toString('base64'),
            'X-Request-Origin': 'code:' + yourOriginEntityId,
            'X-Code-Req-Datetime': datetime,
            'X-Code-Req-Nonce': nonce
        }
    };

    try {
        const response = await axios.get(URL, config);
        console.log(response.data);
    } catch (error) {
        console.log(error.response.data);
    }
})();