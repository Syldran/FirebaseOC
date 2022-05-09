package com.ocr.firebaseoc.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Context;
import com.ocr.firebaseoc.R;
import com.ocr.firebaseoc.databinding.ActivityProfileBinding;
import com.ocr.firebaseoc.manager.UserManager;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding> {

    private UserManager userManager = UserManager.getInstance();

    @Override
    ActivityProfileBinding getViewBinding() {
        return ActivityProfileBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListeners();
        updateUIWithUserData();
    }

    private void updateUIWithUserData() {
        if (userManager.isCurrentUserLogged()){
            FirebaseUser user = userManager.getCurrentUser();
            if (user.getPhotoUrl() != null){
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
        }
    }

    private void setTextUserData(FirebaseUser user) {
        //get mail & username from user.
        String mail = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        binding.emailTextView.setText(mail);
        binding.usernameEditText.setText(username);
    }

    private void setProfilePicture(Uri photoUrl) {
        Glide.with(this).load(photoUrl).apply(RequestOptions.circleCropTransform()).into(binding.profileImageView);
    }


    private void setupListeners(){
        binding.updateButton.setOnClickListener(view -> {

        });
        binding.signOutButton.setOnClickListener(view -> {
            userManager.signOut(this).addOnSuccessListener(aVoid -> {
                finish();
            });
        });
        binding.deleteButton.setOnClickListener(view -> {

            userManager.deleteUser(this).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Delete Successful !",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Delete Failed !",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            /*
            new AlertDialog.Builder(this)
                    .setMessage(R.string.popup_message_confirmation_delete_account)
                    .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                    userManager.deleteUser(ProfileActivity.this)
                            .addOnSuccessListener(aVoid -> {
                                finish();
                            })
                            )
                    .setNegativeButton(R.string.popup_message_choice_no, null)
                    .show();
*/
        });
    }
}
