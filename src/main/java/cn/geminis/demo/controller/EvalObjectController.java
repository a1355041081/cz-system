package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalObject;
import cn.geminis.demo.entity.Material;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo/object")
@RequiredArgsConstructor
public class EvalObjectController {

    private final GeminiRepository geminiRepository;

    @PostMapping
    public Page<EvalObject> findPage(@RequestBody final QueryParameters queryParameters){
        return this.geminiRepository.findPage(EvalObject.class, queryParameters);
    }

    @PutMapping
    public void save(@RequestBody EvalObject object){
        this.geminiRepository.save(object);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> this.geminiRepository.deleteById(EvalObject.class, id));
    }
}
