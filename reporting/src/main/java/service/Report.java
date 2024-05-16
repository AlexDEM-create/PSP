package com.example.report.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "report_title", nullable = false)
    private String reportTitle;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "author", nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status", nullable = false)
    private ReportStatus reportStatus;

    @Column(name = "report_content")
    private String reportContent;

    @Column(name = "report_format", nullable = false)
    private String reportFormat;

    @Column(name = "report_parameters")
    private String reportParameters;

    @Column(name = "report_link")
    private String reportLink;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
