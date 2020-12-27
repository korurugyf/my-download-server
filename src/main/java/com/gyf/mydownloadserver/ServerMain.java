package com.gyf.mydownloadserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class ServerMain {

    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class, args);
    }

    @Controller
    static class FileController {

        @Value("${file.path}")
        String path;

        @GetMapping("/{filename}")
        void getFile(@PathVariable(value = "filename") String filename, HttpServletResponse response) {
            try (FileInputStream in = new FileInputStream(path + filename);
                 ServletOutputStream out = response.getOutputStream()) {
                response.setContentType("application/octet-stream"); //不知道文件类型,以二进制流传输
                byte[] buffer = new byte[1024 * 1024];
                int num;
                while (true) {
                    num = in.read(buffer);
                    if (num == -1) {
                        break;
                    }
                    out.write(buffer, 0, num);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
