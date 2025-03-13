package org.tinhpt.digital.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tinhpt.digital.strategy.TransactionStrategy;
import org.tinhpt.digital.type.TransactionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AccountTransactionFactory {
    private final Map<TransactionType, TransactionStrategy> strategyMap = new HashMap<>();

    @Autowired
    public AccountTransactionFactory(List<TransactionStrategy> strategies){
        for(TransactionStrategy strategy: strategies){
            strategyMap.put(strategy.getTransactionType(), strategy);
        }
    }

    public TransactionStrategy getAccountTransaction(TransactionType transactionType){
        return strategyMap.get(transactionType);
    }

}
