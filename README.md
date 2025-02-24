# SPRING ADVANCED

## Lv.1-1 코드 개선 - Early Return

>해결과정 : https://dandy-tiger-0ef.notion.site/Lv-1-1-Early-Return-1a4970eb707b80d3be6ad0944e442c7c
---

>파일 위치
>
> package org.example.expert.domain.auth.service.AuthService
1. 애초에 이메일이 중복된다면 회원가입이 진행되지 않음.
2. 이메일 중복 체크 코드가 우선 실행되도록 로직 최상단에 위치.


## Lv.1-2 리팩토링 - 불필요한 if-else 피하기
>해결 과정 : https://dandy-tiger-0ef.notion.site/Lv-1-2-if-else-1a4970eb707b8069a467eeb3731ce816
---

>파일 위치
> 
> package org.example.expert.client.WeatherClient
1. 절차적 코드 실행에서, 첫번째 if 문이 실행되면 그 이후 코드는 실행되지 않음.
2. 기존의 로직에서 만약 첫번째 if 문을 통과했다면, 두번째 if문은 실행되지 않음.
3. 하지만 responseEntity 객체의 상태코드가 OK가 아니라면, weatherArray 또한 의미가 없어짐. 또는 responseEntity 객체에 상태코드가 OK여도 데이터가 없는 경우가 발생 할 수 있음.
4. 따라서, 두 조건문을 분리하여도 순서만 올바르다면 3번의 조건을 체크할 수 있음.
5. 4번을 이행하기 위해 기존 if-else 문을 2개의 if문으로 순서에 맞게 분리.


## Lv.1-3 코드 개선 - Validation
>해결 과정 : https://dandy-tiger-0ef.notion.site/Lv-1-3-Validation-1a4970eb707b806c9376c82e915903b7?pvs=74
---

>파일 위치
> 
> package org.example.expert.domain.user.service.UserService
> 
> package org.example.expert.domain.user.dto.request.UserChangePasswordRequest
> 
> package org.example.expert.domain.user.controller.UserController
> 
> package org.example.expert.config.GlobalExceptionHandler
1. 불필요한 검증이 비즈니스 로직에 혼재되어 있음.
2. RequestDto에서 @Valid 어노테이션을 활용하여, 비즈니스 로직의 불필요한 코드를 줄일 수 있음.
3. @Valid 가 적용될 수 있도록, 컨트롤러의 매개변수에 어노테이션 추가
4. @ControllerAdvice 에서 처리할 수 있도록 @Valid 관련 핸들러 매서드 추가.