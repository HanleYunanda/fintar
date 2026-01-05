package com.example.fintar.mapper;

import com.example.fintar.dto.LoanStatusHistoryResponse;
import com.example.fintar.entity.LoanStatusHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanStatusHistoryMapper {

  private final UserMapper userMapper;

  public LoanStatusHistoryResponse toResponse(LoanStatusHistory loanStatusHistory) {
    return LoanStatusHistoryResponse.builder()
        .id(loanStatusHistory.getId())
        .action(loanStatusHistory.getAction())
        .note(loanStatusHistory.getNote())
        .performedBy(userMapper.toResponse(loanStatusHistory.getPerformedBy()))
        .performedAt(loanStatusHistory.getPerformedAt())
        .build();
  }

  public List<LoanStatusHistoryResponse> toResponseList(
      List<LoanStatusHistory> loanStatusHistories) {
    return loanStatusHistories.stream().map(this::toResponse).toList();
  }
}
