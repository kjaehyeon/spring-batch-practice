package com.example.housebatch.job.apt;

import com.example.housebatch.BatchTestConfig;
import com.example.housebatch.adapter.ApartmentApiResource;
import com.example.housebatch.core.repository.LawdRepository;
import com.example.housebatch.core.service.AptDealService;
import com.example.housebatch.job.lawd.LawdInsertJobConfig;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {AptDealInsertJobConfig.class, BatchTestConfig.class})
class AptDealInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private AptDealService aptDealService;
    @MockBean
    private LawdRepository lawdRepository;
    @MockBean
    private ApartmentApiResource apartmentApiResource;

    @Test
    public void success() throws Exception{
        //Given
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("41135"));
        when(apartmentApiResource.getResource(ArgumentMatchers.anyString(), any())).thenReturn(
                new ClassPathResource("test-api-response.xml")
        ); //api응답을 파일로 미리 저장해놓고 이를 리소스로 가져옴

        //When
        JobExecution execution = jobLauncherTestUtils.launchJob(
                new JobParameters(Maps.newHashMap("yearMonth", new JobParameter("2022-01"))));

        //Then
        assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        verify(aptDealService, times(49)).upsert(any());
    }

    @Test
    public void fail_whenYearMonthNotExist() throws Exception{
        //Given
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("41135"));
        when(apartmentApiResource.getResource(ArgumentMatchers.anyString(), any())).thenReturn(
                new ClassPathResource("test-api-response.xml")
        );
        //When
        assertThrows(JobParametersInvalidException.class, () -> {
            jobLauncherTestUtils.launchJob(
                    new JobParameters(Maps.newHashMap("yearMonth", new JobParameter("202201")))
            );
        });

        //Then
        verify(aptDealService, never()).upsert(any());
    }
}