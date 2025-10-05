package com.biblio.core;

public sealed interface CatalogueEvent permits LivreCree, LivreMisAJour, LivreSupprime {
    Livre livre();
}
