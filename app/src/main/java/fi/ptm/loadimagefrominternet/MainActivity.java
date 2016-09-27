package fi.ptm.loadimagefrominternet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author PTM
 */
public class MainActivity extends Activity {



    // image view object
    private ImageView imageView;
    // text view object
    private TextView textView;
    // progress bar object
    private ProgressBar progressBar;
    // example image's path (change to your own if needed...)
    private final String PATH = "http://ptm.fi/android/";
    // example image names (change to your own if needed...)
    private String[] images =
            {"http://student.labranet.jamk.fi/~H8897/php/web-ohjelmointi/viikko38/talo1.jpg",
            "http://student.labranet.jamk.fi/~H8897/php/web-ohjelmointi/viikko38/talo2.jpg",
            "http://student.labranet.jamk.fi/~H8897/php/web-ohjelmointi/viikko38/talo3.jpg"};
    // which image is now visible
    private int imageIndex;
    // async task to load a new image
    private DownloadImageTask task;
    // swipe down and up values
    private float x1,x2;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // draw layout
        setContentView(R.layout.activity_main);
        // get image view
        imageView = (ImageView) findViewById(R.id.imageView);
        
        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // start showing images
        imageIndex = 0;
        showImageURL();


       /* // create a new AsyncTask object
        DownloadImageTask task = new DownloadImageTask();
        // ulr to load data
        String url1 = images[0];
        String url2 = images[1];
        String url3 = images[2];
        // start asynctask
        task.execute(url1,url2,url3);*/
    }

    public void showImage() {
        // create a new AsyncTask object
        task = new DownloadImageTask();
        // start asynctask
        task.execute(PATH + images[imageIndex]);
    }

    public void showImageURL() {
        // create a new AsyncTask object
        task = new DownloadImageTask();
        // start asynctask
        task.execute(images[imageIndex]);
    }







    // asynctask class

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {

        // this is done in UI thread, nothing this time
        @Override
        protected void onPreExecute() {
            // show loading progress bar
            progressBar.setVisibility(View.VISIBLE);
        }

        // this is background thread, load image and pass it to onPostExecute
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL imageUrl;
            Bitmap bitmap = null;
            try {


                imageUrl = new URL(urls[0]);
                InputStream in = imageUrl.openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("<<LOADIMAGE>>", e.getMessage());
            }
            return bitmap;
        }

        // this is done in UI thread
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            textView.setText("Image " + (imageIndex + 1) + "/" + images.length);
            // hide loading progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (x1 < x2) { // left to right -> previous
                    imageIndex--;
                    if (imageIndex < 0) imageIndex = images.length-1;
                } else { // right to left -> next
                    imageIndex++;
                    if (imageIndex > (images.length-1)) imageIndex = 0;
                }
                showImageURL();
                break;
        }
        return false;
    }

}
