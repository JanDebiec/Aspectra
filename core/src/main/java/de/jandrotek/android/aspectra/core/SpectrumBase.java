package de.jandrotek.android.aspectra.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class for reading Chroco-Spectra files.
 * original Chroco files, *.spk,
 * 		without header, default length:, data type:
 *
 * class will be moved to external JAVA library
 * for testing with JUnit from IntelliJ
 * @author jan
 *
 */
public abstract class SpectrumBase {
    protected String mFileName;
    protected static final String mExtensionAsp = "asp";
    protected static final String mExtensionChr = "spk";
    //private String mFileNameWithPath;
    protected int[] mValues;
    protected static final int SPK_CHR_FILE_DEFAULT_SIZE = 800;

    public abstract int getDataSize();

	public abstract void setDataSize(int dataSize);


	public void setFileName(String fileName){
        mFileName = fileName;
	}

	public String getFileName(){
		return mFileName;
	}

    public int[] getValues(){

        return mValues;
    }

    public abstract int readValuesFromFile() ;

//    /*
//    Whereas you can have DateFormat patterns such as:
//
//    "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//    "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//    "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//    "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//    "yyMMddHHmmssZ"-------------------- 010704120856-0700
//    "K:mm a, z" ----------------------- 0:08 PM, PDT
//    "h:mm a" -------------------------- 12:08 PM
//    "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
//     */
//    public static String generateSpectrumAspFileName(){
//
//        // prepare date as string
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String currentDateandTime = sdf.format(new Date());
//
//        return currentDateandTime + "." + mExtensionAsp;
//    }
//
//    public static void saveStringToFile(String text, File target) throws IOException {
//        FileOutputStream fos=new FileOutputStream(target);
//        OutputStreamWriter out=new OutputStreamWriter(fos);
//
//        out.write(text);
//        out.flush();
//        fos.getFD().sync();
//        out.close();
//    }

}
