package com.sod.securityoperationsdefense.ui.upgrades;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sod.securityoperationsdefense.R;
import com.sod.securityoperationsdefense.ui.gallery.GalleryViewModel;

public class UpgradeListFragment extends Fragment {
    private UpgradeListViewModel upgradeListViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        upgradeListViewModel = new ViewModelProvider(this).get(UpgradeListViewModel.class);
        View root = inflater.inflate(R.layout.upgrade_list, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        upgradeListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public View onUpdate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        upgradeListViewModel = new ViewModelProvider(this).get(UpgradeListViewModel.class);
        View root = inflater.inflate(R.layout.upgrade_list, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        upgradeListViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    /* */
    public void changeList(@NonNull LayoutInflater inflater, ViewGroup container, @IdRes int id)
    {
        /** /
        FrameLayout rootLayout = (FrameLayout)findViewById(android.R.id.content);
        rootLayout.removeViewAt(rootLayout.getChildCount()-1);
        View.inflate(this, R.layout.overlay_layout, rootLayout);
        /** /
        View insertPoint =(View) inflater.inflate(R.layout.upgrade_list, container, false).findViewById(id); // edited.
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        /**/
    }
    /**/

}
