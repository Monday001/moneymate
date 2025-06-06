package com.example.moneymate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneymate.adapter.BorrowerLoanAdapter;
import com.example.moneymate.databinding.FragmentBorrowerHomeBinding;
import com.example.moneymate.models.BorrowerLoan;
import com.example.moneymate.models.BorrowerLoanResponse;
import com.example.moneymate.models.LatestLoanResponse;
import com.example.moneymate.models.Loan;
import com.example.moneymate.models.Payment;
import com.example.moneymate.models.PhoneResponse;
import com.example.moneymate.network.ApiClient;
import com.example.moneymate.network.ApiService;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowerHomeFragment extends Fragment {

    private FragmentBorrowerHomeBinding binding;
    private RecyclerView recyclerView;
    private TextView nameTextView, loanBalanceTextView, noLoansTextView;
    private View userBorrowerView;
    private String username;
    private Loan latestLoan;
    private Button repayButton;  // <-- moved to class field

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBorrowerHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView = view.findViewById(R.id.borrower_name);
        loanBalanceTextView = view.findViewById(R.id.loan_balance);
        recyclerView = view.findViewById(R.id.borrower_history_recycler_view);
        noLoansTextView = view.findViewById(R.id.no_loans_text);
        userBorrowerView = view.findViewById(R.id.userBorrower);
        repayButton = view.findViewById(R.id.repay_button);

        repayButton.setOnClickListener(v -> showRepaymentDialog());

        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        username = prefs.getString("username", "User");
        nameTextView.setText("Hi " + username);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        userBorrowerView.setOnClickListener(v -> showLogoutDialog());

        fetchLatestDisbursedLoan();
        fetchLoanHistory();
    }

    private void fetchLoanHistory() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int borrowerId = getBorrowerId(prefs);

        if (borrowerId == -1) {
            showError("Invalid borrower ID.");
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.getBorrowerLoans(borrowerId).enqueue(new Callback<BorrowerLoanResponse>() {
            @Override
            public void onResponse(Call<BorrowerLoanResponse> call, Response<BorrowerLoanResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    List<BorrowerLoan> loanList = response.body().getLoans();
                    if (loanList != null && !loanList.isEmpty()) {
                        BorrowerLoanAdapter adapter = new BorrowerLoanAdapter(loanList);
                        recyclerView.setAdapter(adapter);
                        noLoansTextView.setVisibility(View.GONE);
                    } else {
                        noLoansTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "No loans found", Toast.LENGTH_SHORT).show();
                    noLoansTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BorrowerLoanResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                noLoansTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fetchLatestDisbursedLoan() {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int borrowerId = getBorrowerId(prefs);

        if (borrowerId == -1) {
            loanBalanceTextView.setText("Kshs. 0.00");
            updateRepayButtonVisibility(0);  // Hide repay button
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.getBorrowerPhone(borrowerId).enqueue(new Callback<PhoneResponse>() {
            @Override
            public void onResponse(Call<PhoneResponse> call, Response<PhoneResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    String phoneNumber = response.body().getPhone_number();
                    getLatestDisbursedLoan(phoneNumber);
                } else {
                    loanBalanceTextView.setText("Kshs. 0.00");
                    updateRepayButtonVisibility(0);  // Hide repay button
                }
            }

            @Override
            public void onFailure(Call<PhoneResponse> call, Throwable t) {
                loanBalanceTextView.setText("Kshs. 0.00");
                updateRepayButtonVisibility(0);  // Hide repay button
            }
        });
    }

    private void getLatestDisbursedLoan(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            loanBalanceTextView.setText("Kshs. 0.00");
            updateRepayButtonVisibility(0);  // Hide repay button
            return;
        }

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        apiService.getLatestDisbursedLoan(phoneNumber).enqueue(new Callback<LatestLoanResponse>() {
            @Override
            public void onResponse(Call<LatestLoanResponse> call, Response<LatestLoanResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    latestLoan = response.body().getLoan();
                    updateLoanBalance(latestLoan);
                } else {
                    loanBalanceTextView.setText("Kshs. 0.00");
                    updateRepayButtonVisibility(0);  // Hide repay button
                }
            }

            @Override
            public void onFailure(Call<LatestLoanResponse> call, Throwable t) {
                loanBalanceTextView.setText("Kshs. 0.00");
                updateRepayButtonVisibility(0);  // Hide repay button
            }
        });
    }

    private void updateLoanBalance(Loan loan) {
        if (loan != null) {
            try {
                double disbursed = Double.parseDouble(loan.getDisbursed_amount());
                double repaid = 0.0;

                if (loan.getPaymentHistory() != null) {
                    for (Payment p : loan.getPaymentHistory()) {
                        repaid += Double.parseDouble(p.getAmount());
                    }
                }

                double balance = Math.max(disbursed - repaid, 0);
                loanBalanceTextView.setText("Kshs. " + String.format(Locale.US, "%,.2f", balance));

                updateRepayButtonVisibility(balance); // Show/hide repay button here

            } catch (NumberFormatException e) {
                Log.e("LoanBalance", "Invalid number format", e);
                loanBalanceTextView.setText("Kshs. 0.00");
                updateRepayButtonVisibility(0);
            }
        } else {
            loanBalanceTextView.setText("Kshs. 0.00");
            updateRepayButtonVisibility(0);
        }
    }

    private void updateRepayButtonVisibility(double balance) {
        if (repayButton == null) return;

        if (balance > 0) {
            repayButton.setVisibility(View.VISIBLE);
        } else {
            repayButton.setVisibility(View.GONE);
        }
    }

    private void showRepaymentDialog() {
        final android.widget.EditText input = new android.widget.EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Enter repayment amount");

        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Make a Repayment")
                .setView(input)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String enteredAmount = input.getText().toString().trim();
                    if (!enteredAmount.isEmpty()) {
                        try {
                            double amount = Double.parseDouble(enteredAmount);
                            if (amount > 0) {
                                makeRepayment(amount);
                            } else {
                                Toast.makeText(getContext(), "Enter a valid amount", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Invalid amount entered", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void makeRepayment(double amount) {
        SharedPreferences prefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        int borrowerId = getBorrowerId(prefs);

        if (borrowerId == -1) {
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latestLoan == null) {
            Toast.makeText(getContext(), "No active loan to repay", Toast.LENGTH_SHORT).show();
            return;
        }

        int loanId = latestLoan.getId();

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.makeRepayment(loanId, borrowerId, amount);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Repayment successful", Toast.LENGTH_SHORT).show();
                    fetchLatestDisbursedLoan();
                    fetchLoanHistory();
                } else {
                    Toast.makeText(getContext(), "Repayment failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutDialog() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> logoutUser())
                .setNegativeButton("No", null)
                .show();
    }

    private void logoutUser() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

        startActivity(new Intent(getActivity(), MainActivity.class));
        requireActivity().finish();
    }

    private int getBorrowerId(SharedPreferences prefs) {
        try {
            return Integer.parseInt(prefs.getString("borrower_id", "-1"));
        } catch (NumberFormatException e) {
            Log.e("BorrowerHomeFragment", "Invalid borrower ID", e);
            return -1;
        }
    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
