package com.application.report.controller;

import com.application.report.service.ReportService;
import com.application.report.model.Report;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable Long id) {
        return reportService.getReportById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Report createReport(@RequestBody Report report) {
        return reportService.createReport(report);
    }

    @PutMapping("/{id}")
    public Report updateReport(@PathVariable Long id, @RequestBody Report report) {
        return reportService.updateReport(id, report);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
    }
}
