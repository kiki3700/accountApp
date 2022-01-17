package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dto.ReportDto;
import com.example.demo.service.ReportService;
import com.example.demo.utils.AuthUtil;

@RestController
@RequestMapping("/accounting")
public class AccountingController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	AuthUtil authUtil;
	@Autowired
	ReportService reportService;
	
	@GetMapping("")
	public ModelAndView getAccouting() {
		logger.debug("=======================================");
		logger.debug("get Accouting Page");
		logger.debug("=======================================");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("accounting");
		return mv;
	}
	@GetMapping("/report")
	public ResponseEntity<List<ReportDto>> getReportList(@CookieValue(name = "access-token", required = true) String accessToken, @RequestParam boolean hidden,
			@RequestParam(required = false)  Date reportingDate, @RequestParam(required = true)  boolean paging, @RequestParam(required = false)  Integer start, @RequestParam(required = false) Integer length ){
		logger.debug("=======================================");
		logger.debug("request getReportList start");
		logger.debug("=======================================");
		logger.debug("request param");
		logger.debug("hidden : "+hidden);
		logger.debug("date : "+reportingDate);
		ReportDto reportDto = new ReportDto();
		String UserId = authUtil.getUserIdFromJWT(accessToken);
		reportDto.setUserId(UserId);
		reportDto.setReportingDate(reportingDate);
		reportDto.setHidden(hidden);
		if(paging) {
			reportDto.setStart(start);
			reportDto.setLength(length);
			reportDto.setPaging(paging);
		}
		List<ReportDto> reportDtoList = reportService.getReportList(reportDto);
		
		logger.debug("querting report Dto List : "+ reportDtoList.size());
		logger.debug("=======================================");
		logger.debug("getReportList finish");
		logger.debug("=======================================");
		return ResponseEntity.ok().body(reportDtoList);
	}
	@PostMapping("/report")
	public ResponseEntity<Void> postReport(@CookieValue(name = "access-token", required = true) String accessToken, @RequestBody ReportDto reportDto){
		logger.debug("=======================================");
		logger.debug("post report start");
		logger.debug("=======================================");
		logger.debug(accessToken);
		String userId = authUtil.getUserIdFromJWT(accessToken);
		logger.debug("user : "+userId);
		reportDto.setUserId(userId);
		logger.debug(reportDto.toString());
		boolean result = reportService.postReport(reportDto);
		logger.debug("result of post report : "+result);
		logger.debug("=======================================");
		logger.debug("post report finish");
		logger.debug("=======================================");
		if(result) return ResponseEntity.ok().build();
		else return ResponseEntity.badRequest().build();
	}
	@DeleteMapping("/report")
	public ResponseEntity<Void> deleteReport(@RequestParam int id, @RequestParam boolean hidden){
		logger.debug("=======================================");
		logger.debug("delete report start");
		logger.debug("=======================================");
		logger.debug("id is "+id);
		logger.debug("delete :  "+hidden);
		ReportDto reportDto = new ReportDto();
		reportDto.setId(id);
		reportDto.setHidden(hidden);
		boolean result = reportService.deleteReport(reportDto);
		logger.debug("result : "+result);
		logger.debug("=======================================");
		logger.debug("delete report finish");
		logger.debug("=======================================");
		if(result) return ResponseEntity.ok().build();
		else return ResponseEntity.badRequest().build();
	}
	@PutMapping("/report")
	public ResponseEntity<Void> putReport(@RequestBody  ReportDto reportDto){
		logger.debug("=======================================");
		logger.debug("put report start");
		logger.debug("=======================================");
		logger.debug(reportDto.toString());
		boolean result = reportService.putReport(reportDto);
		logger.debug("result : "+result);
		logger.debug("=======================================");
		logger.debug("put report finish");
		logger.debug("=======================================");
		if(result) return ResponseEntity.ok().build();		
		else return ResponseEntity.badRequest().build();
	}
	@GetMapping("/costPieChart")
	public ResponseEntity<List<List<Object>>> getCostPieChart(@CookieValue(name = "access-token", required = true) String accessToken, @RequestParam Date reportingDate){
		logger.debug("=======================================");
		logger.debug("get CostPieChart start");
		logger.debug("=======================================");
		ReportDto reportDto = new ReportDto();
		String id = authUtil.getUserIdFromJWT(accessToken);
		reportDto.setUserId(id);
		reportDto.setReportingDate(reportingDate);
		List<List<Object>> reportDtoList = reportService.getCostPieChart(reportDto);
		logger.debug("=======================================");
		logger.debug("get CostPieChart finish");
		logger.debug("=======================================");
		return ResponseEntity.ok().body(reportDtoList);
	}
	@GetMapping("/costBarChart")
	public ResponseEntity<List<List<Object>>> getCostBarChart(@CookieValue(name = "access-token", required = true) String accessToken, @RequestParam Date reportingDate){
		logger.debug("=======================================");
		logger.debug("get CostBarChart start");
		logger.debug("=======================================");
		ReportDto reportDto = new ReportDto();
		String id = authUtil.getUserIdFromJWT(accessToken);
		reportDto.setUserId(id);
		reportDto.setReportingDate(reportingDate);
		List<List<Object>> chartDataList = reportService.selectAnalyzeByMonthBarChart(reportDto);
		logger.debug("=======================================");
		logger.debug("get CostBarChart start");
		logger.debug("=======================================");
		return ResponseEntity.ok().body(chartDataList);
	}
}
