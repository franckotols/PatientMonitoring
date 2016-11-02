package com.francesco.patientmonitoring.fragmentParametri;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Vector;

import com.francesco.patientmonitoring.R;
import com.francesco.patientmonitoring.adapters.PagerAdapter;
import com.francesco.patientmonitoring.fragmentParametri.ParametriGraficiFragment;
import com.francesco.patientmonitoring.fragmentParametri.ParametriValoriMediFragment;
import com.francesco.patientmonitoring.fragmentParametri.ParametriValoriPuntualiFragment;

/**
 * Created by Fra on 02/11/2016.
 */
public class ParametriMainFragment extends Fragment {

    ViewPager pager;
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootview = inflater.inflate(R.layout.layout_parametri, container, false);
        List<Fragment> fragments = new Vector<>();
        fragments.add(Fragment.instantiate(getContext(), ParametriValoriMediFragment.class.getName()));
        fragments.add(Fragment.instantiate(getContext(), ParametriValoriPuntualiFragment.class.getName()));
        fragments.add(Fragment.instantiate(getContext(), ParametriGraficiFragment.class.getName()));

        PagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(),fragments);
        pager = (ViewPager)rootview.findViewById(R.id.viewPager_parametri);
        mTabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
        pager.setAdapter(adapter);
        //pager.setOffscreenPageLimit(2);

        if(mTabLayout!=null){
            mTabLayout.setupWithViewPager(pager);
            mTabLayout.getTabAt(0).setText("Valori Medi");
            mTabLayout.getTabAt(1).setText("Valori Puntuali");
            mTabLayout.getTabAt(2).setText("Grafici");
        }


        return rootview;
    }

}
