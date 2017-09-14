package com.mohress.training.service;

import Com.FirstSolver.Splash.FaceId;
import Com.FirstSolver.Splash.FaceIdAnswer;
import Com.FirstSolver.Splash.FaceId_ErrorCode;
import com.google.common.base.Charsets;
import com.mohress.training.util.DateUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by youtao.wan on 2017/9/6.
 */
public class SimpleTest {

    private Pattern pattern = Pattern.compile("");

    @Test
    public void test() throws IOException {

        FaceId client = new FaceId("", 8080);

        String requestBody = String.format("GetRecord(end_time=\"%s\"", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

        FaceIdAnswer answer = new FaceIdAnswer();
        FaceId_ErrorCode resultCode = client.Execute(requestBody, answer, Charsets.UTF_8.name());
        if (FaceId_ErrorCode.Success.equals(resultCode)){
            System.out.println(answer.answer);
        }
    }
}
