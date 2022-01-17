package com.example.demo.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.ReportDto;

@Mapper
public interface ReportMapper {
	List<ReportDto> selectReports(ReportDto reportDto);
	int insertReport(ReportDto reportDto);
	int updateReport(ReportDto reportDto);
	List<String> selectSubCategoryList(ReportDto reportDto);
	int updateReportForDelete(ReportDto reportDto);
	List<ReportDto> selectCostPieChart(ReportDto reportDto);
	List<HashMap<String, Object>> selectAnalyzeByMonthBarChar(ReportDto reportDto);
}
