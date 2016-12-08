package org.debatkusir.rutenia.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.debatkusir.rutenia.R;
import org.debatkusir.rutenia.Rutenia;

public class AngkotFragment extends Fragment {

    AutoCompleteTextView termninalAutoComplete;
    ArrayAdapter<String> terminalAutoCompleteAdapter;
    String[] terminalItem = new String[] {""};

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        terminalAutoCompleteAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_dropdown_item_1line, terminalItem);
        termninalAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.terminalAutoCompleteTextView);
        termninalAutoComplete.setAdapter(terminalAutoCompleteAdapter);

        termninalAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence input, int start, int before, int count) {
                terminalItem = Rutenia.getLocalDatabase().getTerminal(input.toString());
                terminalAutoCompleteAdapter.notifyDataSetChanged();
                terminalAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, terminalItem);
                termninalAutoComplete.setAdapter(terminalAutoCompleteAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_angkot, container, false);
    }

}
