package cn.geminis.demo.eval;

import cn.geminis.data.jpa.GeminiRepository;
import cn.geminis.demo.ConfigParameter;
import cn.geminis.demo.service.DEA.DEATaskService;
import cn.geminis.demo.service.ExecuteEval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EvalFactory {
    private final ExecuteEval deaTaskService;

    public ExecuteEval getExecuteEval(String method) {
        switch (method) {
            case ConfigParameter.EvalMethodForDEA:
                return deaTaskService;
            default:
                throw new IllegalArgumentException("Invalid service type: " + method);
        }
    }
}
