package org.dcm4che2.data;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

/* Le classi che implementano questa interface vengono trasformate 
 * in uno stream di byte */

public interface DicomElement extends Serializable { 
	int tag();

	VR vr();

	int vm(SpecificCharacterSet cs);

	boolean bigEndian();

	DicomElement bigEndian(boolean bigEndian);

	int length();

	boolean isEmpty();

	boolean hasItems();

	int countItems();

	byte[] getBytes();

	boolean hasDicomObjects();

	boolean hasFragments();

	DicomObject getDicomObject();

	DicomObject getDicomObject(int index);

	DicomObject removeDicomObject(int index);

	boolean removeDicomObject(DicomObject item);  

	DicomObject addDicomObject(DicomObject item);

	DicomObject addDicomObject(int index, DicomObject item);

	DicomObject setDicomObject(int index, DicomObject item);

	byte[] getFragment(int index);

	byte[] removeFragment(int index);

	boolean removeFragment(byte[] b);

	byte[] addFragment(byte[] b);

	byte[] addFragment(int index, byte[] b);

	byte[] setFragment(int index, byte[] b);

	short[] getShorts(boolean cache);

	int getInt(boolean cache);

	int[] getInts(boolean cache);

	float getFloat(boolean cache);

	float[] getFloats(boolean cache);

	double getDouble(boolean cache);

	double[] getDoubles(boolean cache);

	String getString(SpecificCharacterSet cs, boolean cache);

	String[] getStrings(SpecificCharacterSet cs, boolean cache);

	Date getDate(boolean cache);

	Date[] getDates(boolean cache);

	DateRange getDateRange(boolean cache);

	Pattern getPattern(SpecificCharacterSet cs, boolean ignoreCase,
			boolean cache);

	DicomElement share();

	DicomElement filterItems(DicomObject filter);

	StringBuffer toStringBuffer(StringBuffer sb, int maxValLen);

	String getValueAsString(SpecificCharacterSet cs, int truncate);
}

