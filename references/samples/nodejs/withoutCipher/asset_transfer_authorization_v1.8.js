import CodeCrypto from '../util/codeCrypto.js';
import axios from "axios";
import { v4 as uuidv4 } from 'uuid';

(async () => {
    const yourOriginEntityId = "Your Own Entity Id";
    const b64YourPrivateKey = 'Your Own Private Key';
    const b64RemotePublicKey = '8s58A8Xid5zPgTZTUYTnWaf64t1QDImy7R7yxtBAs6Y='; // Target VASP Pub key
    const targetVaspEntityId = 'codexchange-non-kor'; // Target VASP Entity ID
    const trasnferId = uuidv4();

    const codeCrypto = new CodeCrypto(b64YourPrivateKey, b64RemotePublicKey);
    await codeCrypto.init(); // Changed from initSodium to init

    const datetime = new Date().toISOString()
    let nonce = Math.floor(Math.random() * 10000)

    const datetimeBuffer = Buffer.from(datetime, 'utf8');
    const encryptedPayload = await codeCrypto.encrypt(Buffer.from('' +
        '{\n' +
        '    "ivms101": {\n' +
        '        "Beneficiary": {\n' +
        '            "accountNumber": [\n' +
        '                "1KzHK8WMRHRCvRjUV5PFny3v6fqT3UAY5K"\n' +
        '            ],\n' +
        '            "beneficiaryPersons": [\n' +
        '                {\n' +
        '                    "naturalPerson": {\n' +
        '                        "dateAndPlaceOfBirth": null,\n' +
        '                        "name": {\n' +
        '                            "nameIdentifier": [\n' +
        '                                {\n' +
        '                                    "nameIdentifierType": "LEGL",\n' +
        '                                    "primaryIdentifier": "kim",\n' +
        '                                    "secondaryIdentifier": "code"\n' +
        '                                }\n' +
        '                            ]\n' +
        '                        }\n' +
        '                    }\n' +
        '                }\n' +
        '            ]\n' +
        '        },\n' +
        '        "OriginatingVASP": {\n' +
        '            "originatingVASP": {\n' +
        '                "legalPerson": {\n' +
        '                    "countryOfRegistration": "AE",\n' +
        '                    "geographicAddress": [\n' +
        '                        {\n' +
        '                            "addressLine": [\n' +
        '                                "Dubai World Trade Centre"\n' +
        '                            ],\n' +
        '                            "addressType": "GEOG",\n' +
        '                            "country": "AE",\n' +
        '                            "townName": "Dubai"\n' +
        '                        }\n' +
        '                    ],\n' +
        '                    "name": {\n' +
        '                        "nameIdentifier": [\n' +
        '                            {\n' +
        '                                "legalPersonName": "Fintech FZE",\n' +
        '                                "legalPersonNameIdentifierType": "LEGL"\n' +
        '                            }\n' +
        '                        ]\n' +
        '                    }\n' +
        '                }\n' +
        '            }\n' +
        '        },\n' +
        '        "Originator": {\n' +
        '            "accountNumber": [\n' +
        '                "TJL6Uo53r4EDKmw16LCebaqmcepEB6aXGP"\n' +
        '            ],\n' +
        '            "originatorPersons": [\n' +
        '                {\n' +
        '                    "naturalPerson": {\n' +
        '                        "dateAndPlaceOfBirth": {\n' +
        '                            "dateOfBirth": "1980-01-01"\n' +
        '                        },\n' +
        '                        "name": {\n' +
        '                            "nameIdentifier": [\n' +
        '                                {\n' +
        '                                    "nameIdentifierType": "LEGL",\n' +
        '                                    "primaryIdentifier": "test",\n' +
        '                                    "secondaryIdentifier": "name"\n' +
        '                                }\n' +
        '                            ]\n' +
        '                        }\n' +
        '                    }\n' +
        '                }\n' +
        '            ]\n' +
        '        }\n' +
        '    }\n' +
        '}' +
        '', 'utf8'));
    const body = Buffer.from(`{"transferId":"${trasnferId}","currency":"BTC","amount":"0.00001","historicalCost":"","tradePrice":"55555","tradeCurrency":"KRW","isExceedingThreshold":true,"originatingVasp":{},"payload": "${Buffer.from(encryptedPayload).toString('base64')}"}`, 'utf8');
    const nonceBuffer = new Uint8Array(new Uint32Array([nonce]).buffer).reverse();

    const data = Buffer.concat([datetimeBuffer, body, nonceBuffer]);

    const signatureBytes = await codeCrypto.sign(data);
    const verifyKey = await codeCrypto.getVerifyKey();

    console.log('Signature:', Buffer.from(signatureBytes).toString('base64'));
    console.log('Verify Key:', verifyKey);

    const URL = `https://trapi-dev.codevasp.com/v1/code/transfer/${targetVaspEntityId}`;

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
        console.log("response.data:",response.data);
        // Extract the payload from response.data
        const { payload } = response.data;

        // Now you can use 'payload' for further processing or operations
        console.log("Extracted Payload:", payload);

        // Example usage: Decrypt the response payload
        if(payload) {
            const encryptedData = Buffer.from(payload, 'base64')
            const decryptedPayload = await codeCrypto.decrypt(encryptedData);
            console.log('Decrypted Body:', Buffer.from(decryptedPayload).toString('utf-8'));
        }

    } catch (error) {
        console.log("error:", error);
    }
})();