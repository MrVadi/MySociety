package ua.mrvadi.mysociety.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ua.mrvadi.mysociety.R;
import ua.mrvadi.mysociety.activities.base.BaseActivity;
import ua.mrvadi.mysociety.constants.Consts;
import ua.mrvadi.mysociety.helpers.DBHelper;
import ua.mrvadi.mysociety.helpers.MyValidator;

/**
 * Created by mrvadi on 03.10.16.
 */
public class SplashActivity extends BaseActivity {

    private ImageView splashLogo;
    private TextView splashGreeting;
    private TextInputLayout editTextLogin;
    private TextInputLayout editTextPassword;
    private Button loginButton;
    private TextView error;
    private ImageView errorIcon;

    private boolean isErrorVisible;
    private DBHelper dbHelper = DBHelper.getInstance();
    int logined = dbHelper.getAdminInfo().isLogined();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initSplashScreenViews();

        if (savedInstanceState == null) {
            prepareError();
            if(logined == 1) {
                animationLogined();
            } else {
                performAnimation();
            }
        } else {
            if (logined == 1) {
                switchActivity();
            } else {
                makeAllViewsVisible();
                handleViewsFunctionality();
                if (savedInstanceState.getBoolean(Consts.STATE_ERROR))
                    makeErrorVisible();
            }
        }
    }

    private void initSplashScreenViews() {

        splashLogo = (ImageView) findViewById(R.id.splash_logo);
        splashGreeting = (TextView) findViewById(R.id.splash_greeting);
        splashGreeting.setText((logined == 1) ? R.string.welcome_back : R.string.please_login);
        editTextLogin = (TextInputLayout) findViewById(R.id.edit_text_login_wrapper);
        editTextPassword = (TextInputLayout) findViewById(R.id.edit_text_password_wrapper);
        loginButton = (Button) findViewById(R.id.splash_login_btn);
        error = (TextView) findViewById(R.id.splash_error_message);
        errorIcon = (ImageView) findViewById(R.id.splash_error_icon);
    }

    private void handleViewsFunctionality() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    private void prepareViewsForAnimation() {

        float distance = getResources().getDimension(R.dimen.animation_distance);

        splashLogo.animate().translationY(-distance).setDuration(1);
        splashGreeting.animate().alpha(0.0f).setDuration(1);
        editTextLogin.animate().translationX(-distance).setDuration(1);
        editTextPassword.animate().translationX(distance).setDuration(1);
        loginButton.animate().alpha(0.0f).setDuration(1)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        makeAllViewsVisible();
                    }
                });
    }

    private void makeAllViewsVisible() {
        splashLogo.setVisibility(View.VISIBLE);
        splashGreeting.setVisibility(View.VISIBLE);
        editTextLogin.setVisibility(View.VISIBLE);
        editTextPassword.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
    }

    private void performAnimation() {
        prepareViewsForAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashLogo.animate().translationY(0)
                        .setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        editTextLogin.animate().translationX(0).setDuration(500);
                        editTextPassword.animate().translationX(0).setDuration(500)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        splashGreeting.animate().alpha(1.0f).setDuration(750);
                                        loginButton.animate().alpha(1.0f).setDuration(750)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        handleViewsFunctionality();
                                                    }
                                                });
                                    }
                                });
                    }
                });
            }
        }, Consts.START_ANIMATION_DELAY);
    }

    private void animationLogined() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.normal_splash_logo_size),
                (int) getResources().getDimension(R.dimen.normal_splash_logo_size));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        splashLogo.setLayoutParams(params);

        prepareViewsForAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashGreeting.animate().alpha(1.0f).setDuration(1000);
                splashLogo.animate().translationY(0)
                        .setDuration(1000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        switchActivity();
                    }

                });
            }
        }, Consts.START_ANIMATION_DELAY);

    }
    private void prepareError() {
        error.animate().alpha(0.0f).setDuration(1);
        errorIcon.animate().alpha(0.0f).setDuration(1).withEndAction(new Runnable() {
            @Override
            public void run() {
                makeErrorVisible();
            }
        });
    }

    private void makeErrorVisible() {
        error.setVisibility(View.VISIBLE);
        errorIcon.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        isErrorVisible = false;

        error.setVisibility(View.GONE);
        errorIcon.setVisibility(View.GONE);

        error.animate().alpha(0.0f).setDuration(1);
        errorIcon.animate().alpha(0.0f).setDuration(1);
    }

    private void performErrorAnimation() {
        isErrorVisible = true;
        errorIcon.animate().alpha(1.0f).setDuration(500);
        error.animate().alpha(1.0f)
                .setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                editTextLogin.startAnimation(anim);
                editTextPassword.startAnimation(anim);
                loginButton.startAnimation(anim);
                error.startAnimation(anim);
                errorIcon.startAnimation(anim);
            }
        });
    }

    private void performLogin() {
        if ( editTextLogin.getEditText() != null && editTextPassword.getEditText() != null &&
                MyValidator.isCorrect(editTextLogin.getEditText().getText().toString()
                        , editTextPassword.getEditText().getText().toString())) {
            hideError();
            dbHelper.setLogined(true);
            switchActivity();
        } else {
            performErrorAnimation();
        }

    }

    private void switchActivity() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        this.overridePendingTransition(R.anim.animation_bottom_in, R.anim.animation_top_out);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(editTextLogin.getEditText() != null
                && !editTextLogin.getEditText().getText().toString().equals(""))
            outState.putString(Consts.STATE_LOGIN, editTextLogin.getEditText().getText().toString());

        if(editTextPassword.getEditText() != null
                && !editTextPassword.getEditText().getText().toString().equals(""))
            outState.putString(Consts.STATE_PASSWORD, editTextPassword.getEditText().getText().toString());

        outState.putBoolean(Consts.STATE_ANIMATED, true);
        outState.putBoolean(Consts.STATE_ERROR, isErrorVisible);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (editTextLogin.getEditText() != null)
            editTextLogin.getEditText().setText(savedInstanceState.getString(Consts.STATE_LOGIN));

        if (editTextPassword.getEditText() != null)
            editTextPassword.getEditText().setText(savedInstanceState.getString(Consts.STATE_PASSWORD));
    }


}
