package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalObjectCategory;
import cn.geminis.demo.entity.Material;
import cn.geminis.demo.entity.ParaData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo/category")
@RequiredArgsConstructor
public class EvalObjectCategoryController {
    private final GeminiRepository geminiRepository;

    @PostMapping(path = "/get/{id}")
    public <T> T getOne(@PathVariable String id){
        return (T) this.geminiRepository.findById(EvalObjectCategory.class, id);
    }

    @PostMapping
    public Page<EvalObjectCategory> findPage(@RequestBody final QueryParameters queryParameters){
        return this.geminiRepository.findPage(EvalObjectCategory.class, queryParameters);
    }

    @PutMapping
    public void save(@RequestBody EvalObjectCategory object){
        this.geminiRepository.save(object);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> this.geminiRepository.deleteById(EvalObjectCategory.class, id));
    }
}
