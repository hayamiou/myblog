package org.wildcodeschool.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Article;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByTitle(String title); // Recherche exacte par titre

    List<Article> findByContentContaining(String keyword); // Recherche d'un mot-clé dans le contenu

    List<Article> findByCreatedAtAfter(LocalDateTime date); // Recherche d'articles créés après une certaine date

    List<Article> findTop5ByOrderByCreatedAtDesc(); // Récupère les 5 derniers articles créés

}
