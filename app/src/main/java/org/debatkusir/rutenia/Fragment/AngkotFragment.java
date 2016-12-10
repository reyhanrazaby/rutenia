package org.debatkusir.rutenia.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.debatkusir.rutenia.LocalDatabase;
import org.debatkusir.rutenia.R;
import org.debatkusir.rutenia.Rutenia;
import org.w3c.dom.Text;

import java.util.Arrays;

import static org.debatkusir.rutenia.R.id.angkotSpinner;
import static org.debatkusir.rutenia.R.id.cariButton;
import static org.debatkusir.rutenia.R.id.nomorAngkotTextView;
import static org.debatkusir.rutenia.R.id.terminalAutoCompleteTextView;

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

    // Instance variabel untuk hasil pencarian
    CardView hasilPencarian;
    Button tombolCari;
    TextView nomorAngkot;
    TextView ruteAngkot;
    ImageView gambarAngkot;

    // Inisiasi lokal database
    private static LocalDatabase localDatabase;

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Casting variable dari XML
        termninalAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.terminalAutoCompleteTextView);
        angkotSpinner = (Spinner) view.findViewById(R.id.angkotSpinner);
        nomorAngkot = (TextView) view.findViewById(nomorAngkotTextView);
        tombolCari = (Button) view.findViewById(R.id.cariButton);
        nomorAngkot = (TextView) view.findViewById(R.id.nomorAngkotTextView);
        ruteAngkot = (TextView) view.findViewById(R.id.ruteAngkotTextView);
        gambarAngkot = (ImageView) view.findViewById(R.id.gambarAngkotImageView);
        hasilPencarian = (CardView) view.findViewById(R.id.hasilPencarianCardView);

        // Inisiasi local database
        localDatabase = new LocalDatabase();

        //Hide card view hasil pencarian saat start
        hasilPencarian.setVisibility(View.GONE);

        // Auto complete untuk pengisian field
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
                        angkotItem = Rutenia.getLocalDatabase().getAngkotByTerminalId(terminalId);
                        if(angkotItem.length > 0) {
                            Arrays.sort(angkotItem);
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
                    hasilPencarian.setVisibility(View.GONE);
                }
            }
        });
        tombolCari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cariButton:
                        if(termninalAutoComplete.getText().toString().length() > 0 && angkotSpinner.getSelectedItem().toString().length() > 0) {
                            slideToTop(hasilPencarian);
                            String[] arrTrayek = localDatabase.getTrayek(angkotSpinner.getSelectedItem().toString());
                            String[] arrTerminal1 = localDatabase.getTerminalbyTerminalId(arrTrayek[0]);
                            String[] arrTerminal2 = localDatabase.getTerminalbyTerminalId(arrTrayek[1]);
                            String[] arrGambarAngkot = localDatabase.getGambarAngkot(angkotSpinner.getSelectedItem().toString());
                            nomorAngkot.setText(angkotSpinner.getSelectedItem().toString());
                            ruteAngkot.setText(arrTerminal1[0].toString() + " - " + arrTerminal2[0].toString());
                            gambarAngkot.setImageResource(getResources().getIdentifier(arrGambarAngkot[0].toString(), "mipmap", getActivity().getPackageName()));
                        }
                }
            }
        });
    }

    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(-view.getHeight(),0,0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_angkot, container, false);
    }
}
