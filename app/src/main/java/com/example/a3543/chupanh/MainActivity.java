package com.example.a3543.chupanh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageButton ibTakePhoto;
    private ImageView ivPhoto;
    private Uri outputFileUri;

    public static final int REQUEST_CAMERA = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ibTakePhoto = (ImageButton) findViewById(R.id.ibTakePhoto);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);


        ibTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                outputFileUri = Uri.fromFile(createFileUri(MainActivity.this));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });
    }
    //Tạo 1 file trong Sdcard để lưu ảnh vừa chụp
    public File createFileUri(Context context) {
        File[] externalFile = ContextCompat.getExternalFilesDirs(context, null);

        if (externalFile == null) {
            externalFile = new File[]{
                    context.getExternalFilesDir(null)};
        }
        final File root = new File(externalFile[0] + File.separator + "Image" + File.separator);

        root.mkdir();
        final String fname = "Image_Photo.jpg";
        final File sdImageMainDirectory = new File(root, fname);
        if (sdImageMainDirectory.exists()) {
            sdImageMainDirectory.delete();
        }
        return sdImageMainDirectory;
    }

    //Xử lý ảnh trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bitmap bitmap = decodeFromFile(outputFileUri.getPath(),800,800);
            if (bitmap != null){
                ivPhoto.setImageBitmap(bitmap);
            }
        }
    }

    //Hàm sử dụng để tạo Bitmap từ File
    public static Bitmap decodeFromFile(String path, int width, int height) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);

            int scale = 1;
            while (o.outWidth / scale / 2 >= width && o.outHeight / scale / 2 >= height)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
