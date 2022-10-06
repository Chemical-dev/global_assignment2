package centilytics.secondassignment.dtos;

import centilytics.secondassignment.enums.STATUS;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class IAMReport {
    private Long accountId;
    private String iamUser;
    private String keyLastRotated;
    private Long dayCount;
    private STATUS status;
}
