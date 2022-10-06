package centilytics.secondassignment.dtos;

import centilytics.secondassignment.dtos.IAMReport;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class IamResponse {
    private List<IAMReport> list = new ArrayList<>();
}
