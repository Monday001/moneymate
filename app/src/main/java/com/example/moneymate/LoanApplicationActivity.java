package com.example.moneymate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneymate.models.GenericResponse;
import com.example.moneymate.network.ApiService;
import com.example.moneymate.network.ApiClient;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_application);

        idFrontEditText = findViewById(R.id.idFrontEditText);
        idBackEditText = findViewById(R.id.idBackEditText);

        idFrontEditText.setOnClickListener(v -> openFileChooser(REQUEST_ID_FRONT));
        idBackEditText.setOnClickListener(v -> openFileChooser(REQUEST_ID_BACK));

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> submitApplication());
    }

    private void openFileChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // or "application/pdf" if accepting PDFs too
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
        if (uri.getScheme().equals("content")) {
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
            // Build loan data JSON
            JSONObject json = new JSONObject();
            json.put("borrower_id", 5); // Replace with actual borrower ID
            json.put("amount", amount);
            json.put("purpose", purpose);

            RequestBody loanDataBody = RequestBody.create(
                    MediaType.parse("application/json"), json.toString()
            );

            // Prepare file parts
            MultipartBody.Part idFrontPart = prepareFilePart("id_front", idFrontUri);
            MultipartBody.Part idBackPart = prepareFilePart("id_back", idBackUri);

            // Call API
            ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
            Call<GenericResponse> call = apiService.applyForLoan(loanDataBody, idFrontPart, idBackPart);

            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(LoanApplicationActivity.this,
                                response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoanApplicationActivity.this,
                                "Submission failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    Toast.makeText(LoanApplicationActivity.this,
                            "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating request", Toast.LENGTH_SHORT).show();
        }
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri uri) {
        File file = new File(getFilePathFromUri(uri));
        RequestBody requestBody = RequestBody.create(
                MediaType.parse(getContentResolver().getType(uri)),
                file
        );
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


