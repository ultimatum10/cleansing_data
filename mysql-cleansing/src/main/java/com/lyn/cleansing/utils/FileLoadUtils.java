package com.lyn.cleansing.utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件读取工具类
 * @author linkaidi
 * @date 2023/7/26
 */
@Slf4j
public class FileLoadUtils {

    /**
     *
     * @param filePath 文件地址
     *
     * 示例：classpath:information_schema.sql
     *
     *      classpath:file：表示在classpath下查找文件。
     *      file:/path/to/file：表示绝对路径查找文件。
     *      file:relative/path/to/file：表示相对路径查找文件。
     *
     *  读取文件并返回字符串
     *
     */
    public static String readFileString(String filePath) {
        String content = null;
        InputStream inputStream = FileLoadUtils.class.getResourceAsStream(filePath);

        try {
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                StringBuilder contentBuilder = new StringBuilder();
                char[] buffer = new char[1024];
                int bytesRead;
                while ((bytesRead = reader.read(buffer)) != -1) {
                    contentBuilder.append(buffer, 0, bytesRead);
                }
                content = contentBuilder.toString();
                System.out.println("File Content: " + content);
            } else {
                System.out.println("File not found.");
            }
        }catch (Exception e){
            log.error("读取失败...",e);
        }

        return content;
    }
}
