package cn.geminis.demo.service.DEA;

import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.ConfigParameter;
import cn.geminis.demo.entity.*;
import cn.geminis.demo.eval.DMU;
import cn.geminis.demo.eval.DMUForm;
import cn.geminis.demo.eval.EvalResult;
import cn.geminis.demo.eval.IndicatorForDEA;
import cn.geminis.demo.util.CalDate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
public class DEA{
    private String start;
    private String end;
    private String type;
    private String cycle;
    private String taskId;

    private List<IndicatorForDEA> indicators = new ArrayList<>(); //经过进一步筛选后的指标
    private Map<String, Map<String, List<String>>> evalOriginData = new HashMap<>(); // 合并所获取的数据集，key: 表名、value: List值列表
    //private Map<HashMap<String, String>, HashMap<String, String>> evalData = new HashMap<>();
    private List<DMU> dmus = new ArrayList<>();

    private GeminiRepository geminiRepository;





//    public void retrieveOriginData(){
//        // 将待评价指标中的参数数据按获取url分组，获取多个数据表?
//        // 将时间、对象等参数传入计算对应的参数值，并存储于evalData   至少包含名称、所属对象名称、数值
//        // 根据对象名称和数值计算出指标的数值 存储于其他列表中
//        try{
//            List<String> urls = getCommonUrls();
//            //假设以RestTemplate发送请求
//            for (String url : urls) {
//                Map<String,String> map = new HashMap<>();
//                map.put("startTime",start);
//                map.put("endTime",end);
//                String response = restTemplate.getForObject(url + "?startTime={startTime}&endTime={endTime}", String.class, map);
//                ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode jsonNode = objectMapper.readTree(response);
//
//                Iterator<Map.Entry<String,JsonNode>> fieldsIterator = jsonNode.fields();
//
//                Map<String, List<String>> temp = new HashMap<>();
//                while (fieldsIterator.hasNext()){
//                    Map.Entry<String,JsonNode> field = fieldsIterator.next();
//                    String fieldName = field.getKey();
//                    JsonNode fieldValue = field.getValue();
//                    List<String> fieldValueList = new ArrayList<>();
//                    if (fieldValue.isArray()) {
//                        for (JsonNode node : fieldValue) {
//                            fieldValueList.add(node.asText());
//                        }
//                    } else {
//                        fieldValueList.add(fieldValue.asText());
//                    }
//                    temp.put(fieldName, fieldValueList);
//                }
//                evalOriginData.put(url, temp);
//            }
//
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        finally {
//
//        }
//    }

    public void calEvalDataByAuto(){
        for(IndicatorForDEA indicator : indicators){
            EvalIndicator ind = this.geminiRepository.findById(EvalIndicator.class, indicator.getId()).get();
            if(ind.getDataSource().equals("database")){
                // 从原始数据集中获取计算
            }
        }
    }

    public void setEvalDataByHandwork(List<DMU> ds){
        for (DMU d : ds) {
            for (DMU dmu : dmus) {
                if (d.getName().equals(dmu.getName())) {
                    for (String key : d.getInput().keySet()) {
                        dmu.getInput().put(key, d.getInput().get(key));
                    }
                    for (String key : d.getOutput().keySet()) {
                        dmu.getOutput().put(key, d.getOutput().get(key));
                    }
                    break;
                }
            }
        }
    }

    public void initAllDMUs() throws ParseException {
        if (cycle.equals(ConfigParameter.EvalCycleByMaterial)) {
            EvalTask evalTask = new EvalTask();
            Optional<?> optional = this.geminiRepository.findById(EvalTask.class, taskId);
            if (optional.isPresent()) evalTask = (EvalTask) optional.get();
            for (EvalObject object : evalTask.getEvalObjectsByEvalTask()) {
                DMU dmu = new DMU();
                dmu.setName(object.getName());
                for (IndicatorForDEA indi : indicators) {
                    HashMap<String, String> t = new HashMap<>();
                    if (indi.getType().equals("input")) {
                        t.put(indi.getName(), "");
                        dmu.setInput(t);
                    }else if(indi.getType().equals("output")) {
                        t.put(indi.getName(), "");
                        dmu.setOutput(t);
                    }
                }
                dmus.add(dmu);
            }
        }else {
            CalDate calDate = new CalDate(start, end);
            List<String> allDMUs = new ArrayList<>();
            switch (cycle){
                case "按月度":
                    allDMUs = calDate.calMonthBetweenStartAndEnd();
                    break;
                case "按季度":
                    allDMUs = calDate.calQuarterBetweenStartAndEnd();
                    break;
                case "按年度":
                    allDMUs = calDate.calYearBetweenStartAndEnd();
                    break;
            }
            for (String dName : allDMUs) {
                DMU dmu = new DMU();
                dmu.setName(dName);
                for (IndicatorForDEA indi : indicators) {
                    HashMap<String, String> t = new HashMap<>();
                    if (indi.getType().equals("input")) {
                        t.put(indi.getName(), "");
                        dmu.setInput(t);
                    }else if(indi.getType().equals("output")) {
                        t.put(indi.getName(), "");
                        dmu.setOutput(t);
                    }
                }
                dmus.add(dmu);
            }
        }
    }

    public void singleRoundTask() throws ParseException {
        //单环节DEA算法， 结果存储在record中
        List<EvalResult> result = new ArrayList<>();
        try {

            int m1 = dmus.get(0).getInput().size();
            int m2 = dmus.get(0).getOutput().size();

            int k = 0;
            //double e = 1.0;
            double e = Math.pow(10, -6);
            for (DMU dmu : dmus) {
                IloCplex cplex = new IloCplex();
                IloNumVar OE = cplex.numVar(-Double.MAX_VALUE, Double.MAX_VALUE, "OE");
                IloNumVar[] lambdas = cplex.numVarArray(dmus.size(), 0, Double.MAX_VALUE);
                IloNumVar[] sNegitive = cplex.numVarArray(m1, 0, Double.MAX_VALUE);
                IloNumVar[] sPositive = cplex.numVarArray(m2, 0, Double.MAX_VALUE);

                IloLinearNumExpr object1 = cplex.linearNumExpr();
                object1.addTerm(1.0, OE);

//                int length = 0;
//                //define e
//                for (String key : dmu.getInput().keySet()) {
//                    int t = 0;
//                    for (DMU d : dmus) {
//                        MathCal mathCal = new MathCal();
//                        double[] input = new double[dmus.size()];
//                        input[t++] = Double.parseDouble(d.getInput().get(key));
//                        length = mathCal.getMaxLength(input);
//                    }
//                }
//                double e = Math.pow(10, -length);

                for (int i = 0; i < m1; i++) {
                    object1.addTerm(-e, sNegitive[i]);
                }
//                for (String key : dmu.getOutput().keySet()) {
//                    int t = 0;
//                    for (DMU d : dmus) {
//                        MathCal mathCal = new MathCal();
//                        double[] input = new double[dmus.size()];
//                        input[t++] = Double.parseDouble(d.getOutput().get(key));
//                        length = mathCal.getMaxLength(input);
//                    }
//                }
//                e = Math.pow(10, -length);

                for (int i = 0; i < m2; i++) {
                    object1.addTerm(-e, sPositive[i]);
                }
                cplex.addMinimize(object1);

                // Define constraints
                int j = 0;
                for (String key : dmu.getInput().keySet()) {
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int i = 0; i < dmus.size(); i++) {
                        expr.addTerm(lambdas[i], Double.parseDouble(dmus.get(i).getInput().get(key)));
                    }
                    expr.addTerm(OE, -Double.parseDouble(dmus.get(k).getInput().get(key)));
                    expr.addTerm(1.0, sNegitive[j]);
                    cplex.addEq(expr, 0);
                    j++;
                }
                int l = 0;
                for (String key : dmu.getOutput().keySet()) {
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int i = 0; i < dmus.size(); i++) {
                        expr.addTerm(lambdas[i], Double.parseDouble(dmus.get(i).getOutput().get(key)));
                    }
                    expr.addTerm(-1.0, sPositive[l]);
                    cplex.addEq(expr, Double.parseDouble(dmus.get(k).getOutput().get(key)));
                    l++;
                }
                if (cplex.solve()) {
                    EvalResult evalResult = new EvalResult();
                    evalResult.setDmu(dmu);
                    evalResult.setResult(cplex.getObjValue());
                    result.add(evalResult);
                    System.out.println("Solution : " + dmu.getName() + " value = " + cplex.getObjValue());
                    for (int m = 0; m < m1; m++) {
                        double value = cplex.getValue(sNegitive[m]);
                        System.out.println("sNegitive : " + value);
                    }
                    for (int m = 0; m < m2; m++) {
                        double value = cplex.getValue(sPositive[m]);
                        System.out.println("sPositive : " + value);
                    }
                }
                cplex.end();
                k++;
            }
        }catch (IloException e) {
            e.printStackTrace();
        } finally {
            setEvalRecord(result);
        }
    }

    private void setEvalRecord(List<EvalResult> evalRecords) throws ParseException {
        EvalRecord evalRecord = new EvalRecord();
        EvalTask evalTask = this.geminiRepository.findById(EvalTask.class, taskId).get();
        evalRecord.setEvalTask(evalTask);
        evalRecord.setName(evalTask.getName()+"的评价记录");
        evalRecord.setCycle(cycle);
        evalRecord.setMethod("DEA");
        evalRecord.setOperator("测试");
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        evalRecord.setStartTime(dateFormat.parse(start));
        evalRecord.setEndTime(dateFormat.parse(end));
        this.geminiRepository.save(evalRecord);
        //将evalRecords结果保存到detail中
    }

    public List<String> getCommonUrls() {
        Set<ParaData> paraDataSet = new HashSet<>();
        for (IndicatorForDEA indicator : indicators) {
            EvalIndicator evalIndicator = this.geminiRepository.findById(EvalIndicator.class, indicator.getId()).get();
            paraDataSet.addAll(evalIndicator.getParaDatas());
        }

        Set<String> accessUrlSet = new HashSet<>();

        for (ParaData paraData : paraDataSet) {
            accessUrlSet.add(paraData.getAccessUrl());
        }
        return new ArrayList<>(accessUrlSet);
    }

    public void execute() throws ParseException {
        if (type.equals("单环节")) {
            singleRoundTask();
        } else if (type.equals("多环节")) {

        }
    }

    public List<DMUForm> getDMUsForm() {
        List<DMUForm> dmuForms = new ArrayList<>();
        for (DMU dmu : dmus) {
            DMUForm dmuForm = new DMUForm();
            for (IndicatorForDEA indicator : indicators) {
                EvalIndicator ind = this.geminiRepository.findById(EvalIndicator.class, indicator.getId()).get();
                dmuForm.setIndicator(ind.getName());
                Boolean disable = ind.getDataSource().equals("dataSource");
                dmuForm.setDisable(disable);
            }
            dmuForms.add(dmuForm);
        }
        return dmuForms;
    }
}