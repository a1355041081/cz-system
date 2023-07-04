package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalIndicator;
import cn.geminis.demo.entity.ParaData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo/parameter")
@RequiredArgsConstructor
public class ParaDataController {

    private final GeminiRepository geminiRepository;

    @PostMapping
    public Page<ParaData> findPage(@RequestBody final QueryParameters queryParameters){
        return this.geminiRepository.findPage(ParaData.class, queryParameters);
    }

    @PutMapping
    public void save(@RequestBody ParaData object){
        this.geminiRepository.save(object);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> this.geminiRepository.deleteById(ParaData.class, id));
    }
}
