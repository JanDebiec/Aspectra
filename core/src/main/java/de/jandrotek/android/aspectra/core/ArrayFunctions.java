package de.jandrotek.android.aspectra.core;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by jan on 14.04.15.
 * 23.07.2015   added handling from ArrayUtils
 * 26.06.2015   move function will be not used (only indexies in spectrumFile)
 *              stretch, only data != 0 will be considered
 *              new stretch = stretch from index 0, plus move data in spectrum
 */
public class ArrayFunctions {

    public static int[] moveArrayRight(int[] data, int move){
        int[] zeroArray = new int[move];

        int[] newArray = ArrayUtils.addAll(zeroArray, data);
        return newArray;
    }

    public static int[] moveArrayLeft(int[] data, int move){
        int[] zeroArray = new int[move];
        int[] leftArray = ArrayUtils.subarray(data, move, data.length - move);

        int[] newArray = ArrayUtils.addAll(leftArray, zeroArray);
        return newArray;
    }

    public static int[] stretchArray(int[] data, float factor){
        int nIndexOutput = 0;
        int newLength = (int)((float)data.length * factor);
        int[] newArray = new int[newLength ];
        int temp;
        int nDiffIndexOutput;
        float fDiffIndexInput;
        float fIndexInput;
        int nIndexInputLeft;
        int nIndexInputRight;
        float diffLeft, diffRight;

        // go to the right
        do{
            nDiffIndexOutput = nIndexOutput;
            fDiffIndexInput = nDiffIndexOutput * factor;
            fIndexInput = fDiffIndexInput;
            nIndexInputLeft = (int)fIndexInput;
            nIndexInputRight = (int)(fIndexInput + 1.0f);
            if(nIndexInputRight > (data.length - 1)){
                break;
            }

            diffLeft = fIndexInput - (float)nIndexInputLeft;
            diffRight = (float)nIndexInputRight - (fIndexInput);
            temp = (int)((float)data[nIndexInputLeft] * diffRight + (float)data[nIndexInputRight] * diffLeft);
            newArray[nIndexOutput] = temp;
            nIndexOutput++;

        } while ((nIndexOutput <  (newArray.length)) && (nIndexInputRight < (data.length)));

        return newArray;
    }
}
