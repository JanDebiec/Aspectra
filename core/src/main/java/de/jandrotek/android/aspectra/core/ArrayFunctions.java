/**
 * This file is part of Aspectra.
 *
 * Aspectra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aspectra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aspectra.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 *
 * Copyright Jan Debiec
 */
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
            fDiffIndexInput = nDiffIndexOutput / factor;
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
