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

/**
 * Created by jan on 09.12.14.
 */
public class Peak {
    private int mPosition;
    private int mValue;
    private int mLeftBorderPos;
    private int mLeftBorderValue;
    private int mRightBorderPos;
    private int mRightBorderValue;

    public Peak(){

    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public int getLeftBorderPos() {
        return mLeftBorderPos;
    }

    public void setLeftBorderPos(int leftBorderPos) {
        mLeftBorderPos = leftBorderPos;
    }

    public int getLeftBorderValue() {
        return mLeftBorderValue;
    }

    public void setLeftBorderValue(int leftBorderValue) {
        mLeftBorderValue = leftBorderValue;
    }

    public int getRightBorderPos() {
        return mRightBorderPos;
    }

    public void setRightBorderPos(int rightBorderPos) {
        mRightBorderPos = rightBorderPos;
    }

    public int getRightBorderValue() {
        return mRightBorderValue;
    }

    public void setRightBorderValue(int rightBorderValue) {
        mRightBorderValue = rightBorderValue;
    }
}
