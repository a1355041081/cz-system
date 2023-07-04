package cn.geminis.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
public class ConfigParameter {
    private HashMap<Integer, List<String>> quarterToMonth = new HashMap<>();
    public void mapQuarterToMonthAndYear(int year){
        List<String> monthList = new ArrayList<>();
        quarterToMonth.put(1, Arrays.asList(year + "-" + "1", year + "-" + "2", year + "-" + "3"));
        quarterToMonth.put(2, Arrays.asList(year + "-" + "4", year + "-" + "5", year + "-" + "6"));
        quarterToMonth.put(3, Arrays.asList(year + "-" + "7", year + "-" + "8", year + "-" + "9"));
        quarterToMonth.put(4, Arrays.asList(year + "-" + "10", year + "-" + "11", year + "-" + "12"));
    }
    public final static String EvalCycleByMonth = "按月度";
    public final static String EvalCycleByQuarter= "按季度";
    public final static String EvalCycleByYear = "按年度";
    public final static String EvalCycleByMaterial = "按物资";


    public final static String EvalMethodForDEA = "DEA";
}
