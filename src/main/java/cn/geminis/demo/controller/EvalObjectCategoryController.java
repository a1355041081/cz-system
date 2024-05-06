package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/demo/category")
@RequiredArgsConstructor
public class EvalObjectCategoryController {
    private final GeminiRepository geminiRepository;

    @GetMapping(path = "/get/{id}/getEvalObjects")
    public List<EvalObject> getOne(@PathVariable String id){
        Optional<EvalObjectCategory> category =  this.geminiRepository.findById(EvalObjectCategory.class, id);
        return category.get().getEvalObjects();
    }
//    @GetMapping(path = "/get/{id}/getEvalIndicator")
//    public HashMap<String, List<EvalIndicator>> getOne(@PathVariable String id){
//        Optional<EvalObjectCategory> category =  this.geminiRepository.findById(EvalObjectCategory.class, id);
//        return category.get().getEvalObjects();
//    }

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
