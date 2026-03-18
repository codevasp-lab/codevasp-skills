import CodeCrypto from './codeCrypto.js';

(async () => {

	const code_privkey_b64 = "HqpupbkYl3zfyw5VVsIB/Rkb2/oPgjl+bMr67N1vmec="
	const vv_pubkey_b64 = "Jigv3O53E3DaYzOLDg1WHDVsjBqaCiKtQyXwaZtMLcA="

	const codeCrypto = new CodeCrypto(code_privkey_b64, vv_pubkey_b64);
	await codeCrypto.init();

	const sodium = codeCrypto.sodium;
	const seed = Buffer.from(code_privkey_b64, 'base64')
	const code_signkey_pair = sodium.crypto_sign_seed_keypair(seed)
	const code_signing_key = code_signkey_pair.privateKey
	const code_privkey = sodium.crypto_sign_ed25519_sk_to_curve25519(code_signing_key)

	const vv_verify_key = Buffer.from(vv_pubkey_b64, 'base64')
	const vv_pubkey = sodium.crypto_sign_ed25519_pk_to_curve25519(vv_verify_key)

	const origin = "code:central"
	const datetime = "2022-04-14T11:10:20Z"
	const payload = '{"timestamp":1649934621}'
	const hashData = origin + datetime + payload
	const hashKey = sodium.crypto_box_beforenm(vv_pubkey, code_privkey)
	console.log('hash key : ',Buffer.from(hashKey).toString('base64'))

	const hashResult = sodium.crypto_generichash(sodium.crypto_generichash_BYTES, hashData, hashKey)
	console.log('hash Result : ',Buffer.from(hashResult).toString('base64'))
})()


