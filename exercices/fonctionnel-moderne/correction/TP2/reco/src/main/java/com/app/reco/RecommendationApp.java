package com.app.reco;

import com.app.catalog.AudioBook;
import com.app.catalog.Book;
import com.app.catalog.CatalogService;
import com.app.catalog.Media;

import java.util.concurrent.ExecutionException;

public final class RecommendationApp {

    private RecommendationApp() {
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RecommendationService service = new RecommendationService(new CatalogService(), new StubRecommendationClient());
        var medias = service.recomander().get();
        medias.forEach(media -> System.out.println(decrire(media)));
    }

    public static String decrire(Media media) {
        return switch (media) {
            case Book book -> "Livre : %s par %s (%d pages)".formatted(book.title(), book.author(), book.pages());
            case AudioBook audio -> "Livre audio : %s par %s (%d minutes) narré par %s".formatted(
                    audio.title(), audio.author(), audio.durationMinutes(), audio.narrator());
        };
    }
}
