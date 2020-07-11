package com.mthoko.mobile.util.encryption;

public class AESUtil {
//
//	public static Object getWord(Object[] word) {
//		Object c = word[0];
//		for (int i = 0; i < 3; i++)
//			word[i] = word[i + 1];
//		word[3] = c;
//
//		return word;
//	}

    public static int[] arrayPush(int[] array, int val) {
        int[] result = new int[array.length + 1];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        result[result.length - 1] = val;
        return result;
    }

    public static int[] splice(int[] data, int startIndex, int numOfItemsToRemove) {
        int[] bkup = data;
        data = new int[data.length - numOfItemsToRemove];
        int index = 0;
        for (int i = 0; i < bkup.length; i++) {
            if (i >= startIndex && i < data.length + numOfItemsToRemove) {
                continue;
            }
            data[index++] = bkup[i];
        }
        return data;
    }

    public static int[] toNumbers(String d) {
        int[] numbers = new int[d.length() / 2];
        int index = 0;
        for (int i = 0; i < d.length(); i += 2, index++) {
            numbers[index] = Integer.parseInt(d.substring(i, i + 2), 16);
        }
        return numbers;
    }

    public static String toHex(int[] decrypt) {
        StringBuilder hex = new StringBuilder();
        for (int num : decrypt) {
            if(num < 16) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(num));
        }
        return hex.toString();
    }

}
