package com.bmilab.backend.domain.research.publication.service;

import com.bmilab.backend.domain.research.publication.dto.request.CreateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.request.UpdateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorResponse;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorSummaryResponse;
import com.bmilab.backend.domain.research.publication.entity.Author;
import com.bmilab.backend.domain.research.publication.repository.AuthorRepository;
import com.bmilab.backend.domain.research.publication.exception.PublicationErrorCode;
import com.bmilab.backend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PublicationService {

    @Qualifier("researchAuthorRepository")
    private final AuthorRepository authorRepository;

    public AuthorResponse createPublication(CreateAuthorRequest dto) {
        Author newAuthor = Author.builder()
                .authors(dto.authors())
                .authorType(dto.authorType())
                .publicationDate(dto.publicationDate())
                .publicationHouse(dto.publicationHouse())
                .publisher(dto.publisher())
                .publicationName(dto.publicationName())
                .title(dto.title())
                .isbn(dto.isbn())
                .build();
        authorRepository.save(newAuthor);
        return new AuthorResponse(newAuthor);
    }

    public void deletePublication(Long userId, boolean isAdmin, Long publicationId) {
        if (!isAdmin) {
            throw new ApiException(PublicationErrorCode.PUBLICATION_ACCESS_DENIED);
        }
        authorRepository.deleteById(publicationId);
    }

    public AuthorResponse getPublication(Long publicationId) {
        Author author = authorRepository.findById(publicationId)
                .orElseThrow(() -> new ApiException(PublicationErrorCode.PUBLICATION_NOT_FOUND));
        return new AuthorResponse(author);
    }

    public AuthorResponse updatePublication(Long publicationId, UpdateAuthorRequest dto) {
        Author author = authorRepository.findById(publicationId)
                .orElseThrow(() -> new ApiException(PublicationErrorCode.PUBLICATION_NOT_FOUND));
        author.update(dto.authors(), dto.authorType(), dto.publicationDate(), dto.publicationHouse(), dto.publisher(), dto.publicationName(), dto.title(), dto.isbn());
        return new AuthorResponse(author);
    }

    @Transactional(readOnly = true)
    public Page<AuthorSummaryResponse> getPublications(String keyword, Pageable pageable) {
        return authorRepository.findAllBy(keyword, pageable);
    }
}
