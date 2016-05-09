package ex5.it.mcm.fhooe.classroomplusplus;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Tob0t on 04.05.2016.
 */
public class ClassroomPlusPlusApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /* Initialize Firebase */
        Firebase.setAndroidContext(this);
        /* Enable disk persistence  */
        //Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
