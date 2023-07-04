package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalIndicator;
import cn.geminis.demo.entity.Material;
import cn.geminis.demo.entity.ParaData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo/material")
@RequiredArgsConstructor
public class MaterialController {

    private final GeminiRepository geminiRepository;

    @PostMapping(path = "/get/{id}")
    public <T> T getOne(@PathVariable String id){
        return (T) this.geminiRepository.findById(Material.class, id);
    }

    @PostMapping
    public Page<Material> findPage(@RequestBody final QueryParameters queryParameters){
        return this.geminiRepository.findPage(Material.class, queryParameters);
    }

    @PutMapping
    public void save(@RequestBody Material object){
        this.geminiRepository.save(object);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> this.geminiRepository.deleteById(Material.class, id));
    }
}
