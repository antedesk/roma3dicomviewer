package de.mdv;

import java.io.File;
import java.io.FileFilter;

/**
 * Questa classe filtra quali sono file dicom accettabili e quali no.
 * come estensione accetta solo .dcm o nessuna estensione
 * 
 * target far accettare altre estensioni dicom conosciute
 * @author utente
 *
 */
public class DicomFileFilter implements FileFilter{

	public boolean accept(File pathname) 
	{
		if (pathname.isFile() && !pathname.isHidden()) 
		{
			// Get the file name
			String fileName = pathname.getName();
			fileName = fileName.toLowerCase();
			// If the file is a dicomdir return false
			if (fileName.equals("dicomdir"))
				return false;
			// file must end with ".dcm" or no extension
			if(fileName.lastIndexOf(".") == -1)return true;
			if(fileName.endsWith("dcm"))return true;
		}
		return false;
	}

}
