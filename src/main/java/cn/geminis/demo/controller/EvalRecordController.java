package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalRecord;
import cn.geminis.demo.entity.EvalTask;
import cn.geminis.demo.entity.Material;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo/record")
@RequiredArgsConstructor
public class EvalRecordController {

    private final GeminiRepository geminiRepository;


    @PostMapping
    public Page<EvalRecord> findPage(@RequestBody final QueryParameters queryParameters){
        return this.geminiRepository.findPage(EvalRecord.class, queryParameters);
    }

    @PutMapping
    public void save(@RequestBody EvalRecord object){
        this.geminiRepository.save(object);
    }

    @DeleteMapping
    public void delete(@RequestBody List<String> ids) {
        ids.forEach(id -> this.geminiRepository.deleteById(EvalRecord.class, id));
    }
}
