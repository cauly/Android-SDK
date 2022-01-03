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

- API Level 30 상향
- 안드로이드 12 대응
- 전면 광고 닫기 버튼 오류 수정

공지(필독)
----
안드로이드 12 신규 정책에 따라 구글 광고ID(GAID) 획득을 위해서는 아래와 같이 업데이트 사항을 필독 부탁드립니다.

1. 구글 광고ID (GAID) 수집을 위한 퍼미션 추가 
   - manifest 에 아래 퍼미션을 추가해야만 안드로이드 12에서 광고ID 를 획득할 수 있습니다.
     <uses-permission android:name="com.google.android.gms.permission.AD_ID"/> ​​​​​
2. 카울리 안드로이드 최신 버전으로 SDK 업데이트
   - 최신 SDK 3.5.21 버전으로 업데이트를 해주셔야 안드로이드 12 환경에서 정상적인 광고ID 수집이 가능하며,
   - 구글 광고ID가 없는 경우에는 app set ID 식별자 값을 수집하여 분석 및 프로드 방어 를 문제 없이 수행 할 수 있도록 지원합니다.
   - 퍼미션 추가와 함께, 카울리 안드로이드 SDK 또한 최신 버전으로 업데이트가 필요합니다.
3. 개인정보 처리방침(Pivacy Policy) 업데이트 
   - 3rd Party SDK 가 수집하는 항목에서 디바이스 래밸의 app set ID 항목이 추가 되었습니다. 귀사 서비스의 개인정보 처리방침에 추가가 필요한 경우 업데이트를 부탁 드립니다.

문의
----

cauly SDK 설치 관련하여 문의 사항은 고객센터 **1544-8867** 또는
<cauly_SDK@fsn.co.kr> 로 문의주시기 바랍니다.

매체 운영 가이드
----

<https://www.cauly.net/index.html#/apps/etc/guide>
