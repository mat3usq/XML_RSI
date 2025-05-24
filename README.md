# 📌 RSI-PROJECT1

Projekt zakłada stworzenie systemu informacyjnego dla miasta Białystok, umożliwiającego zarządzanie wydarzeniami lokalnymi. Składa się z dwóch głównych komponentów:

1. **Web Service** (SOAP)
2. **Aplikacja kliencka** (webowa lub desktopowa)

---

## 🛠️ Wymagania funkcjonalne

Serwis powinien umożliwiać:

1. 📅 Pobieranie wydarzeń dla konkretnego dnia
2. 📆 Pobieranie wydarzeń w danym tygodniu
3. 🔍 Pobieranie szczegółowych informacji o wybranym wydarzeniu
4. ➕ Dodawanie nowego wydarzenia z polami:
    - **Name** – nazwa wydarzenia
    - **Typ** – rodzaj wydarzenia
    - **Date** – data wydarzenia
    - **Week** – numer tygodnia
    - **Month** – numer miesiąca
    - **Year** – rok
    - **Opis** – opis wydarzenia
5. ✏️ Modyfikowanie informacji o istniejącym wydarzeniu
6. 📄 Generowanie zestawienia wydarzeń w formacie **PDF**

---

## 📦 Składowe projektu

- **Web Service (SOAP)**
- **Aplikacja kliencka** (okienkowa lub webowa)

---

## 📃 Dokumentacja projektu

Zawiera:

- Opis projektu
- Opis WSDL
- Przykładowe komunikaty SOAP (request/response)
- Instrukcja integracji z zewnętrznym systemem (dla klientów zewnętrznych)

---

## 📌 Wymagane technologie i funkcje

✅ Przesyłanie załączników binarnych – użycie adnotacji `@MTOM`  
✅ Implementacja `SOAP Handlers`  
✅ Prezentacja przesyłanych komunikatów SOAP na żywo – np. za pomocą **SOAP UI** lub **tcpmonitor**  
✅ Prezentacja działania systemu na dwóch komputerach lub z użyciem **maszyny wirtualnej**
✅ Wykorzystanie szyfrowania SSL/TLS – udostępnienie WS przez HTTPS
✅ Stworzenie klienta w **innym języku niż Java** (Python)

---
