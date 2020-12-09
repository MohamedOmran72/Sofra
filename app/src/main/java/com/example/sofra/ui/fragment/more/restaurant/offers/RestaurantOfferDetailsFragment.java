package com.example.sofra.ui.fragment.more.restaurant.offers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.offer.Offer;
import com.example.sofra.data.pojo.offer.OfferData;
import com.example.sofra.databinding.FragmentRestaurantOfferDetailsBinding;
import com.example.sofra.ui.fragment.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.HelperMethod.convertBitmapToFile;
import static com.example.sofra.utils.HelperMethod.convertFileToMultipart;
import static com.example.sofra.utils.HelperMethod.convertStringToRequestBody;
import static com.example.sofra.utils.HelperMethod.disappearKeypad;
import static com.example.sofra.utils.HelperMethod.showDatePickerDialog;

public class RestaurantOfferDetailsFragment extends BaseFragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    private static final String TAG = RestaurantOfferDetailsFragment.class.getName();
    private FragmentRestaurantOfferDetailsBinding binding;
    private RestaurantOfferViewModel restaurantOfferViewModel;
    private RestaurantOfferDetailsFragmentViewModel restaurantOfferDetailsFragmentViewModel;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantOfferDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setUpActivity();

        restaurantOfferViewModel = new ViewModelProvider(this).get(RestaurantOfferViewModel.class);
        restaurantOfferDetailsFragmentViewModel = new ViewModelProvider(this)
                .get(RestaurantOfferDetailsFragmentViewModel.class);

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

        restaurantOfferDetailsFragmentViewModel.getAddOfferMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Offer>() {
            @Override
            public void onChanged(Offer offer) {
                if (offer.getStatus() == 1) {
                    restaurantOfferViewModel.getRestaurantOfferList(apiToken, 1);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                }
                Toast.makeText(getActivity(), offer.getMsg(), Toast.LENGTH_LONG).show();
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

        binding.fragmentRestaurantOfferDetailsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUploadImageDialog();
            }
        });

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


        binding.fragmentRestaurantOfferDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditTextSet(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferName)
                        && isEditTextSet(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferPrice)
                        && isEditTextSet(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferDescription)
                        && isEditTextSet(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferStartDate)
                        && isEditTextSet(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferEndDate)) {
                    if (offerId > 0) {
                        editOffer();
                    } else {
                        if (imageFile == null) {
                            Toast.makeText(getActivity(), getString(R.string.chose_image), Toast.LENGTH_LONG).show();
                        } else {
                            addOffer();
                        }
                    }
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

    private void addOffer() {
        // convert customer input to RequestBody
        MultipartBody.Part photo = convertFileToMultipart(imageFile, "photo");
        final RequestBody apiToken = convertStringToRequestBody(this.apiToken);
        RequestBody name = convertStringToRequestBody(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferName.getText().toString());
        RequestBody price = convertStringToRequestBody(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferPrice.getText().toString());
        RequestBody description = convertStringToRequestBody(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferDescription.getText().toString());
        RequestBody startDate = convertStringToRequestBody(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferStartDate.getText().toString());
        RequestBody endDate = convertStringToRequestBody(binding.fragmentRestaurantOfferDetailsTextInputEditTextOfferEndDate.getText().toString());

        // start call server
        restaurantOfferDetailsFragmentViewModel.addRestaurantOffer(apiToken, photo, name, price, description
                , startDate, endDate);

    }

    private void editOffer() {

    }

    private void createUploadImageDialog() {
        final CharSequence[] items = {getString(R.string.chose_camera), getString(R.string.chose_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.chose_image));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, REQUEST_CAMERA);
                        break;
                    case 1:
                        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intentGallery.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentGallery, getString(R.string.select_file)), SELECT_FILE);
                        break;
                    default:
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            // if user chose camera to take photo
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                assert bundle != null;
                bitmap = (Bitmap) bundle.get("data");

                binding.fragmentRestaurantOfferDetailsImageView.setImageBitmap(bitmap);

            } else if (requestCode == SELECT_FILE)/* if user select photo from gallery*/ {
                Uri selectedImageUri = data.getData();

                // covert image uri to bitmap
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                binding.fragmentRestaurantOfferDetailsImageView.setImageBitmap(bitmap);
            }
            imageFile = convertBitmapToFile(Objects.requireNonNull(getContext()), bitmap);
        }
    }
}