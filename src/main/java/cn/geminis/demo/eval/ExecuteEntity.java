package cn.geminis.demo.eval;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExecuteEntity {
    EvalTaskWithPara evalTaskWithPara;
    List<DMU> dmuList;
}
