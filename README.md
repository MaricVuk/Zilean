# Zilean
Zilean je moderna Android aplikacija dizajnirana za intuitivno i efikasno praćenje dnevnog unosa nutrijenata i hidratacije. Fokus aplikacije je na pružanju besprekornog korisničkog iskustva kroz elegantan dashboard koji u realnom vremenu vizuelizuje napredak ka postavljenim fitnes ciljevima. Uz pažljivo dizajniran Dark Mode i fluidne animacije, Zilean pretvara disciplinu praćenja ishrane u estetski prijatnu dnevnu naviku.


## Screenshots
<table align="center">
  <tr>
    <td align="center">
      <b>Light Mode</b><br>
      <img src="https://github.com/MaricVuk/Zilean/blob/feature/water/image/zilean_light_mode.png?raw=true" width="250" alt="Light Mode Dashboard">
    </td>
    <td align="center">
      <b>Dark Mode</b><br>
      <img src="https://github.com/MaricVuk/Zilean/blob/feature/water/image/zilean_dark_mode.png?raw=true" width="250" alt="Dark Mode Dashboard">
    </td>
  </tr>
</table>

### Tehnički stack i arhitektura
Projekat je izgrađen korišćenjem savremenih tehnologija i principa softverskog inženjerstva kako bi se osigurala skalabilnost i performanse:

### UI Framework
Kompletno razvijen u Jetpack Compose-u, koristeći deklarativni pristup za izgradnju reaktivnih komponenti.

### Arhitektura
Implementiran MVVM (Model-View-ViewModel) obrazac koji osigurava čisto razdvajanje poslovne logike od korisničkog interfejsa.

### Lokalno skladištenje
Korišćena Room Persistence biblioteka za robusno upravljanje SQLite bazom podataka, omogućavajući trajno čuvanje obroka, hidratacije i korisničkih podešavanja.

### Reaktivno programiranje
Napredna upotreba Kotlin Coroutines i Flow API-ja (uključujući flatMapLatest operatore) za asinhrono procesiranje podataka i automatsko osvežavanje UI-ja pri promeni stanja.

### Computer Vision
Integrisan Google ML Kit Barcode Scanning za automatizaciju unosa namirnica putem skeniranja bar-kodova u realnom vremenu.

### UI/UX Detalji
Dinamičke animacije progresa implementirane kroz animateFloatAsState, podrška za lokalizaciju i potpuno prilagodljiv tamni režim rada.


## Napravljeno sa
- [Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation)
- [Material Design 3](https://developer.android.com/develop/ui/compose/designsystems/material3)
- [Room Persistence Library](https://developer.android.com/jetpack/androidx/releases/room) 
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Kotlin Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Google ML Kit Barcode Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning)
- [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
- [Java Time API](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html)
- [SharedPreferences](https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html)

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)


 
