package centilytics.secondassignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sdk-assignment")
@Data
public class UserBucketConfiguration {
    private String awsId;
    private String awsSecret;
}
