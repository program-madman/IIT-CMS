const crypto = require('crypto');
var Buffer = require('buffer').Buffer;

const key = 's9kf01bz012949ty';
const iv = 'it4shn89a01iq656';

export const aesEncrypt = (data) => {
    const cipher = crypto.createCipheriv('aes128', key, iv);
    return cipher.update(Buffer.from(data), 'utf8', 'hex') + cipher.final('hex');
}

export const aesDecrypt = (encrypted) => {
    const decipher = crypto.createDecipheriv('aes128', key, iv);
    return decipher.update(encrypted, 'hex', 'utf8') + decipher.final('utf8');
}