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
import android.widget.Spinner;
import android.widget.TextView;

import org.debatkusir.rutenia.LocalDatabase;
import org.debatkusir.rutenia.R;
import org.debatkusir.rutenia.Rutenia;
import org.w3c.dom.Text;

import static org.debatkusir.rutenia.R.id.nomorAngkotTextView;

public class AngkotFragment extends Fragment {

    // Instance vairabel untuk field terminal
    AutoCompleteTextView termninalAutoComplete;
    ArrayAdapter<String> terminalAutoCompleteAdapter;
    String[] terminalItem = new String[] {""};
    int terminalId;

    // Instance variabel untuk field angkot
    Spinner angkotSpinner;
    ArrayAdapter<String> angkotSpinnerAdapter;
    String[] angkotItem = new String[] {""};

    TextView nomorAngkot;

    // Inisiasi lokal database
    private static LocalDatabase localDatabase;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Casting variable dari XML
        termninalAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.terminalAutoCompleteTextView);
        angkotSpinner = (Spinner) view.findViewById(R.id.angkotSpinner);
        nomorAngkot = (TextView) view.findViewById(nomorAngkotTextView);

        // Inisiasi local database
        localDatabase = new LocalDatabase();

        // Auto complete untuk field terminal
        termninalAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence input, int start, int before, int count) {
                terminalItem = Rutenia.getLocalDatabase().getTerminal(input.toString());
                terminalAutoCompleteAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, terminalItem);
                termninalAutoComplete.setAdapter(terminalAutoCompleteAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    String[] arrIdTerminal = localDatabase.getIdTerminal(s.toString());
                    if(arrIdTerminal.length > 0) {
                        terminalId = Integer.parseInt(arrIdTerminal[0]);
                        angkotItem = Rutenia.getLocalDatabase().getAngkot(terminalId);
                        if(angkotItem.length > 0) {
                            angkotSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, angkotItem);
                            angkotSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            angkotSpinner.setAdapter(angkotSpinnerAdapter);
                        } else {
                            angkotSpinner.setAdapter(null);
                        }
                    } else {
                        angkotSpinner.setAdapter(null);
                    }
                } else {
                    angkotSpinner.setAdapter(null);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_angkot, container, false);
    }
}
