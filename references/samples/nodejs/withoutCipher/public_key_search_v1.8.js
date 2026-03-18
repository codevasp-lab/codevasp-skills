import CodeCrypto from '../util/codeCrypto.js';
import axios from "axios";

(async () => {
    const yourOriginEntityId = "Your Own Entity Id";
    const b64YourPrivateKey = 'Your Own Private Key';
    const b64RemotePublicKey = '8s58A8Xid5zPgTZTUYTnWaf64t1QDImy7R7yxtBAs6Y='; // Target VASP Pub key
    const targetVaspEntityId = 'codexchange-non-kor'; // Target VASP Entity ID

    const codeCrypto = new CodeCrypto(b64YourPrivateKey, b64RemotePublicKey);
    await codeCrypto.init();

    const datetime = new Date().toISOString()
    let nonce = Math.floor(Math.random() * 10000)

    const datetimeBuffer = Buffer.from(datetime, 'utf8');
    const nonceBuffer = new Uint8Array(new Uint32Array([nonce]).buffer).reverse();

    const data = Buffer.concat([datetimeBuffer, nonceBuffer]);

    const signatureBytes = await codeCrypto.sign(data);
    const verifyKey = await codeCrypto.getVerifyKey();

    console.log('Signature:', Buffer.from(signatureBytes).toString('base64'));
    console.log('Verify Key:', verifyKey);

    const URL = 'https://trapi-dev.codevasp.com/v1/code/Vasp/' + targetVaspEntityId + '/pubkey';

    const config = {
        headers: {
            'X-Code-Req-PubKey': verifyKey,
            'X-Code-Req-Signature': Buffer.from(signatureBytes).toString('base64'),
            'X-Request-Origin': 'code:' + yourOriginEntityId, // change to your id
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