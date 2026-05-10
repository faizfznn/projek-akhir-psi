# 📱 Firebase Firestore Setup Guide - SCARLA

## OPSI 1: Setup Manual via Firebase Console (⚡ Paling Cepat)

### Step 1: Buka Firebase Console

1. Go to: https://console.firebase.google.com/
2. Select project: `projek-akhir-psi`
3. Go to: **Firestore Database**

---

## Step 2: Buat Collection Structures

### A. Top-Level Collections (Root)

Buat 3 collection di level root:

#### 1. **conversations** collection

```
collections → Create collection → "conversations"
```

Dokumentnya akan auto-created saat ada chat pertama kali.

#### 2. **friend_requests** collection

```
collections → Create collection → "friend_requests"
```

Dokumentnya akan auto-created saat ada friend request pertama kali.

#### 3. **community_activities** collection

```
collections → Create collection → "community_activities"
```

Dokumentnya akan auto-created saat ada activity posting pertama kali.

---

### B. Sub-Collections dalam `users/{userId}`

Ketika user pertama kali login/signup, sub-collections ini akan auto-created oleh Firestore saat data ditambahkan:

✅ **Automatically Created** oleh sistem:

- `users/{userId}/achievements/` → Saat unlock achievement
- `users/{userId}/achievement_notifications/` → Saat achievement notification
- `users/{userId}/activities/` → Saat activity recorded
- `users/{userId}/streak/` → Saat first activity
- `users/{userId}/friends/` → Saat friend accepted
- `users/{userId}/conversations/` → Saat chat dimulai (optional, bisa di root)

---

## Step 3: Firestore Security Rules Setup

Paste rules berikut di **Firestore → Rules**:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // Allow authenticated users
    match /users/{userId} {
      // User bisa read/write data mereka sendiri
      allow read, write: if request.auth.uid == userId;

      // Sub-collections
      match /{collection=**} {
        allow read, write: if request.auth.uid == userId;
      }
    }

    // Conversations - only participants can access
    match /conversations/{conversationId} {
      allow read: if request.auth.uid in resource.data.participantIds;
      allow write: if request.auth.uid in resource.data.participantIds;

      match /messages/{messageId} {
        allow read: if request.auth.uid in get(/databases/$(database)/documents/conversations/$(conversationId)).data.participantIds;
        allow create: if request.auth.uid == request.resource.data.senderId;
        allow update: if request.auth.uid == resource.data.senderId;
      }
    }

    // Friend requests
    match /friend_requests/{requestId} {
      allow read: if request.auth.uid in [resource.data.senderId, resource.data.recipientId];
      allow create: if request.auth.uid == request.resource.data.senderId;
      allow update: if request.auth.uid == resource.data.recipientId;
    }

    // Community activities - public read
    match /community_activities/{activityId} {
      allow read: if true;
      allow create: if request.auth.uid == request.resource.data.userId;
      allow update: if request.auth.uid == resource.data.userId;

      match /comments/{commentId} {
        allow read: if true;
        allow create: if request.auth.uid == request.resource.data.userId;
      }
    }
  }
}
```

✅ **Save Rules**

---

## OPSI 2: Automatic Setup via Kotlin Code

Jika ingin auto-create saat user login, gunakan `FirestoreInitializer.kt`:

### Implementation:

**1. In LoginViewModel atau SignupViewModel:**

```kotlin
class LoginViewModel(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) : ViewModel() {

