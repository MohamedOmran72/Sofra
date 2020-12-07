package com.example.sofra.ui.fragment.more.restaurant.offers;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.offer.OfferData;
import com.example.sofra.databinding.FragmentRestaurantOfferDetailsBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.io.File;
import java.util.Objects;

import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.HelperMethod.disappearKeypad;
import static com.example.sofra.utils.HelperMethod.showDatePickerDialog;

public class RestaurantOfferDetailsFragment extends BaseFragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;

    private FragmentRestaurantOfferDetailsBinding binding;
    private View view;
    private OfferData offerData;

    private String apiToken;
    private int offerId;

    private Bitmap bitmap;
    private File imageFile;

    public RestaurantOfferDetailsFragment() {
    }

    public RestaurantOfferDetailsFragment(OfferData offerData) {
        this.offerData = offerData;
        this.offerId = offerData.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOfferDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setUpActivity();

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
            apiToken = "Jptu3JVmDXGpJEaQO9ZrjRg5RuAVCo45OC2AcOKqbVZPmu0ZJPN3T1sm0cWx";
        }

        if (offerId > 0) {
            setDataToFragment();
        }

        binding.fragmentRestaurantOfferDetailsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disappearKeypad(getActivity(), view);
            }
        });

        return view;
    }

    private void setDataToFragment() {

        binding.fragmentRestaurantOfferDetailsTextViewTitle.setText(getString(R.string.editOffer));
        Glide.with(getActivity()).load(offerData.getPhotoUrl()).into(binding.fragmentRestaurantOfferDetailsImageView);
        binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferName.setText(offerData.getName());
        binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferPrice.setText(offerData.getPrice());
        binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferDescription.setText(offerData.getDescription());
        binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferStartDate.setText(offerData.getStartingAt());
        binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferEndDate.setText(offerData.getEndingAt());
        binding.fragmentRestaurantOfferDetailsButton.setText(R.string.edit);
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferStartDate
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            showDatePickerDialog(getContext(), binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferStartDate);
                        }
                    }
                });

        binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferEndDate
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            showDatePickerDialog(getContext(), binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferEndDate);
                        }
                    }
                });

        // handel onBack to set bottom navigation visible
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }
}