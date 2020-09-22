package com.example.sofra.ui.fragment.home.restaurant.foodItems;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sofra.R;
import com.example.sofra.data.pojo.restaurant.foodItems.FoodItems;
import com.example.sofra.databinding.FragmentCreateItemFoodBinding;
import com.example.sofra.ui.fragment.BaseFragment;
import com.example.sofra.utils.GlideApp;

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

public class CreateItemFoodFragment extends BaseFragment {
    private static final String TAG = CreateItemFoodFragment.class.getName();
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;

    FragmentCreateItemFoodBinding binding;

    private String imageUrl, name, description, priceOffer, price, mApiToken;
    private int itemId, mCategoryId;

    private Bitmap bitmap;
    private File imageFile;

    private RestaurantGetFoodItemListViewModel restaurantGetFoodItemListViewModel;

    public CreateItemFoodFragment() {
    }

    public CreateItemFoodFragment(String imageUrl, String name, String description, String price
            , String priceOffer, String mCategoryId, int itemId) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.priceOffer = priceOffer;
        this.mCategoryId = Integer.parseInt(mCategoryId);
        this.price = price;
        this.itemId = itemId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateItemFoodBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUpActivity();

        restaurantGetFoodItemListViewModel =
                new ViewModelProvider(Objects.requireNonNull(getActivity())).get(RestaurantGetFoodItemListViewModel.class);

        if (LoadData(getActivity(), "apiToken") != null) {
            mApiToken = LoadData(getActivity(), "apiToken");
        }

        if (getArguments() != null) {
            mCategoryId = getArguments().getInt("categoryId");
        }

        if (itemId > 0) {
            setDataToFragment();
        }

        return view;
    }

    private void setDataToFragment() {
        binding.fragmentCreateItemFoodTextViewTitle.setText(getString(R.string.edit_product));
        GlideApp.with(Objects.requireNonNull(getActivity())).load(imageUrl).into(binding.fragmentCreateItemFoodLayoutImage);
        binding.fragmentCreateItemFoodEditTextName.setText(name);
        binding.fragmentCreateItemFoodEditTextDescription.setText(description);
        binding.fragmentCreateItemFoodEditTextPrice.setText(price);
        binding.fragmentCreateItemFoodEditTextOfferPrice.setText(priceOffer);
        binding.dialogRestaurantCategoryItemButtonAdd.setText(getString(R.string.edit));
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.fragmentCreateItemFoodLayoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUploadImageDialog();
            }
        });

        binding.dialogRestaurantCategoryItemButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditTextSet(binding.fragmentCreateItemFoodEditTextName
                        , binding.fragmentCreateItemFoodEditTextDescription
                        , binding.fragmentCreateItemFoodEditTextPrice)) {

                    if (itemId > 0) {
                        editItem();
                    } else {
                        if (imageFile == null) {
                            Toast.makeText(getActivity(), getString(R.string.chose_image), Toast.LENGTH_LONG).show();
                        } else {
                            addItem();
                        }
                    }
                }
            }
        });

    }

    private void addItem() {
        CreateFoodItemViewModel createFoodItemViewModel =
                new ViewModelProvider(this).get(CreateFoodItemViewModel.class);

        RequestBody time = null, offerPrice = null;

        RequestBody name = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextName.getText().toString());
        RequestBody description = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextDescription.getText().toString());
        RequestBody price = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextPrice.getText().toString());

        if (binding.fragmentCreateItemFoodEditTextPreparingTime.getText() != null) {
            time = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextPreparingTime.getText().toString());
        }

        if (binding.fragmentCreateItemFoodEditTextOfferPrice.getText() != null) {
            offerPrice = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextOfferPrice.getText().toString());
        }

        final RequestBody apiToken = convertStringToRequestBody(mApiToken);
        final RequestBody categoryId = convertStringToRequestBody(String.valueOf(mCategoryId));
        MultipartBody.Part photo = convertFileToMultipart(imageFile, "photo");

        createFoodItemViewModel.addNewFoodItem(name, description, price, photo
                , offerPrice, time, apiToken, categoryId);

        createFoodItemViewModel.getFoodItemsMutableLiveData().observe(Objects.requireNonNull(getActivity()), new Observer<FoodItems>() {
            @Override
            public void onChanged(FoodItems foodItems) {
                if (foodItems.getStatus() == 1) {
                    Toast.makeText(baseActivity, foodItems.getMsg(), Toast.LENGTH_SHORT).show();
                    onBack();
                    restaurantGetFoodItemListViewModel.getFoodItemList(mApiToken
                            , mCategoryId, 1);

                } else {
                    Toast.makeText(baseActivity, foodItems.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editItem() {
        EditFoodItemViewModel editFoodItemViewModel =
                new ViewModelProvider(this).get(EditFoodItemViewModel.class);

        RequestBody time = null, offerPrice = null;
        MultipartBody.Part photo = null;

        RequestBody name = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextName.getText().toString());
        RequestBody description = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextDescription.getText().toString());
        RequestBody price = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextPrice.getText().toString());

        if (binding.fragmentCreateItemFoodEditTextPreparingTime.getText() != null) {
            time = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextPreparingTime.getText().toString());
        }

        if (binding.fragmentCreateItemFoodEditTextOfferPrice.getText() != null) {
            offerPrice = convertStringToRequestBody(binding.fragmentCreateItemFoodEditTextOfferPrice.getText().toString());
        }

        if (imageFile != null) {
            photo = convertFileToMultipart(imageFile, "photo");
        }

        final RequestBody apiToken = convertStringToRequestBody(mApiToken);
        final RequestBody categoryId = convertStringToRequestBody(String.valueOf(mCategoryId));
        final RequestBody itemId = convertStringToRequestBody(String.valueOf(this.itemId));

        editFoodItemViewModel.editFoodItem(name, description, price, photo
                , offerPrice, time, apiToken, itemId, categoryId);

        editFoodItemViewModel.foodItemsMutableLiveData.observe(Objects.requireNonNull(getActivity()), new Observer<FoodItems>() {
            @Override
            public void onChanged(FoodItems foodItems) {
                if (foodItems.getStatus() == 1) {
                    Toast.makeText(baseActivity, foodItems.getMsg(), Toast.LENGTH_SHORT).show();
                    onBack();
                    restaurantGetFoodItemListViewModel.getFoodItemList(mApiToken
                            , mCategoryId, 1);

                } else {
                    Toast.makeText(baseActivity, foodItems.getMsg(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            // if user chose camera to take photo
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                assert bundle != null;
                bitmap = (Bitmap) bundle.get("data");

                binding.fragmentCreateItemFoodLayoutImage.setImageBitmap(bitmap);

            } else if (requestCode == SELECT_FILE)/* if user select photo from gallery*/ {
                Uri selectedImageUri = data.getData();

                // covert image uri to bitmap
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                binding.fragmentCreateItemFoodLayoutImage.setImageBitmap(bitmap);
                binding.fragmentCreateItemFoodLayoutImage.setBackgroundColor(
                        getResources().getColor(R.color.primaryColor));
            }
            imageFile = convertBitmapToFile(Objects.requireNonNull(getContext()), bitmap);
        }
    }

    @Override
    public void onBack() {
        super.onBack();
    }
}