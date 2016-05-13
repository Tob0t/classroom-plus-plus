package ex5.it.mcm.fhooe.classroomplusplus.utils;

import com.firebase.client.Firebase;

import ex5.it.mcm.fhooe.classroomplusplus.model.Lecture;
import ex5.it.mcm.fhooe.classroomplusplus.model.Vote;

/**
 * Created by Tob0t on 04.05.2016.
 */
public class DataService {
    public static void sendAnswer(Constants.Answer answer, String nfcId, String surveyId) {
        Vote v = new Vote();
        if(answer == Constants.Answer.LEFT){
            v.setLeftAnswer(true);
            v.setRightAnswer(false);
        } else{
            v.setLeftAnswer(false);
            v.setRightAnswer(true);
        }
        nfcId = nfcId.replace('[','(');
        nfcId = nfcId.replace(']',')');
        nfcId = nfcId.replace('.','_');

        // Write to firebase
        Firebase ref = new Firebase(Constants.FIREBASE_URL_VOTES).child(surveyId);
        ref.child(nfcId).setValue(v);
    }

    public static void createLecture(Lecture lecture, String roomId){
        // Write to firebase
        Firebase ref = new Firebase(Constants.FIREBASE_URL_LECTURES).child(roomId);
        ref.push().setValue(lecture);
    }

}
