package ex5.it.mcm.fhooe.classroomplusplus.model;

import com.firebase.client.ServerValue;

import java.util.HashMap;

import ex5.it.mcm.fhooe.classroomplusplus.utils.Constants;

/**
 * Created by Tob0t on 05.05.2016.
 */
public class Vote {
    private boolean leftAnswer;
    private boolean rightAnswer;
    private HashMap<String, Object> timestampLastChanged;

    public Vote() {
        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();
        timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;
    }

    public Vote(boolean leftAnswer, boolean rightAnswer) {
        this.leftAnswer = leftAnswer;
        this.rightAnswer = rightAnswer;
        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();
        timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;
    }

    public boolean isLeftAnswer() {
        return leftAnswer;
    }

    public boolean isRightAnswer() {
        return rightAnswer;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public void setLeftAnswer(boolean leftAnswer) {
        this.leftAnswer = leftAnswer;
    }

    public void setRightAnswer(boolean rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
