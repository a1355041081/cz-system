package cn.geminis.demo.service;

import cn.geminis.demo.entity.EvalObject;
import cn.geminis.demo.entity.EvalObjectCategory;
import cn.geminis.demo.entity.EvalTask;
import cn.geminis.demo.eval.DMU;
import cn.geminis.demo.eval.EvalTaskWithPara;
import cn.geminis.demo.eval.RecordSet;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface ExecuteEval {
    public String executeEvalTask(EvalTaskWithPara evalTaskWithPara, HashMap<String, List<DMU>> dmus) throws ParseException;
}
