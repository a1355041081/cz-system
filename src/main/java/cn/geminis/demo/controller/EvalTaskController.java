package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalTask;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo/task")
@RequiredArgsConstructor
public class EvalTaskController {

    private final GeminiRepository geminiRepository;

    @PostMapping(path = "/get/{id}")
    public <T> T getOne(@PathVariable String id){
        return (T) this.geminiRepository.findById(EvalTask.class, id);
    }

    @PostMapping
    public Page<EvalTask> findPage(@RequestBody final QueryParameters queryParameters){
        return this.geminiRepository.findPage(EvalTask.class, queryParameters);
    }

    @PutMapping
    public void save(@RequestBody EvalTask object){
        this.geminiRepository.save(object);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> this.geminiRepository.deleteById(EvalTask.class, id));
    }
}
