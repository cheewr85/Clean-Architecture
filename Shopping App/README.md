### 쇼핑앱
- 네트워크 통신을 통해 받아온 Mock데이터를 기반으로 실제 상품 리스트 목록처럼 화면 구현

- 상품 목록을 누를 경우 Mock데이터를 기반으로 상세화면을 구현하고 주문하기를 통해 로그인 한 유저의 상품 리스트에 담을 수 있음

- 유저에 대해서 로그인하는 프로필 화면 별도로 구현 이 부분은 Firebase를 활용하여 구글 로그인을 구현함

- 로그인한 정보를 바탕으로 유저 정보를 저장하고 상품 목록을 보여주는 기능 구현

- 마찬가지로 Clean Architecture 기반으로 만듬 & 앱 아키텍쳐 AAC-Repository 패턴으로 진행

- Data & Domain & Presentation 레이어로 구성함

### 기술 스택
- Clean Architecture 구성

  - data

     - db

     - entity

     - network

     - preferences

     - repository

     - response

  - domain

     - UseCase

  - presentation

     - adapter

     - detail

     - list

     - profile

     - main

  - di

- MVVM 패턴 구성

  - ViewModel

  - LiveData

  - Repository

- 의존성 주입(DI)

  - Koin

- 네트워크 통신

  - Okhttp3 & Retrofit2

- 비동기 처리

  - Coroutine

- 새로고침 레이아웃

  - Swipe Refresh Layout

- 이미지 처리

  - Glide

- 로컬 DB

  - Room DB

- 구글 로그인 구현

  - Firebase

## 레이어 구성
- 앞서 만든 ToDo앱과 로직상으로는 유사하기 때문에 세부적인 코드는 생략하고 구성을 설명

### Data Layer
- db패키지의 경우 RoomDB 사용에 필요한 dao와 database 의존성 주입이 필요한 provideDB구현

- entity의 경우 실제 사용할 데이터를 담을 데이터 클래스

- network는 Retrofit을 통해 사용할 Url과 네트워크 통신 처리를 하기 위한 인터페이스와 의존성 주입에 필요한 provideAPI 구현

- preferences의 경우 외부 소셜 로그인을 통해 받은 토큰값을 저장하고 활용할 수 있는 등 내부에 저장해서 사용할 수 있도록 Preferences 클래스

- repository는 Usecase 등 실제 처리할 네트워크 통신을 매개해서 처리할 수 있게 해주는 클래스

- response는 네트워크 통신을 통해 받은 결과값을 담기 위한 dto클래스들

### Domain Layer
- 실제 필요한 기능에 대한 비즈니스 로직 단위로 정의한 UseCase가 모여있는 부분, presentation 부분에서 필요한 기능에 대해서 UseCase를 불러오고 UseCase에서는 해당하는 비즈니스 로직이 작동하게 Repository의 요청해서 처리하고 그 결과를 리턴함(주로 ViewModel에서 사용함)

### Presentation Layer
- 각 화면별로 기능별로 패키지를 나눠서 진행함

- 그 전에 Base 클래스를 만들어두어 공통적으로 적용할 사항에 대해서 미리 구현하고 이를 각 화면이 상속받게끔 처리함, 공통사항을 구현해서 실제 구현하는 구현체 클래스에서 이 공통사항을 신경 쓰지 않고 비즈니스 로직과 UI 변화만 처리해주면 됨

- 그리고 State 패턴을 사용하여 데이터를 받은 것을 기준으로 상태 처리를 진행하고 이를 바탕으로 UI 변화를 이끌어내도록 함, 주로 ViewModel에서는 데이터 처리를 기존 Activity나 Fragment에서는 UI 변화에 포커스를 맞춤

### 기타
- di패키지와 Application 클래스에서는 Koin을 활용한 의존성 주입을 준비하는 작업을 진행함

- extension패키지의 경우 자주 쓸 부분과 처리하는데 클래스에서 만들고 처리하기 길고 반복적으로 처리되는 부분을 확장함수 형태로 빼서 별도로 처리한 부분임

- 마지막 Utility패키지에서는 넘겨받은 Date를 쉽게 처리하기 위해 만들어둔 클래스임