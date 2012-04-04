package de.mdv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * Permette la visualizzazione delle immagini dicom utilizzando ImageGray16Bit. Tale tipologia di immagine è settata a mano
 * Possibilità di creare immagini Bitmap anch'esse saranno settate manualmente.
 * Contiene le informazioni per lo zoom (che stesso l'autore evidenzia) e le informazioni per GestureDetector
 * @author utente
 *
 */

public class DicomImageView extends ImageView{


	Matrix matrix = new Matrix();
	private Context context;
	ImageGray16Bit image;
	GestureDetector gestureDetector;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	/**
	 *  Remember some things for zooming
	 */
	PointF last = new PointF();
	PointF start = new PointF();
	float minScale = 1f;	// zoom minimo
	float maxScale = 5f;	// zoom massimo può essere settato
	float cachedScaleFactor = 1f;
	float[] m;

	float redundantXSpace, redundantYSpace;

	float width, height;
	static final int CLICK = 3;
	float saveScale = 1f;
	float right, bottom, origWidth, origHeight, bmWidth, bmHeight;


	float cachedRight = 0f, cachedBottom = 0f;

	ScaleGestureDetector mScaleDetector;


	/**
	 * Costruttori estendono quelli di ImageView, inizializzando i click event e il touch listener
	 */
	
	public DicomImageView(Context context)
	{
		super(context);
		this.context = context;
		init();
	}

	public DicomImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		init();
	}

	public DicomImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}


	
	void initNew()
	{
		super.setClickable(true);
		setOnTouchListener(new MyOnTouchListener(this.context, this));
	}


	
	 // ma è proprio indispensabile ? -.-
	public void init()
	{
		initNew();
	}

	/**
	 * metodo per disegnare, crea una bitmap a partire da image
	 */
	public void draw()
	{
		if(image != null && image.getImageData() != null)
		{
			Bitmap imageBitmap = Bitmap.createBitmap(image.getImageData(), image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
			// Set the image
			setImageBitmap(imageBitmap);
		}
	}


	public void drawWithMatrix()
	{
		if(image != null && image.getImageData() != null)
		{
			Bitmap imageBitmap = Bitmap.createBitmap(image.getImageData(), image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
			// Set the image
			setImageBitmap(imageBitmap);
			setImageMatrix(matrix);
		}
	}

	/**
	 * metodo per settare un ImageGray16Bit
	 * @param image
	 */
	public void setImage(ImageGray16Bit image)
	{
		this.image = image;
	}

	/**
 	 * metodo per settare un ImageGray16Bit
	 * @return ImageGray16Bit
	 */

	public ImageGray16Bit getImage()
	{
		return this.image;
	}


	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		bmWidth = bm.getWidth();
		bmHeight = bm.getHeight();
	}

	
	public void setMaxZoom(float x)
	{
		maxScale = x;
	}



	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		//Fit to screen.
		float scale;
		float scaleX =  (float)width / (float)bmWidth;
		float scaleY = (float)height / (float)bmHeight;
		scale = Math.min(scaleX, scaleY);
		matrix.setScale(scale, scale);
		setImageMatrix(matrix);
		saveScale = 1f;

		// Center the image
		redundantYSpace = (float)height - (scale * (float)bmHeight) ;
		redundantXSpace = (float)width - (scale * (float)bmWidth);
		redundantYSpace /= (float)2;
		redundantXSpace /= (float)2;

		matrix.postTranslate(redundantXSpace, redundantYSpace);

		origWidth = width - 2 * redundantXSpace;
		origHeight = height - 2 * redundantYSpace;
		right = width * saveScale - width - (2 * redundantXSpace * saveScale);
		bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
		setImageMatrix(matrix);
	}


	public void updateMatrix()
	{
		setImageMatrix(cachedMatrix);
	}


	Matrix cachedMatrix;

	public void setImageMatrix(Matrix matrix)
	{
		cachedMatrix = matrix;
		super.setImageMatrix(matrix);
	}

	/**
	 * Get the image width and height
	 * Compute the translation
	 * Set the transformation
	 * Set the Image Matrix
	 */
	public void paintCachedSize()
	{
		// Get the image width and height
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// Compute the translation
		float dx = ((float) getMeasuredWidth() - imageWidth) / 2f;
		float dy = ((float) getMeasuredHeight() - imageHeight) / 2f;
		//scaleFactor = cachedScaleFactor;
		matrix.set(getImageMatrix());
		// Set the transformation
		matrix.setScale(saveScale, saveScale, 0f, 0f);
		matrix.postTranslate(dx, dy);
		// Set the Image Matrix
		setImageMatrix(matrix);
	}


	/**
	 * permette di settare un fattore di scala (zoom) memorizzato/cached che di default è 1
	 * è possibile settarlo
	 * task: a runtime prendere di volta in volta lo zoom e passarlo all'immagine successiva
	 * @param cachedScaleFactor
	 */
	public void setCachedScaleFactor(float cachedScaleFactor)
	{
		this.cachedScaleFactor = cachedScaleFactor;
	}

}

