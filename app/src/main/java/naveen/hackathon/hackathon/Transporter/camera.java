package naveen.hackathon.hackathon.Transporter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.SignupActivity;
import naveen.hackathon.hackathon.activities.ECartHomeActivity;

public class camera extends AppCompatActivity {
    private Uri selectedImage;
    static FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    Uri downloadUrl;
    private FirebaseAuth auth;
    private static FirebaseFirestore Firebasefirestore;
    String File_name,description;
    private Uri photoURI;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        Firebasefirestore= FirebaseFirestore.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "naveen.hackathon.hackathon",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            uploadImage();
        }
    }

    public void uploadImage() {
        //create reference to images folder and assing a name to the file that will be uploaded
        Dialog d=new Dialog(camera.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.input_dialog);

        final EditText nameEditTxt= (EditText) d.findViewById(R.id.nameEditText);
        final EditText description_EditTxt= (EditText) d.findViewById(R.id.description_EditText);
        Button saveBtn= (Button) d.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (nameEditTxt.getText().toString().length() == 0) {
                    nameEditTxt.setError(getString(R.string.error_field_required));
                }
                else if(description_EditTxt.getText().toString().length() == 0){
                    description_EditTxt.setError(getString(R.string.error_field_required));
                }
                else {
                    File_name=nameEditTxt.getText().toString()+".jpg";
                    description=description_EditTxt.getText().toString();
                    imageRef = storage.getReferenceFromUrl("gs://hackathon-9cbcd.appspot.com").child(File_name);
                    //creating and showing progress dialog
                    progressDialog = new ProgressDialog(camera.this);
                    progressDialog.setMax(100);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    //starting upload
                    uploadTask = imageRef.putFile(photoURI);
                    // Observe state change events such as progress, pause, and resume
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //sets and increments value of progressbar
                            progressDialog.incrementProgressBy((int) progress);
                        }
                    });
                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            //Toast.makeText(getApplication(),"Error in uploading!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            //taskSnapshot.getMetadata()
                            Map<String, Object> newData = new HashMap<>();
                            newData.put("Email",auth.getCurrentUser().getEmail());
                            Firebasefirestore.collection("Transporters Complaint Cell").document(auth.getCurrentUser().getEmail())
                                    .set(newData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(camera.this, Transporter_home.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("key", "value");
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(v, "Data Entry Failed.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    findViewById(R.id.signin_layout).setVisibility(View.VISIBLE);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }
        });
        d.show();
    }

}
