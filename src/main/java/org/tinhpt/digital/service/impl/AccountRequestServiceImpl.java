package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.AccountRequestDTO;
import org.tinhpt.digital.dto.PagedResponse;
import org.tinhpt.digital.dto.request.QueryAccountRequestDTO;
import org.tinhpt.digital.dto.request.TransactionRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.entity.Account;
import org.tinhpt.digital.entity.AccountRequest;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.factory.RequestStrategyFactory;
import org.tinhpt.digital.repository.AccountRequestRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.AccountRequestService;
import org.tinhpt.digital.service.TransactionService;
import org.tinhpt.digital.strategy.RequestStrategy;
import org.tinhpt.digital.type.RequestStatus;
import org.tinhpt.digital.type.RequestType;
import org.tinhpt.digital.type.TransactionStatus;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class AccountRequestServiceImpl implements AccountRequestService {

    private final AccountRequestRepository accountRequestRepository;
    private final RequestStrategyFactory requestStrategyFactory;
    private final UserRepository userRepository;
    private final TransactionService transactionService;

    @Override
    @Transactional
    public BankResponse approveRequest(Long requestId, Long adminId) throws Exception {
        AccountRequest accountRequest = getAccountRequestById(requestId);

        User user = getUserById(adminId);

        if(!accountRequest.getStatus().equals(RequestStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already processed");
        }

        RequestStrategy strategy = requestStrategyFactory.getStrategy(RequestType.valueOf(accountRequest.getRequestType()));
        if(strategy == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported request type");
        }

        strategy.processRequest(accountRequest, accountRequest.getAccount().getUser().getId());

        if(strategy.isTransactionRequired()){
            TransactionRequest transactionRequest = strategy.buildTransactionRequest(accountRequest, user.getId());
            if(transactionRequest != null){
                transactionService.createTransaction(transactionRequest, user.getId());
            }
        }

        accountRequest.setStatus(RequestStatus.APPROVED);
        accountRequest.setApprovedBy(user);
        accountRequest.setApprovedAt(new Date());

        accountRequestRepository.save(accountRequest);

        return BankResponse.builder()
                .responseCode("202")
                .responseMessage(accountRequest.getRequestType() + " is successfully")
                .build();
    }

    @Transactional
    @Override
    public BankResponse rejectRequest(Long requestId, Long adminId) throws Exception {
        AccountRequest accountRequest = getAccountRequestById(requestId);

        User user = getUserById(adminId);

        if(!accountRequest.getStatus().equals(RequestStatus.PENDING)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already processed");
        }

        RequestStrategy strategy = requestStrategyFactory.getStrategy(RequestType.valueOf(accountRequest.getRequestType()));
        if(strategy == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported request type");
        }

        if (strategy.isTransactionRequired()) {
            TransactionRequest transactionRequest = strategy.buildTransactionRequest(accountRequest, user.getId());
            if (transactionRequest != null) {
                transactionRequest.setTransactionStatus(TransactionStatus.FAILED);
                transactionService.createTransaction(transactionRequest, user.getId());
            }
        }

        accountRequest.setStatus(RequestStatus.REJECTED);

        return BankResponse.builder()
                .responseCode("404")
                .responseMessage(accountRequest.getRequestType() + " is failed")
                .build();
    }

    private User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private AccountRequest getAccountRequestById(Long requestId){
        return accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
    }

    @Override
    public PagedResponse<AccountRequestDTO> getAllRequest(QueryAccountRequestDTO dto){
        Page<AccountRequest> accountRequests = accountRequestRepository.findAllRequests(PageRequest.of(dto.getPage(), dto.getTake()));

        Page<AccountRequestDTO> accountRequestDTOPage = accountRequests.map(this::convertToDTO);

        return new PagedResponse<>(accountRequestDTOPage);
    }

    private AccountRequestDTO convertToDTO(AccountRequest accountRequest) {
        return AccountRequestDTO.builder()
                .id(accountRequest.getId())
                .requestBy(accountRequest.getRequestedBy().getId())
                .requestType(RequestType.valueOf(String.valueOf(accountRequest.getRequestType())))
                .requestStatus(accountRequest.getStatus())
                .accountId(accountRequest.getAccount().getId())
                .details(accountRequest.getDetails())
                .requestStatus(accountRequest.getStatus())
                .build();
    }
}
