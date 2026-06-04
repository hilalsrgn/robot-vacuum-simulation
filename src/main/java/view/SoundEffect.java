package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class SoundEffect {

    // Ses oynatıcımız
    private static MediaPlayer supurgeSesiPlayer;

    // Proje ilk açıldığında ses dosyasını hafızaya yükleyen kısım
    static {
        try {
            // Senin oluşturduğun klasör ve dosya adı: /sounds/supurgeses.mp3
            URL supurgeUrl = SoundEffect.class.getResource("/sounds/supurgeses.mp3");

            if (supurgeUrl != null) {
                Media media = new Media(supurgeUrl.toExternalForm());
                supurgeSesiPlayer = new MediaPlayer(media);

                // Motor sesi simülasyon boyunca durmadan çalsın diye sonsuz döngüye alıyoruz
                supurgeSesiPlayer.setCycleCount(MediaPlayer.INDEFINITE);

                // Arka planda baş ağrıtmasın diye ses seviyesini %50 yapıyoruz
                supurgeSesiPlayer.setVolume(0.5);
            } else {
                System.out.println("Hata: Ses dosyası bulunamadı! Yolu kontrol et.");
            }

            // Kızlar ileride yeni ses eklemek isterse tam buraya yeni URL ve oynatıcıları ekleyebilirler.

        } catch (Exception e) {
            System.out.println("Ses yüklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    // --- KONTROL METOTLARI (HelloController'dan bunları çağıracağız) ---

    public static void supurgeSesiBaslat() {
        if (supurgeSesiPlayer != null) {
            supurgeSesiPlayer.play();
        }
    }

    public static void supurgeSesiDurdur() {
        if (supurgeSesiPlayer != null) {
            supurgeSesiPlayer.pause();
        }
    }
}