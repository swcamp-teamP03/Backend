## πͺ‘Β Stack πͺ‘
- Java 17
- Spring
- Spring Boot
- Spring Data JPA
- Query DSL
- MySQL
- Spring Security + JWT Token
- AWS EC2 , RDS(MySQL)
<br>

## πΒ Commit Convention π

>### git flow

> πΒ μ°Έκ³  μ¬μ΄νΈ: [μ°ν κΈ°μ  λΈλ‘κ·Έ](https://techblog.woowahan.com/2553/)
> 
1. **master**(κΈ°μ€) μ­ν μ `team-24` λΈλμΉμ΄λ€. (μ΅μ’ λ°°ν¬μ©)
2. **master**μμ κ°λ°ν  λΈλμΉμΈ `dev` μ μμ±νλ€. 
    - PR μ©μΌλ‘ μ¬μ©νλ FE, BE develop λΈλμΉκ° μλ€
    - κ°λ°μλ€μ μ΄ λΈλμΉλ₯Ό λ² μ΄μ€λ‘ κ°λ°νλ€
    - μ) BE λ `dev` λ₯Ό λ² μ΄μ€λ‘ κΈ°λ₯κ°λ° λΈλμΉλ₯Ό λ΄λ€.

1. κ°λ°ν  κΈ°λ₯μ΄ μκΈ°λ©΄ **issue** λ₯Ό μμ±νλ€
    1. **issue convention** μ°Έκ³ 

```
team-24(master)
  |
  βββ dev
	|
        βββ feat-κΈ°λ₯μ΄λ¦1
	      βββ feat-κΈ°λ₯μ΄λ¦2
        βββ feat-κΈ°λ₯μ΄λ¦3   
        βββ feat-κΈ°λ₯μ΄λ¦
            
```

>### issue

issue λ₯Ό μΈλ μμ νλ¦

1. issue μμ±
    1. μμμ λν μμΈ λ΄μ©
    2. μμμ λν μμ κΈ°λ₯λ€μ μ²΄ν¬λ°μ€λ‘ readme μμ±

```
κΈ°λ₯μ λͺ© [νκ·Έ1][νκ·Έ2]
---

## π‘ issue
[FEAT] CollectionView κ΅¬ν

## π todo
[ ] μμ1
[ ] μμ2
[ ] μμ3
```

>### commit

μμμ λν κ°λ΅ν ν€μλλ₯Ό μ λͺ© κ°μ₯ μμ μΆκ°νλ€.

μμμ μμΈν λ΄μ©μ μ λͺ©μμ νμ€ λμ°κ³  μ°λλ‘ νλ€.

- `feature`: μλ‘μ΄ κΈ°λ₯μ μΆκ°ν  κ²½μ°
- `fix`: λ²κ·Έλ₯Ό κ³ μΉ κ²½μ°
- `refactor`: νλ‘λμ μ½λ λ¦¬ν©ν°λ§
- `test`: νμ€νΈ μΆκ°, νμ€νΈ λ¦¬ν©ν°λ§ (νλ‘λμ μ½λ λ³κ²½ μμ)
- `setting` : μ΄κΈ° μν

```
ν€μλ: μμ μ λͺ© 
//κ³΅λ°±//
μμΈν λ΄μ©1 λ΄μ©λ΄μ©λ΄μ©λ΄μ©
λ΄μ©λ΄μ©222222

μ) 
feat: μ»¬λ μλ·° ViewController μ μ μ© 

μ»¬λ μλ·°λ₯Ό ViewController μ μ μ©νλ€
```
<br>

