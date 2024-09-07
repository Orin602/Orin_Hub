package com.demo.dto;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BlogFood {
	private String title;
	private String link;
	private String description;
	private String bloggername;
	private String bloggerlink;
	private Date postdate;
}