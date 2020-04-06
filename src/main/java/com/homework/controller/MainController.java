package com.homework.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.homework.pojo.Resource;
import com.homework.pojo.Title;
import com.homework.service.impl.ResourceServiceImpl;
import com.homework.service.impl.TitleServiceImpl;

@Controller
public class MainController {

	@Autowired
	private ResourceServiceImpl resourceServiceImpl;
	@Autowired
	private TitleServiceImpl titleServiceImpl;

	// 主页
	@GetMapping("/")
	public String main() {
		return "redirect:/0";
	}

	// 跳转到登录页面
	@GetMapping("/login")
	public String login(Model model) {
		Title title = titleServiceImpl.selectByLast();
		model.addAttribute("title", title);
		return "login";
	}

	@GetMapping("/{directory_id:[0-9]*}")
	public String index(Model model, @PathVariable Integer directory_id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 若是0则说明是根文件夹
		if (directory_id == 0) {
			// 取出其下所有文件夹
			List<Resource> directoryList = resourceServiceImpl.selectByDirectory_idIs_file(directory_id, false);
			// 取出其下所有文件
			List<Resource> fileList = resourceServiceImpl.selectByDirectory_idIs_file(directory_id, true);
			Title title = titleServiceImpl.selectByLast();
			// 将数据都放入model
			model.addAttribute("title", title);
			model.addAttribute("directoryList", directoryList);
			model.addAttribute("fileList", fileList);
			model.addAttribute("directory_id", directory_id);// 当前文件夹的主键
			model.addAttribute("parent_directory_id", -1);// 父文件夹的主键，为-1说明不存在父文件夹
			return "index";
		}
		// 若运行到此处，则说明不是根文件夹
		// 取出该对象的数据
		Resource resource = resourceServiceImpl.selectById(directory_id);
		// 判断是文件还是文件夹
		if (resource.getIs_file() != true) {// 是文件夹
			// 取出其下所有文件夹
			List<Resource> directoryList = resourceServiceImpl.selectByDirectory_idIs_file(directory_id, false);
			// 取出所有文件
			List<Resource> fileList = resourceServiceImpl.selectByDirectory_idIs_file(directory_id, true);
			Title title = titleServiceImpl.selectByLast();
			// 将数据都放入model
			model.addAttribute("title", title);
			model.addAttribute("directoryList", directoryList);
			model.addAttribute("fileList", fileList);
			model.addAttribute("directory_id", directory_id);// 当前文件夹的主键
			model.addAttribute("parent_directory_id", resource.getDirectory_id());// 父文件夹的主键
			return "index";
		} else {// 是文件
			return "redirect:/download/" + directory_id;
		}
	}

	// 使该资源的排序号下降1，即使资源在更前的位置
	@GetMapping("/up/{id:[0-9]*}")
	public String up(@PathVariable Integer id) {
		// 取出该资源
		Resource resource = resourceServiceImpl.selectById(id);
		// 判断该资源的排序号是否仍不是最低（1），是则使其排序号下降1，同时使被替代资源的排序号上升1
		if (resource.getSort_number() != 1) {
			// 取出该资源要替代的位置的资源
			Resource resource2 = resourceServiceImpl.selectByDirectory_idIs_fileSort_number(resource.getDirectory_id(),
					resource.getIs_file(), resource.getSort_number() - 1);
			// 将两个资源的排序号分别进行增减
			resource.setSort_number(resource.getSort_number() - 1);
			resource2.setSort_number(resource2.getSort_number() + 1);
			// 更新数据数据
			resourceServiceImpl.update(resource);
			resourceServiceImpl.update(resource2);
		}
		return "redirect:/" + resource.getDirectory_id();
	}

	// 使排序号上升1，即使资源在更后的位置
	@GetMapping("/down/{id:[0-9]*}")
	public String down(@PathVariable Integer id) {
		// 取出该资源
		Resource resource = resourceServiceImpl.selectById(id);
		// 取出排序最后的资源
		Resource resource3 = resourceServiceImpl.selectByDirectory_idIs_fileSort_number(resource.getDirectory_id(),
				resource.getIs_file());
		// 判断该资源的排序号是否最高，若不是最高则进行排序操作
		if (resource.getSort_number() != resource3.getSort_number()) {
			// 取出该资源要替代的位置的资源
			Resource resource2 = resourceServiceImpl.selectByDirectory_idIs_fileSort_number(resource.getDirectory_id(),
					resource.getIs_file(), resource.getSort_number() + 1);
			// 将两个资源的排序号分别进行增减
			resource.setSort_number(resource.getSort_number() + 1);
			resource2.setSort_number(resource2.getSort_number() - 1);
			// 更新数据数据
			resourceServiceImpl.update(resource);
			resourceServiceImpl.update(resource2);
		}
		return "redirect:/" + resource.getDirectory_id();
	}

	@GetMapping("/delete/{id:[0-9]*}")
	@Transactional // 由于涉及到删除，设置进行事务管理
	public String delete(@PathVariable Integer id) {
		// 取出该资源
		Resource resource = resourceServiceImpl.selectById(id);
		// 删除该资源
		resourceServiceImpl.deleteById(id);
		// 取出排序大于该序号的资源，并使其排序均下降1
		List<Resource> resourceList = resourceServiceImpl.selectByDirectory_idIs_fileMoreThanSort_number(
				resource.getDirectory_id(), resource.getIs_file(), resource.getSort_number());
		if (resourceList.size() != 0) {
			Iterator<Resource> iterator = resourceList.iterator();
			while (iterator.hasNext()) {
				Resource next = iterator.next();
				next.setSort_number(next.getSort_number() - 1);
				resourceServiceImpl.update(next);
			}
		}
		return "redirect:/" + resource.getDirectory_id();
	}

	// 跳转至修改页
	@GetMapping("/update/{id:[0-9]*}")
	public String updatePage(@PathVariable Integer id, Model model) {
		Title title = titleServiceImpl.selectByLast();
		model.addAttribute("title", title);
		Resource resource = resourceServiceImpl.selectById(id);
		model.addAttribute("resource", resource);
		return "update";
	}

	// 进行修改
	@PostMapping("/update")
	@ResponseBody
	public HashMap<String, String> update(Resource resource) {
		// 去除两侧空格
		resource.setName(resource.getName().trim());
		if (resource.getName().equals("")) {
			throw new RuntimeException("新名字不可为空");
		}
		// 取出数据库中的该资源
		Resource resource2 = resourceServiceImpl.selectById(resource.getId());
		// 修改资源名
		resource2.setName(resource.getName());
		// 判断新名字是否已被使用
		Resource resource3 = resourceServiceImpl.selectByDirectory_idNameIs_file(resource2.getDirectory_id(),
				resource2.getName(), resource2.getIs_file());
		if (resource3 == null) {
			resourceServiceImpl.update(resource2);
		} else {
			throw new RuntimeException("已存在同名文件（夹）");
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("new_url", "/" + resource2.getDirectory_id());
		return map;
	}
}
