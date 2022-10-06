package centilytics.secondassignment.controllers;

import centilytics.secondassignment.dtos.IamResponse;
import centilytics.secondassignment.dtos.ImageResponse;
import centilytics.secondassignment.request.EC2Request;
import centilytics.secondassignment.annotations.GET;
import centilytics.secondassignment.services.EC2IAMService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/security-audit")
public class EC2IAMController {
    @Autowired
    EC2IAMService ec2Service;

    @GET("/ec2/public-amis")
    public ImageResponse getImage(@RequestBody() EC2Request request) throws Exception{
        return ec2Service.describeImage(request.getRegions());
    }

    @GET("/iam/rotation-check")
    public IamResponse getIamReport() throws Exception{
        return  ec2Service.getIamReport();
    }
}
