package com.example.myapplication.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.Change_icon;
import com.example.myapplication.Activities.Change_name;
import com.example.myapplication.Activities.history_show;
import com.example.myapplication.Activities.login;
import com.example.myapplication.R;
import com.example.myapplication.Database.UsersDatabaseHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class MineFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String mParam1;
    private String mParam2;
    private TextView account;
    private TextView name;
    private TextView change_name;
    private TextView history_search;
    private TextView exit;
    private ImageView headIcon;
    private LinearLayout lChangename;
    private LinearLayout lHistory;
    private LinearLayout lexit;
    private Context context;
    private  Activity activity;
    private  ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<Intent> cropImageLauncher;

    public MineFragment(Activity activity) {
        // 初始化pickImageLauncher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        startCrop(selectedImageUri);
                    }
                });

        // 初始化cropImageLauncher
        cropImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri croppedImageUri = UCrop.getOutput(result.getData());
                        if (croppedImageUri != null) {
                            displayCroppedImage(croppedImageUri);
                        }
                    }
                });
        this.activity=activity;
    }

    public static MineFragment newInstance(String param1, String param2,Activity activity) {
        MineFragment fragment = new MineFragment(activity);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        account = view.findViewById(R.id.detail_account);
        name = view.findViewById(R.id.detail_name);
        change_name = view.findViewById(R.id.change_name);
        headIcon=view.findViewById(R.id.head);
        history_search=view.findViewById(R.id.history_search);
        exit=view.findViewById(R.id.exit);
        lChangename=view.findViewById(R.id.lchange_name);
        lHistory=view.findViewById(R.id.lshow_history_search);
        lexit=view.findViewById(R.id.lexit_account);

        change_name.setOnClickListener(this);
        headIcon.setOnClickListener(this);
        history_search.setOnClickListener(this);
        exit.setOnClickListener(this);
        lChangename.setOnClickListener(this);
        lHistory.setOnClickListener(this);
        lexit.setOnClickListener(this);
        showDetail(mParam2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_name:
            case R.id.lchange_name:
                changeName();
                break;
            case R.id.head:
               /// openGallery();
                break;
            case R.id.history_search:
            case R.id.lshow_history_search:
                Intent intenthistory=new Intent(context, history_show.class);
                Intent intent=getActivity().getIntent();
                intenthistory.putExtra("account",intent.getStringExtra("account"));
                startActivity(intenthistory);
                break;
            case R.id.exit:
            case R.id.lexit_account:
                Intent intentlog=new Intent(context, login.class);
                startActivity(intentlog);
                getActivity().finish();
                pref=context.getSharedPreferences("logined",MODE_PRIVATE);
                SharedPreferences.Editor editor =pref.edit();
                editor.clear();
                editor.apply();
                break;
            default:
                break;
        }
    }
    @SuppressLint("Range")
    private void showDetail(String newAccount) {
        UsersDatabaseHelper dbhelp = new UsersDatabaseHelper(context, "Users", null, 1);
        SQLiteDatabase db = dbhelp.getWritableDatabase();
        Cursor cursor = db.query("Users", null, "account=?", new String[]{mParam2}, null, null, null);
        int num = cursor.getCount();//问符合条件值个数
        cursor.moveToFirst();
        if (num > 0) {
            name.setText(cursor.getString(cursor.getColumnIndex("name")));
            account.setText(newAccount);
        }
    }

    private void changeName() {
        Intent intent=new Intent(context, Change_name.class);
        intent.putExtra("account",mParam2);
        intent.putExtra("MineFragID",android.os.Process.myPid());
        startActivity(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    private void startCrop(Uri imageUri) {
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true); // 允许自由裁剪
        options.setCompressionQuality(90); // 设置压缩质量

        // 创建临时文件存储裁剪后的图片
        File cacheDir = context.getCacheDir();
        File destinationFile = new File(cacheDir, "cropped_image_" + System.currentTimeMillis() + ".jpg");

        Intent cropIntent = UCrop.of(imageUri, Uri.fromFile(destinationFile))
                .withAspectRatio(1, 1) // 设置为正方形裁剪
                .withMaxResultSize(512, 512) // 设置最大结果尺寸
                .withOptions(options)
                .getIntent(context);

        cropImageLauncher.launch(cropIntent);
    }

    private void displayCroppedImage(Uri croppedImageUri) {
        Glide.with(this)
                .load(croppedImageUri)
                .circleCrop() // 使用Glide将图像显示为圆形
                .into(headIcon);
    }
}