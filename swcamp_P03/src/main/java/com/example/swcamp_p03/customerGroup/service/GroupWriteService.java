package com.example.swcamp_p03.customerGroup.service;

import com.example.swcamp_p03.common.exception.ErrorCode;
import com.example.swcamp_p03.common.exception.GlobalException;
import com.example.swcamp_p03.common.valid.ValidCheck;
import com.example.swcamp_p03.config.UserDetailsImpl;
import com.example.swcamp_p03.customerGroup.dto.ExcelDataDto;
import com.example.swcamp_p03.customerGroup.dto.ExcelDataHistoryDto;
import com.example.swcamp_p03.customerGroup.dto.request.FileDownloadRequestDto;
import com.example.swcamp_p03.customerGroup.dto.request.GroupWriteRequestDto;
import com.example.swcamp_p03.customerGroup.dto.request.PropertyDto;
import com.example.swcamp_p03.customerGroup.entity.*;
import com.example.swcamp_p03.customerGroup.entity.history.CustomerGroupHistory;
import com.example.swcamp_p03.customerGroup.entity.history.CustomerPropertyHistory;
import com.example.swcamp_p03.customerGroup.entity.history.ExcelDataHistory;
import com.example.swcamp_p03.customerGroup.entity.history.ExcelFileHistory;
import com.example.swcamp_p03.customerGroup.repository.*;
import com.example.swcamp_p03.customerGroup.repository.history.CustomerGroupHistoryRepository;
import com.example.swcamp_p03.customerGroup.repository.history.CustomerPropertyHistoryRepository;
import com.example.swcamp_p03.customerGroup.repository.history.ExcelDataHistoryRepository;
import com.example.swcamp_p03.customerGroup.repository.history.ExcelFileHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupWriteService {
    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerPropertyRepository customerPropertyRepository;
    private final ExcelFileRepository excelFileRepository;
    private final ExcelDataRepository excelDataRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerGroupHistoryRepository customerGroupHistoryRepository;
    private final CustomerPropertyHistoryRepository customerPropertyHistoryRepository;
    private final ExcelDataHistoryRepository excelDataHistoryRepository;
    private final ExcelFileHistoryRepository excelFileHistoryRepository;
    private final ValidCheck validCheck;
    private final ExcelDownloadRepository excelDownLoadRepository;

    private final String exelDirectory = System.getProperty("user.dir") + "/excelFile/";

    @Transactional
    public void writeGroup(UserDetailsImpl userDetails, GroupWriteRequestDto requestDto) throws IOException {
        // 절대 경로에 폴더 생성
        File folder = new File(exelDirectory);
        if (!folder.exists()) {
            try {
                folder.mkdirs();
                log.info("폴더가 생성되었습니다.");
            } catch (Exception e) {
                e.getStackTrace();
            }
        } else {
            log.info("이미 폴더가 생성되어 있습니다.");
        }

        MultipartFile file = requestDto.getFile();
        if (file == null) {
            throw new GlobalException(ErrorCode.NOT_UPLOAD_FILE);
        }

        String orgName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(orgName);
        String savedName = UUID.randomUUID() + "." + extension;
        String savedPath = exelDirectory + savedName;
        String fileSize = file.getSize() / 1000 + "KB";

        //1. 엑셀파일 저장
        ExcelFile excelFile = ExcelFile.register()
                .excelFileOrgName(orgName)
                .excelFileSavedName(savedName)
                .excelFileSavedPath(savedPath)
                .excelFileSize(fileSize)
                .build();
        excelFileRepository.save(excelFile);

        //2. 엑셀파일인 경우 절대경로에 파일 업로드
        InputStream fileInputStream = excelUpload(file, extension, savedPath);

        //3. 엑셀파일 파싱해서 데이터로 저장
        Workbook workbook = getWorkbookXlsxOrXls(extension, fileInputStream);
        excelParsingAndSave(excelFile, workbook);

        //4. group 저장
        CustomerGroup customerGroup = CustomerGroup.register()
                .customerGroupName(requestDto.getGroupName())
                .favorite(false)
                .user(userDetails.getUser())
                .excelFile(excelFile)
                .build();
        customerGroupRepository.save(customerGroup);

        //5. property 저장
        saveGroupProperties(requestDto, customerGroup);

    }

    @Transactional
    public void updateGroup(Long groupId, GroupWriteRequestDto requestDto, UserDetailsImpl userDetails) throws IOException {
        CustomerGroup findGroup = customerGroupRepository.findByCustomerGroupIdAndUser(groupId, userDetails.getUser()).orElseThrow(
                () -> new GlobalException(ErrorCode.CANT_EDIT)
        );
        if (Boolean.TRUE.equals(findGroup.getUnableEdit())) {
            log.warn("캠페인을 생성한 고객 그룹은 수정이 불가합니다.");
            throw new GlobalException(ErrorCode.CANT_EDIT);
        }
        MultipartFile multipartFile = requestDto.getFile();

        //파일 업로드 존재 여부
        if (multipartFile != null) {
            //파일 업로드 O --> 파일 수정, 데이터 수정, property 수정, group 수정

            ExcelFile excelFile = findGroup.getExcelFile();

            // 엑셀파일 히스토리 저장 -> 엑셀데이터 히스토리 저장 -> 그룹 히스토리 저장 -> 그룹 속성 히스토리 저장
            ExcelFileHistory historyExcelFile = saveExcelFileHistory(excelFile);
            saveAllExcelDataHistory(excelFile, historyExcelFile);
            saveCustomerGroupHistoryAndGroupPropertiesHistory(findGroup, historyExcelFile);

            String orgName = multipartFile.getOriginalFilename();
            String extension = FilenameUtils.getExtension(orgName);
            String savedName = UUID.randomUUID().toString() + "." + extension;
            String savedPath = exelDirectory + savedName;
            String fileSize = requestDto.getFile().getSize() / 1000 + "KB";

            //1. 엑셀 데이터 비우기
            excelDataRepository.deleteByExcelFile(excelFile);

            //2. 절대경로에 있는 엑셀파일 삭제하기
            Path filePath = Paths.get(excelFile.getExcelFileSavedPath());
            Files.deleteIfExists(filePath);

            //3. 엑셀 파일 수정
            excelFile.update(orgName, savedName, savedPath, fileSize);

            //4. 엑셀 파일인 경우만 업로드하고 절대경로에 저장
            InputStream fileInputStream = excelUpload(multipartFile, extension, savedPath);

            //5. 엑셀 파일 파싱하고 엑셀 데이터 저장하기
            Workbook workbook = getWorkbookXlsxOrXls(extension, fileInputStream);
            excelParsingAndSave(excelFile, workbook);

            //6. property 삭제
            customerPropertyRepository.deleteAll(findGroup.getPropertyList());

            //7. property 저장
            saveGroupProperties(requestDto, findGroup);
        } else {
            //파일 업로드 X --> group 수정, property 수정

            // 엑셀파일 히스토리 저장 -> 엑셀데이터 히스토리 저장 -> 그룹 히스토리 저장 -> 그룹 속성 히스토리 저장
            ExcelFileHistory historyExcelFile = saveExcelFileHistory(null);
            saveAllExcelDataHistory(null, historyExcelFile);
            saveCustomerGroupHistoryAndGroupPropertiesHistory(findGroup, historyExcelFile);

            //1. property 삭제
            customerPropertyRepository.deleteAll(findGroup.getPropertyList());

            //2. property 저장
            saveGroupProperties(requestDto, findGroup);
        }
        findGroup.update(requestDto.getGroupName());
    }

    public void fileDownloadReason(Long groupId, UserDetailsImpl userDetails, FileDownloadRequestDto requestDto) {
        boolean matches = bCryptPasswordEncoder.matches(requestDto.getPassword(), userDetails.getPassword());
        if (matches) {
            ExcelDownload download = new ExcelDownload(requestDto.getDownloadReason(), userDetails.getUser());
            excelDownLoadRepository.save(download);
        } else {
            log.warn("비밀번호가 틀렸습니다.");
            throw new GlobalException(ErrorCode.NOT_VALID_DOWNLOAD);
        }
    }

    public ResponseEntity<Resource> fileDownload(Long groupId) throws MalformedURLException {
        CustomerGroup customerGroup = customerGroupRepository.findById(groupId).orElseThrow(
                () -> new GlobalException(ErrorCode.DATA_NOT_FOUND)
        );
        ExcelFile excelFile = customerGroup.getExcelFile();
        UrlResource resource = new UrlResource("file:" + excelFile.getExcelFileSavedPath());
        String encodedFileName = UriUtils.encode(excelFile.getExcelFileOrgName(), StandardCharsets.UTF_8);
        // 파일 다운로드 대화상자가 뜨도록 하는 헤더를 설정해주는 것
        // Content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 준다.
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel")
                .body(resource);
    }

    @Transactional
    public void favoriteCheck(Long groupId) {
        CustomerGroup customerGroup = customerGroupRepository.findById(groupId).orElseThrow(
                () -> new GlobalException(ErrorCode.DATA_NOT_FOUND)
        );
        customerGroup.setFavorite(!customerGroup.getFavorite());
    }

    private static InputStream excelUpload(MultipartFile file, String extension, String savedPath) throws IOException {
        if (extension == null || (!extension.equals("xlsx") && !extension.equals("xls"))) {
            log.warn("엑셀 확장자가 유효하지 않습니다.");
            throw new GlobalException(ErrorCode.NOT_EXCEL_FILE);
        }
        InputStream fileInputStream = file.getInputStream();
        file.transferTo(new File(savedPath));
        return fileInputStream;
    }

    private static Workbook getWorkbookXlsxOrXls(String extension, InputStream fileInputStream) throws IOException {
        Workbook workbook = null;
        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(fileInputStream);
        } else {
            workbook = new HSSFWorkbook(fileInputStream);
        }
        return workbook;
    }

    private void saveGroupProperties(GroupWriteRequestDto requestDto, CustomerGroup customerGroup) {
        List<PropertyDto> properties = requestDto.getProperties();
        if (properties != null) {
            List<CustomerProperty> customerProperties = new ArrayList<>();
            for (PropertyDto property : properties) {
                CustomerProperty customerProperty = CustomerProperty.register()
                        .property(property)
                        .customerGroup(customerGroup)
                        .build();
                customerProperties.add(customerProperty);
            }
            customerPropertyRepository.saveAll(customerProperties);
        } else {
            CustomerProperty customerProperty = CustomerProperty.register()
                    .property(null)
                    .customerGroup(customerGroup)
                    .build();
            customerPropertyRepository.save(customerProperty);
        }
    }

    private void saveCustomerGroupHistoryAndGroupPropertiesHistory(CustomerGroup findGroup, ExcelFileHistory historyExcelFile) {
        CustomerGroupHistory historyCustomerGroup = saveCustomerGroupHistory(findGroup, historyExcelFile);
        saveAllCustomerGroupPropertiesHistory(findGroup, historyCustomerGroup);
    }

    private void saveAllCustomerGroupPropertiesHistory(CustomerGroup findGroup, CustomerGroupHistory historyCustomerGroup) {
        List<CustomerProperty> propertyList = findGroup.getPropertyList();
        List<CustomerPropertyHistory> historyCustomerProperties = new ArrayList<>();
        for (CustomerProperty customerProperty : propertyList) {
            historyCustomerProperties.add(
                    CustomerPropertyHistory.historyRegister()
                            .customerProperty(customerProperty)
                            .customerGroupHistory(historyCustomerGroup)
                            .build()
            );
        }
        customerPropertyHistoryRepository.saveAll(historyCustomerProperties);
    }

    private CustomerGroupHistory saveCustomerGroupHistory(CustomerGroup findGroup, ExcelFileHistory historyExcelFile) {
        CustomerGroupHistory historyCustomerGroup = CustomerGroupHistory.historyRegister()
                .customerGroup(findGroup)
                .excelFileHistory(historyExcelFile)
                .build();
        customerGroupHistoryRepository.save(historyCustomerGroup);
        return historyCustomerGroup;
    }

    private void saveAllExcelDataHistory(ExcelFile excelFile, ExcelFileHistory historyExcelFile) {
        if (excelFile == null) {
            ExcelDataHistory historyExcelData = ExcelDataHistory.historyRegister()
                    .excelData(null)
                    .excelFileHistory(historyExcelFile)
                    .build();
            excelDataHistoryRepository.save(historyExcelData);
        } else {
            List<ExcelData> allByExcelFile = excelDataRepository.findAllByExcelFile(excelFile);
            List<ExcelDataHistoryDto> historyExcelDataList = new ArrayList<>();
            for (ExcelData excelData : allByExcelFile) {
                historyExcelDataList.add(
                        ExcelDataHistoryDto.historyRegister()
                                .excelData(excelData)
                                .excelFileHistoryId(historyExcelFile.getExcelFileHistoryId())
                                .build()
                );
            }
            excelDataHistoryRepository.bulkInsert(historyExcelDataList);
        }
    }

    private ExcelFileHistory saveExcelFileHistory(ExcelFile excelFile) {
        ExcelFileHistory historyExcelFile = ExcelFileHistory.historyRegister()
                .excelFile(excelFile)
                .build();
        excelFileHistoryRepository.save(historyExcelFile);
        return historyExcelFile;
    }

    private void excelParsingAndSave(ExcelFile excelFile, Workbook workbook) {
        List<ExcelDataDto> dataList = new ArrayList<>();
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            if (row != null) {
                String username = row.getCell(0).getStringCellValue();
                String phoneNumber = row.getCell(1).getStringCellValue();
                if (!username.equals("") || !phoneNumber.equals("")) {
                    if (Boolean.FALSE.equals(validCheck.isUsername(username))) {
                        log.warn("이름이 유효하지 않습니다.");
                        throw new GlobalException(ErrorCode.NOT_VALID_EXCEL_USERNAME);
                    } else if (Boolean.FALSE.equals(validCheck.isPhoneNumber(phoneNumber))) {
                        log.warn("전화번호가 유효하지 않습니다.");
                        throw new GlobalException(ErrorCode.NOT_VALID_EXCEL_PHONE_NUMBER);
                    } else {
                        ExcelDataDto excelData = ExcelDataDto.excelDataRegister()
                                .username(username)
                                .phoneNumber(phoneNumber)
                                .excelFileId(excelFile.getExcelFileId())
                                .build();
                        dataList.add(excelData);
                    }
                }
            }
        }
        excelDataRepository.bulkInsert(dataList);
    }
}
