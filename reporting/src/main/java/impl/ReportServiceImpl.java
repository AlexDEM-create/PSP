package com.example.report.service;
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final IncomingPaymentRepository incomingPaymentRepository;

    public ReportServiceImpl(ReportRepository reportRepository, IncomingPaymentRepository incomingPaymentRepository) {
        this.reportRepository = reportRepository;
        this.incomingPaymentRepository = incomingPaymentRepository;
    }

    @Override
    public Map<String, BigDecimal> calculateSuccessfulTransactionsSum(String merchantId, LocalDateTime startDate, LocalDateTime endDate) {
        return reportRepository.calculateSuccessfulTransactionsSum(merchantId, startDate, endDate);
    }

    @Override
    public List<Payment> getAllPayments(String merchantId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            startDate = LocalDateTime.now().minusDays(1);
            endDate = LocalDateTime.now();
        }
        return incomingPaymentRepository.findAllByMerchantIdAndPaymentDateBetweenAndPaymentStatus(merchantId, startDate, endDate, SUCCESS);
    }

    @Override
    public BigDecimal calculateTotalPaymentSum(String merchantId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = getAllPayments(merchantId, startDate, endDate);
        return payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

}