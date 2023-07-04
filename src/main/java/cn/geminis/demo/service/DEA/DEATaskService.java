package cn.geminis.demo.service.DEA;

import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.*;
import cn.geminis.demo.eval.DMU;
import cn.geminis.demo.eval.DMUForm;
import cn.geminis.demo.eval.EvalTaskWithPara;
import cn.geminis.demo.service.ExecuteEval;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Setter
@Getter
@Service
@RequiredArgsConstructor
public class DEATaskService implements ExecuteEval {

    private final GeminiRepository geminiRepository;

    public List<DMUForm> getDMUsByTask(EvalTaskWithPara evalTaskWithPara) throws ParseException {
        DEA dea = new DEA();
        dea.setGeminiRepository(geminiRepository);
        dea.setStart(evalTaskWithPara.getStartTime());
        dea.setEnd(evalTaskWithPara.getEndTime());
        dea.setType(evalTaskWithPara.getType());
        dea.setCycle(evalTaskWithPara.getCycle());
        dea.setIndicators(evalTaskWithPara.getCommonIndicators());
        dea.setTaskId(evalTaskWithPara.getTaskId());
        dea.initAllDMUs();
        return dea.getDMUsForm();
    }

    public void executeEvalTask(EvalTaskWithPara evalTaskWithPara, List<DMU> dmus) throws ParseException {
        DEA dea = new DEA();
        dea.setGeminiRepository(geminiRepository);
        dea.setStart(evalTaskWithPara.getStartTime());
        dea.setEnd(evalTaskWithPara.getEndTime());
        dea.setType(evalTaskWithPara.getType());
        dea.setCycle(evalTaskWithPara.getCycle());
        dea.setIndicators(evalTaskWithPara.getCommonIndicators());
        dea.setTaskId(evalTaskWithPara.getTaskId());
        dea.initAllDMUs();

        //dea.retrieveOriginData();
        dea.calEvalDataByAuto();

        dea.setEvalDataByHandwork(dmus);
        dea.execute();
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
