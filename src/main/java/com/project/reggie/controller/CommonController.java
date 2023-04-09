package com.project.reggie.controller;

import com.project.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.PanelUI;
import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    public static final String UPLOAD_PATH = "D:/uploadfile/";
    @PostMapping("/upload")
    @ResponseBody
    public R upload(@RequestParam("file") MultipartFile file){
        String filename = file.getOriginalFilename();
        String substring = filename.substring(filename.lastIndexOf("."));
        String path = UUID.randomUUID() + substring;
        filename = UPLOAD_PATH+path;
        File file1 = new File(filename);
        if (!file1.exists()){
            try {
                file.transferTo(file1);
            } catch (IOException e) {

                return R.success("上传失败，请再次重试");
            }
        }
        return R.success(path);
    }
    @GetMapping("/download")
    @ResponseBody
    public R download(HttpServletResponse response, String name){
        File file = new File(UPLOAD_PATH + name);
        if (!file.exists()){
            return R.error("文件不存在");
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            response.setContentType("application/force-download");
            response.addHeader("Content-disposition", "attachment;fileName=" + file.getName());
            OutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
//            return R.error("下载失败");
        } catch (IOException e) {
            throw new RuntimeException(e);
//            return R.error("下载失败");
        }
        return R.success("下载成功");
    }
}
