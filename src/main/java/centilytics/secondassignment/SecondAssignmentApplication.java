package centilytics.secondassignment;

import centilytics.secondassignment.config.UserBucketConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {UserBucketConfiguration.class})
public class SecondAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondAssignmentApplication.class, args);
    }

}
