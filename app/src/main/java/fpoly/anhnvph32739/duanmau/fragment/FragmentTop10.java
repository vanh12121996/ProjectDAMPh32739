package fpoly.anhnvph32739.duanmau.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fpoly.duanmau.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.prefs.Preferences;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.adapter.Top10Adapter;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.PhieuMuonDAO;
import fpoly.anhnvph32739.duanmau.model.Top10;

public class FragmentTop10 extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_top10, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_top10);
        ((MainActivity)getActivity()).toolbar.setTitle("Top 10 sách mượn nhiều nhất");
        ArrayList<Top10> list;
        PhieuMuonDAO phieuMuonDAO = new PhieuMuonDAO(getContext(), new DbHelper(getContext()));
        list = phieuMuonDAO.getTop10();
        Top10Adapter adapter = new Top10Adapter(list, getActivity());



        Collections.sort(list, new Comparator<Top10>() {
            @Override
            public int compare(Top10 o1, Top10 o2) {
                return Integer.compare(o2.getSoLuong(), o1.getSoLuong());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}