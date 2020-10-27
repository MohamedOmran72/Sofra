package com.example.sofra.ui.fragment.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.sofra.R;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.data.pojo.client.login.User;
import com.example.sofra.data.pojo.general.city.CityData;
import com.example.sofra.databinding.FragmentEditProfileBinding;
import com.example.sofra.ui.activity.AuthActivity;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.ui.fragment.register.RegisterFragment;
import com.example.sofra.utils.HelperMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.sofra.data.local.SharedPreferencesManger.LoadData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.CheckInput.isEmailValid;
import static com.example.sofra.utils.CheckInput.isPhoneSet;
import static com.example.sofra.utils.GeneralResponse.getCityList;
import static com.example.sofra.utils.GeneralResponse.getRegionList;
import static com.example.sofra.utils.HelperMethod.convertBitmapToFile;
import static com.example.sofra.utils.HelperMethod.convertFileToMultipart;
import static com.example.sofra.utils.HelperMethod.convertStringToRequestBody;

public class EditProfileFragment extends BaseFragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;

    FragmentEditProfileBinding binding;
    private final ArrayList<CityData> cityDataArrayList = new ArrayList<>();
    private final ArrayList<CityData> regionDataArrayList = new ArrayList<>();
    private String userType;
    private String apiToken;
    private Bitmap bitmap;
    private File imageFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        final View view = binding.getRoot();
        setUpActivity();

        assert getArguments() != null;
        userType = getArguments().getString("userType");

        if (LoadData(getActivity(), "apiToken") != null) {
            apiToken = LoadData(getActivity(), "apiToken");
        } else {
            Intent intent = new Intent(getActivity(), AuthActivity.class);
            intent.putExtra("userType", userType);
            startActivity(intent);
            return null;
        }

        // find user type {seller or client} to set equivalents view
        assert userType != null;
        if (userType.equals("seller")) {
            restaurantEnableView();
            getRestaurantProfileFromServer();
        } else {
            getClientProfileFromServer();
        }

        // disappear keyboard if user click on any empty view in screen
        binding.fragmentEditProfileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.disappearKeypad(getActivity(), view);
            }
        });

        getCityList(getActivity(), cityDataArrayList, binding.fragmentEditProfileCitySpinner, getString(R.string.choose_city));
        binding.fragmentEditProfileCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    regionDataArrayList.clear();
                    // get region list from server
                    getRegionList(getActivity(), regionDataArrayList, position, binding.fragmentEditProfileDistrictSpinner, getString(R.string.choose_region));

                } else {
                    getRegionList(getActivity(), regionDataArrayList, position, binding.fragmentEditProfileDistrictSpinner, getString(R.string.choose_city_first));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getRegionList(getActivity(), regionDataArrayList, 0, binding.fragmentEditProfileDistrictSpinner, getString(R.string.choose_city_first));
            }
        });

        return view;
    }

    /**
     * This method is to modify {@link RegisterFragment (View)  }
     * from Client to restaurant View.
     */
    private void restaurantEnableView() {
        binding.fragmentEditProfileCustomerEditTextPhoneNumber.setVisibility(View.GONE);
        binding.fragmentEditProfileEditTextName.setHint(R.string.restaurant_name);
        binding.fragmentEditProfileRestaurantDataContainer.setVisibility(View.VISIBLE);
    }

    /**
     * to get client data from server
     */
    private void getClientProfileFromServer() {

    }

    /**
     * to get restaurant data from server
     */
    private void getRestaurantProfileFromServer() {
        RestaurantGetProfileViewModel restaurantGetProfileViewModel =
                new ViewModelProvider(Objects.requireNonNull(getActivity())).get(RestaurantGetProfileViewModel.class);
        restaurantGetProfileViewModel.getRestaurantProfile(apiToken);
        restaurantGetProfileViewModel.getRestaurantProfileMutableLiveData.observe(getActivity(), new Observer<Login>() {
            @Override
            public void onChanged(Login login) {
                if (login.getStatus() == 1) {
                    setRestaurantDataToView(login.getData().getUser());
                } else {
                    Toast.makeText(baseActivity, login.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Update profile data with restaurant data.
     *
     * @param restaurant the data of restaurant
     */
    private void setRestaurantDataToView(final User restaurant) {

        Glide.with(Objects.requireNonNull(getActivity())).load(restaurant.getPhotoUrl()).into(binding.fragmentEditProfileImageView);
        binding.fragmentEditProfileEditTextName.setText(restaurant.getName());
        binding.fragmentEditProfileEditTextEmail.setText(restaurant.getEmail());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.fragmentEditProfileCitySpinner.setSelection(restaurant.getRegion().getCity().getId());
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.fragmentEditProfileDistrictSpinner.setSelection(restaurant.getRegion().getId());
            }
        }, 2000);

        binding.fragmentEditProfileRestaurantEditTextMinimumCharge.setText(restaurant.getMinimumCharger());
        binding.fragmentEditProfileRestaurantEditTextDeliveryCost.setText(restaurant.getDeliveryCost());
        binding.fragmentEditProfileRestaurantEditTextDeliveryTime.setText(restaurant.getDeliveryTime());
        binding.fragmentEditProfileRestaurantEditTextPhoneNumber.setText(restaurant.getPhone());
        binding.fragmentEditProfileRestaurantEditTextWhatsappNumber.setText(restaurant.getWhatsapp());

        binding.fragmentEditProfileSwitch.setChecked(restaurant.getAvailability().equals("open"));

    }


    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link EditProfileFragment#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        binding.fragmentEditProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUploadImageDialog();
            }
        });

        binding.fragmentEditProfileButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // find user type {seller or client} to set equivalents view
                if (userType.equals("seller")) {
                    // check layout before register new restaurant
                    if (isEditTextSet(binding.fragmentEditProfileEditTextName, binding.fragmentEditProfileEditTextEmail
                            , binding.fragmentEditProfileRestaurantEditTextPhoneNumber, binding.fragmentEditProfileRestaurantEditTextMinimumCharge
                            , binding.fragmentEditProfileRestaurantEditTextDeliveryCost, binding.fragmentEditProfileRestaurantEditTextDeliveryTime
                            , binding.fragmentEditProfileRestaurantEditTextWhatsappNumber)
                            && isEmailValid(binding.fragmentEditProfileEditTextEmail)
                            && isPhoneSet(binding.fragmentEditProfileRestaurantEditTextPhoneNumber)
                            && isPhoneSet(binding.fragmentEditProfileRestaurantEditTextWhatsappNumber)) {
                        if (binding.fragmentEditProfileDistrictSpinner.getSelectedItemPosition() <= 0) {
                            Toast.makeText(baseActivity, getString(R.string.select_your_location), Toast.LENGTH_LONG).show();
                        } else {
                            setRestaurantProfileToServer();
                        }
                    }
                } else {
                    setClientProfileToServer();
                }
            }
        });

    }

    /**
     * get client data from view and send to server
     */
    private void setClientProfileToServer() {
    }

    /**
     * get restaurant data from view and send to server
     */
    private void setRestaurantProfileToServer() {
        RestaurantEditProfileViewModel restaurantEditProfileViewModel =
                new ViewModelProvider(Objects.requireNonNull(getActivity())).get(RestaurantEditProfileViewModel.class);

        // convert customer input to RequestBody
        RequestBody api_token = convertStringToRequestBody(apiToken);
        MultipartBody.Part image = convertFileToMultipart(imageFile, "photo");
        RequestBody name = convertStringToRequestBody(binding.fragmentEditProfileEditTextName.getText().toString());
        RequestBody email = convertStringToRequestBody(binding.fragmentEditProfileEditTextEmail.getText().toString());
        RequestBody regionIds = convertStringToRequestBody(String.valueOf(binding.fragmentEditProfileDistrictSpinner.getSelectedItemPosition()));
        RequestBody deliveryCost = convertStringToRequestBody(binding.fragmentEditProfileRestaurantEditTextDeliveryCost.getText().toString());
        RequestBody minimumCharger = convertStringToRequestBody(binding.fragmentEditProfileRestaurantEditTextMinimumCharge.getText().toString());
        RequestBody deliveryTime = convertStringToRequestBody(binding.fragmentEditProfileRestaurantEditTextDeliveryTime.getText().toString());

        RequestBody availability =
                (binding.fragmentEditProfileSwitch.isChecked()) ? convertStringToRequestBody("open") : convertStringToRequestBody("closed");

        RequestBody phone = convertStringToRequestBody(binding.fragmentEditProfileRestaurantEditTextPhoneNumber.getText().toString());
        RequestBody whatsApp = convertStringToRequestBody(binding.fragmentEditProfileRestaurantEditTextWhatsappNumber.getText().toString());

        restaurantEditProfileViewModel.editRestaurantProfile(api_token, image, name, email, regionIds
                , deliveryCost, minimumCharger, availability, deliveryTime, phone, whatsApp);

        restaurantEditProfileViewModel.editRestaurantProfileMutableLiveData.observe(getActivity(), new Observer<Login>() {
            @Override
            public void onChanged(Login login) {
                Toast.makeText(getActivity(), login.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
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


    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {@link EditProfileFragment#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            // if user chose camera to take photo
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                assert bundle != null;
                bitmap = (Bitmap) bundle.get("data");

                binding.fragmentEditProfileImageView.setImageBitmap(bitmap);

            } else if (requestCode == SELECT_FILE)/* if user select photo from gallery*/ {
                Uri selectedImageUri = data.getData();

                // covert image uri to bitmap
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                binding.fragmentEditProfileImageView.setImageBitmap(bitmap);

            }
            imageFile = convertBitmapToFile(Objects.requireNonNull(getContext()), bitmap);
        }
    }

}