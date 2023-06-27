# 🍸 compose-cocktail-recipes

![Android icon](https://img.shields.io/badge/android-808080?style=for-the-badge&logo=android&logoColor=3ddc84) ![kotlin icon](https://img.shields.io/badge/kotlin-808080?style=for-the-badge&logo=kotlin&logoColor=7f52ff) ![Jetpack Compose icon](https://img.shields.io/badge/jetpack_compose-808080?style=for-the-badge&logo=jetpackcompose&logoColor=4285f4) ![MUI icon](https://img.shields.io/badge/mui-808080?style=for-the-badge&logo=mui&logoColor=007fff)

## 🏀 status
[![Android CI](https://github.com/gogoadl/composecocktailrecipes/actions/workflows/android.yml/badge.svg)](https://github.com/gogoadl/composecocktailrecipes/actions/workflows/android.yml) ![API Version](https://img.shields.io/badge/API-21+-blue)

compose-cocktail-recipes demonstrates modern Android development with Jetpack (Compose, ViewModel), Flow, Hilt, Coil, Retrofit
based on MVVM architecture.

I was inspired by developer [skydoves](https://github.com/skydoves)'s project [Pokedex](https://github.com/skydoves/Pokedex).

The project's database used [ThecocktailDB](https://www.thecocktaildb.com/)

![녹화_2023_06_27_17_22_21_409](https://github.com/gogoadl/compose-cocktail-recipes/assets/49335446/8c11d9e0-0376-49f8-bd4b-d31b6bb7bcca)


## 📚 Tech stacks & libraries

+ MVVM Architecture
+ Jetpack Compose
+ Flow
+ Retrofit
+ Hilt (DI)
+ Gson
+ Coil
+ Timber
+ Github Action (CI/CD)

## 🏛️ Architecture

<img src="https://github.com/gogoadl/compose-cocktail-recipes/assets/49335446/74fd293e-cdd7-48b2-b863-522c240b5e65" width="650" height="400">

### Architecture

일반적인 아키텍처 원칙을 고려하여 각 애플리케이션에는 레이어가 두 개 이상 있어야 합니다.

화면에 애플리케이션 데이터를 표시하는 UI 레이어
앱의 비즈니스 로직을 포함하고 애플리케이션 데이터를 노출하는 데이터 레이어

<img src="https://github.com/gogoadl/compose-cocktail-recipes/assets/49335446/fddd2576-778c-41cb-9322-e77a3b92d32e" width="650" height="400">

### UI Layer

UI 레이어(또는 프레젠테이션 레이어)의 역할은 화면에 애플리케이션 데이터를 표시하는 것입니다. 
사용자 상호작용(예: 버튼 누르기) 또는 외부 입력(예: 네트워크 응답)으로 인해 데이터가 변할 때마다 변경사항을 반영하도록 UI가 업데이트되어야 합니다.

UI 레이어는 다음 두 가지로 구성됩니다.

+ 화면에 데이터를 렌더링하는 UI 요소. 이러한 요소는 뷰 또는 Jetpack Compose 함수를 사용하여 빌드할 수 있습니다.
+ 데이터를 보유하고 이를 UI에 노출하며 로직을 처리하는 상태 홀더(예: ViewModel 클래스)

<img src="https://github.com/gogoadl/compose-cocktail-recipes/assets/49335446/ac060e39-5754-4073-b881-e744ecb2f381" width="650" height="400">

## Data Layer

앱의 데이터 레이어에는 비즈니스 로직이 포함되어 있습니다. 비즈니스 로직은 앱에 가치를 부여하는 요소로, 앱의 데이터 생성, 저장, 변경 방식을 결정하는 규칙으로 구성됩니다.

저장소 클래스에서 담당하는 작업은 다음과 같습니다.

+ 앱의 나머지 부분에 데이터 노출
+ 데이터 변경사항을 한 곳에 집중
+ 여러 데이터 소스 간의 충돌 해결
+ 앱의 나머지 부분에서 데이터 소스 추상화
+ 비즈니스 로직 포함

<img src="https://github.com/gogoadl/compose-cocktail-recipes/assets/49335446/0aafef67-89cc-47c9-a5e3-e9b6a53ca03e" width="650" height="400">

## CI / CD





## 💎 Download

you can download this app on [Releases](https://github.com/gogoadl/compose-cocktail-recipes/releases)
