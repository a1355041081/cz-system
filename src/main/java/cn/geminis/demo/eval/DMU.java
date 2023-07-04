package cn.geminis.demo.eval;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class DMU {
    String name;
    HashMap<String, String> input;
    HashMap<String, String> output;
}
