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

import java.io.File;
import java.util.ArrayList;

public class FileWalker {

	private ArrayList mFileArray;
	private String mFileExt;
	private String mStartPath;
	private int mLengthOfBasePath;

	public FileWalker(String startPath){
		mStartPath = startPath;
		mLengthOfBasePath = mStartPath.length();
	}

	public String[] search4Files(String extension){
		String[] fileList;
		int listSize;
		mFileArray = new ArrayList();
		mFileExt = extension;

		walk(mStartPath);

		listSize = mFileArray.size();
		fileList = new String[listSize];
		for(int i = 0; i < listSize; i++){
			fileList[i] = mFileArray.get(i).toString();
		}

		return fileList;
	}

    public void walk( String path ) {
    	int lengthOfThePath = path.length();
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk( f.getAbsolutePath() );
            }
            else {
            	if(f.getName().endsWith(mFileExt))
            	{
            		String fullNameWithPath = f.getAbsolutePath();
            		String nameWithoutPath = fullNameWithPath.substring(lengthOfThePath);
            		String nameWithoutBase = fullNameWithPath.substring(mLengthOfBasePath + 1);// 1 for '/'
            		String relativePath = path.substring(mLengthOfBasePath);
            		mFileArray.add(nameWithoutBase);
            	}
            }
        }
    }
}
