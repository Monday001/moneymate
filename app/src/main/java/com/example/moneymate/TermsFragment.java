package com.example.moneymate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.moneymate.models.LenderTermsRequest;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsFragment extends Fragment {

    private EditText term1, term2, term3, term4, term5;
    private Button btnUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_terms, container, false);

        // Initialize EditTexts
        term1 = view.findViewById(R.id.term_1);
        term2 = view.findViewById(R.id.term_2);
        term3 = view.findViewById(R.id.term_3);
        term4 = view.findViewById(R.id.term_4);
        term5 = view.findViewById(R.id.term_5);

        // Initialize Update Button
        btnUpdate = view.findViewById(R.id.btn_update);

        // Fetch the stored terms and populate the EditText fields
        loadTerms();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t1 = term1.getText().toString().trim();
                String t2 = term2.getText().toString().trim();
                String t3 = term3.getText().toString().trim();
                String t4 = term4.getText().toString().trim();
                String t5 = term5.getText().toString().trim();

                ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

                int lenderId = getLoggedInLenderId();
                if (lenderId == -1) {
                    Toast.makeText(getContext(), "Lender ID not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                LenderTermsRequest request = new LenderTermsRequest(
                        lenderId, t1, t2, t3, t4, t5
                );

                Call<ResponseBody> call = apiService.submitTerms(request);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Terms saved", Toast.LENGTH_SHORT).show();
                            btnUpdate.setVisibility(View.GONE);
                            disableInputs();
                            saveTermsLocally(t1, t2, t3, t4, t5);
                        } else {
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                                Toast.makeText(getContext(), "Failed: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private void disableInputs() {
        term1.setEnabled(false);
        term2.setEnabled(false);
        term3.setEnabled(false);
        term4.setEnabled(false);
        term5.setEnabled(false);
    }

    private int getLoggedInLenderId() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("moneymate_prefs", Context.MODE_PRIVATE);
        return prefs.getInt("lender_id", -1); // -1 as default/fallback
    }

    private void loadTerms() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("moneymate_prefs", Context.MODE_PRIVATE);

        // Load saved terms from SharedPreferences and set them to the EditText fields
        String t1 = prefs.getString("term_1", "");
        String t2 = prefs.getString("term_2", "");
        String t3 = prefs.getString("term_3", "");
        String t4 = prefs.getString("term_4", "");
        String t5 = prefs.getString("term_5", "");

        term1.setText(t1);
        term2.setText(t2);
        term3.setText(t3);
        term4.setText(t4);
        term5.setText(t5);
    }

    private void saveTermsLocally(String t1, String t2, String t3, String t4, String t5) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("moneymate_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save the updated terms in SharedPreferences
        editor.putString("term_1", t1);
        editor.putString("term_2", t2);
        editor.putString("term_3", t3);
        editor.putString("term_4", t4);
        editor.putString("term_5", t5);
        editor.apply();
    }
}
