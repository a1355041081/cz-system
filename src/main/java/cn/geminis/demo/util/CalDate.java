package cn.geminis.demo.util;

import cn.geminis.demo.ConfigParameter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalDate {
    Calendar startCal;
    Calendar endCal;

    public CalDate(String start, String end) throws ParseException {
        startCal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = dateFormat.parse(start);
        Date endTime = dateFormat.parse(end);
        startCal.setTime(startTime);
        endCal = Calendar.getInstance();
        endCal.setTime(endTime);
    }

    public List<String> calMonthBetweenStartAndEnd() {
        List<String> allDates = new ArrayList<>();
        while(startCal.before(endCal)) {
            allDates.add(startCal.get(Calendar.YEAR) + "-" + (startCal.get(Calendar.MONTH) + 1));
            startCal.add(Calendar.MONTH, 1);
        }
        //allDates.add(startCal.get(Calendar.YEAR) + "-" + (startCal.get(Calendar.MONTH) + 1));
        return allDates;
    }

    public List<String> calQuarterBetweenStartAndEnd(){
        List<String> allDates = new ArrayList<>();
        while (startCal.before(endCal)) {
            int year = startCal.get(Calendar.YEAR);
            int month = startCal.get(Calendar.MONTH) + 1;
            int quarter = (month - 1) / 3 + 1;
            String dateStr = year + "-Q" + quarter;
            allDates.add(dateStr);
            startCal.add(Calendar.MONTH, 3);
        }
        int year = startCal.get(Calendar.YEAR);
        int month = startCal.get(Calendar.MONTH) + 1;
        int quarter = (month - 1) / 3 + 1;
        String dateStr = year + "-Q" + quarter;
        allDates.add(dateStr);
        return allDates;
//        for (int i = startYear; i<=endYear; i++){
//            ConfigParameter configParameter = new ConfigParameter();
//            configParameter.mapQuarterToMonthAndYear(startYear);
//            if(startYear != endYear){
//                for (int j = startQuarter; j<=4; j++){
//                    allDates.add(configParameter.getQuarterToMonth().get(j));
//                }
//            }else{
//                for (int j = startQuarter; j<=endQuarter; j++){
//                    allDates.add(configParameter.getQuarterToMonth().get(j));
//                }
//            }
//        }
    }
    public List<String> calYearBetweenStartAndEnd(){
        List<String> allDates = new ArrayList<>();
        while (startCal.before(endCal)) {
            int year = startCal.get(Calendar.YEAR);
            String dateStr = String.valueOf(year);
            allDates.add(dateStr);
            startCal.add(Calendar.YEAR, 1);
        }
        int year = startCal.get(Calendar.YEAR);
        String dateStr = String.valueOf(year);
        allDates.add(dateStr);
        return allDates;
    }
}
