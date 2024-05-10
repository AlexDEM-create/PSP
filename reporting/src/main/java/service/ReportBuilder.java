import java.time.LocalDate;
import java.util.List;

public class ReportBuilder {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> metrics;
    private String format;
    private List<String> filters;
    private boolean sendEmail;

    public ReportBuilder withDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public ReportBuilder withMetrics(List<String> metrics) {
        this.metrics = metrics;
        return this;
    }

    public ReportBuilder withFormat(String format) {
        this.format = format;
        return this;
    }

    public ReportBuilder withFilters(List<String> filters) {
        this.filters = filters;
        return this;
    }

    public ReportBuilder withEmailSending(boolean sendEmail) {
        this.sendEmail = sendEmail;
        return this;
    }

    public Report build() {
        Report report = new Report();
        report.setStartDate(this.startDate);
        report.setEndDate(this.endDate);
        report.setMetrics(this.metrics);
        report.setFormat(this.format);
        report.setFilters(this.filters);
        report.setSendEmail(this.sendEmail);
        return report;
    }
}
