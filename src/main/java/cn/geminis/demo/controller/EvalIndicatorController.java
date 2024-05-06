package cn.geminis.demo.controller;

import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalIndicator;
import cn.geminis.demo.entity.EvalObjectCategory;
import cn.geminis.demo.entity.ParaData;
import lombok.RequiredArgsConstructor;
import cn.geminis.core.data.query.QueryParameters;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/demo/indicator")
@RequiredArgsConstructor //为nonnull或final修饰符属性生成构造函数
public class EvalIndicatorController {

    private final GeminiRepository geminiRepository;

    @PostMapping(path = "/get/{id}")
    public <T> T getOne(@PathVariable String id){
        EvalIndicator evalIndicator =  this.geminiRepository.findById(EvalIndicator.class, id).get();
        evalIndicator.getParaDatas();
        return (T) evalIndicator;
    }

    @PostMapping
    public Page<EvalIndicator> findPage(@RequestBody final QueryParameters queryParameters){
        return this.geminiRepository.findPage(EvalIndicator.class, queryParameters);
    }

    @PutMapping
    public void save(@RequestBody EvalIndicator object){
        this.geminiRepository.save(object);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> this.geminiRepository.deleteById(EvalIndicator.class, id));
    }
}
