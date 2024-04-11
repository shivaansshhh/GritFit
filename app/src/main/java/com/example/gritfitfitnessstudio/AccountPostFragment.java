package com.example.gritfitfitnessstudio;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

import io.grpc.HandlerRegistry;

public class AccountPostFragment extends Fragment {
    private GridView gridView;
    private ImageAdapterUserProfile mAdapter;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private DatabaseReference userRef;
    private String UserName;
   private ArrayList<String> mImageUrls = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_post, container, false);

        gridView = rootView.findViewById(R.id.gridView);
        progressBar = rootView.findViewById(R.id.progressBar);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users_UID");

        fetchUsername();
        progressBar.setVisibility(View.VISIBLE);

        
        return rootView;
}
private void fetchImageUrlsFromFirebase(){
    databaseReference = FirebaseDatabase.getInstance().getReference("User_Own_Post").child(UserName);
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mImageUrls.clear();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                String imageURL = snapshot.getValue(String.class);
                if (imageURL != null){
                mImageUrls.add(imageURL);
                }
            }
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "Fetched", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getContext(), "Error to fetch", Toast.LENGTH_SHORT).show();

        }
    });

//
//
//    databaseReference.addChildEventListener(new ChildEventListener() {
//        @Override
//        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            String post = snapshot.getValue(String.class);
//            mImageUrls.add(post);
//            mAdapter.notifyDataSetChanged();
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//
//        @Override
//        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//        }
//
//        @Override
//        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//        }
//
//        @Override
//        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });
}
    private void fetchUsername() {
        String uid = currentUser.getUid();
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserName = snapshot.child("username").getValue(String.class);
                Toast.makeText(getContext(), "welcome "+UserName, Toast.LENGTH_SHORT).show();
                mAdapter = new ImageAdapterUserProfile(getContext(), mImageUrls);
                gridView.setAdapter(mAdapter);
                fetchImageUrlsFromFirebase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}