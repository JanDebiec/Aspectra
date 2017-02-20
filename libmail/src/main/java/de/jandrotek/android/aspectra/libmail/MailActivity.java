package de.jandrotek.android.aspectra.libmail;

import android.content.Intent;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import android.view.Menu;
import android.view.MenuItem;

import de.cketti.mailto.EmailIntentBuilder;

public class MailActivity extends AppCompatActivity {

    View mainContent;

    EditText emailTo;

    EditText emailCc;

    EditText emailBcc;

    EditText emailSubject;

    EditText emailBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        emailTo.setText(R.string.content_to);
        emailSubject.setText(R.string.content_subject);
    }

    private void sendFeedback() {
        boolean success = EmailIntentBuilder.from(this)
                .to("cketti@gmail.com")
                .subject(getString(R.string.feedback_subject))
                .body(getString(R.string.feedback_body))
                .start();

        if (!success) {
            Snackbar.make(mainContent, R.string.error_no_email_app, Snackbar.LENGTH_LONG).show();
        }
    }


    private void sendEmail() {
        String to = emailTo.getText().toString();
        String cc = emailCc.getText().toString();
        String bcc = emailBcc.getText().toString();
        String subject = emailSubject.getText().toString();
        String body = emailBody.getText().toString();

        EmailIntentBuilder builder = EmailIntentBuilder.from(this);

        try {
            if (!TextUtils.isEmpty(to)) {
                builder.to(to);
            }
            if (!TextUtils.isEmpty(cc)) {
                builder.cc(cc);
            }
            if (!TextUtils.isEmpty(bcc)) {
                builder.bcc(bcc);
            }
            if (!TextUtils.isEmpty(subject)) {
                builder.subject(subject);
            }
            if (!TextUtils.isEmpty(body)) {
                builder.body(body);
            }

            boolean success = builder.start();
            if (!success) {
                Snackbar.make(mainContent, R.string.error_no_email_app, Snackbar.LENGTH_LONG).show();
            }
        } catch (IllegalArgumentException e) {
            String errorMessage = getString(R.string.argument_error, e.getMessage());
            Snackbar.make(mainContent, errorMessage, Snackbar.LENGTH_LONG).show();
        }
    }
}