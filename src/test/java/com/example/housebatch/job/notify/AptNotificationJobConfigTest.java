package com.example.housebatch.job.notify;

import com.example.housebatch.BatchTestConfig;
import com.example.housebatch.adapter.FakeSendService;
import com.example.housebatch.core.dto.AptDto;
import com.example.housebatch.core.entity.AptNotification;
import com.example.housebatch.core.entity.Lawd;
import com.example.housebatch.core.repository.AptNotificationRepository;
import com.example.housebatch.core.repository.LawdRepository;
import com.example.housebatch.core.service.AptDealService;
import com.example.housebatch.job.lawd.LawdInsertJobConfig;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AptNotificationJobConfig.class, BatchTestConfig.class})
class AptNotificationJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private AptNotificationRepository aptNotificationRepository; //페이징 쿼리는 mocking처리가 까다로워서 여기서는 그냥 주입 받기로

    @MockBean
    private AptDealService aptDealService;

    @MockBean
    private LawdRepository lawdRepository;

    @MockBean
    private FakeSendService fakeSendService;

    @AfterEach
    public void tearDown(){
        aptNotificationRepository.deleteAll();
    }

    @Test
    public void success() throws Exception{
        //Given
        LocalDate dealDate = LocalDate.now().minusDays(1);
        String guLawdCd = "11110";
        String email = "abc@gmail.com";
        String anotherEmail = "efg@gmail.com";

        givenAptNotification(email, guLawdCd, true);
        givenAptNotification(anotherEmail, guLawdCd, false);
        givenLawdCd(guLawdCd);
        givenAptDeal(guLawdCd, dealDate);
        //When
        JobExecution execution = jobLauncherTestUtils.launchJob(
                new JobParameters(Maps.newHashMap("dealDate", new JobParameter(dealDate.toString())))
        );

        //Then
        assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        verify(fakeSendService, times(1)).send(eq(email), anyString());
        verify(fakeSendService, never()).send(eq(anotherEmail), anyString());
    }
    private void givenAptNotification(String email, String guLawdCd, boolean enabled){
        AptNotification aptNotification = new AptNotification();
        aptNotification.setEmail(email);
        aptNotification.setEnabled(enabled);
        aptNotification.setGuLawdCd(guLawdCd);
        aptNotification.setCreatedAt(LocalDateTime.now());
        aptNotification.setUpdatedAt(LocalDateTime.now());
        aptNotificationRepository.save(aptNotification);
    }
    private void givenLawdCd(String guLawdCd){
        String lawdCd = guLawdCd + "00000";

        Lawd lawd = Lawd.builder()
                .lawdCd(lawdCd)
                .lawdDong("경기도 성남시 분당구")
                .exist(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(lawdRepository.findByLawdCd(lawdCd))
                .thenReturn(Optional.of(lawd));
    }
    private void givenAptDeal(String guLawdCd, LocalDate dealDate){
        when(aptDealService.findByGuLawdCdAndDealDate(guLawdCd, dealDate))
                .thenReturn(Arrays.asList(
                        new AptDto("아파트1", 1000000000L),
                        new AptDto("아파트2", 200000000000L)
                ));
    }
}