package com.example.gritfitfitnessstudio;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class CommunityFragment extends Fragment {

    RecyclerView recyclerFeed;
    RecyclerCommunityAdapter recyclerCommunityAdapter;
    FloatingActionButton addBtn;
    ArrayList<UserModel> arrFeed = new ArrayList<>();
    DatabaseReference feedRef;
    DatabaseReference userOwnFeedRef;
    DatabaseReference userRef;
    FirebaseUser firebaseUser;

    StorageReference storageReference;
    DatabaseReference imgRef;
    String Username;
    String postId;
    private static final int PICK_IMAGE_REQUEST = 1;
    String imageUrl;
    String enteredCaption;
    Uri imageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        // initialized Ui components and firebase references
        init(view);

        // initialized recycler view
        initRecyclerView(view);

        // fetch username of the current user
        fetchUsername();

        // fetch feed data from Firebase
        fetchFeedData();

        // set click listener for add button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog to add caption
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.add_feed_dialog);

                // Find views in the dialog layout
                EditText edtCaption = dialog.findViewById(R.id.edtCaption);
                Button btnPost = dialog.findViewById(R.id.btnPost);
                ImageView inputImage = dialog.findViewById(R.id.inputImage);

                inputImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                    }
                });

                // set click listener for post button in the dialog
                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtCaption.getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Enter Something", Toast.LENGTH_SHORT).show();
                        } else {
                            enteredCaption = edtCaption.getText().toString();
                            // Upload the image to Firebase Storage
                            uploadImage(imageUri);



                            // dismiss the dialog after saving the post
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        return view;
    }

    private void init(View view) {
        addBtn = view.findViewById(R.id.addBtn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userOwnFeedRef = FirebaseDatabase.getInstance().getReference("User_Own_Post");
        feedRef = FirebaseDatabase.getInstance().getReference("feed_post");
        // enable offline persistence for feedRef
        feedRef.keepSynced(true);
        userRef = FirebaseDatabase.getInstance().getReference("Users_UID");
        userRef.keepSynced(true);
        storageReference = FirebaseStorage.getInstance().getReference().child("images");
        imgRef = FirebaseDatabase.getInstance().getReference("image_urls");
    }

    private void initRecyclerView(View view) {
        recyclerFeed = view.findViewById(R.id.recyclerFeed);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCommunityAdapter = new RecyclerCommunityAdapter(arrFeed);
        recyclerFeed.setAdapter(recyclerCommunityAdapter);
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the image URI from the intent
            imageUri = data.getData();
        }
    }
    private void uploadImage(Uri imageUri) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // If successful, get the download URL of the image
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrl.addOnSuccessListener(uri -> {
                        imageUrl = uri.toString();
                        Toast.makeText(getContext(),"Uploadedddddddddd", Toast.LENGTH_SHORT).show();

//                        save post to firebase(realtime)
                        savePostToFirebase(enteredCaption, imageUrl);
                        saveUserOwnPost(enteredCaption, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to get the file extension of an image URI
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Method to save the image URL to Firebase Database
    private void saveImageToDatabase(String imageUrl) {
        // Save the image URL to Realtime Database under a unique key
        String imageKey = imgRef.push().getKey();
        imgRef.child(imageKey).setValue(imageUrl)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // If successful, show a success message
                        Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // If unsuccessful, show an error message
                        Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
        }




    // fetch username of the current user from firebase
    private void fetchUsername() {
        String uid = firebaseUser.getUid();
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Username = snapshot.child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserOwnPost(String enteredCaption, String imageUrl) {
        // create a new object for a new post
        UserModel newPost = new UserModel(imageUrl);

        // Save the new post to Firebase
//        userOwnFeedRef.child(Username).setValue(newPost)

        DatabaseReference newPostRef = userOwnFeedRef.child(Username).push();
        newPostRef.setValue(imageUrl)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to add post", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void savePostToFirebase(String enteredCaption, String imageUrl) {
        DatabaseReference newPostRef = feedRef.push();

        postId = newPostRef.getKey();
        // create a new object for a new post
        UserModel newPost = new UserModel(firebaseUser.getDisplayName(), Username, enteredCaption, imageUrl, R.drawable.profile);

        // Save the new post to Firebase
        newPostRef.setValue(newPost)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to add post", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // fetch feed data from firebase
    private void fetchFeedData() {
        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserModel> newPosts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel communityModel = snapshot.getValue(UserModel.class);
                    if (communityModel != null) {
                        newPosts.add(communityModel);
                    }
                }
                // Reverse the order of newPosts to show the latest posts at the top
                Collections.reverse(newPosts);
                arrFeed.clear();
                arrFeed.addAll(newPosts);
                recyclerCommunityAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load feed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
