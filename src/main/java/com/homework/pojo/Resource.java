package com.homework.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "resource")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class Resource {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Integer id;// 主键

	@Column(name = "is_file")
	Boolean is_file;// 是否文件，true为文件，false为文件夹

	@Column(name = "directory_id")
	Integer directory_id;// 所在文件夹的主键

	@Column(name = "name")
	String name;// 文件名（显示用）

	@Column(name = "real_name")
	String real_name;// 文件名（保存用）

	@Column(name = "sort_number")
	Integer sort_number;// 排序

//	@Column(name = "size")
//	Double size;// 文件大小
//
//	@Column(name = "icon")
//	String icon;// 图标
//
//	@Column(name = "time")
//	Date time;// 上传时间
//
//	@Column(name = "quantity")
//	Integer quantity;// 下载次数

}
