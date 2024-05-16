import com.example.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("FROM Report r WHERE r.createdDate >= :startDate AND r.createdDate <= :endDate")
    List<Report> findReportsByDateRange(Instant startDate, Instant endDate);

    @Query("FROM Report r WHERE r.user.id = :userId")
    List<Report> findReportsByUserId(Long userId);

    @Query("FROM Report r WHERE r.merchant.id = :merchantId")
    List<Report> findReportsByMerchantId(Long merchantId);

    @Query("FROM Report r WHERE r.type = :type")
    List<Report> findReportsByType(String type);

    // other methods for updating and deleting reports if necessary
}

