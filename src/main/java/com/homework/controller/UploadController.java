package com.homework.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.homework.pojo.Resource;
import com.homework.service.impl.ResourceServiceImpl;

@Controller
@RequestMapping("/upload")
public class UploadController {
	@Autowired
	private ResourceServiceImpl resourceServiceImpl;

	// 新建文件夹
	@PostMapping("/directory")
	@ResponseBody
	public HashMap<String, String> uploadDirectory(Resource resource) {
		// 去除名字两侧的空格
		resource.setName(resource.getName().trim());
		// 判断新文件夹名字是否不为空
		if (resource.getName().equals("")) {
			throw new RuntimeException("新文件夹名不可为空");
		}
		// 判断该文件夹名字是否已被同级其他文件夹使用
		Resource resource2 = resourceServiceImpl.selectByDirectory_idNameIs_file(resource.getDirectory_id(),
				resource.getName(), false);
		if (resource2 != null) {
			throw new RuntimeException("该文件夹已存在");
		}
		// 获取该目录中目前排序最后的文件夹，将新建的文件夹的排序号设置为其排序号加1
		Resource resource3 = resourceServiceImpl.selectByDirectory_idIs_fileSort_number(resource.getDirectory_id(),
				false);
		if (resource3 == null) {
			resource.setSort_number(1);
		} else {
			resource.setSort_number(resource3.getSort_number() + 1);
		}
		// 添加新文件夹
		resource.setIs_file(false);
		resource = resourceServiceImpl.insert(resource);
		// 重定向
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("new_url", "/" + resource.getDirectory_id());
		return map;
	}

	// 上传文件
	@PostMapping("/file")
	@ResponseBody
	public HashMap<String, String> uploadFile(MultipartFile file_name, Integer directory_id) throws Exception {
		// 判断传输的文件名是否为空
		if (file_name.getOriginalFilename().equals("")) {
			throw new RuntimeException("您上传的文件有问题");
		}
		String name = file_name.getOriginalFilename();
		// 若存在目录名为前缀，则去除目录名，只保留文件名
		if (name.lastIndexOf("/") != -1) {
			name = name.substring(name.lastIndexOf("/") + 1);
		}
		String prefix = name.substring(0, name.lastIndexOf("."));// 获取前缀
		String suffix = name.substring(name.lastIndexOf("."));// 获取后缀
		// 判断是否存在同名文件，若存在则改变文件名
		int i = 1;
		while (true) {
			Resource resource2 = resourceServiceImpl.selectByDirectory_idNameIs_file(directory_id, name, true);
			if (resource2 == null) {
				break;
			} else {
				name = prefix + "(" + i + ")" + suffix;
				i++;
			}
		}
		// 使用UUID新建随机字符串，作为文件保存时的真实命名，避免冲突
		String real_name = UUID.randomUUID().toString() + suffix;
		// 获取用于保存上传文件的文件夹路径
		String directoryPath = ResourceUtils.getURL("src").getPath() + "main/resources/static/my_file/";
		// 保存文件
		file_name.transferTo(new File(directoryPath, real_name));
		// 保存到数据库中
		Resource resource = new Resource();
		resource.setIs_file(true);
		resource.setDirectory_id(directory_id);
		resource.setName(name);
		resource.setReal_name(real_name);
		// 获取该目录中目前排序最后的文件，将新建的文件的排序号设置为其排序号加1
		Resource resource3 = resourceServiceImpl.selectByDirectory_idIs_fileSort_number(resource.getDirectory_id(),
				true);
		if (resource3 == null) {
			resource.setSort_number(1);
		} else {
			resource.setSort_number(resource3.getSort_number() + 1);
		}
		resourceServiceImpl.insert(resource);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("new_url", "/" + directory_id);
		return map;
	}

}
