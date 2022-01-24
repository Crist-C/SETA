package com.silencedaemon.seta.MainMenu;


public interface MainMenuContract {

    interface view{
    }

    interface presenter{
        public void goToActivity(Class activityItem);

    }

}
