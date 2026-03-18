import { encrypt, decrypt, sign, getSodium } from './sodium.js';

class CodeCrypto {
    constructor(b64OwnSecretKey, b64RemotePublicKey) {
        this.ownSecretKey = Buffer.from(b64OwnSecretKey, 'base64');
        this.remotePublicKey = b64RemotePublicKey ? Buffer.from(b64RemotePublicKey, 'base64') : null;
        this.sodium = null;
        this.signKeyPair = null;
        this.peerPublicKey = null;
        this.privateKey = null;
        this.sharedKey = null;
    }

    async init() {
        this.sodium = await getSodium();
        this.signKeyPair = this.sodium.crypto_sign_seed_keypair(this.ownSecretKey);

        if (this.remotePublicKey) {
            this.peerVerifyKey = this.remotePublicKey;
            this.peerPublicKey = this.sodium.crypto_sign_ed25519_pk_to_curve25519(this.peerVerifyKey);
            const privateKey = this.sodium.crypto_sign_ed25519_sk_to_curve25519(this.signKeyPair.privateKey);
            this.privateKey = privateKey;
            this.sharedKey = this.sodium.crypto_box_beforenm(this.peerPublicKey, this.privateKey);
        }
    }

    async sign(data) {
        return sign(data, this.signKeyPair.privateKey);
    }

    async getVerifyKey() {
        return Buffer.from(this.signKeyPair.publicKey).toString('base64');
    }

    async encrypt(data) {
        if (!this.peerPublicKey) {
            console.log('This CodeCrypto instance does not have peerPublicKey, return false.');
            return null;
        }
        return encrypt(data, this.peerPublicKey, this.privateKey);
    }

    async decrypt(data) {
        if (!this.peerPublicKey) {
            console.log('This CodeCrypto instance does not have peerPublicKey, return false.');
            return null;
        }
        return decrypt(data, this.peerPublicKey, this.privateKey);
    }
}

export default CodeCrypto;