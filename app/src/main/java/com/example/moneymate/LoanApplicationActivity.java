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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.GenericResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String borrowerIdStr = preferences.getString("borrower_id", null);

        if (borrowerIdStr != null) {
            try {
                borrowerId = Integer.parseInt(borrowerIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid borrower ID format", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            Toast.makeText(this, "Borrower ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (lenderId == -1 || borrowerId == -1) {
            Toast.makeText(this, "Missing lender or borrower ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        this.borrowerId = borrowerId;

        idFrontEditText = findViewById(R.id.idFrontEditText);
        idBackEditText = findViewById(R.id.idBackEditText);

        idFrontEditText.setOnClickListener(v -> openFileChooser(REQUEST_ID_FRONT));
        idBackEditText.setOnClickListener(v -> openFileChooser(REQUEST_ID_BACK));

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submitApplication());
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
            Uri selectedUri = data.getData();
            String fileName = getFileNameFromUri(selectedUri);

            if (requestCode == REQUEST_ID_FRONT) {
                idFrontUri = selectedUri;
                idFrontEditText.setText(fileName);
            } else if (requestCode == REQUEST_ID_BACK) {
                idBackUri = selectedUri;
                idBackEditText.setText(fileName);
            }
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex >= 0) {
                        result = cursor.getString(columnIndex);
                    }
                }
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void submitApplication() {
        String fullName = ((EditText) findViewById(R.id.fullNameEditText)).getText().toString().trim();
        String phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        String amount = ((EditText) findViewById(R.id.amountEditText)).getText().toString().trim();
        String purpose = ((EditText) findViewById(R.id.purposeEditText)).getText().toString().trim();

        if (idFrontUri == null || idBackUri == null) {
            Toast.makeText(this, "Please upload both ID images", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("lender_id", lenderId);
            json.put("borrower_id", borrowerId);
            json.put("amount", amount);
            json.put("purpose", purpose);

            RequestBody loanDataBody = RequestBody.create(
                    MediaType.parse("application/json"), json.toString()
            );

            MultipartBody.Part idFrontPart = prepareFilePart("id_front", idFrontUri);
            MultipartBody.Part idBackPart = prepareFilePart("id_back", idBackUri);

            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Call<GenericResponse> call = apiService.applyForLoan(loanDataBody, idFrontPart, idBackPart);

            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        showSuccessDialog();
                    } else {
                        String errorMessage = "Submission failed. Please try again.";
                        try {
                            if (response.errorBody() != null) {
                                errorMessage = response.errorBody().string();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        showFailureDialog(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    showFailureDialog("Network error: " + t.getMessage());
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating request", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_success); // Ensure this layout is created
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button okButton = dialog.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(this, P2PFragment.class); // <-- Replace with your actual class
            startActivity(intent);
            finish();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void showFailureDialog(String message) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_failure);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button retryButton = dialog.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(v -> dialog.dismiss());

        // Set error message
        EditText messageTextView = dialog.findViewById(R.id.failureMessageTextView);
        messageTextView.setText(message);

        dialog.setCancelable(false);
        dialog.show();
    }


    private MultipartBody.Part prepareFilePart(String partName, Uri uri) {
        File file = new File(getFilePathFromUri(uri));
        RequestBody requestBody = RequestBody.create(
                MediaType.parse(getContentResolver().getType(uri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

    private String getFilePathFromUri(Uri uri) {
        String fileName = getFileNameFromUri(uri);
        File tempFile = new File(getCacheDir(), fileName);

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tempFile.getAbsolutePath();
    }
}
