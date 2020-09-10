package com.example.sofra.ui.fragment.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sofra.R;
import com.example.sofra.data.pojo.client.login.Login;
import com.example.sofra.data.pojo.general.city.CityData;
import com.example.sofra.databinding.FragmentRegisterBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.ui.fragment.login.LoginFragment;
import com.example.sofra.utils.HelperMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.example.sofra.data.local.SharedPreferencesManger.SaveData;
import static com.example.sofra.utils.CheckInput.isEditTextSet;
import static com.example.sofra.utils.CheckInput.isEmailValid;
import static com.example.sofra.utils.CheckInput.isPasswordMatched;
import static com.example.sofra.utils.CheckInput.isPhoneSet;
import static com.example.sofra.utils.GeneralResponse.getCityList;
import static com.example.sofra.utils.GeneralResponse.getRegionList;
import static com.example.sofra.utils.HelperMethod.convertBitmapToFile;
import static com.example.sofra.utils.HelperMethod.convertFileToMultipart;
import static com.example.sofra.utils.HelperMethod.convertStringToRequestBody;
import static com.example.sofra.utils.HelperMethod.replaceFragment;

public class RegisterFragment extends BaseFragment {

    private static final String TAG = RegisterFragment.class.getName();
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;

    private Bitmap bitmap;
    private File imageFile;

    private FragmentRegisterBinding binding;
    private String userType;

    private ArrayList<CityData> cityDataArrayList = new ArrayList<>();
    private ArrayList<CityData> regionDataArrayList = new ArrayList<>();

    private ClientRegisterViewModel clientRegisterViewModel;
    private RestaurantRegisterViewModel restaurantRegisterViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        final View view = binding.getRoot();
        setUpActivity();

        assert getArguments() != null;
        userType = getArguments().getString("userType");

        // find user type {seller or client} to set equivalents view
        assert userType != null;
        if (userType.equals("seller")) {
            restaurantEnableView();
        }

