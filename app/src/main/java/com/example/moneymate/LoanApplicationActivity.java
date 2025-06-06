package com.example.moneymate;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.GenericResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import org.json.JSONObject;

import java.io.*;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanApplicationActivity extends AppCompatActivity {

    private static final int REQUEST_ID_FRONT = 1;
    private static final int REQUEST_ID_BACK = 2;

    private EditText idFrontEditText, idBackEditText;
    private Uri idFrontUri, idBackUri;
    private int lenderId, borrowerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);

        lenderId = getIntent().getIntExtra("lender_id", -1);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String borrowerIdStr = prefs.getString("borrower_id", null);

        if (borrowerIdStr == null || lenderId == -1) {
            Toast.makeText(this, "Missing IDs", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            borrowerId = Integer.parseInt(borrowerIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid borrower ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        idFrontEditText = findViewById(R.id.idFrontEditText);
        idBackEditText = findViewById(R.id.idBackEditText);
        idFrontEditText.setOnClickListener(v -> openFileChooser(REQUEST_ID_FRONT));
        idBackEditText.setOnClickListener(v -> openFileChooser(REQUEST_ID_BACK));

        findViewById(R.id.submitButton).setOnClickListener(v -> submitApplication());
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select ID Image"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String fileName = getFileNameFromUri(uri);

            if (requestCode == REQUEST_ID_FRONT) {
                idFrontUri = uri;
                idFrontEditText.setText(fileName);
            } else if (requestCode == REQUEST_ID_BACK) {
                idBackUri = uri;
                idBackEditText.setText(fileName);
            }
        }
    }

    private String getFileNameFromUri(Uri uri) {
        if (uri == null) return null;
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) result = cursor.getString(index);
                }
            }
        }

        if (result == null && uri.getPath() != null) {
            int cut = uri.getPath().lastIndexOf('/');
            if (cut != -1) result = uri.getPath().substring(cut + 1);
        }

        return result;
    }

    private void submitApplication() {
        String fullName = ((EditText) findViewById(R.id.fullNameEditText)).getText().toString().trim();
        String phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String amount = ((EditText) findViewById(R.id.amountEditText)).getText().toString().trim();
        String purpose = ((EditText) findViewById(R.id.purposeEditText)).getText().toString().trim();
        TextView phoneErrorTextView = findViewById(R.id.phoneErrorTextView);
        phoneErrorTextView.setVisibility(View.GONE);

        if (idFrontUri == null || idBackUri == null) {
            Toast.makeText(this, "Upload both ID images", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.isEmpty()) {
            phoneErrorTextView.setText("Phone is required");
            phoneErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("lender_id", lenderId);
            json.put("borrower_id", borrowerId);
            json.put("amount", amount);
            json.put("purpose", purpose);
            json.put("full_name", fullName);
            json.put("email_address", email);
            json.put("phone_number", phone);

            RequestBody jsonBody = RequestBody.create(json.toString(), MediaType.parse("application/json"));

            MultipartBody.Part frontPart = prepareFilePart("id_front", idFrontUri);
            MultipartBody.Part backPart = prepareFilePart("id_back", idBackUri);

            ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
            service.applyForLoan(jsonBody, frontPart, backPart).enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        showSuccessDialog();
                    } else {
                        String message = "Submission failed";
                        try {
                            if (response.errorBody() != null) {
                                message = response.errorBody().string();
                                if (message.contains("user does not exist")) {
                                    phoneErrorTextView.setText("User not found");
                                    phoneErrorTextView.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        showFailureDialog(message);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    showFailureDialog("Network error: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Request creation error", Toast.LENGTH_SHORT).show();
        }
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri uri) {
        if (uri == null) return null;
        File file = new File(getFilePathFromUri(uri));
        if (!file.exists()) {
            Log.e("FileUpload", "File not found: " + file.getAbsolutePath());
            return null;
        }

        RequestBody requestFile = RequestBody.create(file, MediaType.parse(getContentResolver().getType(uri)));
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private String getFilePathFromUri(Uri uri) {
        File tempFile = new File(getCacheDir(), getFileNameFromUri(uri));
        try (InputStream in = getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        dialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(this, BorrowerHomeActivity.class).putExtra("navigate_to", "p2p"));
            finish();
        });

        dialog.show();
    }

    private void showFailureDialog(String message) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_failure);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        ((TextView) dialog.findViewById(R.id.failureMessageTextView)).setText(message);
        dialog.findViewById(R.id.retryButton).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
