package com.example.gritfitfitnessstudio;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    TextView user;
    Button editProfileBtn;
    TextView etUsername;
    TextView etBio;
    TabLayout tab_layoutAccount;
    ViewPager2 view_pager;
    FirebaseUser firebaseUser;
    DatabaseReference userRef;
    String Username;
    String Bio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_account, container, false);

       init(view);

        ViewPagerAccountAdapter adapter = new ViewPagerAccountAdapter(this);
        view_pager.setAdapter(adapter);

        new TabLayoutMediator(tab_layoutAccount, view_pager,
                (tab, position) -> {
                    if (position == 0) {
                        tab.setText("Post");
                    } else {
                        tab.setText("Progress");
                    }
                }
        ).attach();


        if (firebaseUser != null) {
            String name = firebaseUser.getDisplayName();
            user.setText(name);
                }

        fetchUsernameAndBio();

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        return view;
    }
    private void init(View view){
        user = view.findViewById(R.id.user);
        etUsername = view.findViewById(R.id.username);
        etBio =view.findViewById(R.id.bio);
        tab_layoutAccount = view.findViewById(R.id.tab_layoutAccount);
        view_pager = view.findViewById(R.id.view_pager);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users_UID");
    }

    private void fetchUsernameAndBio() {
        if (firebaseUser!=null) {
            String uid = firebaseUser.getUid();
            userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Username = snapshot.child("username").getValue(String.class);
                    Bio = snapshot.child("bio").getValue(String.class);
                    etUsername.setText(Username);
                    etBio.setText(Bio);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void editProfile(){
        Intent intent = new Intent(getContext(), CreateProfileActivity.class);
        startActivity(intent);
    }
}
