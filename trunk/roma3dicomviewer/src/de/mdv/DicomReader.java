package de.mdv;

import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.dcm4che2.data.BasicDicomObject;
import org.dcm4che2.io.DicomInputStream;

public class DicomReader {


	BasicDicomObject bdo;
	DicomInputStream dis;
	int pixelData[] = null;
	int width, height;

	String PatientName = "";
	String PatientPrename = "";
	Date PatientBirth = null;
	String PatientBirthString = "";
	
	/**
	 * Semplice costruttore che prende in input una stringa, questa stringa viene poi passata
	 * come paramtero al metodo " init(String filename)"
	 * @param fileName
	 */

	public DicomReader(String fileName)
	{
		this.init(fileName);
	}

	/**
	 * 
	 * @param fileName
	 */
	
	private void init(String fileName)
	{
		try
		{
			bdo = new BasicDicomObject();
			dis = new DicomInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(fileName)));
			dis.readDicomObject(bdo, -1);
			height = bdo.getInt(org.dcm4che2.data.Tag.Rows);
			width = bdo.getInt(org.dcm4che2.data.Tag.Columns);
			boolean invert = bdo.get(0x00280004).toString().toUpperCase().endsWith("[MONOCHROME1]") ? true : false;

			String completeName = bdo.getString(org.dcm4che2.data.Tag.PatientName);
			StringTokenizer tokenizer = new StringTokenizer(completeName, "^");
			int counter = 0;
			while(tokenizer.hasMoreElements())
			{
				if(counter == 0)
					PatientName = tokenizer.nextToken();
				else if(counter == 1)
					PatientPrename = tokenizer.nextToken();
				counter++;
			}
			PatientBirth = bdo.getDate(org.dcm4che2.data.Tag.PatientBirthDate);
			if(PatientBirth != null)
			{
				PatientBirthString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(PatientBirth);
			}
			int bitsAllocated = bdo.getInt(org.dcm4che2.data.Tag.BitsAllocated);
			if(bitsAllocated == 8 || bitsAllocated == 12 || bitsAllocated == 16)
			{
				byte bytePixels[] = DicomHelper.readPixelData(bdo);
				pixelData = DicomHelper.convertToIntPixelData(bytePixels, bitsAllocated, width, height, invert);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getCause());
		}
	}

	/*
	 * metodi per la gestione delle informazioni sull'immagine
	 */
	
	public int[] getPixelData()
	{
		return pixelData;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	/*
	 * metodi per la gestione dell'anagrafica di base del paziente
	 */
	
	public String getPatientName()
	{
		return PatientName;
	}

	public String getPatientPrename()
	{
		return PatientPrename;
	}
	public String getPatientBirthString()
	{
		return PatientBirthString;
	}
}

