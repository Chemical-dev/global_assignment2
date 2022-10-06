package centilytics.secondassignment.dtos;

import centilytics.secondassignment.enums.STATUS;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Ec2Image {
    private String accountId;
    private String region;
    private String imageId;
    private String imageType;
    private String creationDate;
    private STATUS status;
}
