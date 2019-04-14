package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/imgs")
public class ImagesController {

    //指定图片上传的地址
    private static final String UPLOADER_PATH  ="D:\\IDEA3\\images\\";

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @RequestMapping("/uploader")
    @ResponseBody
    public String uploadImages(MultipartFile file){
        System.out.println("有图片开始上传啦:"+file.getOriginalFilename());

        //获得图片文件最后一个"."的后缀的下标
        int index = file.getOriginalFilename().lastIndexOf(".");
        String houzhui = file.getOriginalFilename().substring(index + 1);
        System.out.println("图片的后缀为："+houzhui);

        //上传到fastDFSs
        try {
            StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(),
                    file.getSize(),
                    houzhui,
                    null);

            //获取上传到FastDFS中的图片访问路径
            String storeUrl = storePath.getFullPath();
            System.out.println("上传到FastDFS中的路径为：" + storeUrl);

            return "{\"uploadPath\":\"" + storeUrl + "\"}";

        } catch (IOException e) {
            e.printStackTrace();
        }

        //上传到本地
        /*try(
                InputStream inputStream = file.getInputStream();
                OutputStream outputStream = new FileOutputStream(UPLOADER_PATH + UUID.randomUUID().toString());
                ) {
            IOUtils.copy(inputStream,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return null;
    }
}
