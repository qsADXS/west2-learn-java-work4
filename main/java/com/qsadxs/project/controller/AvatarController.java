package com.qsadxs.project.controller;
import com.qsadxs.project.Dao.ResultMap;
import com.qsadxs.project.Mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@Slf4j
public class AvatarController {
    @Autowired
    UserMapper userMapper;
    @Value("${qsadxs.defaultAvatar}")
    String defaultAvatar;

    @PostMapping("/user/change-avatar")
    public ResultMap uploadAvatar(@RequestParam("newAvatar") MultipartFile newAvatar) throws IOException {
        if (newAvatar.isEmpty()) {
            return ResultMap.fail("头像为空");
        }
        if(newAvatar.getSize()>=1048576){
            log.info("图片太大(小于1MB)");
            return ResultMap.fail("图片太大(需小于1MB)");
        }
        UserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userDetails = (UserDetails) authentication.getPrincipal();
        } else {
            return ResultMap.fail("error");
        }
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + newAvatar.getOriginalFilename());
        newAvatar.transferTo(file);
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String base64 = new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
        userMapper.updateAvatar(base64,userMapper.findIdByUsername(userDetails.getUsername()));
        log.info("储存成功");
        return ResultMap.success(null);
    }

    @GetMapping("/getAvatar/{userId}")
    public ResultMap getBase64(@PathVariable int userId) throws IOException {
        log.info("尝试获取头像");
        if(userMapper.findByUserid(userId)==null){
            return ResultMap.fail("找不到该用户");
        }
        String base64Image = userMapper.findAvatarByUserid(userId);
        if(base64Image == null){
            base64Image = defaultAvatar;
        }
        log.info("获取成功");
        return ResultMap.success(base64Image);
    }
}

/*
原本是将图片储存在服务器，然后再返回base64的，但是不知道为什么一直显示文件不存在，研究了好久，最后决定直接编成base64放在mysql,以下是原本的代码
@PostMapping("/user/change-avatar")
public ResultMap uploadAvatar(@RequestParam("newAvatar") MultipartFile newAvatar) {
    if (newAvatar.isEmpty()) {
        return ResultMap.fail("头像为空");
    }
    UserDetails userDetails = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
        userDetails = (UserDetails) authentication.getPrincipal();
    } else {
        return ResultMap.fail("error");
    }
    try {
        String userId = String.valueOf(userMapper.findIdByUsername(userDetails.getUsername()));
        String filePath = "/juejin/user/avatar";
        File dest = new File(filePath + "/" + userId + ".png");
        // 检查父目录是否存在，如果不存在则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 将文件保存到服务器
        newAvatar.transferTo(dest);
        log.info("保存成功");
        log.info("路径为："+dest.getAbsolutePath());

        Resource resource = new ClassPathResource(dest.getPath());
        if(resource.exists()){
            log.info("dest.getPath():"+dest.getPath());
        }else{
            log.info("不存在:" + dest.getPath());
        }

        if(!resource.exists()){
            log.info("头像不存在");
            return ResultMap.fail("头像不存在");
        }


        return ResultMap.success(null);
    } catch (IOException e) {
        e.printStackTrace();
        return ResultMap.fail("error");
    }
}

@GetMapping("/getAvatar/{userId}")
public ResultMap getBase64(@PathVariable int userId) throws IOException {
    log.info("尝试获取头像");
    String path = "/juejin/user/avatar/"+ userId +".png";
    log.info("头像路径"+path);
    // 读取图片文件
    Resource resource = new ClassPathResource(path);
    if(!resource.exists()){
        log.info("头像不存在");
        return ResultMap.fail("头像不存在");
    }
    InputStream inputStream = resource.getInputStream();

    // 将图片转换为Base64字符串
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
    }
    byte[] imageBytes = outputStream.toByteArray();
    String base64Image = Base64.encodeBase64String(imageBytes);
    // 关闭流
    outputStream.close();
    inputStream.close();
    return ResultMap.success(base64Image);

}
 */
