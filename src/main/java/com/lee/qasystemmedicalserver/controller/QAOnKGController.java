package com.lee.qasystemmedicalserver.controller;

import com.lee.qasystemmedicalserver.model.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName QAOnKGController
 * @Description 基于知识图谱的问答控制层
 * @Author lee
 * @Date 2023/5/29 19:55
 * @Version 1.0
 */
@RestController
@RequestMapping("/qa")
@Slf4j
public class QAOnKGController {


    @GetMapping("/getAnswer/{questionContent}")
    public R getAnswer(@PathVariable String questionContent){
        log.info("questionContent:" + questionContent);

        String pythonPath = "/Users/liting/opt/anaconda3/envs/lee_base/bin/python3"; // Python 解释器的路径
        String scriptPath = "src/main/resources/python/chatbot_graph.py"; // Python 脚本的路径

        // 执行 python 脚本
        try {

            // 构建 ProcessBuilder 对象
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList(pythonPath,scriptPath, questionContent));

            // 启动进程并等待执行完毕
            Process process = pb.start();
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

            // 获取 Python 脚本的输出结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String result = sb.toString();
            System.out.println("Python script returned: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return R.error();
    }
}
