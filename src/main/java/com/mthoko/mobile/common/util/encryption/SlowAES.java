package com.mthoko.mobile.common.util.encryption;

import java.util.ArrayList;
import java.util.List;

//var slowAES = {
/*
 * START AES SECTION
 */
public class SlowAES {

    public AES aes = new AES();
    /*
     * START MODE OF OPERATION SECTION
     */
    // structure of supported modes of operation
    public ModeOfOperation modeOfOperation = new ModeOfOperation(0, 1, 2);

    // get a 16 byte block (aes operates on 128bits)
    public int[] getBlock(int[] bytesIn, int start, int end, int mode) {
        if (end - start > 16) {
            end = start + 16;
        }
        return arraySlice(bytesIn, start, end);
    }

    private int[] arraySlice(int[] bytesIn, int start, int end) {
        List<Integer> list = new ArrayList<>();
        for (int num : bytesIn) {
            list.add(num);
        }
        list = list.subList(start, end);
        bytesIn = new int[list.size()];
        for (int i = 0; i < bytesIn.length; i++) {
            bytesIn[i] = list.get(i);
        }
        return bytesIn;
    }

    public int[] padBytesIn(int[] data) {
        int len = data.length;
        int padByte = 16 - (len % 16);
        int[] newData = new int[data.length + padByte];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        for (int i = 0; i < padByte; i++) {
            newData[data.length + i] = padByte;
        }
        return newData;
    }

    public int[] unpadBytesOut(int[] data) {
        int padCount = 0;
        int padByte = -1;
        int blockSize = 16;
        if (data.length > 16) {
            for (int i = data.length - 1; i >= data.length - 1 - blockSize; i--) {
                if (data[i] <= blockSize) {
                    if (padByte == -1)
                        padByte = data[i];
                    if (data[i] != padByte) {
                        padCount = 0;
                        break;
                    }
                    padCount++;
                } else
                    break;
                if (padCount == padByte)
                    break;
            }
            if (padCount > 0) {
                data = AESUtil.splice(data, data.length - padCount, padCount);
            }
        }
        return data;
    }

    /*
     * Mode of Operation Encryption bytesIn - Input String as array of bytes mode -
     * mode of type modeOfOperation key - a number array of length 'size' size - the
     * bit length of the key iv - the 128 bit number array Initialization Vector
     */
    public int[] encrypt(int[] bytesIn, int mode, int[] key, int[] iv) {
        int size = key.length;
        if (iv.length % 16 != 0) {
            throw new RuntimeException("iv length must be 128 bits.");
        }
        // the AES input/output
        int[] byteArray;
        int[] input = new int[16];
        int[] output;
        int[] ciphertext = new int[16];
        int[] cipherOut = null;
        // char firstRound
        boolean firstRound = true;
        if (mode == this.modeOfOperation.CBC)
            this.padBytesIn(bytesIn);
        if (bytesIn != null) {
            for (int j = 0; j < Math.ceil(bytesIn.length / 16); j++) {
                int start = j * 16;
                int end = j * 16 + 16;
                if (j * 16 + 16 > bytesIn.length)
                    end = bytesIn.length;
                byteArray = this.getBlock(bytesIn, start, end, mode);
                if (mode == this.modeOfOperation.CFB) {
                    if (firstRound) {
                        output = this.aes.encrypt(iv, key, size);
                        firstRound = false;
                    } else
                        output = this.aes.encrypt(input, key, size);
                    for (int i = 0; i < 16; i++)
                        ciphertext[i] = byteArray[i] ^ output[i];
                    for (int k = 0; k < end - start; k++) {
                        cipherOut = AESUtil.arrayPush(cipherOut, ciphertext[k]);
                    }
                    input = ciphertext;
                } else if (mode == this.modeOfOperation.OFB) {
                    if (firstRound) {
                        output = this.aes.encrypt(iv, key, size);
                        firstRound = false;
                    } else
                        output = this.aes.encrypt(input, key, size);
                    for (int i = 0; i < 16; i++)
                        ciphertext[i] = byteArray[i] ^ output[i];
                    for (int k = 0; k < end - start; k++)
                        cipherOut = AESUtil.arrayPush(cipherOut, ciphertext[k]);
                    input = output;
                } else if (mode == this.modeOfOperation.CBC) {
                    for (int i = 0; i < 16; i++)
                        input[i] = byteArray[i] ^ ((firstRound) ? iv[i] : ciphertext[i]);
                    firstRound = false;
                    ciphertext = this.aes.encrypt(input, key, size);
                    // always 16 bytes because of the padding for CBC
                    for (int k = 0; k < 16; k++)
                        cipherOut = AESUtil.arrayPush(cipherOut, ciphertext[k]);
                }
            }
        }
        return cipherOut;
    }

    /*
     * Mode of Operation Decryption cipherIn - Encrypted String as array of bytes
     * originalsize - The unencrypted string length - required for CBC mode - mode
     * of type modeOfOperation key - a number array of length 'size' size - the bit
     * length of the key iv - the 128 bit number array Initialization Vector
     */
    public int[] decrypt(int[] cipherIn, int mode, int[] key, int[] iv) {
        int size = key.length;
        if (iv.length % 16 != 0) {
            throw new RuntimeException("iv length must be 128 bits.");
        }
        // the AES input/output
        int[] ciphertext;
        int[] input = new int[16];
        int[] output;
        int[] byteArray = new int[16];
        int[] bytesOut = new int[] {};
        // char firstRound
        boolean firstRound = true;
        if (cipherIn != null) {
            for (int j = 0; j < Math.ceil(cipherIn.length / 16); j++) {
                int start = j * 16;
                int end = j * 16 + 16;
                if (j * 16 + 16 > cipherIn.length)
                    end = cipherIn.length;
                ciphertext = this.getBlock(cipherIn, start, end, mode);
                if (mode == this.modeOfOperation.CFB) {
                    if (firstRound) {
                        output = this.aes.encrypt(iv, key, size);
                        firstRound = false;
                    } else
                        output = this.aes.encrypt(input, key, size);
                    for (int i = 0; i < 16; i++)
                        byteArray[i] = output[i] ^ ciphertext[i];
                    for (int k = 0; k < end - start; k++)
                        bytesOut = AESUtil.arrayPush(bytesOut, byteArray[k]);
                    input = ciphertext;
                } else if (mode == this.modeOfOperation.OFB) {
                    if (firstRound) {
                        output = this.aes.encrypt(iv, key, size);
                        firstRound = false;
                    } else
                        output = this.aes.encrypt(input, key, size);
                    for (int i = 0; i < 16; i++)
                        byteArray[i] = output[i] ^ ciphertext[i];
                    for (int k = 0; k < end - start; k++)
                        bytesOut = AESUtil.arrayPush(bytesOut, byteArray[k]);
                    input = output;
                } else if (mode == this.modeOfOperation.CBC) {
                    output = this.aes.decrypt(ciphertext, key, size);
                    for (int i = 0; i < 16; i++)
                        byteArray[i] = ((firstRound) ? iv[i] : input[i]) ^ output[i];
                    firstRound = false;
                    for (int k = 0; k < end - start; k++)
                        bytesOut = AESUtil.arrayPush(bytesOut, byteArray[k]);
                    input = ciphertext;
                }
            }
            if (mode == this.modeOfOperation.CBC)
                this.unpadBytesOut(bytesOut);
        }
        return bytesOut;
    }
}
/*
 * END MODE OF OPERATION SECTION
 */
//};