package com.finzly.bharat_bijili_co.bill_payment_platform.service.wallet;

import com.finzly.bharat_bijili_co.bill_payment_platform.dto.request.WalletPaymentRequest;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.CustomerNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.InsufficientWalletBalanceException;
import com.finzly.bharat_bijili_co.bill_payment_platform.exception.WalletNotFoundException;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Customer;
import com.finzly.bharat_bijili_co.bill_payment_platform.model.Wallet;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.customer.CustomerRepository;
import com.finzly.bharat_bijili_co.bill_payment_platform.repository.wallet.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    CustomerRepository customerRepository;

    WalletService(WalletRepository walletRepository, CustomerRepository customerRepository) {
        this.walletRepository = walletRepository;
        this.customerRepository = customerRepository;
    }

    public Wallet payUsingWallet(WalletPaymentRequest walletPaymentRequest) {
        Customer customer = customerRepository.findByCustomerId(walletPaymentRequest.getCustomerId()).
                orElseThrow(() -> new CustomerNotFoundException("Customer not found with customer Id " + walletPaymentRequest.getCustomerId()));


        Wallet wallet = walletRepository.findByCustomer(customer).
                orElseThrow(()-> new WalletNotFoundException("Wallet not found with customer id: " + customer));

        BigDecimal amount = BigDecimal.valueOf(walletPaymentRequest.getAmount());
        if(wallet.getBalance().compareTo(amount) < 0){
            throw new InsufficientWalletBalanceException("Wallet Balance is insufficient to pay bill : " + wallet.getBalance());
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

//        RecordPaymentRequest recordPaymentRequest = RecordPaymentRequest.builder()
//                .billId(walletPaymentRequest.getBillId())
//                .amount(walletPaymentRequest.getAmount())
//                .paymentMethod(walletPaymentRequest.getPaymentMethod())
//                .paymentStatus(walletPaymentRequest.getPaymentStatus())
//                .txnRefId(UUID.randomUUID().toString())
//                .build();


        return wallet;
    }

    public Wallet getWalletBalance(String customerId) {
        Wallet wallet = walletRepository.findByCustomerId(customerId).orElseThrow(()->
                new WalletNotFoundException("Wallet Not found with customer Id " + customerId)
        );
        return wallet;
    }
}
