package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActiviy";
    private static final String KEY_INDEX = "index";
    private static final String KEY_SCORE = "score";
    private static final String KEY_CHEATER = "cheater";
    private static final String KEY_TOKENS = "tokens";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private TextView mRemainingTokensTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_america, true),
            new Question(R.string.question_asia, true),
    };

    private boolean[] mIsCheater = new boolean[mQuestionBank.length];
    private int mCurrentIndex = 0;
    private int mCurrentScore = 0;
    //You can use max 3 cheat tokens
    private int mRemainingCheatTokens = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //Save the index of the question
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            //Save the right answered questions
            mCurrentScore = savedInstanceState.getInt(KEY_SCORE, 0);
            //Save how many cheat tokens left
            mRemainingCheatTokens = savedInstanceState.getInt(KEY_TOKENS, 3);
            //Save if on the question is used a cheat
            mIsCheater = savedInstanceState.getBooleanArray(KEY_CHEATER);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                MainActivity.this.nextQuestion();
            }
        });
        //Label the cheat button with a text and remaining tokens
        mRemainingTokensTextView = findViewById(R.id.cheat_button);
        mRemainingTokensTextView.setText
                ("Remaining Cheat Tokens: " + mRemainingCheatTokens);

        //True button
        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                MainActivity.this.checkAnswer(true);
            }
        });

        //False button
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                MainActivity.this.checkAnswer(false);
            }
        });

        //Next button
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                MainActivity.this.nextQuestion();
            }
        });

        //Previous button
        mPreviousButton = findViewById(R.id.prev_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                MainActivity.this.previousQuestion();
            }
        });

        //Cheat button
        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                MainActivity.this.startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater[mCurrentIndex] = CheatActivity.wasAnswerShown(data);
            //Limited to 3 cheats
            mRemainingCheatTokens--;
            //Update the label
            mRemainingTokensTextView.setText
                    ("Remaining Cheat Tokens: " + mRemainingCheatTokens);
            if (mRemainingCheatTokens <= 0) {
                //If it goes to 0 unable the cheat button
                mCheatButton.setEnabled(false);
            }
        }
    }


    //On save instance,that remember the current question,
    // the answered question,remaining cheat tokens
    // and if the cheat is used on the question
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState() called");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_SCORE, mCurrentScore);
        savedInstanceState.putInt(KEY_TOKENS, mRemainingCheatTokens);
        savedInstanceState.putBooleanArray(KEY_CHEATER, mIsCheater);
    }


    //For previous question
    private void previousQuestion() {
        if (mCurrentIndex != 0) {
            mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
        } else {
            mCurrentIndex = mQuestionBank.length - 1;
        }

        updateQuestion();
    }
    //For next question and to restart the score
    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

        updateQuestion();

        //Restart the score when start over
        if (mCurrentIndex == 0) {
            mCurrentScore = 0;
        }
    }
    //Update question method
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        toggleAnswerButtonsTo(true);
    }

    private void checkAnswer(boolean userPressedTrue) {

        toggleAnswerButtonsTo(false);
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;

        if (mIsCheater[mCurrentIndex]) {
            //Pop the cheating toast
            messageResId = R.string.judgement_toast;
            //Cheaters doesn't score
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                //Pop the correct toast
                messageResId = R.string.correct_toast;
                //Update the right answer that are not cheated
                mCurrentScore += 1;
                Log.d(TAG, mCurrentScore + "");
            }
            else {
                //Pop the incorrect toast
                messageResId = R.string.incorrect_toast;
            }
        }
        //Toast for correctness
        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.show();
        //IF reach the final question
        if (mCurrentIndex == mQuestionBank.length - 1) {
            showScore();
        }
    }

    private void showScore() {
        //Calculate the percentage
        int percentage = (int) (((double)mCurrentScore/mQuestionBank.length)*100);
        //String for the toast
        String stringScore = "You got " + percentage + "% correct answers";
        //Pop the toast
        Toast.makeText(this, stringScore, Toast.LENGTH_SHORT).show();
    }

    //To stop the ability of the user to answer
    private void toggleAnswerButtonsTo(boolean b) {
        if (!b) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
        else  {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

}
