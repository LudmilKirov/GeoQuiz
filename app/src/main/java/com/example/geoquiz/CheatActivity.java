package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE=
            "com.example.qeoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN=
            "com.example.qeoquiz.answer_shown";
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
            public void onClick(View view) {
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }
                else{
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);
            }
        });

    }
    //Set the answer is true when
    // the user presses the show answer button
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data=new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }
    //gg
    String gg="";
}
