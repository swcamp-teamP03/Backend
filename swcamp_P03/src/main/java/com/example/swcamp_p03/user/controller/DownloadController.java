package com.example.swcamp_p03.user.controller;

import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.user.dto.UserDataDto;
import com.example.swcamp_p03.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DownloadController {
    @Value("${admin.id}")
    private String adminCheck;
    @Value("${admin.password}")
    private String passwordCheck;

    private final UserRepository userRepository;

    @GetMapping("/download")
    public void getUsers(@RequestParam("admin") String admin, @RequestParam("password") String password, HttpServletResponse res) throws IOException {
        // admin 계정만 다운로드 할 수 있게 설정
        if (!adminCheck.equals(admin) || !passwordCheck.equals(password)) {
            log.warn("유요한 관리자만 다운로드 가능합니다.");
            throw new GlobalException(ErrorCode.INVALID_DOWNLOAD);
        }
        log.info("관리자가 회원정보 다운중");

        List<UserDataDto> userDataAll = userRepository.getUserDataAll();

        /**
         * excel sheet 생성
         */
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1"); // 엑셀 sheet 이름

        /**
         * header data
         */
        int rowCount = 0; // 데이터가 저장될 행
        String[] headerNames = new String[]{"가입일", "최근접속일", "메일(ID)", "담당자 이름", "담당자 전화번호", "브랜드(기업)명", "카피 저장 수", "나에게 테스트 하기 수"};

        Row headerRow = null;
        Cell headerCell = null;

        headerRow = sheet.createRow(rowCount++);
        for(int i=0; i<headerNames.length; i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headerNames[i]); // 데이터 추가
        }

        /**
         * body data
         */
        String bodyDatass[][] = new String[userDataAll.size()][headerNames.length];
        for (int i = 0; i < bodyDatass.length; i++) {
            bodyDatass[i][0] = userDataAll.get(i).getCreatedAt();
            bodyDatass[i][1] = userDataAll.get(i).getVisitedTime();
            bodyDatass[i][2] = userDataAll.get(i).getEmail();
            bodyDatass[i][3] = userDataAll.get(i).getUsername();
            bodyDatass[i][4] = userDataAll.get(i).getPhoneNumber();
            bodyDatass[i][5] = userDataAll.get(i).getCompany();
            bodyDatass[i][6] = userDataAll.get(i).getCopyCount();
            bodyDatass[i][7] = userDataAll.get(i).getCampaignTestCount();
        }

        Row bodyRow = null;
        Cell bodyCell = null;

        for(String[] bodyDatas : bodyDatass) {
            bodyRow = sheet.createRow(rowCount++);

            for(int i=0; i<bodyDatas.length; i++) {
                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(bodyDatas[i]); // 데이터 추가
            }
        }

        /**
         * download
         */
        String fileName = "Copyt_Users";

        String contentDisposition = "attachment; filename=\"" + fileName + "\".xlsx";
        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
        ServletOutputStream servletOutputStream = res.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
