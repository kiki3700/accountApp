package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ReportDto;
import com.example.demo.mapper.ReportMapper;
import com.example.demo.service.ReportService;
@Primary
@Service
public class ReportServiceImpl implements ReportService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ReportMapper reportMapper;
	
	@Override
	public List<ReportDto> getReportList(ReportDto reportDto) {
		logger.debug("==================================");
		logger.debug("getReportList service start");
		logger.debug("==================================");
		// TODO Auto-generated method stub
		List<ReportDto> reportDtoList = new ArrayList<>();
		reportDtoList = reportMapper.selectReports(reportDto);
		logger.debug("report amount is : "+reportDtoList.size());
		logger.debug("==================================");
		logger.debug("getReportList finish");
		logger.debug("==================================");
		return reportDtoList;
	}

	@Override
	public boolean postReport(ReportDto reportDto) {
		// TODO Auto-generated method stub
		logger.debug("==================================");
		logger.debug("post Report service start");
		logger.debug("==================================");
		String mainCategory = reportDto.getMainCategory();
		int amount = Math.abs(reportDto.getAmount());
		if(mainCategory.equals("지출")) amount = -amount;
		reportDto.setAmount(amount);
		boolean result = reportMapper.insertReport(reportDto) == 1? true : false;
		logger.debug("result : "+result);
		logger.debug("==================================");
		logger.debug("post Report service finish");
		logger.debug("==================================");
		return result;
	}
	@Override
	public boolean deleteReport(ReportDto reportDto) {
		logger.debug("==================================");
		logger.debug("delete Report service start");
		logger.debug("==================================");
		int n = reportMapper.updateReportForDelete(reportDto);
		boolean result = n==1 ? true : false;
		logger.debug("result : "+ result);
		logger.debug("==================================");
		logger.debug("delete Report service finish");
		logger.debug("==================================");
		return result;
	}
	
	@Override
	public boolean putReport(ReportDto reportDto) {
		// TODO Auto-generated method stub
		logger.debug("==================================");
		logger.debug("put Report service start");
		logger.debug("==================================");
		String mainCategory = reportDto.getMainCategory();
		int amount = Math.abs(reportDto.getAmount());
		if(mainCategory.equals("지출")) amount = -amount;
		reportDto.setAmount(amount);
		
		int n = reportMapper.updateReport(reportDto);
		boolean result = n==1 ? true : false;
		logger.debug("==================================");
		logger.debug("put Report service start");
		logger.debug("==================================");
		return result;
	}

	@Override
	public List<List<Object>> getCostPieChart(ReportDto reportDto) {
		// TODO Auto-generated method stub
		logger.debug("=======================================");
		logger.debug("get CostChart service start");
		logger.debug("=======================================");
		List<ReportDto> reportDtoList = reportMapper.selectCostPieChart(reportDto);
		List<List<Object>> chartList = new ArrayList<>();
		for(ReportDto report : reportDtoList) {
			List<Object> array = new ArrayList<>();
			array.add(report.getSubCategory());
			array.add(Math.abs(report.getAmount()));
			chartList.add(array);
		}
		logger.debug("lengt : "+ chartList.size());
		logger.debug("=======================================");
		logger.debug("get CostChart service finish");
		logger.debug("=======================================");
		return chartList;
	}

	@Override
	public List<List<Object>> selectAnalyzeByMonthBarChart(ReportDto reportDto) {
		// TODO Auto-generated method stub
		logger.debug("=======================================");
		logger.debug("get barchrt service start");
		logger.debug("=======================================");
		List<HashMap<String, Object>> hashMapList = reportMapper.selectAnalyzeByMonthBarChar(reportDto);
		List<List<Object>> chartDataList = new ArrayList<>();
		for(HashMap<String, Object> map : hashMapList) {
			String category = (String) map.get("category");
			int month = (int) map.get("month");
			String monthString = new DateFormatSymbols().getMonths()[month-1];
			BigDecimal amount = (BigDecimal) map.get("amount");
			amount = amount.abs();
			if(category.equals("수입")) {
				List<Object> list = new ArrayList<>();
				list.add(monthString);
				list.add(amount);
				list.add(0);
				chartDataList.add(list);
			}else {
				List<Object> list =chartDataList.get(chartDataList.size()-1);
				list.set(2, amount);
			}
		}
		logger.debug("=======================================");
		logger.debug("get CostChart service finish");
		logger.debug("=======================================");
		return chartDataList;
	}

}
