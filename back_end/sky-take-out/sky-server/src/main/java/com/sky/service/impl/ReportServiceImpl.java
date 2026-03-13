package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportMapper reportMapper;
    @Autowired
    WorkspaceService workspaceService;

    /**
     * 营业额统计
     *
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 创建集合存储日期表
        List<LocalDate> dateList = new ArrayList();
        dateList.add(begin);

        while (begin.isBefore(end)) {
            // 循环计算日期，并装入集合
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 获取每日营业额集合
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 将日期转换成日期时间
            LocalDateTime dateBegin = date.atStartOfDay();
            LocalDateTime dateEnd = date.plusDays(1).atStartOfDay();
            // 计算每日营业额
            Double turnover = reportMapper.sumTurnoverByDay(getMap(dateBegin, dateEnd, Orders.COMPLETED));
            turnover = turnover == null ? 0.0 : turnover;    // 三元运算判空
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(toFormatString(dateList))
                .turnoverList(toFormatString(turnoverList))
                .build();
    }

    /**
     * 用户统计
     *
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 创建集合存储日期表
        List<LocalDate> dateList = new ArrayList();
        dateList.add(begin);

        while (begin.isBefore(end)) {
            // 循环计算日期，并装入集合
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 每天新增用户数
        List<Integer> newUserList = new ArrayList<>();
        // 每天总用户数
        List<Integer> totallUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 将日期转换成日期时间
            LocalDateTime dateBegin = date.atStartOfDay();
            LocalDateTime dateEnd = date.plusDays(1).atStartOfDay();

            Map map = new HashMap();
            map.put("dateEnd", dateEnd);
            // 计算最终日期截至的总用户数
            Integer totallUser = reportMapper.countUser(map);
            totallUser = totallUser == null ? 0 : totallUser;    // 三元运算判空
            totallUserList.add(totallUser);

            // 计算每日新增用户
            map.put("dateBegin", dateBegin);
            Integer newUser = reportMapper.countUser(map);
            newUser = newUser == null ? 0 : newUser;    // 三元运算判空
            newUserList.add(newUser);
        }

        return UserReportVO.builder()
                .dateList(toFormatString(dateList))
                .newUserList(toFormatString(newUserList))
                .totalUserList(toFormatString(totallUserList))
                .build();
    }

    /**
     * 订单统计
     *
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        // 创建集合存储日期表
        List<LocalDate> dateList = new ArrayList();
        dateList.add(begin);

        while (begin.isBefore(end)) {
            // 循环计算日期，并装入集合
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 每日订单统计
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 将日期转换成日期时间
            LocalDateTime dateBegin = date.atStartOfDay();
            LocalDateTime dateEnd = date.plusDays(1).atStartOfDay();

            // 计算每日订单数
            Map orderCountMap = getMap(dateBegin, dateEnd, null);
            Integer orderCount = reportMapper.countOrder(orderCountMap);
            orderCount = orderCount == null ? 0 : orderCount;
            orderCountList.add(orderCount);

            // 计算每日有效订单数
            Map validOrderMap = getMap(dateBegin, dateEnd, Orders.COMPLETED);
            Integer validOrderCount = reportMapper.countOrder(validOrderMap);
            validOrderCount = validOrderCount == null ? 0 : validOrderCount;
            validOrderCountList.add(validOrderCount);
        }
        // 累加计算总订单数
        Integer totalOrderCount = orderCountList.stream().mapToInt(Integer::intValue).sum();
        // 累加计算总有效订单
        Integer validOrderCount = validOrderCountList.stream().mapToInt(Integer::intValue).sum();

        // 订单完成率
        double orderCompletionRate = 0.0;
        // 分母不能为0
        if (totalOrderCount != 0) {
            orderCompletionRate = Double.valueOf(validOrderCount) / Double.valueOf(totalOrderCount);
        }

        return OrderReportVO.builder()
                .dateList(toFormatString(dateList))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCountList(toFormatString(orderCountList))
                .validOrderCountList(toFormatString(validOrderCountList))
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 销量排名
     *
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        // 将Date转换为DateTime
        LocalDateTime beginTime = begin.atStartOfDay();
        LocalDateTime endTime = end.plusDays(1).atStartOfDay();

        Map map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("status", Orders.COMPLETED);

        List<GoodsSalesDTO> goodsSalesList = reportMapper.getSalesTop10(map);
        // 通过Steam流获取 nameList 和 numberList
        List<String> nameList = goodsSalesList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = goodsSalesList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(toFormatString(nameList))
                .numberList(toFormatString(numberList))
                .build();
    }

    /**
     * 导出运营数据
     *
     * @param response
     */
    public void exportBussinessData(HttpServletResponse response) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 1. 查询数据库，获取营业数据 -- 查询最近30天的运营数据
        LocalDateTime begin = LocalDate.now().minusDays(30).atStartOfDay();
        LocalDateTime end = LocalDate.now().atStartOfDay();

        // 查询概览数据
        BusinessDataVO bussinessData = workspaceService.getBussinessData(begin, end);

        // 2. 通过POI，将数据写入Excel文件中
        // 获取模板文件输入流对象 -- 这里用反射的方式获取是因为项目打包后路径会变化，所以放在 resource目录的里的文件通过这个方式获取会更稳
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            // 基于模板文件，在内存中创建一个excel文件
            XSSFWorkbook excel = new XSSFWorkbook(is);
            // 获取表格sheet
            XSSFSheet sheet1 = excel.getSheet("sheet1");
            // 填充数据--时间
            sheet1.getRow(1).getCell(1).setCellValue("时间: " + dtf.format(begin) + "至" + dtf.format(end));
            // 填充概览数据--营业额，完成率，新增用户数，有效订单，平均客单价
            // 获取第四行
            XSSFRow row4 = sheet1.getRow(3);
            row4.getCell(2).setCellValue(bussinessData.getTurnover());
            row4.getCell(4).setCellValue(bussinessData.getOrderCompletionRate());
            row4.getCell(6).setCellValue(bussinessData.getNewUsers());
            // 获取第五行
            XSSFRow row5 = sheet1.getRow(4);
            row5.getCell(2).setCellValue(bussinessData.getValidOrderCount());
            row5.getCell(4).setCellValue(bussinessData.getUnitPrice());

            // 填充明细数据
            int rowIdx = 7;
            XSSFRow row;
            while (begin.isBefore(end)) {
                BusinessDataVO bussinessDataPerDay = workspaceService.getBussinessData(begin, begin.plusDays(1));
                log.info(String.valueOf(bussinessDataPerDay));
                // 获取当前行
                row = sheet1.getRow(rowIdx);
                row.getCell(1).setCellValue(dtf.format(begin));
                row.getCell(2).setCellValue(bussinessDataPerDay.getTurnover());
                row.getCell(3).setCellValue(bussinessDataPerDay.getValidOrderCount());
                row.getCell(4).setCellValue(bussinessDataPerDay.getOrderCompletionRate());
                row.getCell(5).setCellValue(bussinessDataPerDay.getUnitPrice());
                row.getCell(6).setCellValue(bussinessDataPerDay.getNewUsers());

                rowIdx++;
                begin = begin.plusDays(1);
            }

            // 3. 通过 response对象 获取输出流，将Excel文件下载到客户端浏览器 -- 浏览器会弹出保存文件的对话框
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            out.close();
            excel.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取每日订单统计数据
     *
     * @param dateBegin
     * @param dateEnd
     * @param status
     * @return
     */
    private Map getMap(LocalDateTime dateBegin, LocalDateTime dateEnd, Integer status) {

        Map map = new HashMap();
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        map.put("status", status);

        return map;
    }

    /**
     * 将集合转成特定格式的字符串
     *
     * @param list
     * @return
     */
    private <T> String toFormatString(List<T> list) {

        List<String> strList = list.stream().map(Object::toString).collect(Collectors.toList());

        return String.join(",", strList);
    }
}
