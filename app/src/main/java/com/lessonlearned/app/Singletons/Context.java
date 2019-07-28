package com.lessonlearned.app.Singletons;

import com.lessonlearned.app.Models.User;

public class Context {
    private User user;

    private static final Context ourInstance = new Context();

    public static Context getInstance() {
        return ourInstance;
    }

    private Context() {
    }

    public static void setUser(User user){
        ourInstance.user = user;
    }

    public static User getUser(){
        return ourInstance.user;
    }

}
