package identitycrises.com.identitycrisis.imagehandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * This class manages image capture and compression
 * @author Sagar Makwana
 * @version 1.0
 */
public class ImageManager {

	public static final int REQUEST_IMAGE_CAPTURE = 1;
	private Uri mCurrentImageFileUri = null;
	private Uri mTempImageFileUri = null;
	private Activity activity;
	
	private int samplingFactor = 4;
	private int compressionQualityFactor = 70;
	

	public ImageManager(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * This method needs to be called when you need to capture an image. As soon as this method is
	 * called a callback is made {@link #onActivityResult(int, int, Intent)}, so implement it in the 
	 * onActivityResult of the activity from which is called.
	 * @throws InvalidPathException Thrown if ExternalFileDirectory cannot be resolved.
	 */
	public void captureImage(int requestCode) throws InvalidPathException{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
		File imageFile =  createTempImageFile();
		if(imageFile == null){
			throw new InvalidPathException("External Files Directory Not Found ");
		}
		else {
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempImageFileUri());
			activity.startActivityForResult(takePictureIntent, requestCode);
		}
	}

	public void captureSelfieImage(int requestCode) throws InvalidPathException {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
		File imageFile = createTempImageFile();
		if (imageFile == null) {
			throw new InvalidPathException("External Files Directory Not Found ");
		}
		else {
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempImageFileUri());
            activity.startActivityForResult(takePictureIntent, requestCode);
		}
	}

	public int getSamplingFactor() {
		return samplingFactor;
	}
	
	/**
	 * This method sets the sampling factor for sub-sampling the image
	 * @param samplingFactor Sampling factor in powers of 2. eg. 2, 4 , 8 ,...
	 */
	public void setSamplingFactor(int samplingFactor) {
		this.samplingFactor = samplingFactor;
	}

	public int getCompressionQualityFactor() {
		return compressionQualityFactor;
	}

	/**
	 * This method sets the compression quality factor for JPEG compression 
	 * @param compressionQualityFactor Compression quality factor between 0 - 100. Higher the factor more the quality is preserved.  
	 */
	public void setCompressionQualityFactor(int compressionQualityFactor) {
		this.compressionQualityFactor = compressionQualityFactor;
	}
	
	/**
	 * This function has to be compulsorily called from the overridden function onActivityResult in the
	 * calling activity.
	 * @param requestCode Same as the requestCode parameter in onActivityResult method in calling activity 
	 * @param resultCode Same as the resultCode parameter in onActivityResult method in calling activity
	 * @param data Same as the data parameter in onActivityResult method in calling activity
	 * @return Uri Returns URI of the newly created image if resultCode is RESULT_OK else returns null
	 * @throws IOException  Thrown if there is some problem in FileOutputStream
	 * @throws FileNotFoundException Thrown if created image file cannot be found
	 */
	public Uri onActivityResult(int requestCode, int resultCode, Intent data)throws IOException,FileNotFoundException{
		if ( resultCode == activity.RESULT_OK){
			Bitmap scaledBitmap = createScaledBitmap(getTempImageFileUri(), getSamplingFactor());
			
			storeCompressedImageFile(scaledBitmap, getCompressionQualityFactor());
			
			deleteTempImageFile(getTempImageFileUri());
			
			return getCurrentImageFileUri();
			
		}
		else {
			return null;
		}
			
			
	}
	
	//This method creates a new File which later holds an image.
	private File createImageFile(){
		String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File imageFile = new File(storageDir, imageFileName+".jpg");
		return imageFile;
	}
		
	/**
	 * This function returns the URI of the newly created compressed image.
	 * @return URI of newly created compressed image if onActivityResult function is called or else it returns null.
	 * 
	 */
	public Uri getCurrentImageFileUri(){
		return mCurrentImageFileUri;
	}
		
	private void setCurrentImageFileUri(File imageFile){
		mCurrentImageFileUri = Uri.fromFile(imageFile);
	}

	private void storeCompressedImageFile (Bitmap scaledBitmap,int percentQuality) throws IOException,FileNotFoundException{
		File imageFile = createImageFile();
		
		FileOutputStream fos = new FileOutputStream(imageFile);
		scaledBitmap.compress(Bitmap.CompressFormat.JPEG, percentQuality, fos);
		fos.flush();
		fos.close();
		
		setCurrentImageFileUri(imageFile);
		
	}	
	
	//This method subsamples the image with the provided sampling factor.
	private Bitmap createScaledBitmap(Uri imageFileUri , int sampleSize){
		
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = sampleSize;
		bmOptions.inPurgeable=true;
		
		Bitmap scaledBitmap = BitmapFactory.decodeFile(imageFileUri.getPath(), bmOptions);		
		
		return scaledBitmap;
	}

	//The following 4 functions deal with handling temporary original image created by the Camera
	private File createTempImageFile(){
		String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File imageFile = new File(storageDir, "temp_"+imageFileName+".jpg");
		setTempImageFileUri(imageFile);
		return imageFile;
	}
		
	private Uri getTempImageFileUri(){
		return mTempImageFileUri;
	}
		
	private void setTempImageFileUri(File imageFile){
		if(imageFile != null)
			mTempImageFileUri = Uri.fromFile(imageFile);
		else
			mTempImageFileUri=null;			
	}
		
	private void deleteTempImageFile(Uri tempImageFileUri){
		new File(tempImageFileUri.getPath()).delete();
		setTempImageFileUri(null);
	}	
}