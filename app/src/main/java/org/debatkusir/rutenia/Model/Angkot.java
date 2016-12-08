package org.debatkusir.rutenia.Model;

/**
 * Created by Reyhan on 12/7/2016.
 */

public class Angkot {
    String id;
    String name;
    Terminal terminalStart;
    Terminal terminalEnd;

    public Angkot(String id, String name, Terminal terminalEnd, Terminal terminalStart) {
        this.id = id;
        this.name = name;
        this.terminalEnd = terminalEnd;
        this.terminalStart = terminalStart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Terminal getTerminalEnd() {
        return terminalEnd;
    }

    public void setTerminalEnd(Terminal terminalEnd) {
        this.terminalEnd = terminalEnd;
    }

    public Terminal getTerminalStart() {
        return terminalStart;
    }

    public void setTerminalStart(Terminal terminalStart) {
        this.terminalStart = terminalStart;
    }
}
