package com.example.housebatch.job.lawd;

import com.example.housebatch.BatchTestConfig;
import com.example.housebatch.core.service.LawdService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {LawdInsertJobConfig.class, BatchTestConfig.class})
class LawdInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @MockBean
    private LawdService lawdService;

    @Test
    public void success() throws Exception{
        //Given

        //When
        JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("TEST_LAWD_CODE.txt")));
        JobExecution execution = jobLauncherTestUtils.launchJob(parameters);
        //Then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        verify(lawdService, times(6)).upsert(any());//정상적으로 실행되면 lawdService가 6번 호출된다.
    }

    @Test
    public void fail_whenFileNotFound() throws Exception{
        //Given

        //When
        JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("NOT_EXIST_FILE.txt")));
        //Then
        Assertions.assertThrows(JobParametersInvalidException.class,
                () -> jobLauncherTestUtils.launchJob(parameters));
        verify(lawdService, never()).upsert(any());
    }
}