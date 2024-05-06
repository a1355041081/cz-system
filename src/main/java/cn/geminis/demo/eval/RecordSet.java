package cn.geminis.demo.eval;

import cn.geminis.demo.entity.EvalObject;
import cn.geminis.demo.entity.EvalRecord;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class RecordSet {
    EvalRecord record;
    List<EvalObject> evalObjects;
}
