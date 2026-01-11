package com.example.mobileapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.mobileapp.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

//        ViewCompat.setOnApplyWindowInsetsListener(
//                view.findViewById(R.id.scroll),
//                (v, insets) -> {
//                    Insets systemBars = insets.getInsets(
//                            WindowInsetsCompat.Type.systemBars()
//                    );
//                    v.setPadding(
//                            systemBars.left,
//                            systemBars.top,
//                            systemBars.right,
//                            systemBars.bottom
//                    );
//                    return insets;
//                }
//        );

        ((EditText) view.findViewById(R.id.et_first_name))
                .setText("Andrew");
        ((EditText) view.findViewById(R.id.et_last_name))
                .setText("Wilson");
        ((EditText) view.findViewById(R.id.et_email))
                .setText("andrewwilson@email.com");
        ((EditText) view.findViewById(R.id.et_address))
                .setText("Bradford");
        ((EditText) view.findViewById(R.id.et_phone))
                .setText("065 123 1233");

        ((TextView) view.findViewById(R.id.tv_full_name))
                .setText("Andrew Wilson");
        ((TextView) view.findViewById(R.id.tv_email))
                .setText("andrewwilson@email.com");

        return view;
    }
}