package cn.geminis.demo.service;

import cn.geminis.demo.entity.EvalTask;
import cn.geminis.demo.eval.DMU;
import cn.geminis.demo.eval.EvalTaskWithPara;

import java.text.ParseException;
import java.util.List;

public interface ExecuteEval {
    public void executeEvalTask(EvalTaskWithPara evalTaskWithPara, List<DMU> dmus) throws ParseException;
}
