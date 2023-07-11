package cn.geminis.demo.eval;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class DMU implements Cloneable {
    String name;
    HashMap<String, String> input;
    HashMap<String, String> output;

    public Object clone() throws CloneNotSupportedException {
        Object obj = super.clone();
        HashMap<String, String> a = ((DMU) obj).getInput();
        HashMap<String, String> b = ((DMU) obj).getOutput();
        ((DMU) obj).setInput((HashMap<String, String>) a.clone());
        ((DMU) obj).setOutput((HashMap<String, String>) b.clone());
        return obj;

    }
}
