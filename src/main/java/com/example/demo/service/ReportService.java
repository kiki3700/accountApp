package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ReportDto;

public interface ReportService {
	List<ReportDto> getReportList(ReportDto reportDto);
	boolean postReport(ReportDto reportDto);
	boolean putReport(ReportDto rportDto);
	boolean deleteReport(ReportDto reportDto);
	List<List<Object>> getCostPieChart(ReportDto reportDto);
	List<List<Object>>selectAnalyzeByMonthBarChart(ReportDto reportDto);
}