    fun loginSuccess(userId: String, userName: String, userEmail: String) {
        viewModelScope.launch {
            try {
                // Initialize Firestore collections
                val initializer = FirestoreInitializer(firestore)
                initializer.initializeUserCollections(userId, userName, userEmail)

                // Login success, navigate to home
                navigateToHome()
            } catch (e: Exception) {
                showError("Failed to setup user data: ${e.message}")
            }
        }
    }
}
```

**2. One-Time Setup** (Run di MainActivity atau splash screen):

```kotlin
// In MainActivity.kt - run once during app startup
val initializer = FirestoreInitializer(FirebaseFirestore.getInstance())
viewModelScope.launch {
    initializer.createAllCollectionsStructure()
}
```

---

## Quick Reference: Database Structure

```
Firestore Root Collections:
├── users/
│   └── {userId}/
│       ├── name: String
│       ├── email: String
│       ├── avatar: String
│       ├── stats: Map {
│       │   ├── completedMaterials: Int
│       │   ├── friendCount: Int
│       │   ├── totalPoints: Int
│       │   └── totalAchievements: Int
│       ├── createdAt: Timestamp
│       ├── achievements/ (sub-collection)
│       │   └── {achievementId}: Map {
│       │       ├── unlocked: Boolean
│       │       ├── unlockedAt: Timestamp
│       │       └── ...
│       ├── activities/ (sub-collection)
│       │   └── {activityId}: Map {
│       │       ├── date: String (yyyy-MM-dd)
│       │       ├── activityType: String
│       │       ├── points: Int
│       │       └── timestamp: Timestamp
│       ├── streak/ (sub-collection)
│       │   └── current: Map {
│       │       ├── currentStreak: Int
│       │       ├── longestStreak: Int
│       │       ├── lastActivityDate: Timestamp
│       │       └── breakDate: Timestamp?
│       ├── friends/ (sub-collection)
│       │   └── {friendId}: Map {
│       │       ├── name: String
│       │       ├── avatar: String
│       │       ├── isOnline: Boolean
│       │       └── friendSince: Timestamp
│       └── (achievement_notifications/, conversations/ auto-created)
│
├── conversations/
│   └── {conversationId}: Map {
│       ├── participantIds: Array
│       ├── participantNames: Array
│       ├── lastMessage: String
│       ├── lastMessageTimestamp: Timestamp
│       └── messages/ (sub-collection)
│           └── {messageId}: Map {
│               ├── senderId: String
│               ├── senderName: String
│               ├── content: String
│               ├── timestamp: Timestamp
│               └── reactions: Array
│
├── friend_requests/
│   └── {requestId}: Map {
│       ├── senderId: String
│       ├── senderName: String
│       ├── recipientId: String
│       ├── status: String (PENDING/ACCEPTED/REJECTED)
│       ├── sentAt: Timestamp
│       └── message: String
│
└── community_activities/
    └── {activityId}: Map {
        ├── userId: String
        ├── userName: String
        ├── activityType: String
        ├── title: String
        ├── timestamp: Timestamp
        ├── likes: Array
        └── comments/ (sub-collection)
            └── {commentId}: Map {
                ├── userId: String
                ├── userName: String
                ├── content: String
                └── timestamp: Timestamp
```

---

## ✅ Checklist Setup:

- [ ] Buat collection: `conversations` (root)
- [ ] Buat collection: `friend_requests` (root)
- [ ] Buat collection: `community_activities` (root)
- [ ] Setup Firestore Security Rules
- [ ] Optional: Integrate `FirestoreInitializer` ke LoginViewModel
- [ ] Test: Buat user baru dan cek apakah subcollections auto-created

---

## 🚀 Next Steps:

1. **Manual Setup** (Recommended): Buka Firebase Console → buat 3 collections → paste security rules
2. **Verify**: Buat test user via app → cek di Firebase Console apakah data terbuat
3. **Deploy**: Cloud Functions (opsional untuk backend logic)
4. **Test**: Run app dan test fitur reactive

---

## Troubleshooting:

**Q: Subcollections tidak muncul di Firebase Console?**  
A: Itu normal! Subcollections hanya muncul saat ada data di dalamnya. Mereka akan auto-created saat app menambahkan data.

**Q: Error "Permission denied"?**  
A: Check security rules, pastikan sudah di-update ke rules di atas.

**Q: Mau batch create data untuk testing?**  
A: Gunakan `FirestoreInitializer.createAllCollectionsStructure()` function.
