import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ReportFilterRequest {
    private String transactionType;
    private String transactionStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private String merchantId;
}
