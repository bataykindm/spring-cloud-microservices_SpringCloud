package com.javastart.bill.service;

import com.javastart.bill.entity.Bill;
import com.javastart.bill.exception.BillNotFoundException;
import com.javastart.bill.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;

    @Autowired
    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Bill getBillById(Long billID) {
        return billRepository.findById(billID).
                orElseThrow(() -> new BillNotFoundException("Unable to find bill with id: " + billID));
    }

    public Long createBill(Long accountId, BigDecimal amount, Boolean isDefault, Boolean overdraftEnabled) {
        Bill bill = new Bill(accountId, amount, isDefault, overdraftEnabled, OffsetDateTime.now());
        return billRepository.save(bill).getBillId();
    }

    public Bill updateBill(Long billId, Long accountId, BigDecimal amount,
                           Boolean isDefault, Boolean overdraftEnabled, OffsetDateTime creationTime) {
        Bill bill = new Bill(accountId, amount, isDefault, overdraftEnabled, creationTime);
        bill.setBillId(billId);
        return billRepository.save(bill);
    }

    public Bill deleteBill(Long billId) {
        Bill deletedBill = getBillById(billId);
        billRepository.deleteById(billId);
        return deletedBill;
    }

    public List<Bill> getBillsByAccountId(Long accountId) {
        return billRepository.getBillsByAccountId(accountId);
    }
}
