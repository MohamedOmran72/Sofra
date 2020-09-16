package com.example.sofra.ui.dialog.category;

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
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sofra.R;
import com.example.sofra.data.pojo.restaurant.restaurantCategories.RestaurantCategories;
import com.example.sofra.databinding.DialogRestaurantCategoryItemBinding;
import com.example.sofra.ui.fragment.home.RestaurantCategoriesViewModel;
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

public class RestaurantCategoryItemDialog extends DialogFragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 0;
    DialogRestaurantCategoryItemBinding binding;
    RestaurantCategoriesViewModel restaurantCategoriesViewModel;

    private String categoryItemName;
    private String categoryImageUrl;
    private String mApiToken;
    private int mCategoryId;

    private Bitmap bitmap;
    private File imageFile;

    public RestaurantCategoryItemDialog() {
    }

    public RestaurantCategoryItemDialog(String categoryItemName, String categoryImageUrl, int mCategoryId) {
        this.categoryItemName = categoryItemName;
        this.categoryImageUrl = categoryImageUrl;
        this.mCategoryId = mCategoryId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogRestaurantCategoryItemBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        restaurantCategoriesViewModel =
                new ViewModelProvider(Objects.requireNonNull(getActivity())).get(RestaurantCategoriesViewModel.class);

        if (LoadData(getActivity(), "apiToken") != null) {
            mApiToken = LoadData(getActivity(), "apiToken");
        }

        if (mCategoryId > 0) {
            setDataToDialog();
        }

        return view;
    }

    private void setDataToDialog() {

        binding.dialogRestaurantCategoryItemTextViewTitle.setText(getString(R.string.edit_category));
        GlideApp.with(Objects.requireNonNull(getActivity())).load(categoryImageUrl).into(binding.dialogRestaurantCategoryItemCircleImage);
        binding.dialogRestaurantCategoryItemEditTextName.setText(categoryItemName);
        binding.dialogRestaurantCategoryItemButtonAdd.setText(getText(R.string.edit));

        binding.dialogRestaurantCategoryItemCircleImage.setBackgroundColor(
                getResources().getColor(R.color.primaryColor));
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.dialogRestaurantCategoryItemCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUploadImageDialog();
            }
        });

        binding.dialogRestaurantCategoryItemButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditTextSet(binding.dialogRestaurantCategoryItemEditTextName)) {
                    if (mCategoryId > 0) {
                        editCategory();
                    } else {
                        if (imageFile == null) {
                            Toast.makeText(getActivity(), getString(R.string.chose_image), Toast.LENGTH_LONG).show();
                        } else {
                            addCategory();
                        }
                    }
                }
                Objects.requireNonNull(getDialog()).dismiss();
            }
        });
    }

    private void editCategory() {
        RestaurantEditCategoryViewModel restaurantEditCategoryViewModel =
                new ViewModelProvider(this).get(RestaurantEditCategoryViewModel.class);

        // convert customer input to RequestBody
        RequestBody name = convertStringToRequestBody(binding.dialogRestaurantCategoryItemEditTextName.getText().toString());
        final RequestBody apiToken = convertStringToRequestBody(mApiToken);
        RequestBody categoryId = convertStringToRequestBody(String.valueOf(mCategoryId));

        // start call server
        if (imageFile != null) {
            MultipartBody.Part photo = convertFileToMultipart(imageFile, "photo");
            restaurantEditCategoryViewModel.updateCategory(name, photo, apiToken, categoryId);
        } else {
            restaurantEditCategoryViewModel.updateCategory(name, null, apiToken, categoryId);
        }

        restaurantEditCategoryViewModel.updateCategoryMutableLiveData.observe(Objects.requireNonNull(getActivity()), new Observer<RestaurantCategories>() {
            @Override
            public void onChanged(RestaurantCategories restaurantCategories) {
                if (restaurantCategories.getStatus() == 1) {
                    restaurantCategoriesViewModel.getRestaurantCategories(mApiToken, 1);
                }
                Toast.makeText(getActivity(), restaurantCategories.getMsg(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void addCategory() {
        RestaurantNewCategoryViewModel newCategoryViewModel =
                new ViewModelProvider(this).get(RestaurantNewCategoryViewModel.class);

        // convert customer input to RequestBody
        RequestBody name = convertStringToRequestBody(binding.dialogRestaurantCategoryItemEditTextName.getText().toString());
        final RequestBody apiToken = convertStringToRequestBody(mApiToken);
        MultipartBody.Part photo = convertFileToMultipart(imageFile, "photo");

        // start call server
        newCategoryViewModel.addNewCategory(name, photo, apiToken);

        // start observing data
        newCategoryViewModel.newCategoryMutableLiveData.observe(Objects.requireNonNull(getActivity()), new Observer<RestaurantCategories>() {
            @Override
            public void onChanged(RestaurantCategories restaurantCategories) {
                if (restaurantCategories.getStatus() == 1) {
                    restaurantCategoriesViewModel.getRestaurantCategories(mApiToken, 1);
                }
                Toast.makeText(getActivity(), restaurantCategories.getMsg(), Toast.LENGTH_LONG).show();
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

                binding.dialogRestaurantCategoryItemCircleImage.setImageBitmap(bitmap);

            } else if (requestCode == SELECT_FILE)/* if user select photo from gallery*/ {
                Uri selectedImageUri = data.getData();

                // covert image uri to bitmap
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                binding.dialogRestaurantCategoryItemCircleImage.setImageBitmap(bitmap);
                binding.dialogRestaurantCategoryItemCircleImage.setBackgroundColor(
                        getResources().getColor(R.color.primaryColor));
            }
            imageFile = convertBitmapToFile(Objects.requireNonNull(getContext()), bitmap);
        }
    }

}
