package com.example.fintar.scheduler;

import com.example.fintar.entity.CustomerDetail;
import com.example.fintar.repository.CustomerDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerPlafondScheduler {

  private final CustomerDetailRepository customerDetailRepository;

  @Scheduled(cron = "0 0 0 1 * ?")
  // @Scheduled(cron = "0 */5 * * * ?")
  @Transactional
  public void resetCustomerPlafonds() {
    log.info("Starting monthly customer plafond reset scheduler...");

    List<CustomerDetail> customerDetails = customerDetailRepository.findAll();
    int updatedCount = 0;

    for (CustomerDetail customer : customerDetails) {
      if (customer.getPlafond() != null && customer.getPlafond().getMaxAmount() != null) {
        customer.setRemainPlafond(customer.getPlafond().getMaxAmount());
        updatedCount++;
      }
    }

    customerDetailRepository.saveAll(customerDetails);
    log.info("Completed monthly customer plafond reset. Updated {} customers.", updatedCount);
  }
}