        // disappear keyboard if user click on any empty view in screen
        binding.fragmentRegisterContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperMethod.disappearKeypad(getActivity(), view);
            }
        });

        getCityList(getActivity(), cityDataArrayList, binding.fragmentRegisterCitySpinner, getString(R.string.choose_city));

        binding.fragmentRegisterCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    regionDataArrayList.clear();
                    // get region list from server
                    getRegionList(getActivity(), regionDataArrayList, position, binding.fragmentRegisterDistrictSpinner, getString(R.string.choose_region));

                } else {
                    getRegionList(getActivity(), regionDataArrayList, position, binding.fragmentRegisterDistrictSpinner, getString(R.string.choose_city_first));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getRegionList(getActivity(), regionDataArrayList, 0, binding.fragmentRegisterDistrictSpinner, getString(R.string.choose_city_first));
            }
        });

        return view;
    }


    /**
     * This method is to modify {@link RegisterFragment(View)  }
     * from Client to restaurant View.
     */
    private void restaurantEnableView() {
        binding.fragmentRegisterCustomerImageViewAddPicture.setVisibility(View.GONE);
        binding.fragmentRegisterCustomerEditTextPhoneNumber.setVisibility(View.GONE);
        binding.fragmentRegisterEditTextName.setHint(R.string.restaurant_name);
        binding.fragmentRegisterRestaurantDataContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link RegisterFragment#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();

        binding.fragmentRegisterCustomerImageViewAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUploadImageDialog();
            }
        });

        binding.fragmentRegisterRestaurantImageViewRestaurantPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUploadImageDialog();
            }
        });

        binding.fragmentRegisterLinearSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // find user type {seller or client} to set equivalents view
                assert userType != null;
                if (userType.equals("seller")) {
                    // check layout before register new restaurant
                    if (isEditTextSet(binding.fragmentRegisterEditTextName, binding.fragmentRegisterEditTextEmail
                            , binding.fragmentRegisterRestaurantEditTextPhoneNumber, binding.fragmentRegisterEditTextPassword
                            , binding.fragmentRegisterEditTextConfirmPassword, binding.fragmentRegisterRestaurantEditTextMinimumCharge
                            , binding.fragmentRegisterRestaurantEditTextDeliveryCost, binding.fragmentRegisterRestaurantEditTextDeliveryTime
                            , binding.fragmentRegisterRestaurantEditTextWhatsappNumber)
                            && isEmailValid(binding.fragmentRegisterEditTextEmail)
                            && isPhoneSet(binding.fragmentRegisterRestaurantEditTextPhoneNumber)
                            && isPhoneSet(binding.fragmentRegisterRestaurantEditTextWhatsappNumber)
                            && isPasswordMatched(binding.fragmentRegisterEditTextPassword, binding.fragmentRegisterEditTextConfirmPassword)) {
                        if (binding.fragmentRegisterDistrictSpinner.getSelectedItemPosition() <= 0) {
                            Toast.makeText(baseActivity, getString(R.string.select_your_location), Toast.LENGTH_LONG).show();
                        } else {
                            if (imageFile == null) {
                                Toast.makeText(baseActivity, getString(R.string.chose_image), Toast.LENGTH_LONG).show();
                            } else {
                                registerNewRestaurant();
                            }
                        }
                    }
                } else {
                    // check layout before register new client
                    if (isEditTextSet(binding.fragmentRegisterEditTextName, binding.fragmentRegisterEditTextEmail
                            , binding.fragmentRegisterCustomerEditTextPhoneNumber, binding.fragmentRegisterEditTextPassword
                            , binding.fragmentRegisterEditTextConfirmPassword)
                            && isEmailValid(binding.fragmentRegisterEditTextEmail)
                            && isPhoneSet(binding.fragmentRegisterCustomerEditTextPhoneNumber)
                            && isPasswordMatched(binding.fragmentRegisterEditTextPassword, binding.fragmentRegisterEditTextConfirmPassword)) {
                        if (binding.fragmentRegisterDistrictSpinner.getSelectedItemPosition() <= 0) {
                            Toast.makeText(baseActivity, getString(R.string.select_your_location), Toast.LENGTH_LONG).show();
                        } else {
                            if (imageFile == null) {
                                Toast.makeText(baseActivity, getString(R.string.chose_image), Toast.LENGTH_LONG).show();
                            } else {
                                registerNewClient();
                            }
                        }
                    }
                }
            }
        });
    }

    private void registerNewRestaurant() {
        restaurantRegisterViewModel = new ViewModelProvider(this).get(RestaurantRegisterViewModel.class);

        // convert customer input to RequestBody
        RequestBody name = convertStringToRequestBody(binding.fragmentRegisterEditTextName.getText().toString());
        RequestBody email = convertStringToRequestBody(binding.fragmentRegisterEditTextEmail.getText().toString());
        RequestBody password = convertStringToRequestBody(binding.fragmentRegisterEditTextPassword.getText().toString());
        RequestBody confirmPassword = convertStringToRequestBody(binding.fragmentRegisterEditTextConfirmPassword.getText().toString());
        RequestBody phone = convertStringToRequestBody(binding.fragmentRegisterRestaurantEditTextPhoneNumber.getText().toString());
        RequestBody whatsApp = convertStringToRequestBody(binding.fragmentRegisterRestaurantEditTextWhatsappNumber.getText().toString());
        RequestBody regionIds = convertStringToRequestBody(String.valueOf(binding.fragmentRegisterDistrictSpinner.getSelectedItemPosition()));
        RequestBody deliveryCost = convertStringToRequestBody(binding.fragmentRegisterRestaurantEditTextDeliveryCost.getText().toString());
        RequestBody minimumCharger = convertStringToRequestBody(binding.fragmentRegisterRestaurantEditTextMinimumCharge.getText().toString());
        MultipartBody.Part image = convertFileToMultipart(imageFile, "profile_image");
        RequestBody deliveryTime = convertStringToRequestBody(binding.fragmentRegisterRestaurantEditTextDeliveryTime.getText().toString());

        // start call server
        restaurantRegisterViewModel.getRestaurantRegister(name, email, password, confirmPassword
                , phone, whatsApp, regionIds, deliveryCost, minimumCharger, image, deliveryTime);

        // start observing data
        restaurantRegisterViewModel.registerMutableLiveData.observe(this, new Observer<Login>() {
            @Override
            public void onChanged(Login login) {
                if (login.getStatus() == 1) {
                    Toast.makeText(baseActivity, login.getMsg(), Toast.LENGTH_LONG).show();

                    // save data to SharedPreferences
                    SaveData(getActivity(), "userType", "seller");
                    SaveData(getActivity(), "clientId", login.getData().getUser().getId());
                    SaveData(getActivity(), "email", binding.fragmentRegisterEditTextEmail.getText().toString());
                    SaveData(getActivity(), "password", binding.fragmentRegisterEditTextPassword.getText().toString());
                    SaveData(getActivity(), "apiToken", login.getData().getApiToken());

                    Bundle bundle = new Bundle();
                    bundle.putString("userType", "seller");

                    replaceFragment(getParentFragmentManager()
                            , Objects.requireNonNull(getActivity()).findViewById(R.id.auth_activity_frame).getId()
                            , new LoginFragment(), null, bundle);

                } else {
                    Toast.makeText(baseActivity, login.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerNewClient() {
        clientRegisterViewModel = new ViewModelProvider(this).get(ClientRegisterViewModel.class);

        // convert customer input to RequestBody
        RequestBody name = convertStringToRequestBody(binding.fragmentRegisterEditTextName.getText().toString());
        RequestBody email = convertStringToRequestBody(binding.fragmentRegisterEditTextEmail.getText().toString());
        RequestBody regionIds = convertStringToRequestBody(String.valueOf(binding.fragmentRegisterDistrictSpinner.getSelectedItemPosition()));
        RequestBody password = convertStringToRequestBody(binding.fragmentRegisterEditTextPassword.getText().toString());
        RequestBody confirmPassword = convertStringToRequestBody(binding.fragmentRegisterEditTextConfirmPassword.getText().toString());
        RequestBody phone = convertStringToRequestBody(binding.fragmentRegisterCustomerEditTextPhoneNumber.getText().toString());
        MultipartBody.Part image = convertFileToMultipart(imageFile, "profile_image");

        // start call server
        clientRegisterViewModel.getRestaurantRegister(name, email, password, confirmPassword, phone, regionIds, image);

        // start observing data
        clientRegisterViewModel.clientMutableLiveData.observe(this, new Observer<Login>() {
            @Override
            public void onChanged(Login login) {
                if (login.getStatus() == 1) {
                    Toast.makeText(baseActivity, login.getMsg(), Toast.LENGTH_LONG).show();

                    // save data to SharedPreferences
                    SaveData(getActivity(), "userType", "client");
                    SaveData(getActivity(), "clientId", login.getData().getUser().getId());
                    SaveData(getActivity(), "email", binding.fragmentRegisterEditTextEmail.getText().toString());
                    SaveData(getActivity(), "password", binding.fragmentRegisterEditTextPassword.getText().toString());
                    SaveData(getActivity(), "apiToken", login.getData().getApiToken());

                    Bundle bundle = new Bundle();
                    bundle.putString("userType", "client");

                    replaceFragment(getParentFragmentManager()
                            , Objects.requireNonNull(getActivity()).findViewById(R.id.auth_activity_frame).getId()
                            , new LoginFragment(), null, bundle);

                } else {
                    Toast.makeText(baseActivity, login.getMsg(), Toast.LENGTH_SHORT).show();
                }
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
     * {@link RegisterFragment#onActivityResult(int, int, Intent)}.
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

                // chose which image view will be set {seller or client}
                if (userType.equals("seller")) {
                    binding.fragmentRegisterRestaurantImageViewRestaurantPicture.setImageBitmap(bitmap);
                } else {
                    binding.fragmentRegisterCustomerImageViewAddPicture.setImageBitmap(bitmap);
                }

            } else if (requestCode == SELECT_FILE)/* if user select photo from gallery*/ {
                Uri selectedImageUri = data.getData();

                // covert image uri to bitmap
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // chose which image view will be set {seller or client}
                if (userType.equals("seller")) {
                    binding.fragmentRegisterRestaurantImageViewRestaurantPicture.setImageBitmap(bitmap);
                } else {
                    binding.fragmentRegisterCustomerImageViewAddPicture.setImageBitmap(bitmap);
                }

            }

            // use system time as file name
            String imageFileName = String.valueOf(System.currentTimeMillis());
            imageFile = new File(imageFileName);
            imageFile = convertBitmapToFile(Objects.requireNonNull(getContext()), bitmap, imageFileName);
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}