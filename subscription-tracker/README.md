\# 📊 Subscription Tracker



O aplicație web modernă pentru gestionarea abonamentelor personale, dezvoltată în Java cu Spring Boot.



!\[Dashboard](screenshots/dashboard.png)



\## ✨ Funcționalități



\- ✅ \*\*CRUD complet\*\* - Adaugă, vizualizează, editează și șterge abonamente

\- ✅ \*\*Calcul automat\*\* - Costuri lunare și anuale calculate automat

\- ✅ \*\*Grafic interactiv\*\* - Vizualizare cheltuieli pe categorii (Doughnut Chart)

\- ✅ \*\*Filtrare și sortare\*\* - Caută după nume/categorie, sortează după preț/dată

\- ✅ \*\*Validări\*\* - Verificare date server-side cu mesaje în română

\- ✅ \*\*Verificare duplicat\*\* - Previne adăugarea aceluiași abonament de două ori

\- ✅ \*\*REST API\*\* - 14 endpoint-uri pentru integrări externe

\- ✅ \*\*Design modern\*\* - Interfață Dark Mode responsivă



\## 🛠️ Tehnologii utilizate



| Categorie | Tehnologii |

|-----------|------------|

| \*\*Backend\*\* | Java 17, Spring Boot 3.2, Spring MVC, Spring Data JPA |

| \*\*Frontend\*\* | Thymeleaf, HTML5, CSS3, JavaScript, Chart.js |

| \*\*Baza de date\*\* | H2 Database (embedded) |

| \*\*Build\*\* | Maven |

| \*\*IDE\*\* | IntelliJ IDEA |

| \*\*Testare API\*\* | Postman |



\## 📁 Structura proiectului

```

src/main/java/com/awj/proiect/subscription\_tracker/

├── controller/

│   ├── SubscriptionController.java    # REST API

│   └── SubscriptionWebController.java # Web interface

├── model/

│   ├── Subscription.java              # Entitatea principală

│   ├── Category.java                  # Enum categorii

│   └── BillingCycle.java              # Enum frecvențe

├── repository/

│   └── SubscriptionRepository.java    # Acces baza de date

├── service/

│   └── SubscriptionService.java       # Logica de business

└── SubscriptionTrackerApplication.java

```



\## 🚀 Cum rulezi aplicația



\### Cerințe:

\- Java 17+

\- Maven



\### Pași:



1\. \*\*Clonează repository-ul:\*\*

```bash

git clone https://github.com/nedelcubianca/Subscription-Tracker.git

cd Subscription-Tracker

```



2\. \*\*Rulează aplicația:\*\*

```bash

mvn spring-boot:run

```



3\. \*\*Deschide în browser:\*\*

```

http://localhost:8081

```



4\. \*\*H2 Console (opțional):\*\*

```

http://localhost:8081/h2-console

JDBC URL: jdbc:h2:file:~/subscriptionDB

User: sa

Password: password

```



\## 📸 Screenshots



\### Dashboard

Pagina principală cu lista abonamentelor și statistici.



!\[Dashboard](screenshots/dashboard.png)



\### Formular adăugare/editare

Formular cu validări pentru gestionarea abonamentelor.



!\[Formular](screenshots/form.png)



\### Validări

Mesaje de eroare în limba română pentru date invalide.



!\[Validări](screenshots/validation.png)



\### Grafic categorii

Distribuția cheltuielilor pe categorii.



\## 🔌 REST API



\### Endpoint-uri disponibile:



| Metodă | Endpoint | Descriere |

|--------|----------|-----------|

| GET | `/api/subscriptions` | Lista toate abonamentele |

| GET | `/api/subscriptions/{id}` | Obține un abonament |

| POST | `/api/subscriptions` | Creează abonament nou |

| PUT | `/api/subscriptions/{id}` | Actualizează abonament |

| DELETE | `/api/subscriptions/{id}` | Șterge abonament |

| GET | `/api/subscriptions/stats/total-monthly` | Cost lunar total |

| GET | `/api/subscriptions/stats/total-yearly` | Cost anual total |

| GET | `/api/subscriptions/stats/by-category` | Costuri pe categorii |

| GET | `/api/subscriptions/stats/most-expensive` | Cel mai scump |

| GET | `/api/subscriptions/stats/cheapest` | Cel mai ieftin |

| GET | `/api/subscriptions/upcoming` | Scadențe în 7 zile |

| GET | `/api/subscriptions/category/{cat}` | Filtrare categorie |



\## 📂 Categorii disponibile



\- 🎬 \*\*ENTERTAINMENT\*\* - Netflix, Spotify, HBO

\- 🏠 \*\*UTILITIES\*\* - Internet, Telefonie, Electricitate

\- 📚 \*\*EDUCATION\*\* - Cursuri online, Duolingo

\- 💪 \*\*HEALTH\*\* - Sală, Abonamente medicale

\- 💻 \*\*SOFTWARE\*\* - Microsoft 365, Adobe, JetBrains

\- 🚇 \*\*TRANSPORT\*\* - Metrou, STB, Bolt

\- 📦 \*\*OTHER\*\* - Altele



\## 👩‍💻 Autor



\*\*Nedelcu Bianca-Nicoleta\*\*



\- 🎓 Universitatea POLITEHNICA din București

\- 📚 Facultatea de Automatică și Calculatoare

\- 👥 Grupa 332AA



---



\*Proiect realizat pentru disciplina "Aplicații Web cu suport Java" - 2025\*

