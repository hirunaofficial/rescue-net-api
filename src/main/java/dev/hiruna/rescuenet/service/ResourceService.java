package dev.hiruna.rescuenet.service;

import dev.hiruna.rescuenet.entity.Resource;
import dev.hiruna.rescuenet.exception.ResourceNotFoundException;
import dev.hiruna.rescuenet.repository.ResourceRepository;
import dev.hiruna.rescuenet.dto.ResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public ResourceDTO saveResource(ResourceDTO resourceDTO) {
        Resource resource = convertToEntity(resourceDTO);

        if (resource.getCreatedAt() == null) {
            resource.setCreatedAt(LocalDateTime.now());
        }
        resource.setUpdatedAt(LocalDateTime.now());

        resource = resourceRepository.save(resource);

        return convertToDTO(resource);
    }

    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResourceDTO getResourceById(Integer id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID " + id + " not found"));
        return convertToDTO(resource);
    }

    public void deleteResource(Integer id) {
        Optional<Resource> resource = resourceRepository.findById(id);
        if (resource.isPresent()) {
            resourceRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Resource with ID " + id + " not found");
        }
    }

    public ResourceDTO updateResource(Integer id, ResourceDTO resourceDTO) {
        Resource existingResource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID " + id + " not found"));

        existingResource.setResourceType(resourceDTO.getResourceType());
        existingResource.setDescription(resourceDTO.getDescription());
        existingResource.setQuantity(resourceDTO.getQuantity());
        existingResource.setLocationName(resourceDTO.getLocationName());
        existingResource.setLatitude(resourceDTO.getLatitude());
        existingResource.setLongitude(resourceDTO.getLongitude());
        existingResource.setContactPerson(resourceDTO.getContactPerson());
        existingResource.setContactPhone(resourceDTO.getContactPhone());
        existingResource.setStatus(Resource.ResourceStatus.valueOf(resourceDTO.getStatus()));
        existingResource.setUpdatedAt(LocalDateTime.now());

        existingResource = resourceRepository.save(existingResource);
        return convertToDTO(existingResource);
    }

    private ResourceDTO convertToDTO(Resource resource) {
        return new ResourceDTO(
                resource.getId(),
                resource.getResourceType(),
                resource.getDescription(),
                resource.getQuantity(),
                resource.getLocationName(),
                resource.getLatitude(),
                resource.getLongitude(),
                resource.getContactPerson(),
                resource.getContactPhone(),
                resource.getDateAdded(),
                resource.getStatus().name(), // Convert enum to string
                resource.getCreatedAt(),
                resource.getUpdatedAt()
        );
    }

    private Resource convertToEntity(ResourceDTO resourceDTO) {
        return new Resource(
                resourceDTO.getResourceType(),
                resourceDTO.getDescription(),
                resourceDTO.getQuantity(),
                resourceDTO.getLocationName(),
                resourceDTO.getLatitude(),
                resourceDTO.getLongitude(),
                resourceDTO.getContactPerson(),
                resourceDTO.getContactPhone(),
                resourceDTO.getDateAdded(),
                Resource.ResourceStatus.valueOf(resourceDTO.getStatus()), // Convert string to enum
                resourceDTO.getCreatedAt(),
                resourceDTO.getUpdatedAt()
        );
    }
}