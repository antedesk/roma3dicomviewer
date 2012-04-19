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
			// Prende il nome del file
			String fileName = pathname.getName();
			fileName = fileName.toLowerCase();
			// Se il file è una dicomdir ritorna false
			if (fileName.equals("dicomdir"))
				return false;
			// il file deve finire con ".dcm" oppure ritorna false
			if(fileName.lastIndexOf(".") == -1)return true;
			if(fileName.endsWith("dcm"))return true;
		}
		return false;
	}

}
