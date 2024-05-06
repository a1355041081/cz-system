package cn.geminis.demo.eval;

import cn.geminis.demo.entity.EvalObject;
import cn.geminis.demo.entity.EvalObjectCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class ExecuteEntity {
    EvalTaskWithPara evalTaskWithPara;
    HashMap<String, List<DMU>> dmuList;
}
