package com.lc.springioinit.springinitdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * upload files
 *
 * @auther lichi
 * @create 2017-10-31 21:23
 */
@Controller
@RequestMapping(value = "/user")
public class UploadController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "fileupload";
    }

    /**
     * 提取上传方法为公共方法
     *
     * @param uploadDir 上传文档目录
     * @param file      上传文件
     * @throws Exception
     */
    private void executeUpload(String uploadDir, MultipartFile file) throws Exception {
        // 文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 上传文件名，使用UUID防止重名
        String fileName = UUID.randomUUID() + suffix;
        // 服务器端保存对象，并存入服务器文件夹中
        File serverFile = new File(uploadDir + fileName);
        file.transferTo(serverFile);
    }


    /**
     * 上传文件方法
     *
     * @param request
     * @param file    前台上传的文件
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public @ResponseBody
    String upload(HttpServletRequest request, MultipartFile file) {
        try {
            // 构造上传目录地址
            String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/";
            // 如果目录不存在，自动创建
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdir();

            executeUpload(uploadDir, file);
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }

        return "上传成功";
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public @ResponseBody
    String uploads(HttpServletRequest request, MultipartFile[] file) {
        try {
            // 构造上传目录地址
            String uploadDir = request.getSession().getServletContext().getRealPath("/") + "upload/";
            // 如果目录不存在，自动创建
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdir();
            for (MultipartFile signleFile : file) {
                if (signleFile != null)
                    executeUpload(uploadDir, signleFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }

        return "上传成功";
    }
}
