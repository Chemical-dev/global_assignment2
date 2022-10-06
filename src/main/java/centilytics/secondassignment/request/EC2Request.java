package centilytics.secondassignment.request;


import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class EC2Request {
    private List<String> regions;
}