## π ERD π
```mermaid
erDiagram
  User ||--o{ CustomerGroup : ""
  User ||--o{ CopyGroup : ""
	User ||--o{ Campaign : ""
	User ||--o{ ExcelDownload : ""
	CustomerGroup  ||--o{ GroupProperty : ""
	CustomerGroup ||--|| ExcelFile : ""
	CustomerGroup ||--o{ CutomerGroupHistory : ""
	CutomerGroupHistory ||--o{ GroupPropertyHistory : ""
	CutomerGroupHistory ||--|| ExcelFileHistory : ""
	ExcelFileHistory ||--o{ ExcelDataHistory : ""
	ExcelFile ||--o{ ExcelData : ""
	CopyGroup ||--o{ GptCopy : ""
	CopyGroup ||--o{ CopyGroupHistory : ""
	CopyGroupHistory ||--o{ GptCopyHistory : ""
	Campaign ||--o{ CampaignMessage : ""
	Campaign ||--o{ SendMessages : ""
	CustomerGroup ||--o{ Campaign : ""
	CopyGroup ||--o{ Campaign : ""
	CampaignMessage ||--o{ SendMessages : ""

	User {
		Long userId PK
		String email
		String password
		String company
		String username
		String phoneNumber
		String companyNumber
		String callingNumber
		String callRejectionNumber
	}
	CustomerGroup {
		Long customerGroupId PK
    Long userId FK
		Long excelFileId FK
		String groupName
		Boolean likeCheck
		LocalDateTime createdAt
		Boolean unableEdit
	}
	CutomerGroupHistory {
		Long customerGroupHistoryId PK
		Long customerGroupId FK
		Long excelFileHistoryId FK
		String groupName
		Boolean likeCheck
		LocalDateTime createdAt
		Boolean unableEdit
  }
	GroupProperty {
		Long groupPropertyId PK
		Long customerGroupId FK
		String propertyValue
		LocalDateTime createdAt
	}
	GroupPropertyHistory {
		Long groupPropertyHistoryId PK
		Long customerGroupHistoryId FK
		String propertyValue
		LocalDateTime createdAt
  }
	ExcelFile {
		Long excelFileId PK
		String excelFileOrgName
		String excelFileSavedName
		String excelFileSavedPath
		String excelFileSize
		LocalDateTime createdAt
	}
	ExcelFileHistory {
		Long excelFileHistoryId PK
		String excelFileOrgName
		String excelFileSavedName
		String excelFileSavedPath
		String excelFileSize
		LocalDateTime createdAt
	}
	ExcelData {
		Long excelDataId PK
		Long excelFileId FK
		String phoneNumber
		LocalDateTime createdAt
	}
	ExcelDataHistory {
		Long excelDataId PK
		Long excelFileHistoryId FK
		String phoneNumber
		LocalDateTime createdAt
	}
	CopyGroup {
		Long copyGroupId PK
    Long userId FK
		String copyGroupName
		String brandName
		String productName
		String keyword
		String type
		Boolean likeCheck
		Integer createCount
		Integer copyLength
		Boolean unableEdit
		LoaclDateTime updatedAt
		LoaclDateTime createdAt
	}
	CopyGroupHistory {
		Long copyGroupHistoryId PK
		Long copyGroupId FK
		String copyGroupName
		String brandName
		String productName
		String keyword
		String type
		Boolean likeCheck
		Integer createCount
		Integer copyLength
		Boolean unableEdit
		LoaclDateTime createdAt
		LoaclDateTime updatedAt
	}
	GptCopy {
		Long gptCopyId PK
		Long copyGroupId FK
		String content
		Boolean pin
		LoaclDateTime createdAt
		LoaclDateTime updatedAt
	}
	GptCopyHistory {
		Long gptCopyHistoryId PK
		Long copyGroupHistoryId FK
		String content
		Boolean pin
		LoaclDateTime createdAt
		LoaclDateTime updatedAt
	}
	Campaign {
		Long campaignId PK
		Long userId FK
		Long customerGroupId FK
		Long CopyGroupId FK 
    String messageType
		String sendType
		String sendURL
		LocalDateTime sendingDate
		LocalDateTime createdAt
	}
	CampaignMessage {
		Long campaignMessageId PK
		Long campaignId FK
		String message
		String messageSection
	}
	SendMessages {
		Long sendMessageId PK
		Long campaginId FK
		Long campaignMessageId FK 
    LocalDateTime sendDateTime
		String name
	  String phoneNumber
		Boolean sendCheck
		String errorMessage
		String uniqueUrl
    LocalDateTime visitedTime
		LocalDate visitedDate
	}
	ExcelDownload {
		Long excelDownloadId PK
		Long userId FK
		String downloadReasonEx
		LocalDateTime createdAt
	}
	
	
```
---
# [API λͺμΈμ](https://documenter.getpostman.com/view/22820772/2s93CNNZ8b)
