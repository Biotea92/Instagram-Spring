# 📌 Instagram-Spring
## [Numble 챌린지] [네카라쿠배 개발자와 함께 Spring으로 인스타그램 서버 만들기](https://www.numble.it/deepdive/38)
인스타그램의 기능들을 Spring으로 구현하는 서버입니다.  
Rest API 서버로 좀 더 Spring에 집중하여 구현하기 위해 클라이언트는 따로 있지않습니다.  
자세한 구현 내용은 [ISSUES](https://github.com/Kwoojong/Instagram-Spring/issues)와 [PR](https://github.com/Kwoojong/Instagram-Spring/pulls)에서 확인하실 수 있습니다. 컨벤션이나 기술적인 해결 방법은 [WIKI](https://github.com/Kwoojong/Instagram-Spring/wiki)에서 확인하실 수 있습니다.

## 📖 프로젝트 목표
- Github Actions로 CI / CD 파이프라인을 구성해 배포 자동화한다.
- 커밋 컨벤션과 클린코드의 규칙을 지켜서 프로젝트를 완수한다.
- 절대 하드코딩 하지 않으며 단위테스트를 잘 작성한다.
- 각 API의 기능들을 기능적으로 잘 동작할 수 있게 고민해서 로직을 작성한다.

## 📖 배포 환경
- Java : openJDK 17
- Spring boot 3.0.5
- AWS EC2 micro.t2, RAM 1GB, vCPU 1, 볼륨 20GiB
- Database : MySQL Community (AWS RDS : t3.micro) RAM 1GB, vCPU 2, 볼륨 20GiB