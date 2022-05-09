package com.ocr.firebaseoc.manager;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.ocr.firebaseoc.repositories.UserRepository;

public class UserManager {
    private static volatile UserManager instance;
    private  UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public boolean isCurrentUserLogged(){
        return (UserRepository.getInstance().getCurrentUser() != null);
    }

    public FirebaseUser getCurrentUser(){
        return UserRepository.getInstance().getCurrentUser();
    }

    public Task<Void> signOut(Context context){
        return userRepository.signOut(context);
    }

    public Task deleteUser(Context context){
        return userRepository.deleteUser(context);
    }
}
