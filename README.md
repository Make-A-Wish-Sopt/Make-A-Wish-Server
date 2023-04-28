# Make-A-Wish-Server
솝텀 Make A Wish 서버

## 프로젝트 개요

### 🛠Used Stacks
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat-square&logo=Thymeleaf&logoColor=005F0F"/>&nbsp;<img src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/SpringDataJPA-007396?style=flat-square&logo=SpringDataJPA&logoColor=6DB33F"/>&nbsp;


### 💻 Code Convention
[NAVER 코드 컨벤션](https://naver.github.io/hackday-conventions-java/) 


### ✉️ Commit Convention

```
[CHORE] 코드 수정, 내부 파일 수정 
[FEAT] 새로운 기능 구현 
[ADD] Feat 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성 시, 에셋 추가
[HOTFIX] issue나, QA에서 급한 버그 수정에 사용
[FIX] 버그, 오류 해결
[REMOVE] 쓸모없는 코드 삭제 
[DOCS] README나 WIKI 등의 문서 개정
[MOVE] 프로젝트 내 파일이나 코드의 이동 
[RENAME] 파일 이름, 변수명, 함수명 변경이 있을 때 사용합니다. 
[REFACTOR] 전면 수정이 있을 때 사용합니다 
```

### 💡Git Working Process
1. issue를 등록한다.
2. 깃 컨벤션에 맞게 Branch를 생성한다.
3. Add - Commit - Push - Pull Request 의 과정을 거친다.
3. 작업이 완료되었다면, Remote Repository(Github)에 해당 브랜치를 올린다.
4. Github에서 PR을 생성한다.
5. CI 테스트를 잘 통과했다면, 해당 PR에 관한 리뷰를 요청한다.
6. 리뷰에서 Approve를 받지 못했다면, 수정 사항을 처리해서 다시 올린다. 
7. Approve를 받았다면, Merge를 진행한다.
8. merge된 Branch는 삭제한다.
9. 종료된 Issue와 Pull Request의 Label과 Project를 관리한다.

### 🌴 브랜치
---
#### 📌 브랜치 단위
- 브랜치 단위 = 이슈 단위 = PR단위

#### 📌 브랜치명
- 브랜치는 뷰 단위로 생성합니다.
- 브랜치 규칙 → {이름}_#{#이슈번호_간단한 설명}
- `ex) donut_#1-login`

#### 🛠 Development Environment
<p align="left">
<img src ="https://img.shields.io/badge/Thymeleaf-5.0-005F0F">
<img src ="https://img.shields.io/badge/SpringBoot-13.3-6DB33F">
<img src ="https://img.shields.io/badge/SpringDataJPA-15.0-6DB33F">
<img src ="https://img.shields.io/badge/Java-17.0-white">
