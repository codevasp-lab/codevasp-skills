import sodium from 'libsodium-wrappers';

export async function getSodium() {
    await sodium.ready;
    return sodium;
}

export function generateKeyPair(seed) {
    return {
        publicKey: sodium.crypto_sign_seed_keypair(seed).publicKey,
        privateKey: sodium.crypto_sign_seed_keypair(seed).privateKey
    };
}

export function ed25519ToCurve25519(key) {
    return sodium.crypto_sign_ed25519_pk_to_curve25519(key);
}

export function sign(data, privateKey) {
    return sodium.crypto_sign_detached(data, privateKey);
}

export function encrypt(data, peerPublicKey, privateKey) {
    const nonce = sodium.randombytes_buf(sodium.crypto_box_NONCEBYTES);
    const encrypted = sodium.crypto_box_easy(data, nonce, peerPublicKey, privateKey);
    const encryptedWithNonce = new Uint8Array(nonce.length + encrypted.length);
    encryptedWithNonce.set(nonce);
    encryptedWithNonce.set(encrypted, nonce.length);
    return encryptedWithNonce;
}

export function decrypt(encryptedData, peerPublicKey, privateKey) {
    const nonce = encryptedData.slice(0, sodium.crypto_box_NONCEBYTES);
    const rawEncrypted = encryptedData.slice(sodium.crypto_box_NONCEBYTES);
    return sodium.crypto_box_open_easy(rawEncrypted, nonce, peerPublicKey, privateKey);
}