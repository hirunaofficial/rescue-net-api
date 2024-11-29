package dev.hiruna.rescuenet.service;

import dev.hiruna.rescuenet.entity.News;
import dev.hiruna.rescuenet.exception.NewsNotFoundException;
import dev.hiruna.rescuenet.repository.NewsRepository;
import dev.hiruna.rescuenet.dto.NewsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    // **Create or Update News Article**
    public NewsDTO saveNews(NewsDTO newsDTO) {
        News news = convertToEntity(newsDTO);

        if (news.getPublishDate() == null) {
            news.setPublishDate(LocalDateTime.now());
        }

        news = newsRepository.save(news);

        return convertToDTO(news);
    }

    // **Get All News Articles**
    public List<NewsDTO> getAllNews() {
        return newsRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // **Get News Article by ID**
    public NewsDTO getNewsById(Integer id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("News article with ID " + id + " not found"));

        return convertToDTO(news);
    }

    // **Delete News Article by ID**
    public void deleteNews(Integer id) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            newsRepository.deleteById(id);
        } else {
            throw new NewsNotFoundException("News article with ID " + id + " not found");
        }
    }

    // **Update News Article**
    public NewsDTO updateNews(Integer id, NewsDTO newsDTO) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new NewsNotFoundException("News article with ID " + id + " not found"));

        existingNews.setTitle(newsDTO.getTitle());
        existingNews.setContent(newsDTO.getContent());
        existingNews.setImageUrl(newsDTO.getImageUrl());
        existingNews.setTags(newsDTO.getTags()); // Update tags (comma-separated)

        existingNews = newsRepository.save(existingNews);
        return convertToDTO(existingNews);
    }

    // **Convert News Entity to NewsDTO**
    private NewsDTO convertToDTO(News news) {
        return new NewsDTO(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getPublishDate(),
                news.getImageUrl(),
                news.getTags()
        );
    }

    // **Convert NewsDTO to News Entity**
    private News convertToEntity(NewsDTO newsDTO) {
        return new News(
                newsDTO.getTitle(),
                newsDTO.getContent(),
                newsDTO.getPublishDate(),
                newsDTO.getImageUrl(),
                newsDTO.getTags()
        );
    }
}