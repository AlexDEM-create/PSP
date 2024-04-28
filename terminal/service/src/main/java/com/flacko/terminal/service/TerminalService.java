package com.flacko.terminal.service;

import com.flacko.common.exception.TerminalNotFoundException;

import java.util.List;

public interface TerminalService {

    TerminalBuilder create();

    TerminalBuilder update(String id) throws TerminalNotFoundException;

    List<Terminal> list();

    Terminal get(String id) throws TerminalNotFoundException;

}
