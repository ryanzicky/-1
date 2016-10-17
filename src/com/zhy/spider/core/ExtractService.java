package com.zhy.spider.core;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhy.spider.bean.LinkTypeData;
import com.zhy.spider.rule.Rule;
import com.zhy.spider.rule.RuleException;

public class ExtractService {
	public static List<LinkTypeData> extract(Rule rule){
		// 进行对rule的必要校验  
        validateRule(rule);  
  
        List<LinkTypeData> datas = new ArrayList<LinkTypeData>();  
        LinkTypeData data = null;
        try {
			/**
			 * 解析url
			 */
        	String url = rule.getUrl();
        	String[] params = rule.getParams();
        	String[] values = rule.getValues();
        	String resultTagName = rule.getResultTagName();
        	int type = rule.getType();
        	int requestMethod = rule.getRequestMethod();
        	
        	Connection conn = Jsoup.connect(url);
        	//设置查询参数
        	if(params != null){
        		for (int i = 0; i < params.length; i++) {
					conn.data(params[i],values[i]);
				}
        	}
        	
        	//设置请求类型
        	Document doc = null;
        	switch (requestMethod) {
			case Rule.GET:
				doc = conn.timeout(100000).get();
				break;
			case Rule.POST:
				doc = conn.timeout(100000).post();
				break;
			}
        	
        	//处理返回的数据
        	Elements results = new Elements();
        	switch (type) {
			case Rule.CLASS:
				results = doc.getElementsByClass(resultTagName);
				break;
			case Rule.ID:
				Element result = doc.getElementById(resultTagName);
				results.add(result);
				break;
			case Rule.SELECTION:
				results = doc.select(resultTagName);
				break;
			default:
				if (resultTagName == null) {
					results = doc.getElementsByTag("body");
				}
			}
        	
        	for (Element element : results) {
				Elements links = element.getElementsByTag("a");
				
				for (Element element2 : links) {
					//必要的筛选
					String linkHref = element2.attr("href");
					String linkText = element2.text();
					
					data = new LinkTypeData();
					data.setLinkText(linkText);
					data.setLinkHref(linkHref);
					
					datas.add(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
		
	}

	/** 
     * 对传入的参数进行必要的校验 
     */
	private static void validateRule(Rule rule) {
		String url = rule.getUrl();
		if("".equals(url)){
			throw new RuleException("url不能为空!");
		}
		if(!url.startsWith("https://")){
			throw new RuleException("url的格式不正确!");
		}
		if(rule.getParams() != null && rule.getValues() != null){
			if(rule.getParams().length != rule.getValues().length){
				throw new RuleException("参数的键值对个数不匹配!");
			}
		}
	}
	
	
}
