## Rapport d’évaluation – Projet Pokédex
# Examen de mi‑session – Algorithmes et modèles de programmation Présenté à : Monsieur Ahmed Imed Eddine Rabah Réalisé par : Pascale et Francis


## Introduction
Dans le cadre de l’examen de mi‑session, nous avons développé un Pokédex en Java intégrant une logique métier complète, une gestion de données et plusieurs fonctionnalités avancées. Ce rapport résume la contribution de chaque membre de l’équipe, les principales difficultés rencontrées et les améliorations envisagées.

## Travail accompli par Pascale
* DAO : mise en place de la couche d’accès aux données pour la persistance des Pokémon.
* 
* Modèle Pokemon : définition de la structure interne et des attributs essentiels.
* 
* PokemonViewFX : construction de la vue principale et intégration des éléments nécessaires à l’affichage.
* 
* UI + CSS : conception et stylisation complète de l’interface, un travail majeur du projet.
* 
* Fonctions de filtrage : ajout des mécanismes permettant de sélectionner les Pokémon selon différents critères.
* 
* MainController : contribution importante à la logique centrale et à la coordination des actions.
* 
* Corrections et ajouts divers : stabilisation du projet et amélioration continue.
* 
* Documentation : rédaction des notes techniques et organisation des informations.

## Travail accompli par Francis
* PokemonApiService : développement du service de communication avec l’API et récupération des données externes.
  
* Gestion des erreurs : création d’erreurs personnalisées, gestion des timeouts, rate limiting et traitement des cas d’échec.
  
* Fonctions utilitaires : ajout d’outils internes (Capitalize, ConfirmationBox, gestion des types, MessageUtils).
  
* Conversion des types en français : adaptation des données API au contexte francophone.
  
* Système de favoris : ajout d’une fonctionnalité permettant de marquer certains Pokémon.
  
* MainController : travail important sur la logique interne et la synchronisation des données.
  
* Corrections et ajouts divers : interventions régulières pour stabiliser le projet.
  
* Multithreading + cache local : mise en place de threads pour éviter les blocages et ajout d’un cache pour optimiser les performances.

## Difficultés rencontrées
* Versions Java différentes : incompatibilités initiales entre les environnements de développement.
  
* Problèmes GitHub : pulls incomplets et écrasements accidentels de fichiers lors de push subséquents.
  
* Multithreading : mise en place complexe au début, nécessitant plusieurs ajustements.
  
* Organisation du code : nécessité de mieux séparer les responsabilités pour éviter un contrôleur trop volumineux.

## Améliorations possibles avec plus de temps
Meilleure séparation des classes métiers et d’affichage, notamment en fractionnant PokemonMainController.
Implémentation des bonus majeurs, comme les combats entre Pokémon.
Ajout d’animations pour rendre l’application plus dynamique.
Correction de certains bugs d’affichage, dont la stabilisation complète du cadre d’image.

## Conclusion
Le projet Pokédex représente un travail conséquent réalisé en équipe, combinant gestion de données, logique métier, communication API et multithreading. Malgré plusieurs défis techniques, le résultat final est fonctionnel et conforme aux attentes du cours. Avec plus de temps, plusieurs améliorations auraient pu être apportées, mais le travail accompli démontre une bonne maîtrise des concepts et une collaboration efficace.

