package com.sky.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) throws ClientException {
        log.info("文件上传, {}", file);
        if (file == null || file.isEmpty()) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

        String url = aliOssUtil.upload(file);
        log.info("url {}", url);
        return Result.success(url);
    }
}
