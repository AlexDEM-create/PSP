package com.flacko.appeal;

import com.flacko.appeal.exception.AppealNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final ApplicationContext context;

    @Override
    public Appeal get(String id) throws AppealNotFoundException {
        return appealRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AppealNotFoundException(id));
    }

    @Override
    public AppealBuilder create() {
        return context.getBean(AppealBuilderImpl.class)
                .initializeNew();
    }

}
