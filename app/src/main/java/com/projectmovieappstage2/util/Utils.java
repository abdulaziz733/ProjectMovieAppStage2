package com.projectmovieappstage2.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.gson.Gson;
import com.projectmovieappstage2.api.ApiClient;
import com.projectmovieappstage2.api.ApiInterface;

/**
 * Created by abdul on 6/13/2017.
 */

public class Utils {

    private static Gson gson;
    private static SharedPreferencesUtils sharedPreferencesUserData;


    public static ApiInterface getAPIService() {
        return ApiClient.getClient(Constant.BASE_URL).create(ApiInterface.class);
    }

    public static void showToast(Context ctx, String message, int duration){
        Toast.makeText(ctx, message, duration).show();
    }

    public static void startThisActivity(Activity ctx, Class classTujuan){
        Intent intent = new Intent(ctx, classTujuan);
        ctx.startActivity(intent);
    }

    public static void startThisActivityWithParams(Activity ctx, Class classTujuan, String data, String key){
        Intent intent = new Intent(ctx, classTujuan);
        intent.putExtra(key, data);
        ctx.startActivity(intent);
    }

    public static void setFrameLayout(FragmentManager fragmentManager, int resId, Fragment targetFragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resId, targetFragment);
        fragmentTransaction.commit();
    }




}
