package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE=
            "com.example.qeoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN=
            "com.example.qeoquiz.answer_shown";
    private static final String TAG = "CheatActivity";
    private static final String KEY_IS_CHEATER = "cheater";

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;
    private TextView mAnswerTextView;
    private Button mShowAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        //If it is not null remember the state if the answer is shown
        if (savedInstanceState!=null){
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
        }

        mAnswerIsTrue=getIntent()
                .getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        mAnswerTextView=(TextView)findViewById(R.id.answer_text_view);
        mShowAnswer = findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                CheatActivity.this.displayAnswer();
            }
        });

        if (mIsAnswerShown) {
            displayAnswer();
        }
    }

    //To save the state if the answer is shown and if the answer is true
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        Log.i(TAG, "in onSavedInstanceState");
        savedInstanceState.putBoolean(KEY_IS_CHEATER, mIsAnswerShown);
        super.onSaveInstanceState(savedInstanceState);
    }

    //To save the state of the button
    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
    super.onRestoreInstanceState(saveInstanceState);
    CheatActivity.this.displayAnswer();
    }

    //Show the answer
    public void displayAnswer(){
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }

        mIsAnswerShown = true;
        setAnswerShownResult(mIsAnswerShown);
    }


    public static Intent newIntent(Context packageContent,boolean answerIsTrue){
        Intent i=new Intent(packageContent,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }

    //Set default value for the cheat button
    public  static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    //Set the answer is true when
    // the user presses the show answer button
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data=new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }
}