package com.homework.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.pojo.Resource;
import com.homework.service.impl.ResourceServiceImpl;

@Controller
@RequestMapping("/download")
public class DownloadController {
	@Autowired
	private ResourceServiceImpl resourceServiceImpl;

	@GetMapping("/{directory_id:[0-9]*}")
	public void download(@PathVariable Integer directory_id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 取出资源信息
		Resource resource = resourceServiceImpl.selectById(directory_id);
		// 获取用于保存上传文件的文件夹路径
		String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/my_file/";
		// 进行下载
		InputStream input = new FileInputStream(new File(directoryPath + resource.getReal_name()));
		OutputStream output = response.getOutputStream();
		response.setContentType("application/x-download");
		// URLEncoder.encode()用于进行编码，否则会出现乱码，使得文件名均由下划线组成。
		// 文件名的空格会自动转为+，replaceAll用于将这些+转回空格
		response.addHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(resource.getName(), "UTF-8").replaceAll("\\+", "%20"));
		IOUtils.copy(input, output);
		output.flush();
	}

}
