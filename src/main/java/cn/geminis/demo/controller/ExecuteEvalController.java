package cn.geminis.demo.controller;

import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.entity.EvalIndicator;
import cn.geminis.demo.eval.*;
import cn.geminis.demo.service.DEA.DEATaskService;
import cn.geminis.demo.service.ExecuteEval;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo/execute")
@RequiredArgsConstructor
public class ExecuteEvalController {

    private final GeminiRepository geminiRepository;

    private final DEATaskService deaTaskService;

    private final EvalFactory evalFactory;

    @GetMapping(path = "/get/{id}/indicators")
    public Map<String, List<EvalIndicator>> getIndicatorsByEvalTask(@PathVariable String id){
        return deaTaskService.getEvalIndicator(id);
    }

    @PostMapping(path = "/get/dums")
    public List<DMU> getDMUs(@RequestBody EvalTaskWithPara evalTaskWithPara) throws ParseException {
        return deaTaskService.getDMUsByTask(evalTaskWithPara);
    }

    @PostMapping(path = "/set/evalContent")
    public void executeDEATask(@RequestBody ExecuteEntity executeEntity) throws ParseException {
        String method = executeEntity.getEvalTaskWithPara().getMethod();
        ExecuteEval executeEval = evalFactory.getExecuteEval(method);
        executeEval.executeEvalTask(executeEntity.getEvalTaskWithPara(), executeEntity.getDmuList());
    }



//    @PostMapping(path = "/execute/deaByObject")
//    public void executeDeaByObject(@RequestBody EvalTaskWithPara object) throws ParseException {
//        try{
//            //调用DEA执行服务
//            deaTaskService.setStart(object.getStartTime());
//            deaTaskService.setEnd(object.getEndTime());
//            deaTaskService.setType(object.getType());
//            deaTaskService.setEvalIndicators(object.getCommonIndicatorsById());
//            deaTaskService.startByObject();
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            deaTaskService.executeEvalTask(object.getTaskId());
//        }
//    }
//
//    @PostMapping(path = "/execute/deaTask")
//    public void executeDeaEval(@RequestBody ){
//
//    }
}
