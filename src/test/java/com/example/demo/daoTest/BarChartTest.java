package com.example.demo.daoTest;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.ReportDto;
import com.example.demo.service.ReportService;

@SpringBootTest
public class BarChartTest {
	@Autowired
	ReportService reportService;
	
	ReportDto reportDto;
	
	@BeforeEach
	void init() {
		reportDto = new ReportDto();
		reportDto.setUserId("5575492q@gmail.com");
		reportDto.setReportingDate(new Date());
	}
	@Test
	void getBarData() {
		List<List<Object>> listList = reportService.selectAnalyzeByMonthBarChart(reportDto);
		for(int i = 0; i <listList.size(); i ++) {
			List<Object> list = listList.get(i);
			for(Object val : list) {
				System.out.print(val+" ");
			}
			System.out.println();
		}
	}
}
