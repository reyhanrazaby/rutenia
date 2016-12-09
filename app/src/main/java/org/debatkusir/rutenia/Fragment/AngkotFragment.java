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
import android.widget.TextView;

import org.debatkusir.rutenia.LocalDatabase;
import org.debatkusir.rutenia.R;
import org.debatkusir.rutenia.Rutenia;

public class AngkotFragment extends Fragment {

    // Instance vairabel untuk field terminal
    AutoCompleteTextView termninalAutoComplete;
    ArrayAdapter<String> terminalAutoCompleteAdapter;
    String[] terminalItem = new String[] {""};
    int terminalId;

    // Instance variabel untuk field angkot
    AutoCompleteTextView angkotAutoComplete;
    ArrayAdapter<String> angkotAutoCompleteAdapter;
    String[] angkotItem = new String[] {""};

    // Inisiasi lokal database
    private static LocalDatabase localDatabase;

    TextView tes;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Casting variable dari XML
        tes = (TextView) view.findViewById(R.id.tesTextView);
        termninalAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.terminalAutoCompleteTextView);

        //Inisiasi local database
        localDatabase = new LocalDatabase();

        termninalAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence input, int start, int before, int count) {
                terminalItem = Rutenia.getLocalDatabase().getTerminal(input.toString());
                //terminalAutoCompleteAdapter.notifyDataSetChanged();
                terminalAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, terminalItem);
                termninalAutoComplete.setAdapter(terminalAutoCompleteAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    String[] arrIdTerminal = localDatabase.getIdTerminal(s.toString());
                    if(arrIdTerminal.length > 0) {
                        terminalId = Integer.parseInt(arrIdTerminal[0]);
                        String[] arrAngkot = localDatabase.getAngkot(terminalId);
                        tes.setText(arrAngkot[0]);
                    } else {
                        tes.setText("Angkot tidak tersedia");
                    }
                }
            }
        });

        angkotAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence input, int start, int before, int count) {
                angkotItem = Rutenia.getLocalDatabase().getTerminal(input.toString());
                angkotAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, terminalItem);
                angkotAutoComplete.setAdapter(terminalAutoCompleteAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    String[] arrIdTerminal = localDatabase.getIdTerminal(s.toString());
                    if(arrIdTerminal.length > 0) {
                        terminalId = Integer.parseInt(arrIdTerminal[0]);
                        String[] arrAngkot = localDatabase.getAngkot(terminalId);
                        tes.setText(arrAngkot[0]);

                    } else {
                        tes.setText("Angkot tidak tersedia");
                    }
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_angkot, container, false);
    }
}
