package com.kelompok2.scarla.ui.utils

fun introductionTextForCommunity(communityId: String): String {
    return when (communityId) {
        "pecinta_matematika" ->
                "📜 Peraturan Grup Pecinta Matematika:\n" +
                        "Fokus Produktif – Bahas konsep, rumus, dan pembahasan soal.\n" +
                        "Tanya yang Jelas – Sertakan langkah pengerjaan/foto soal bila perlu.\n" +
                        "Aksi > Wacana – Share progress latihan harian.\n" +
                        "Saling Dukung – Koreksi boleh, wajib sopan.\n" +
                        "Minim Spam – Off-topic seperlunya."
        "pecinta_fisika" ->
                "📜 Peraturan Grup Pecinta Fisika:\n" +
                        "Fokus Produktif – Bahas teori, eksperimen, dan latihan soal.\n" +
                        "Pakai Satuan – Biasakan tulis satuan & langkah perhitungan.\n" +
                        "Aksi > Wacana – Share progress belajarmu.\n" +
                        "Saling Dukung – Diskusi sehat, bukan debat kusir.\n" +
                        "Minim Spam – Off-topic seperlunya."
        "pecinta_kimia" ->
                "📜 Peraturan Grup Pecinta Kimia:\n" +
                        "Fokus Produktif – Bahas reaksi, konsep, dan latihan soal.\n" +
                        "Jujur Praktikum – Share hasil & proses, bukan asal jawab.\n" +
                        "Aksi > Wacana – Bagikan rangkuman/flashcard yang kamu buat.\n" +
                        "Saling Dukung – Jelaskan pelan-pelan untuk yang masih bingung.\n" +
                        "Minim Spam – Off-topic seperlunya."
        "pecinta_biologi" ->
                "📜 Peraturan Grup Pecinta Biologi:\n" +
                        "Fokus Produktif – Bahas biologi, alam & kehidupan.\n" +
                        "Sumber Jelas – Kalau share fakta/berita, sertakan sumber.\n" +
                        "Aksi > Wacana – Share catatan, gambar, atau rangkuman.\n" +
                        "Saling Dukung – Tanya jawab sopan dan jelas.\n" +
                        "Minim Spam – Off-topic seperlunya."
        "pecinta_informatika" ->
                "📜 Peraturan Grup Pecinta Informatika:\n" +
                        "Fokus Produktif – Bahas coding, algoritma, dan teknologi.\n" +
                        "Kasih Konteks – Sertakan error/log/screenshot kalau tanya bug.\n" +
                        "Aksi > Wacana – Share project kecil atau progress belajar.\n" +
                        "Saling Dukung – Review code boleh, jangan merendahkan.\n" +
                        "Minim Spam – Off-topic seperlunya."
        "pecinta_olahraga" ->
                "📜 Peraturan Grup Pecinta Olahraga:\n" +
                        "Fokus Sehat – Bahas latihan, progres, dan kebiasaan sehat.\n" +
                        "No Toxic – Dukung progres orang, jangan body shaming.\n" +
                        "Aksi > Wacana – Share jadwal latihan & bukti konsistensi.\n" +
                        "Safety First – Jangan saranin hal ekstrem/berbahaya.\n" +
                        "Minim Spam – Off-topic seperlunya."
        else ->
                "📜 Peraturan Grup:\n" +
                        "Fokus Produktif – Bahas topik komunitas.\n" +
                        "Saling Dukung – Diskusi sopan.\n" +
                        "Minim Spam – Off-topic seperlunya."
    }
}
