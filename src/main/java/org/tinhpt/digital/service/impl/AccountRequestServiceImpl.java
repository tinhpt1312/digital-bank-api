package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.AccountDTO;
import org.tinhpt.digital.dto.AccountRequestDTO;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.AccountRequest;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.helper.RequestStrategyFactory;
import org.tinhpt.digital.repository.AccountRequestRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.AccountRequestService;
import org.tinhpt.digital.share.Strategy.RequestStrategy;
import org.tinhpt.digital.type.AccountStatus;
import org.tinhpt.digital.type.AccountType;
import org.tinhpt.digital.type.RequestStatus;
import org.tinhpt.digital.type.RequestType;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountRequestServiceImpl implements AccountRequestService {

    private final AccountRequestRepository accountRequestRepository;
    private final RequestStrategyFactory requestStrategyFactory;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BankResponse approveRequest(Long requestId, Long adminId) {
        AccountRequest accountRequest = accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(!accountRequest.getStatus().equals(RequestStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already processed");
        }

        RequestStrategy strategy = requestStrategyFactory.getStrategy(accountRequest.getRequestType());
        if(strategy == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported request type");
        }

        strategy.process(accountRequest, accountRequest.getAccount().getUser().getId());

        accountRequest.setStatus(RequestStatus.APPROVED);
        accountRequest.setApprovedBy(user);
        accountRequest.setApprovedAt(new Date());

        accountRequestRepository.save(accountRequest);

        return BankResponse.builder()
                .responseCode("202")
                .responseMessage(accountRequest.getRequestType() + " is successfully")
                .build();
    }

    @Override
    public List<AccountRequestDTO> getAllRequest(){
        return accountRequestRepository.findAll().stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    private AccountRequestDTO convertToDTO(AccountRequest accountRequest) {
        return AccountRequestDTO.builder()
                .id(accountRequest.getId())
                .requestBy(accountRequest.getRequestedBy().getId())
                .requestType(RequestType.valueOf(String.valueOf(accountRequest.getRequestType())))
                .accountId(accountRequest.getAccount().getId())
                .details(accountRequest.getDetails())
                .build();
    }
}
