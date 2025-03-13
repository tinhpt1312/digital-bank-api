package org.tinhpt.digital.factory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tinhpt.digital.strategy.RequestStrategy;
import org.tinhpt.digital.type.RequestType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestStrategyFactory {
    private final Map<RequestType, RequestStrategy> strategyMap = new HashMap<>();

    @Autowired
    public RequestStrategyFactory(List<RequestStrategy> strategies){
        for(RequestStrategy strategy: strategies){
            strategyMap.put(strategy.getRequestType(), strategy);
        }
    }

    public RequestStrategy getStrategy(RequestType requestType){
        return strategyMap.get(requestType);
    }
}
