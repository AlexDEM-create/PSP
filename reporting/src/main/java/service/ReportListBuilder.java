public interface ReportListBuilder {

    ReportListBuilder withCreationDate(LocalDate creationDate);

    ReportListBuilder withReportType(ReportType reportType);

    ReportListBuilder withReportStatus(ReportStatus reportStatus);

    List<Report> build();

}
