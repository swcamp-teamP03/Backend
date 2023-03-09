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
import com.example.swcamp_p03.customerGroup.entity.CustomerGroup;
import com.example.swcamp_p03.customerGroup.entity.CustomerProperty;
import com.example.swcamp_p03.customerGroup.entity.ExcelData;
import com.example.swcamp_p03.customerGroup.entity.ExcelFile;
import com.example.swcamp_p03.customerGroup.entity.history.CustomerGroupHistory;
import com.example.swcamp_p03.customerGroup.entity.history.CustomerPropertyHistory;
import com.example.swcamp_p03.customerGroup.entity.history.ExcelDataHistory;
import com.example.swcamp_p03.customerGroup.entity.history.ExcelFileHistory;
import com.example.swcamp_p03.customerGroup.repository.CustomerGroupRepository;
import com.example.swcamp_p03.customerGroup.repository.CustomerPropertyRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelDataRepository;
import com.example.swcamp_p03.customerGroup.repository.ExcelFileRepository;
import com.example.swcamp_p03.customerGroup.repository.history.CustomerGroupHistoryRepository;
import com.example.swcamp_p03.customerGroup.repository.history.CustomerPropertyHistoryRepository;
import com.example.swcamp_p03.customerGroup.repository.history.ExcelDataHistoryRepository;
import com.example.swcamp_p03.customerGroup.repository.history.ExcelFileHistoryRepository;
import com.example.swcamp_p03.customerGroup.entity.ExcelDownload;
import com.example.swcamp_p03.user.entity.User;
import com.example.swcamp_p03.customerGroup.repository.ExcelDownloadRepository;
import com.example.swcamp_p03.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupWriteService {
    private final CustomerGroupRepository customerGroupRepository;
    private final CustomerPropertyRepository customerPropertyRepository;
    private final ExcelFileRepository excelFileRepository;
    private final ExcelDataRepository excelDataRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomerGroupHistoryRepository customerGroupHistoryRepository;
    private final CustomerPropertyHistoryRepository customerPropertyHistoryRepository;
    private final ExcelDataHistoryRepository excelDataHistoryRepository;
    private final ExcelFileHistoryRepository excelFileHistoryRepository;
    private final ValidCheck validCheck;
    private final ExcelDownloadRepository excelDownLoadRepository;

    private final String EXCEL_DIR = System.getProperty("user.dir") + "/excelFile/";

    // group 작성
    public void groupWrite(UserDetailsImpl userDetails, GroupWriteRequestDto requestDto) throws IOException {
        // 절대 경로에 폴더 생성
        File folder = new File(EXCEL_DIR);
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

        //1. 엑셀파일 저장
        MultipartFile file = requestDto.getFile();
        String orgName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(orgName);
        String savedName = UUID.randomUUID() + "." + extension;
        String savedPath = EXCEL_DIR + savedName;

        String fileSize = file.getSize() / 1000 + "KB";
        ExcelFile excelFile = ExcelFile.register()
                .excelFileOrgName(orgName)
                .excelFileSavedName(savedName)
                .excelFileSavedPath(savedPath)
                .excelFileSize(fileSize)
                .build();
        excelFileRepository.save(excelFile);

        //2. 엑셀파일인 경우 절대경로에 파일 업로드
        if (extension != null && !extension.equals("xlsx") && !extension.equals("xls")) {
            throw new GlobalException(ErrorCode.NOT_EXCEL_FILE);
        }
        InputStream fileInputStream = file.getInputStream();
        file.transferTo(new File(savedPath));

        //3. 엑셀파일 파싱해서 데이터로 저장
        Workbook workbook = null;
        workbook = new XSSFWorkbook(fileInputStream);
        List<ExcelDataDto> dataList = new ArrayList<>();
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            String username = worksheet.getRow(i).getCell(0).getStringCellValue();
            String phoneNumber = worksheet.getRow(i).getCell(1).getStringCellValue();
            if (!username.equals("") || !phoneNumber.equals("")) {
                if (!validCheck.isUsername(username)) {
                    throw new GlobalException(ErrorCode.NOT_VALID_EXCEL_USERNAME);
                } else if (!validCheck.isPhoneNumber(phoneNumber)) {
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
        excelDataRepository.bulkInsert(dataList);

        //4. group 저장
        CustomerGroup customerGroup = CustomerGroup.register()
                .customerGroupName(requestDto.getGroupName())
                .favorite(false)
                .user(userDetails.getUser())
                .excelFile(excelFile)
                .build();
        customerGroupRepository.save(customerGroup);

        //5. property 저장
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

    // group 수정
    @Transactional
    public void groupUpdate(Long groupId, GroupWriteRequestDto requestDto, UserDetailsImpl userDetails) throws IOException {
        CustomerGroup findGroup = customerGroupRepository.findByCustomerGroupIdAndUser(groupId, userDetails.getUser()).orElseThrow(
                () -> new GlobalException(ErrorCode.CANT_EDIT)
        );
        if (findGroup.getUnableEdit()) {
            throw new GlobalException(ErrorCode.CANT_EDIT);
        }
        // 업데이트 가장 최근 파일 불러옴

        MultipartFile multipartFile = requestDto.getFile();

        //파일 업로드 존재 여부
        if (!multipartFile.isEmpty()) {
            //파일 업로드 O --> 파일 수정, 데이터 수정, property 수정, group 수정

            ExcelFile excelFile = findGroup.getExcelFile();

            // 엑셀파일 히스토리 저장 -> 엑셀데이터 히스토리 저장 -> 그룹 히스토리 저장 -> 그룹 속성 히스토리 저장
            ExcelFileHistory historyExcelFile = ExcelFileHistory.historyRegister()
                    .excelFile(excelFile)
                    .build();
            excelFileHistoryRepository.save(historyExcelFile);
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
            CustomerGroupHistory historyCustomerGroup = CustomerGroupHistory.historyRegister()
                    .customerGroup(findGroup)
                    .excelFileHistory(historyExcelFile)
                    .build();
            customerGroupHistoryRepository.save(historyCustomerGroup);
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

            String orgName = multipartFile.getOriginalFilename();
            String extension = FilenameUtils.getExtension(orgName);
            String savedName = UUID.randomUUID().toString() + "." + extension;
            String savedPath = EXCEL_DIR + savedName;
            String fileSize = requestDto.getFile().getSize() / 1000 + "KB";

            //1. 엑셀 데이터 비우기
            excelDataRepository.deleteByExcelFile(excelFile);

            //2. 절대경로에 있는 엑셀파일 삭제하기
            File file = new File(excelFile.getExcelFileSavedPath());
            file.delete();

            //3. 엑셀 파일 수정 --> 엑셀 파일 히스토리 저장
            excelFile.update(orgName, savedName, savedPath, fileSize);

            //4. 엑셀 파일인 경우만 업로드하고 절대경로에 저장
            if (!extension.equals("xlsx") && !extension.equals("xls")) {
                throw new IOException("엑셀파일만 업로드 해주세요");
            }
            InputStream fileInputStream = multipartFile.getInputStream();
            multipartFile.transferTo(new File(savedPath));

            //5. 엑셀 파일 파싱하고 엑셀 데이터 저장하기 -> 수정된 엑셀 데이터 히스토리 저장
            Workbook workbook = null;
            if (extension.equals("xlsx") || extension.equals("xls")) {
                workbook = new XSSFWorkbook(fileInputStream);
            }
            List<ExcelDataDto> dataList = new ArrayList<>();
            Sheet worksheet = workbook.getSheetAt(0);
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                String username = worksheet.getRow(i).getCell(0).getStringCellValue();
                String phoneNumber = worksheet.getRow(i).getCell(1).getStringCellValue();
                if (!username.equals("") || !phoneNumber.equals("")) {
                    if (!validCheck.isUsername(username)) {
                        throw new GlobalException(ErrorCode.NOT_VALID_EXCEL_USERNAME);
                    } else if (!validCheck.isPhoneNumber(phoneNumber)) {
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
            excelDataRepository.bulkInsert(dataList);

            //6. property 삭제
            customerPropertyRepository.deleteAll(findGroup.getPropertyList());

            //7. property 저장 -> 수정된 property 히스토리 저장
            List<CustomerProperty> customerProperties = new ArrayList<>();
            for (int i = 0; i < requestDto.getProperties().size(); i++) {
                CustomerProperty customerProperty = CustomerProperty.register()
                        .property(requestDto.getProperties().get(i))
                        .customerGroup(findGroup)
                        .build();
                customerProperties.add(customerProperty);
            }
            customerPropertyRepository.saveAll(customerProperties);
        } else {
            //파일 업로드 X --> group 수정, property 수정

            // 엑셀파일 히스토리 저장 -> 엑셀데이터 히스토리 저장 -> 그룹 히스토리 저장 -> 그룹 속성 히스토리 저장
            ExcelFileHistory historyExcelFile = ExcelFileHistory.historyRegister()
                    .excelFile(null)
                    .build();
            excelFileHistoryRepository.save(historyExcelFile);
            ExcelDataHistory historyExcelData = ExcelDataHistory.historyRegister()
                    .excelData(null)
                    .excelFileHistory(historyExcelFile)
                    .build();
            excelDataHistoryRepository.save(historyExcelData);
            CustomerGroupHistory historyCustomerGroup = CustomerGroupHistory.historyRegister()
                    .customerGroup(findGroup)
                    .excelFileHistory(historyExcelFile)
                    .build();
            customerGroupHistoryRepository.save(historyCustomerGroup);
            List<CustomerProperty> propertyList = findGroup.getPropertyList();
            List<CustomerPropertyHistory> historyPropertyList = new ArrayList<>();
            for (CustomerProperty customerProperty : propertyList) {
                historyPropertyList.add(
                        CustomerPropertyHistory.historyRegister()
                                .customerProperty(customerProperty)
                                .customerGroupHistory(historyCustomerGroup)
                                .build()
                );
            }
            customerPropertyHistoryRepository.saveAll(historyPropertyList);

            //1. property 삭제
            customerPropertyRepository.deleteAll(findGroup.getPropertyList());

            //2. property 저장 -> 수정된 property 히스토리 저장
            List<PropertyDto> properties = requestDto.getProperties();
            if (properties != null) {
                List<CustomerProperty> customerProperties = new ArrayList<>();
                for (PropertyDto property : properties) {
                    CustomerProperty customerProperty = CustomerProperty.register()
                            .property(property)
                            .customerGroup(findGroup)
                            .build();
                    customerProperties.add(customerProperty);
                }
                customerPropertyRepository.saveAll(customerProperties);
            }
        }
        findGroup.update(requestDto.getGroupName());
    }

    // 다운로드
    public ResponseEntity<Resource> fileDownload(Long groupId, UserDetailsImpl userDetails, FileDownloadRequestDto requestDto) throws MalformedURLException {
        User user = userRepository.findByEmail(userDetails.getUser().getEmail()).get();
        CustomerGroup customerGroup = customerGroupRepository.findById(groupId).get();
        ExcelFile excelFile = customerGroup.getExcelFile();
        boolean matches = bCryptPasswordEncoder.matches(requestDto.getPassword(), user.getPassword());
        if (matches) {
            UrlResource resource = new UrlResource("file:" + excelFile.getExcelFileSavedPath());
            String encodedFileName = UriUtils.encode(excelFile.getExcelFileOrgName(), StandardCharsets.UTF_8);
            // 파일 다운로드 대화상자가 뜨도록 하는 헤더를 설정해주는 것
            // Content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 준다.
            String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
            ExcelDownload download = new ExcelDownload(requestDto.getDownloadReason(), user);
            excelDownLoadRepository.save(download);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
        } else {
            throw new GlobalException(ErrorCode.NOT_VALID_DOWNLOAD);
        }
    }

    // 즐겨찾기
    @Transactional
    public void favoriteCheck(Long groupId) {
        CustomerGroup customerGroup = customerGroupRepository.findById(groupId).get();
        customerGroup.setFavorite(!customerGroup.getFavorite());
    }

}
