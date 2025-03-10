package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.model.Article;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        return articleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(articleRepository.save(article));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteArticle(@PathVariable Long id) {
        return articleRepository.findById(id)
                .map(article -> {
                    articleRepository.delete(article);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search-title")
    public ResponseEntity<List<Article>> getArticlesByTitle(@RequestParam String searchTerms) {
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        return articles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(articles);
    }

    @GetMapping("/search-content")
    public ResponseEntity<List<Article>> getArticlesByContent(@RequestParam String keyword) {
        List<Article> articles = articleRepository.findByContentContaining(keyword);
        return articles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(articles);
    }

    @GetMapping("/search-after-date")
    public ResponseEntity<List<Article>> getArticlesCreatedAfter(@RequestParam String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        List<Article> articles = articleRepository.findByCreatedAtAfter(dateTime);
        return articles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(articles);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Article>> getLastFiveArticles() {
        List<Article> articles = articleRepository.findTop5ByOrderByCreatedAtDesc();
        return articles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(articles);
    }
}
