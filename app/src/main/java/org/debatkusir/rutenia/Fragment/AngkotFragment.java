package org.debatkusir.rutenia.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.debatkusir.rutenia.R;

public class AngkotFragment extends Fragment {

    AutoCompleteTextView termninalAutoComplete;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        termninalAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.terminalAutoCompleteTextView);
        termninalAutoComplete.setAdapter(adapter);

    }

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_angkot, container, false);
    }

}
