public class ReportListBuilderImpl implements ReportListBuilder {

    private final ReportRepository reportRepository;
    private Optional<LocalDate> creationDate = Optional.empty();
    private Optional<ReportType> reportType = Optional.empty();
    private Optional<ReportStatus> reportStatus = Optional.empty();

    @Override
    public ReportListBuilder withCreationDate(LocalDate creationDate) {
        this.creationDate = Optional.of(creationDate);
        return this;
    }

    @Override
    public ReportListBuilder withReportType(ReportType reportType) {
        this.reportType = Optional.of(reportType);
        return this;
    }

    @Override
    public ReportListBuilder withReportStatus(ReportStatus reportStatus) {
        this.reportStatus = Optional.of(reportStatus);
        return this;
    }

    @Override
    public List<Report> build() {
        return reportRepository.findAll(createSpecification())
                .stream()
                .sorted(Comparator.comparing(Report::getCreationDate).reversed())
                .collect(Collectors.toList());
    }

    private Specification<Report> createSpecification() {
        Specification<Report> spec = Specification.where(null);
        if (creationDate.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), creationDate.get()));
        }
        if (reportType.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("reportType"), reportType.get()));
        }
        if (reportStatus.isPresent()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("reportStatus"), reportStatus.get()));
        }
        return spec;
    }
}
