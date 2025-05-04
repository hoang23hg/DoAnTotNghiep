package com.example.test3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Man3 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;
    private DatabaseReference databaseReference;
    private EditText et_username, et_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_man3);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        ImageView ivTogglePassword = findViewById(R.id.ivTogglePassword);

        ivTogglePassword.setOnClickListener(v -> {
            if (et_password.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.closeview);
            } else {
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivTogglePassword.setImageResource(R.drawable.view);
            }
            et_password.setSelection(et_password.getText().length());
        });



        Button btn_login = findViewById(R.id.btn_login);


        btn_login.setOnClickListener(v -> {
            String email = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(Man3.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            loginUser(email, password);
        });

        TextView tv_resetpass_link = findViewById(R.id.tv_resetpass_link);
        tv_resetpass_link.setOnClickListener(v -> {
            Intent intent = new Intent(Man3.this, Man5.class);
            startActivity(intent);
                });

        TextView tv_register_link = findViewById(R.id.tv_register_link);

        tv_register_link.setOnClickListener(v -> {
            Intent intent = new Intent(Man3.this, Man4.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("979602285050-thfevjcig05p15ti8k2lvlsne9c2q59o.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth.signOut();
        googleSignInClient.signOut();

        CardView googleSignInCard = findViewById(R.id.googleSignInCard);
        googleSignInCard.setOnClickListener(v -> selectGoogleAccount());

        CardView cardLoginPhone = findViewById(R.id.cardLoginPhone);
        cardLoginPhone.setOnClickListener(v -> {
            Intent intent = new Intent(Man3.this, LoginPhoneActivity.class);
            startActivity(intent);
        });

        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                }
        );

    }

    private void selectGoogleAccount() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account);
            }
        } catch (ApiException e) {
            Log.e("GoogleSignIn", "Lỗi đăng nhập", e);
            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToDatabase(user);

                            // ✅ Lưu uid vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uid", user.getUid());
                            editor.apply();

                            Toast.makeText(this, "Đăng nhập thành công: " + user.getEmail(), Toast.LENGTH_LONG).show();
                            goToMainActivity();
                        }
                    } else {
                        Toast.makeText(this, "Xác thực Firebase thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void saveUserToDatabase(FirebaseUser user) {
        if (user == null) {
            Log.e("FirebaseDB", "User is null, cannot save to database");
            return;
        }

        String userId = user.getUid();
        Loginlinkemail.User userData = new Loginlinkemail.User(user.getDisplayName(), user.getEmail(), userId);
        databaseReference.child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseDB", "User data saved successfully"))
                .addOnFailureListener(e -> Log.e("FirebaseDB", "Failed to save user data", e));
    }


    private void goToMainActivity() {
        Intent intent = new Intent(Man3.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // ✅ Lưu uid vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uid", user.getUid());
                            editor.apply();

                            Toast.makeText(Man3.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Man3.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Man3.this, "Email chưa được xác minh. Kiểm tra hộp thư!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Man3.this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static class User {
        public String name;
        public String email;
        public String uid;

        public User() {
        }

        public User(String name, String email, String uid) {
            this.name = name;
            this.email = email;
            this.uid = uid;
        }
    }


}