package com.application.report.service;

import com.application.report.model.Report;
import com.application.report.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public Report updateReport(Long id, Report report) {
        return reportRepository.findById(id)
                .map(reportObj -> {
                    reportObj.setTitle(report.getTitle());
                    reportObj.setContent(report.getContent());
                    return reportRepository.save(reportObj);
                })
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}
