package com.silencedaemon.seta.MainMenu.presenter;

import android.content.Context;
import android.content.Intent;

import com.silencedaemon.seta.MainMenu.MainMenuContract;

public class MainMenuPresenter implements MainMenuContract.presenter {

    private MainMenuContract.view vista;
    private Context context;
    private Intent NextActivity;


    public MainMenuPresenter(MainMenuContract.view vista, Context context) {
        this.vista = vista;
        this.context = context;
    }

    @Override
    public void goToActivity(Class activityItem) {

        NextActivity = new Intent(context, activityItem);
        context.startActivity(NextActivity);
    }

}
