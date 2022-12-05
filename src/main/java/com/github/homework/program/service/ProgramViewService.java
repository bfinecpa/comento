package com.github.homework.program.service;

import com.github.homework.program.domain.Program;
import com.github.homework.program.model.ProgramViewDetailDto;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgramViewService {
    private final ProgramRepository programRepository;
    private final ViewCount viewCount;

    @Transactional
    public List<ProgramViewDto> getByViews(){
        viewCount.getFieldViews().entrySet().stream()
                .forEach(entry -> {
                    programRepository.updateViews(entry.getKey(), entry.getValue());
                    ResetViews(entry.getKey());
                });

        List<Program> views = programRepository.findTop10ByOrderByViewsDesc();
        return views.stream().map(program -> {
                    return new ProgramViewDto(
                            program.getId(),
                            program.getName(),
                            program.getTheme().getName(),
                            program.getViews());
                }).collect(Collectors.toList());
    }


    public Optional<ProgramViewDetailDto> getBy(Long id) {
        Optional<Program> byId = programRepository.findById(id);
        return byId.map(program -> {
            viewCount.increaseView(id);
            return new ProgramViewDetailDto(
                    program.getId(),
                    program.getName(),
                    program.getIntroduction(),
                    program.getIntroductionDetail(),
                    program.getRegion(),
                    program.getTheme().getName(),
                    program.getViews() + viewCount.getViews(id));
        }
       );
    }


    public void ResetViews(Long id) {
        viewCount.setViews(id, 0);
    }



    public Optional<ProgramViewDetailDto> getByName(String name) {
        Optional<Program> byName = programRepository.findByName(name);
        return byName.map(program -> new ProgramViewDetailDto(
                program.getId(),
                program.getName(),
                program.getIntroduction(),
                program.getIntroductionDetail(),
                program.getRegion(),
                program.getTheme().getName(),
                program.getViews()+viewCount.getViews(program.getId())
        ));
    }


    public Page<ProgramViewDto> pageBy(Pageable pageable) {
        return programRepository.findBy(pageable);
    }

}
