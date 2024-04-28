package com.flacko.terminal.impl;

import com.flacko.terminal.service.Terminal;
import com.flacko.terminal.service.TerminalBuilder;

public interface InitializableTerminalBuilder extends TerminalBuilder {

    TerminalBuilder initializeNew();

    TerminalBuilder initializeExisting(Terminal existingTerminal);

}
