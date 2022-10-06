package centilytics.secondassignment.dtos;

import centilytics.secondassignment.dtos.Ec2Image;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class ImageResponse {
    private List<Ec2Image> list = new ArrayList<>();
}
