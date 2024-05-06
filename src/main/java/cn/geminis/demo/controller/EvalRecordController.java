package cn.geminis.demo.controller;

import cn.geminis.core.data.query.QueryParameters;
import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalObject;
import cn.geminis.demo.entity.EvalObjectCategory;
import cn.geminis.demo.entity.EvalRecord;
import cn.geminis.demo.eval.RecordSet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/demo/record")
@RequiredArgsConstructor
public class EvalRecordController {

    private final GeminiRepository geminiRepository;

    @GetMapping(value = "/get/{id}")
    public RecordSet getOne(@PathVariable String id) {
        List<EvalObject> objects = new ArrayList<>();
        EvalRecord record = this.geminiRepository.findById(EvalRecord.class, id).get();
        for(String objectId : record.getObjectIds()) {
            EvalObject object = this.geminiRepository.findById(EvalObject.class, objectId).get();
            objects.add(object);
        }
        RecordSet recordSet = new RecordSet();
        recordSet.setEvalObjects(objects);
        recordSet.setRecord(record);
        return recordSet;
    }

    @PostMapping
    public Page<RecordSet> findPage(@RequestBody final QueryParameters queryParameters){
        Page<EvalRecord> records = this.geminiRepository.findPage(EvalRecord.class, queryParameters);
        List<RecordSet> recordSets = new ArrayList<>();
        for(EvalRecord record : records) {
            List<EvalObject> objects = new ArrayList<>();
            for(String objectId : record.getObjectIds()) {
                EvalObject object = this.geminiRepository.findById(EvalObject.class, objectId).get();
                objects.add(object);
            }
            RecordSet recordSet = new RecordSet();
            recordSet.setEvalObjects(objects);
            recordSet.setRecord(record);
            recordSets.add(recordSet);
        }
        Long count = this.geminiRepository.count(EvalRecord.class, queryParameters);
        Pageable pageable = PageRequest.of(0, recordSets.size());

        return new PageImpl<>(recordSets, pageable, count);
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
