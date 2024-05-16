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
public class ReportPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "report_name", nullable = false)
    private String reportName;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_status", nullable = false)
    private ReportStatus reportStatus;

    // данные отчета, представленные в виде JSON, XML, строки или другого формата,
    // в зависимости от требований и возможностей вашей системы
    @Column(name = "report_data")
    private String reportData;

    // любые дополнительные метаданные, специфичные для отчета
    @Column(name = "metadata")
    private String metadata;
}
