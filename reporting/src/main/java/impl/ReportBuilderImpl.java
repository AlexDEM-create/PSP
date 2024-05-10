import com.flacko.report.service.dto.ReportRequest;
import com.flacko.report.service.dto.ReportResponse;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ReportBuilderImpl {

    public ReportRequest buildReportRequest(String merchantId, LocalDateTime startDate, LocalDateTime endDate) {
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setMerchantId(merchantId);
        reportRequest.setStartDate(startDate);
        reportRequest.setEndDate(endDate);
        return reportRequest;
    }

    public ReportResponse buildReportResponse(Map<String, BigDecimal> transactionsSum) {
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setTransactionsSum(transactionsSum);
        return reportResponse;
    }
}

