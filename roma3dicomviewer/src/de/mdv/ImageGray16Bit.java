package de.mdv;

public class ImageGray16Bit {

	private int width = 0;

	/**
	 * L'altezza dell'immagine che corrsponde
	 * al numero di righe.
	 *
	 */
	private int height = 0;

	/**
	 * Image data.
	 */
	private int[] imageData = null;
	private int[] originalImageData = null;

	/**
	 * Informazioni sul paziente
	 */
	private String patientName;
	private String patientPrename;
	private String patientBirth;


	public void setImageData(int[] imageData)
	{
		this.imageData = imageData;
	}

	public int[] getImageData()
	{
		return imageData;
	}

	
	/**
	/* Se l'immagine non esiste, creo una nuova immagine vuota.
	 * Altrimenti creo una nuova immagine uguale a quella passata
	 * come parametro.
	 * Fa una copia sicura dei dati dell'immagine.
	 * @param imageData
	 */
	public void setOriginalImageData(int[] imageData)
	{
		
		if(imageData == null) 
			this.originalImageData = imageData;
		this.originalImageData = new int[imageData.length];
		System.arraycopy(imageData, 0, this.originalImageData, 0, imageData.length);
	}

	/**
	 * Ritorna solo una copia dell'immagine originale
	 * lascia l'originale intatta.
	 * @return
	 */
	public int[] getOriginalImageData()
	{
		if(this.originalImageData == null)
			return originalImageData;
		int result[] = new int[this.originalImageData.length];
		System.arraycopy(originalImageData, 0, result, 0, this.originalImageData.length);
		return result;
	}

	/*
	 * metodi per la gestione dei dati delle immagini e dei pazienti
	 */
	
	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getWidth()
	{
		return width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getHeight()
	{
		return height;
	}

	public void setPatientName(String value)
	{
		this.patientName = value;
	}
	public void setPatientPrename(String value)
	{
		this.patientPrename = value;
	}
	public void setPatientBirth(String value)
	{
		this.patientBirth = value;
	}

	public String getPatientName()
	{
		return patientName;
	}
	public String getPatientPrename()
	{
		return patientPrename;
	}
	public String getPatientBirth()
	{
		return patientBirth;
	}
}
