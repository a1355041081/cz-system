package cn.geminis.demo.eval;

import cn.geminis.demo.entity.EvalIndicator;
import cn.geminis.demo.entity.EvalTask;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EvalTaskWithPara {
    String taskId;
    String startTime;
    String endTime;
    String type;
    String cycle;
    String method;
    List<IndicatorForDEA> commonIndicators = new ArrayList<>();
}
