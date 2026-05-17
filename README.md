# 🗒️ Lab 19 — Room, MVVM, Repository, ViewModel, LiveData et RecyclerView

**Cours** : Programmation Mobile — Android avec Java
**Auteur** : MADILI Kenza
---

## 🎯 Compétences Visées

- Distinguer une app codée directement dans l'Activity d'une app structurée en **MVVM**
- Comprendre le rôle exact de chaque couche de l'architecture
- Savoir pourquoi Room **interdit les opérations sur le thread principal**
- Comprendre ce que ViewModel résout concrètement (rotation, changement de config, recréation)
- Connaître sa limite : le **process death** (nécessite `SavedStateHandle`)

---

## 🏗️ Architecture Globale

```
UI (Activity / RecyclerView)
        ↕  observe / appelle
    ViewModel
        ↕  délègue
    Repository
        ↕  requêtes SQL
    Room DAO  →  SQLite
```

### Rôle de chaque couche

| Couche | Classe | Rôle |
|---|---|---|
| **Entity** | `Note.java` | Structure des données persistées (`@Entity`) |
| **DAO** | `NoteDao.java` | Interface d'accès SQL (`@Dao`) |
| **Database** | `NoteDatabase.java` | Point central Room (`@Database`) |
| **Repository** | `NoteRepository.java` | Intermédiaire données ↔ ViewModel |
| **ViewModel** | `NoteViewModel.java` | Logique de présentation, survie aux rotations |
| **LiveData** | — | Observation automatique, lifecycle-aware |
| **RecyclerView** | `NoteAdapter.java` | Affichage performant de la liste |

---

## 📁 Organisation des Packages

```
app/
└── src/main/java/com/example/roommvvmdemo/
    ├── data/
    │   └── local/
    │       ├── Note.java
    │       ├── NoteDao.java
    │       └── NoteDatabase.java
    ├── repository/
    │   └── NoteRepository.java
    ├── viewmodel/
    │   └── NoteViewModel.java
    └── ui/
        ├── MainActivity.java
        └── NoteAdapter.java
```

---

## ✨ Fonctionnalités de l'Application

| Action | Description |
|---|---|
| ➕ Ajouter une note | Saisir un titre + une description, appuyer sur **AJOUTER** |
| 🗑️ Supprimer une note | Appui long sur une carte |
| 👁️ Voir le titre | Appui simple sur une carte |
| 🧹 Supprimer tout | Bouton **SUPPRIMER TOUTES LES NOTES** |
| 🔄 Rotation d'écran | La liste est préservée (ViewModel + Room) |

---

## 🔬 Extraits de Code Clés

### Entity
```java
@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
}
```

### DAO
```java
@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    LiveData<List<Note>> getAllNotes();
}
```

### Repository
```java
public class NoteRepository {
    private NoteDao noteDao;

    public void insert(Note note) {
        new Thread(() -> noteDao.insert(note)).start();
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }
}
```

### Observer dans l'Activity
```java
noteViewModel.getAllNotes().observe(this, notes -> {
    adapter.setNotes(notes); // UI mise à jour automatiquement
});
```

---

## 🛠️ Dépendances

```kotlin
// build.gradle.kts
val room_version = "2.6.1"
val lifecycle_version = "2.8.7"

implementation("androidx.room:room-runtime:$room_version")
annotationProcessor("androidx.room:room-compiler:$room_version")
implementation("androidx.room:room-ktx:$room_version")

implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version")
implementation("androidx.lifecycle:lifecycle-livedata:$lifecycle_version")

implementation("androidx.recyclerview:recyclerview:1.4.0")
implementation("androidx.cardview:cardview:1.0.0")
```

---

## 🔬 Tests à Réaliser
**Une vidéo de test compressée veuillez la voir : --->**
[Enregistrement de l'écran 2026-05-12 223844.zip](https://github.com/user-attachments/files/27903080/Enregistrement.de.l.ecran.2026-05-12.223844.zip)

| Scénario | Résultat attendu |
|---|---|
| Ajouter une note | Apparaît immédiatement dans la liste |
| Rotation de l'écran | La liste est conservée |
| Fermer et rouvrir l'app | Les notes persistent (Room) |
| Appui long sur une note | Note supprimée |
| Supprimer tout | Liste vide, persistance effacée |

---

## 📚 Ce que ce Lab enseigne

| Sans MVVM | Avec MVVM + Room |
|---|---|
| Tout dans l'Activity | Responsabilités séparées par couche |
| Données perdues à la rotation | ViewModel conserve l'état |
| Pas de persistance | Room persiste dans SQLite |
| UI couplée aux données | LiveData découple UI et logique |
| Accès DB sur le main thread | Opérations sur thread secondaire |

---

## ⚠️ Limite du ViewModel

> Le **ViewModel** survit aux rotations et changements de configuration,
> mais **pas au kill du processus** (*process death*).
> Pour ce cas, utiliser `SavedStateHandle` (module Saved State for ViewModel).

---

**Cours** : Programmation Mobile — Android avec Java  
**Niveau** : Lab 19 / Intermédiaire-Avancé
