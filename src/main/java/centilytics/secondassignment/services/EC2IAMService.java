package centilytics.secondassignment.services;

import centilytics.secondassignment.config.UserBucketConfiguration;
import centilytics.secondassignment.dtos.Ec2Image;
import centilytics.secondassignment.dtos.IAMReport;
import centilytics.secondassignment.dtos.IamResponse;
import centilytics.secondassignment.dtos.ImageResponse;
import centilytics.secondassignment.enums.STATUS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Image;
import software.amazon.awssdk.services.iam.IamClient;
;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
@RequiredArgsConstructor
@Slf4j
public class EC2IAMService {
    private List<Image> imagesResponse;
    private final UserBucketConfiguration config;
    @Autowired
    private ImageResponse ec2List;
    @Autowired
    private IamResponse iamList;

    public Ec2Client connectEC2Client(String region) {
        return Ec2Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        config.getAwsId(),
                                        config.getAwsSecret()
                                )
                        )
                )
                .build();
    }

    public IamClient connectIAMClient(){
        return IamClient.builder().region(Region.AWS_GLOBAL)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        config.getAwsId(),
                                        config.getAwsSecret()
                                )
                        )
                )
                .build();
    }

    public ImageResponse describeImage(List<String> regions){

        regions.parallelStream().forEachOrdered(region-> {
        imagesResponse= connectEC2Client(region).describeImages().images();
            imagesResponse.forEach(image -> {
                Ec2Image ec2Image = new Ec2Image();
                ec2Image.setImageId(image.imageId());
                ec2Image.setImageType(image.imageTypeAsString());
                ec2Image.setAccountId(image.ownerId());
                ec2Image.setCreationDate(image.creationDate());
                ec2Image.setRegion(region);
                if(image.publicLaunchPermissions()){
                  ec2Image.setStatus(STATUS.OK);
                }else {
                    ec2Image.setStatus(STATUS.CRITICAL);
                }
                ec2List.getList().add(ec2Image);
            });
        });
        return ec2List;
    }
    public IamResponse getIamReport() throws IOException {

            IamClient iamClient  = connectIAMClient();
            String iamCSV = iamClient.getCredentialReport().content().asUtf8String();
            CSVFormat iamCsvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim();
            CSVParser parser = new CSVParser(new StringReader(iamCSV), iamCsvFormat);
            parser.getRecords().forEach(csvDetail -> {
                IAMReport iamReport = new IAMReport();
                iamReport.setAccountId(csvDetail.getRecordNumber());
                iamReport.setIamUser(csvDetail.get("user"));
                iamReport.setKeyLastRotated(csvDetail.get("access_key_2_last_rotated"));
                if (Boolean.parseBoolean(String.valueOf(csvDetail.get("access_key_1_active")))  && Boolean.parseBoolean(String.valueOf(csvDetail.get("access_key_2_active")))){
                   Long days = daysDifference(dateFormat(usingTruncateMethod(csvDetail.get("access_key_1_last_rotated"), 10))
                           , dateFormat(usingTruncateMethod(csvDetail.get("access_key_2_last_rotated"), 10)));
                    iamReport.setDayCount(days);
                   if (days <= 30 ){
                       iamReport.setStatus(STATUS.OK);
                   }else {
                       iamReport.setStatus(STATUS.CRITICAL);
                   }
                   iamList.getList().add(iamReport);
                }

            });
        return iamList;
}
 public LocalDate dateFormat(String str){
     LocalDate date = LocalDate.parse(str);
        return date;
 }

 public long daysDifference(LocalDate dateAfter, LocalDate dateBefore){
     long daysBetween = DAYS.between(dateAfter, dateBefore);
     return daysBetween;
 }

    public static String usingTruncateMethod(String text, int length) {
        text = text.substring(0, length);
        return text;
    }

}


