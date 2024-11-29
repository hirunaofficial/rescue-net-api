package dev.hiruna.rescuenet.service;

import dev.hiruna.rescuenet.entity.Post;
import dev.hiruna.rescuenet.exception.PostNotFoundException;
import dev.hiruna.rescuenet.repository.PostRepository;
import dev.hiruna.rescuenet.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // **Create or Update Post**
    public PostDTO savePost(PostDTO postDTO) {
        Post post = convertToEntity(postDTO);

        // Set the publish date if not present
        if (post.getPublishDate() == null) {
            post.setPublishDate(LocalDateTime.now());
        }

        post = postRepository.save(post);

        return convertToDTO(post);
    }

    // **Get All Posts**
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // **Get Post by ID**
    public PostDTO getPostById(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));

        return convertToDTO(post);
    }

    // **Delete Post by ID**
    public void deletePost(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.deleteById(id);
        } else {
            throw new PostNotFoundException("Post with ID " + id + " not found");
        }
    }

    // **Update Post**
    public PostDTO updatePost(Integer id, PostDTO postDTO) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post with ID " + id + " not found"));

        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setImageUrl(postDTO.getImageUrl());

        existingPost = postRepository.save(existingPost);
        return convertToDTO(existingPost);
    }

    // **Get Posts by User ID**
    public List<PostDTO> getPostsByUserId(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        if (posts.isEmpty()) {
            throw new PostNotFoundException("No posts found for User ID " + userId);
        }
        return posts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // **Convert Post Entity to PostDTO**
    private PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getPublishDate(),
                post.getImageUrl(),
                post.getUser().getId()
        );
    }

    // **Convert PostDTO to Post Entity**
    private Post convertToEntity(PostDTO postDTO) {
        return new Post(
                postDTO.getTitle(),
                postDTO.getContent(),
                postDTO.getPublishDate(),
                postDTO.getImageUrl(),
                postDTO.getUser_id()
        );
    }
}
