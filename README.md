# 📌 Instagram-Spring
## [Numble 챌린지] [네카라쿠배 개발자와 함께 Spring으로 인스타그램 서버 만들기](https://www.numble.it/deepdive/38)
인스타그램의 기능들을 Spring으로 구현하는 서버입니다.  


Rest API 서버로 좀 더 Spring에 집중하여 구현하기 위해 클라이언트는 따로 있지않습니다.  


자세한 구현 내용은 [ISSUES](https://github.com/Kwoojong/Instagram-Spring/issues)와 [PR](https://github.com/Kwoojong/Instagram-Spring/pulls)에서 확인하실 수 있습니다.  


컨벤션이나 프로젝트의 소개는 [WIKI](https://github.com/Kwoojong/Instagram-Spring/wiki)에서 확인하실 수 있습니다.  


배포된 [Rest API 문서](http://52.79.50.196:8081/docs/index.html)를 통해 서버의 API 목록을 확인하실 수 있습니다.  

## 📖 프로젝트 목표
- Github Actions로 CI / CD 파이프라인을 구성해 배포 자동화한다.
- 커밋 컨벤션과 클린코드의 규칙을 지켜서 프로젝트를 완수한다.
- 절대 하드코딩 하지 않으며 단위테스트를 잘 작성한다.
- 각 API의 기능들을 기능적으로 잘 동작할 수 있게 고민해서 로직을 작성한다.

## 📖 개발 상세 내용 
- [01. 프로젝트 컨벤션](https://github.com/Kwoojong/Instagram-Spring/wiki/01.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%BB%A8%EB%B2%A4%EC%85%98)  
- [02. CI / CD](https://github.com/Kwoojong/Instagram-Spring/wiki/02.-CI---CD)  
- [03. JWT 인증하기 시나리오](https://github.com/Kwoojong/Instagram-Spring/wiki/03.-JWT-%EC%9D%B8%EC%A6%9D%ED%95%98%EA%B8%B0-flow)  
- [04. Rest API 설계](https://github.com/Kwoojong/Instagram-Spring/wiki/04.-Rest-API-%EC%84%A4%EA%B3%84)  
- [05. ERD 및 Entity 관계도](https://github.com/Kwoojong/Instagram-Spring/wiki/05.-ERD-%EB%B0%8F-Entity%EA%B4%80%EA%B3%84%EB%8F%84)  
- [06. 프로젝트 아키텍처 소개](https://github.com/Kwoojong/Instagram-Spring/wiki/06.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EC%86%8C%EA%B0%9C)

## 📖 배포 환경
- Java : openJDK 17
- Spring boot 3.0.5
- AWS EC2 micro.t2, RAM 1GB, vCPU 1, 볼륨 20GiB
- Database : MySQL Community (AWS RDS : t3.micro) RAM 1GB, vCPU 2, 볼륨 20GiB