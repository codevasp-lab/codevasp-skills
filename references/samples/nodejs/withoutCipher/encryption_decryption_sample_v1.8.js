import CodeCrypto from '../util/codeCrypto.js';

// encrypt, decrypt Example
(async () => {
    const b64OwnSecretKey = 'Your Own Private Key'; // 자신의 PrivateKey
    const b64RemotePublicKey = '8s58A8Xid5zPgTZTUYTnWaf64t1QDImy7R7yxtBAs6Y='; // 대상 VASP의 PublicKey

    const codeCrypto = new CodeCrypto(b64OwnSecretKey, b64RemotePublicKey);
    await codeCrypto.init();

    const datetime = new Date().toISOString()
    let nonce = Math.floor(Math.random() * 10000)

    const datetimeBuffer = Buffer.from(datetime, 'utf8');
    const body = '{"transferId":"8e2ceaea-9b9e-493c-ada4-06265f0ddc78", "txid": "e24ca4dd-3d2b-414c-8747-35e5852322e6", "vout": ""}';
    const bodyBuffer = Buffer.from(body, 'utf8');
    const nonceBuffer = toBytes(nonce);

    const data = Buffer.concat([datetimeBuffer, bodyBuffer, nonceBuffer]);

    const signatureBytes = await codeCrypto.sign(data);
    const verifyKey = await codeCrypto.getVerifyKey();

    console.log('Signature:', Buffer.from(signatureBytes).toString('base64'));
    console.log('Verify Key:', verifyKey);

    const encryptedBody = await codeCrypto.encrypt(body);
    console.log('Encrypted Body:', Buffer.from(encryptedBody).toString('base64'));

    const encryptedData = Buffer.from(encryptedBody, 'base64')
    const decryptedBody = await codeCrypto.decrypt(encryptedData);
    console.log('Decrypted Body:', Buffer.from(decryptedBody).toString('utf-8'));

    if (body.toString() === Buffer.from(decryptedBody).toString('utf-8')) {
        console.log('Decrypted Body is same as original body.');
    }
})();

function toBytes(int) {
    const result = new Uint8Array(4);

    result[0] = (int >> 24) & 0xFF;
    result[1] = (int >> 16) & 0xFF;
    result[2] = (int >> 8) & 0xFF;
    result[3] = int & 0xFF;

    return result;
}