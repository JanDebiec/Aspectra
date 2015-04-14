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
            		String nameWithoutBase = fullNameWithPath.substring(mLengthOfBasePath);
            		String relativePath = path.substring(mLengthOfBasePath);
            		mFileArray.add(nameWithoutBase);
            	}
            }
        }
    }
}
