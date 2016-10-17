package com.zhy.spider.test;

import java.util.List;

import com.zhy.spider.bean.LinkTypeData;
import com.zhy.spider.core.ExtractService;
import com.zhy.spider.rule.Rule;

public class Test {
	@org.junit.Test
	public void getDatasByClass(){
		Rule rule = new Rule("https://www.baidu.com/", new String[] { "word" }, new String[] { "支付宝" }, null, -1, Rule.GET);
		List<LinkTypeData> extracts = ExtractService.extract(rule);
		printf(extracts);
	}

	private void printf(List<LinkTypeData> datas) {
		for (LinkTypeData data : datas) {
			System.out.println(data.getLinkText());
			System.out.println(data.getLinkHref());
			System.out.println("***********************************");
		}
	}
}
