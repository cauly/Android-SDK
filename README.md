[Cauly Android SDK](https://github.com/cauly/Android-SDK/blob/master/Android%20SDK%20%EC%97%B0%EB%8F%99%20%EA%B0%80%EC%9D%B4%EB%93%9C.md)
====
- 최신 버전의 CAULY SDK 사용을 권장합니다. 
- 최신 버전의 Android Studio 사용을 권장합니다. Eclipse에 대한 기술 지원은 하지 않습니다.
- CAULY SDK는 Android 4.0(Ice Cream Sandwich, API Level 14) 이상 기기에서 동작합니다.
- Lifecycle에 따라 pause/resume/destroy API를 호출하지 않을 경우, 광고 수신에 불이익을 받을 수 있습니다.

Android SDK 연동
----

SDK 연동 방법은 [Android SDK 연동 가이드](https://github.com/cauly/Android-SDK/blob/master/Android%20SDK%20%EC%97%B0%EB%8F%99%20%EA%B0%80%EC%9D%B4%EB%93%9C.md) 에서 확인하실 수 있습니다.

Release Note
----

- API Level 31 상향
- 안드로이드 13 대응
- 전면 광고 사용성 개선

공지사항 (필독)
----
Google Play 정책사항 준수를 위해 아래 업데이트 사항 필독 부탁드립니다.

신규 업데이트된 안드로이드 13의 경우에도, GAID 수집을 위한 퍼미션 추가가 필요합니다. 관련 내용은 하기 안드로이드 12에 기재된 내용을 참고부탁드립니다.
안드로이드 12 정책에 따라 Google 광고ID (GAID) 획득을 위해 아래의 사항에 대한 확인 부탁드립니다.

1. Google 광고ID (GAID) 수집을 위한 퍼미션 추가 
   - manifest 파일에 아래 퍼미션을 추가해야만 안드로이드 12에서 광고ID 를 획득할 수 있습니다.
     ```xml
     <uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
     ```
2. Cauly SDK 최신 버전으로 업데이트
   - 3.5.21 이상 버전으로 업데이트를 해주셔야 안드로이드 12 환경에서 정상적인 광고ID 수집이 가능하며,
   - Google 광고ID 가 없는 경우에는 [App set ID](https://developer.android.com/training/articles/app-set-id) 값을 수집하여 분석 및 Fraud 방어를 문제 없이 수행 할 수 있도록 지원합니다.
   - 퍼미션 추가와 함께, 카울리 안드로이드 SDK 또한 최신 버전으로 업데이트가 필요합니다.
   - 로컬 저장소에 [Cauly SDK 를 수동으로 Import 하여 사용하시는 경우](https://github.com/cauly/Android-SDK/tree/master/CaulyLib) App set ID 수집을 위해 gradle 파일에 아래 Dependency 를 추가 해야 합니다.
     ```
     implementation 'com.google.android.gms:play-services-ads-identifier:17.0.0'
     implementation 'com.google.android.gms:play-services-appset:16.0.0'
     ```
3. 개인정보 처리방침 (Pivacy Policy) 업데이트 
   - 3rd Party SDK 가 수집하는 항목에서 디바이스 레벨의 App set ID 항목이 추가 되었습니다. 귀사 서비스의 개인정보 처리방침에 추가가 필요한 경우 업데이트를 부탁 드립니다.

안내사항
----
Google Play 의 데이터 보안정책 업데이트에 따라, 앱 개발사(자)는 Google 측에 해당 앱이 수집하는 데이터의 종류와 범위를 설문 양식에 작성하여 제출해야 합니다.

아래의 일정 참고하여 기한 안에 Play Console 에서 데이터 보안 양식 작성이 필요함을 알려드립니다.

- 22년 4월 말 : Google Play 스토어에서 보안섹션이 사용자에게 표기
- 22년 7월 20일 : 양식 제출 및 개인정보처리방침 승인 기한 (양식과 관련해 해결되지 않은 문제가 있는 경우 신규 앱 제출 및 앱 업데이트 거부될수 있습니다)

업데이트된 정책을 준수하실 수 있도록 카울리 SDK 에서 수집하는 데이터 항목에 대해 안내드립니다.

Cauly SDK 에서 광고 및 분석 목적으로 다음 데이터를 공유합니다.

카테고리|데이터 유형
---|---
기기 또는 기타 식별자|Android 광고 ID 를 공유
앱 활동|인스톨 된 앱
앱 정보 및 성능|오류 로그, 앱 관련 기타 진단 데이터

Cauly SDK 에서 전송하는 모든 사용자 데이터는 전송 중에 암호화 되며 사용자가 데이터 삭제를 요청할 수 있는 방편을 제공하지 않습니다.

Google Play 보안섹션 양식 작성과 관련 상세 내용은 [가이드 문서](https://github.com/cauly/Android-SDK/blob/master/GooglePlay_%E1%84%87%E1%85%A9%E1%84%8B%E1%85%A1%E1%86%AB%E1%84%89%E1%85%A6%E1%86%A8%E1%84%89%E1%85%A7%E1%86%AB_%E1%84%8B%E1%85%A3%E1%86%BC%E1%84%89%E1%85%B5%E1%86%A8_%E1%84%8C%E1%85%A1%E1%86%A8%E1%84%89%E1%85%A5%E1%86%BC_%E1%84%80%E1%85%A1%E1%84%8B%E1%85%B5%E1%84%83%E1%85%B3.pdf)를 확인 부탁드립니다.


문의
----

cauly SDK 설치 관련하여 문의 사항은 고객센터 **1544-8867** 또는
<cauly_sdk@fsn.co.kr> 로 문의주시기 바랍니다.

매체 운영 가이드
----

<https://www.cauly.net/index.html#/apps/etc/guide>
