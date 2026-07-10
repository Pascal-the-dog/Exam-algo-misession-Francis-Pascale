# 🎒 Pokedex - Exam Algo Mi-Session

> Application JavaFX de gestion de Pokémon, développée pour l'examen d'algorithmique (Collège de Maisonneuve). Intègre des appels API, un cache, un *rate limiting*, une UI stylisée et la persistance locale via PostgreSQL.

## 🚀 Fonctionnalités Clés

*   **Recherche & Filtrage** : Recherche en ligne par ID/Nom et filtrage local par type/génération.
*   **PokeAPI** : Récupération en direct (données et sprites).
*   **Cache & Rate Limiting** : `HashMap` pour la performance et limitation à 100 requêtes/min.
*   **Audio & UI** : Lecture des cris et interface avec 18 badges de types.
*   **Statistiques** : Visualisation via `ProgressBar`.
*   **Persistance (DAO)** : Gestion via le pattern DAO avec `Upsert` et horodatage `capture_le`.

## 🧵 Gestion de l'Asynchronisme & Performance

*   **Threads d'Arrière-Plan** : Requêtes API/SQL et téléchargements audio.
*   **Thread UI** : `Platform.runLater()` pour la fluidité.

## 🛠️ Stack Technique

*   **Java 23**
*   **OpenJFX 21**
*   **Jackson 2.17.2**
*   **PostgreSQL JDBC 42.7.4**

## 🗄️ Configuration de la Base de Données

L'application nécessite une base PostgreSQL locale nommée `pokedexdb`.

1.  **Créer la base** : `CREATE DATABASE pokedexdb;`
2.  **Appliquer le schéma** : Exécutez le script `src/main/resources/schema_dump.sql` (contient la table `public.pokemon` avec `id` en `PRIMARY KEY`).
3.  **Connexion** : `jdbc:postgresql://localhost:5432/pokedexdb` (User: `postgres`, Pass: `slcwsnbm`).

## ⚙️ Configuration & Installation

### 1. Cloner le projet (Toutes plateformes)
```bash
git clone https://github.com
cd Exam-algo-misession-Francis-Pascale
```

### 2. Initialisation du Maven Wrapper (Selon l'OS)
*   **Windows (Command Prompt / PowerShell)** :
    Aucune action requise. Utilisez directement le fichier `mvnw.cmd`.
*   **Linux / macOS** :
    Donnez les droits d'exécution au script :
    ```bash
    chmod +x mvnw
    ```

## 💻 Exécution de l'Application

Lancez l'interface graphique du Pokedex en utilisant la commande appropriée à votre système :

*   **Windows** :
    ```cmd
    mvnw.cmd exec:java -Dexec.mainClass="org.example.pokedex_francis_pascale.MainFX"
    ```
*   **Linux / macOS** :
    ```bash
    ./mvnw exec:java -Dexec.mainClass="org.example.pokedex_francis_pascale.MainFX"
    ```

## 📁 Architecture

*   `modele/` : Structures de données (`Pokemon.java`) et persistance des données (`PokemonDAO.java`).
*   `view/` : Layouts et composants graphiques codés nativement (`PokemonViewFX.java`).
*   `controller/` : Gestion événementielle et gestion asynchrone des threads (`PokemonMainController.java`).
*   `service/` : Consommation API, logique du cache mémoire et rate limiter (`PokemonApiService.java`).
*   `exceptions/` : Exceptions applicatives personnalisées.
*   `utils/` : Gestion de la connectivité SQL (`Connexion.java`) et boîtes de dialogue de confirmation.
