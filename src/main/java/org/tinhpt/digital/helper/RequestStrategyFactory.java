package org.tinhpt.digital.helper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tinhpt.digital.share.Strategy.RequestStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestStrategyFactory {
    private final Map<String, RequestStrategy> strategyMap = new HashMap<>();

    @Autowired
    public RequestStrategyFactory(List<RequestStrategy> strategies){
        for(RequestStrategy strategy: strategies){
            strategyMap.put(strategy.getRequestType(), strategy);
        }
    }

    public RequestStrategy getStrategy(String requestType){
        return strategyMap.get(requestType);
    }
}
