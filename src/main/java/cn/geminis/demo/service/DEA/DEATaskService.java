package cn.geminis.demo.service.DEA;

import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.*;
import cn.geminis.demo.eval.DMU;
import cn.geminis.demo.eval.EvalTaskWithPara;
import cn.geminis.demo.eval.IndicatorForDEA;
import cn.geminis.demo.eval.RecordSet;
import cn.geminis.demo.service.ExecuteEval;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Setter
@Getter
@Service
@RequiredArgsConstructor
public class DEATaskService implements ExecuteEval {
    private final GeminiRepository geminiRepository;
    DEA dea;
    public HashMap<String, List<DMU>> getDMUsByTask(EvalTaskWithPara evalTaskWithPara) throws ParseException {
        DEA dea = new DEA();
        HashMap<String, List<DMU>> dumsByCategory = new HashMap<>();
        dea.setGeminiRepository(geminiRepository);
        dea.setStart(evalTaskWithPara.getStartTime());
        dea.setEnd(evalTaskWithPara.getEndTime());
        dea.setType(evalTaskWithPara.getType());
        dea.setCycle(evalTaskWithPara.getCycle());
        for(EvalObjectCategory category : evalTaskWithPara.getEvalObjectCategory_List()) {
            List<IndicatorForDEA> indicatorForDEAS = new ArrayList<>();
            String categoryId = category.getId();
            Optional<EvalObjectCategory> evalObjectCategory =  this.geminiRepository.findById(EvalObjectCategory.class, categoryId);
            for(EvalIndicator indicator : evalObjectCategory.get().getEvalIndicatorsByCategory()) {
                IndicatorForDEA t = new IndicatorForDEA();
                t.setId(indicator.getId());
                t.setName(indicator.getName());
                t.setType(indicator.getIndicatorType().name());
                indicatorForDEAS.add(t);
            }
            dea.setIndicators(indicatorForDEAS);
            List<EvalObject> objects = new ArrayList<>();
            for(EvalObject object : category.getEvalObjects()) {
                Optional<EvalObject> evalObject =  this.geminiRepository.findById(EvalObject.class, object.getId());
                objects.add(evalObject.get());
            }
            dea.setEvalObjects(objects);
            dea.setDmus(dea.initAllDMUs());
            dea.calEvalDataByAuto();
            dumsByCategory.put(evalObjectCategory.get().getName(), dea.getDmus());
        }

        return dumsByCategory;
    }

    public String executeEvalTask(EvalTaskWithPara evalTaskWithPara, HashMap<String, List<DMU>> dmus) throws ParseException {
        EvalRecord record = null;
        String sRecordId = null;
        if(evalTaskWithPara.getType().equals("多环节")) {
            EvalRecord evalRecord = new EvalRecord();
            evalRecord.setName("全过程评价记录");
            evalRecord.setCycle(evalTaskWithPara.getCycle());
            evalRecord.setMethod("DEA");
            evalRecord.setOperator("测试");
            String pattern = "yyyy-MM";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            evalRecord.setStartTime(dateFormat.parse(evalTaskWithPara.getStartTime()));
            evalRecord.setEndTime(dateFormat.parse(evalTaskWithPara.getEndTime()));
            record = this.geminiRepository.save(evalRecord);
        }
        DEA dea = new DEA();
        dea.setGeminiRepository(geminiRepository);
        dea.setStart(evalTaskWithPara.getStartTime());
        dea.setEnd(evalTaskWithPara.getEndTime());
        dea.setType(evalTaskWithPara.getType());
        dea.setCycle(evalTaskWithPara.getCycle());
        List<EvalObjectCategory> evalObjectCategoryList = evalTaskWithPara.getEvalObjectCategory_List();
        for(int i=0; i<evalObjectCategoryList.size(); i++) {
            List<IndicatorForDEA> indicatorForDEAS = new ArrayList<>();
            List<String> objectIds = new ArrayList<>();
            String categoryId = evalObjectCategoryList.get(i).getId();

            Optional<EvalObjectCategory> evalObjectCategory =  this.geminiRepository.findById(EvalObjectCategory.class, categoryId);
            for(EvalIndicator indicator : evalObjectCategory.get().getEvalIndicatorsByCategory()) {
                IndicatorForDEA t = new IndicatorForDEA();
                t.setId(indicator.getId());
                t.setName(indicator.getName());
                t.setType(indicator.getIndicatorType().name());
                indicatorForDEAS.add(t);
            }
            dea.setIndicators(indicatorForDEAS);
            List<EvalObject> objects = new ArrayList<>();
            for(EvalObject object : evalObjectCategoryList.get(i).getEvalObjects()) {
                Optional<EvalObject> evalObject =  this.geminiRepository.findById(EvalObject.class, object.getId());
                objects.add(evalObject.get());
            }
            dea.setDmus(dmus.get(evalObjectCategory.get().getName()));
            dea.setEvalObjects(objects);
            if(record!=null)
                sRecordId = dea.execute(categoryId, record.getId(), objectIds);
            else sRecordId = dea.execute(categoryId, null, objectIds);

        }
        if(record!=null) {
            record = this.geminiRepository.save(record);
            List<EvalRecord> childEvalRecords = record.getChildRecords();
            HashMap<String, Double> allScore = new HashMap<>();
            for(EvalRecord childEvalRecord : childEvalRecords) {
                for(EvalRecordDetail detail : childEvalRecord.getEvalRecordDetails()) {
                    String key = detail.getType();
                    if(key.equals("OE")) {
                        Double multi = Double.parseDouble(detail.getValue());
                        if(allScore.containsKey(detail.getDmuName())) {
                            Double existingScore = allScore.get(detail.getDmuName());
                            allScore.put(detail.getDmuName(), existingScore * multi);
                        } else {
                            allScore.put(detail.getDmuName(), multi);
                        }
                    }
                }
            }
            List<EvalRecordDetail> details = new ArrayList<>();
            for (Map.Entry<String, Double> entry : allScore.entrySet()) {
                EvalRecordDetail detail = new EvalRecordDetail();
                String key = entry.getKey();
                Double value = entry.getValue();
                detail.setDmuName(key);
                detail.setName(key);
                detail.setType("all_OE");
                detail.setValue(String.valueOf(value));
                detail.setEvalRecord(record);
                details.add(detail);
            }
            record.setEvalRecordDetails(details);
            this.geminiRepository.save(record);
            return record.getId();
        }
        return sRecordId;
    }


    public HashMap<String, List<EvalIndicator>> getEvalIndicator(String taskId) {
        Optional<?> optional = this.geminiRepository.findById(EvalTask.class, taskId);
        HashMap<String, List<EvalIndicator>> evalIndicatorByCategory = new HashMap<>();
        if (optional.isPresent()) {
            EvalTask evalTask = (EvalTask) optional.get();
            List<EvalObject> objects = evalTask.getEvalObjectsByEvalTask();
            Set<String> categorySet = new HashSet<>();

            for (EvalObject object : objects) {
                EvalObject evalObject = this.geminiRepository.findById(EvalObject.class, object.getId()).get();
                categorySet.add(evalObject.getEvalObjectCategory().getId());
            }

            for (String categoryId : categorySet) {
                EvalObjectCategory category = this.geminiRepository.findById(EvalObjectCategory.class, categoryId).get();
                evalIndicatorByCategory.put(category.getName(), category.getEvalIndicatorsByCategory());
            }
        }
        return evalIndicatorByCategory;
    }

}
