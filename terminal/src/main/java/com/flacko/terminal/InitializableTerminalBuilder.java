package com.flacko.terminal;

public interface InitializableTerminalBuilder extends TerminalBuilder {

    TerminalBuilder initializeNew();

    TerminalBuilder initializeExisting(Terminal existingTerminal);

}
