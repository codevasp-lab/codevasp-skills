from base64 import b64encode

from nacl.signing import SigningKey

signing_key = SigningKey.generate()
verify_key = signing_key.verify_key

# use signing key as private key
private_key_b64 = b64encode(bytes(signing_key)).decode('utf-8')

# use verify key as public key
public_key_b64 = b64encode(bytes(verify_key)).decode('utf-8')

print("Key generation complete.")
print("sk: " + private_key_b64)
print("pk: " + public_key_b64)

