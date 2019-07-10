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
    private static final String KEY_CHEAT_CHECKER = "cheat_checker";
    private static final String KEY_ANSWER = "answer_to_question";
    private boolean mIsAnswerShown;
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue=getIntent()
                .getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        mAnswerTextView=(TextView)findViewById(R.id.answer_text_view);
        mShowAnswer=(Button)findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAnswer();
                setAnswerShownResult(true);
                mIsAnswerShown = true;
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth() / 2;
                    int cy = mShowAnswer.getHeight() / 2;
                    float radius = mShowAnswer.getWidth();
                    final Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }

                    });
                    anim.start();
                }
                else{
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
        //If it is not null remember the state if the answer is shown
        if (savedInstanceState!=null){
            mIsAnswerShown = savedInstanceState
                    .getBoolean(KEY_CHEAT_CHECKER, false);
            mAnswerIsTrue = savedInstanceState
                    .getBoolean(KEY_ANSWER, false);
            if(mIsAnswerShown){
                setAnswerShownResult(true);
                displayAnswer();
            }
        }
    }

    //Show the answer
    public void displayAnswer(){
        if(mAnswerIsTrue){
            mAnswerTextView.setText(R.string.true_button);
        }
        else{
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    //To save the state if the answer is shown and if the answer is true
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "in onSavedInstanceState");
        savedInstanceState.putBoolean(KEY_CHEAT_CHECKER, mIsAnswerShown);
        savedInstanceState.putBoolean(KEY_ANSWER, mAnswerIsTrue);
    }


}