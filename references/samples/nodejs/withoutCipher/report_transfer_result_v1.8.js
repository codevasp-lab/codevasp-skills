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
    const body = Buffer.from('{"transferId":"4a79c30a-9192-4527-b349-f53871da821b", "txid": "test-transaction", "vout": ""}', 'utf8');
    const nonceBuffer = new Uint8Array(new Uint32Array([nonce]).buffer).reverse();

    const data = Buffer.concat([datetimeBuffer, body, nonceBuffer]);

    const signatureBytes = await codeCrypto.sign(data);
    const verifyKey = await codeCrypto.getVerifyKey();

    console.log('Signature:', Buffer.from(signatureBytes).toString('base64'));
    console.log('Verify Key:', verifyKey);

    const URL = 'https://trapi-dev.codevasp.com/v1/code/transfer/' + targetVaspEntityId + '/txid';

    const config = {
        headers: {
            'X-Code-Req-PubKey': verifyKey,
            'X-Code-Req-Signature': Buffer.from(signatureBytes).toString('base64'),
            'X-Request-Origin': 'code:' + yourOriginEntityId,
            'X-Code-Req-Datetime': datetime,
            'X-Code-Req-Nonce': nonce,
            'X-Code-Req-Remote-PubKey': b64RemotePublicKey,
            'Content-Length': body.length,
            'Content-Type': 'application/json'
        }
    };

    console.log('Request Body:', body.toString('utf8'));

    try {
        const response = await axios.post(URL, body.toString('utf8'), config);
        console.log(response.data);
    } catch (error) {
        console.log(error.response.data);
    }
})();