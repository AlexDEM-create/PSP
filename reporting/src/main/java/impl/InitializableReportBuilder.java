public interface InitializableReportBuilder extends ReportBuilder {

    ReportBuilder initializeNew();

    ReportBuilder initializeExisting(Report existingReport);
}