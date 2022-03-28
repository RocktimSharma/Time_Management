package com.tuinite.timemanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.recycler_adapters.RoutineRecyclerAdapter;
import com.tuinite.timemanagement.routines.AddRoutine;
import com.tuinite.timemanagement.viewmodels.RoutineViewmodel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView routine_recyclerView;

    public RoutineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoutineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoutineFragment newInstance(String param1, String param2) {
        RoutineFragment fragment = new RoutineFragment();
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.routine_fragment, container, false);

        FloatingActionButton addRoutine_fab = view.findViewById(R.id.routine_frag_add_routine_fab);

        addRoutine_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddRoutine.class));
            }
        });

        routine_recyclerView = view.findViewById(R.id.routine_frag_recycler_view2);
        routine_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        routine_recyclerView.setHasFixedSize(true);

        RoutineRecyclerAdapter routineRecyclerAdapter = new RoutineRecyclerAdapter();
        routine_recyclerView.setAdapter(routineRecyclerAdapter);

        RoutineViewmodel routineViewmodel = ViewModelProviders.of((FragmentActivity) getContext()).get(RoutineViewmodel.class);

        try {
            routineViewmodel.getRoutine().observe((LifecycleOwner) getContext(), new Observer<List<Routine>>() {
                @Override
                public void onChanged(List<Routine> routines) {
                    routineRecyclerAdapter.setRoutines(routines);
                    Log.i("ROUTINE_RETIVAL", "success");
                }
            });
        } catch (Exception e) {
            Log.i("ROUTINE_RETIVAL", "Failed" + e);
        }

        return view;
    }
}






     /*   ImageView dropbox_imv=(ImageView)view.findViewById(R.id.routine_drop_box_imv);
        TextView epmty_tv=(TextView)view.findViewById(R.id.routine_empty_tv);

        RecyclerView routineRecyclerView=(RecyclerView)view.findViewById(R.id.routine_recycler);
        routineRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        routineRecyclerView.setHasFixedSize(true);

        RoutineRecyclerAdapter routineRecyclerAdapter=new RoutineRecyclerAdapter();
        routineRecyclerView.setAdapter(routineRecyclerAdapter);

        RoutineViewmodel routineViewmodel= ViewModelProviders.of(getActivity()).get(RoutineViewmodel.class);


        try{
            //Reteiving data from routineItems
            routineViewmodel.getRoutinewithRoutineItems().observe((LifecycleOwner) getContext(), new Observer<List<RoutineWithRoutineItem>>() {

                @Override
                public void onChanged(List<RoutineWithRoutineItem> routineWithRoutineItems) {
                    //passing data to recycler adapter
                    routineRecyclerAdapter.setRoutineWithRoutineItems(routineWithRoutineItems);
                }
            });

            //Reteiving data from routine
            routineViewmodel.getRoutine().observe((LifecycleOwner) getContext(), new Observer<List<Routine>>() {
                @Override
                public void onChanged(List<Routine> routines) {
                    if(routines.isEmpty()){
                        routineRecyclerView.setVisibility(GONE);
                        dropbox_imv.setVisibility(View.VISIBLE);
                        epmty_tv.setVisibility(View.VISIBLE);
                    }else{
                        routineRecyclerView.setVisibility(View.VISIBLE);
                        dropbox_imv.setVisibility(GONE);
                        epmty_tv.setVisibility(GONE);
                        routineRecyclerAdapter.setRoutines(routines);
                    }
                }
            });

        }catch (Exception e){
            Log.i("Size Failed",String.valueOf(e));
        }


        FloatingActionButton addRoutine=(FloatingActionButton)view.findViewById(R.id.add_routine_fab);
        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigating to add routine activity
                startActivity(new Intent(getActivity().getApplicationContext(),AddRoutine.class));
            }
        });*/