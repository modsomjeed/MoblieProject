package th.ac.pim.moblieproject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//import th.ac.pim.mobileproject.R;


import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private Uri mImageCaptureUri;
	private ImageView mImageView;	
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        final String [] items			= new String [] {"Camera", "Gallery"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		
		builder.setTitle("ChooseImage");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file		 = new File(Environment.getExternalStorageDirectory(),
							   			"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {			
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}			
					
					dialog.cancel();
				} else {
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );
		
		final AlertDialog dialog = builder.create();
		
		mImageView = (ImageView) findViewById(R.id.iv_pic);
		
		((Button) findViewById(R.id.btn_choose)).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
		Bitmap bitmap 	= null;
		String path		= "";
		
		if (requestCode == PICK_FROM_FILE) {
			mImageCaptureUri = data.getData(); 
			path = getRealPathFromURI(mImageCaptureUri); //from Gallery 
		
			if (path == null)
				path = mImageCaptureUri.getPath(); //from File Manager
			
			if (path != null) 
				bitmap 	= BitmapFactory.decodeFile(path);
		} else {
			path	= mImageCaptureUri.getPath();
			bitmap  = BitmapFactory.decodeFile(path);
		}
			
		mImageView.setImageBitmap(bitmap);		
	}
	
	public String getRealPathFromURI(Uri contentUri) {
        String [] proj 		= {MediaStore.Images.Media.DATA};
        Cursor cursor 		= managedQuery( contentUri, proj, null, null,null);
        
        if (cursor == null) return null;
        
        int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}
	
	
	
	
	//Class ´Ö§ Intent ·ÕèÊÒÁÁÒÃ¶áªÃìä¿Åìä´é¨Ò¡ã¹à¤Ã×èÍ§
//	public void share () {
//		Intent share = new Intent(Intent.ACTION_SEND);
//		share.setType("image/jpeg");
//		share.putExtra(Intent.EXTRA_STREAM, mImageCaptureUri);
//		startActivity(Intent.createChooser(share, "Share Image"));
//	}
//	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			//
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
			
//		case R.id.menuitem_share:
//			share();
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
	public void share () {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/jpeg");
		share.putExtra(Intent.EXTRA_STREAM, mImageCaptureUri);
		startActivity(Intent.createChooser(share, "Share Image"));
	}
	

//	private static Uri getOutputMediaFileUri(int type){
//	      return Uri.fromFile(getOutputMediaFile(type));
//	}

//	private static File getOutputMediaFile(int type){
//	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//	              Environment.DIRECTORY_PICTURES), "MyApp");
//	    if (! mediaStorageDir.exists()){
//	        if (! mediaStorageDir.mkdirs()){
//	            Log.d("MyApp", "failed to create directory");
//	            return null;
//	        }
//	    }
//
//	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//	    File mediaFile;
//	    if (type == MEDIA_TYPE_IMAGE){
//	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//	        "IMG_"+ timeStamp + ".jpg");
//	    } else {
//	        return null;
//	    }
//
//	    return mediaFile;
//	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
			
		case R.id.menuitem_share:
			share();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

}