package com.bmilab.backend.domain.research.publication.service;

import com.bmilab.backend.domain.research.publication.dto.request.CreateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.request.UpdateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorFindAllResponse;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorResponse;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorSummaryResponse;
import com.bmilab.backend.domain.research.publication.entity.Author;
import com.bmilab.backend.domain.research.publication.repository.AuthorRepository;
import com.bmilab.backend.domain.research.publication.exception.PublicationErrorCode;
import com.bmilab.backend.domain.user.entity.User;
import com.bmilab.backend.domain.user.repository.UserRepository;
import com.bmilab.backend.global.exception.ApiException;
import com.bmilab.backend.global.exception.GlobalErrorCode;
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
    private final UserRepository userRepository;

    public AuthorResponse createPublication(Long userId, CreateAuthorRequest dto) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(GlobalErrorCode.GLOBAL_NOT_FOUND));
        Author newAuthor = Author.builder()
                .authors(dto.authors())
                .authorType(dto.authorType())
                .publicationDate(dto.publicationDate())
                .publicationHouse(dto.publicationHouse())
                .publisher(dto.publisher())
                .publicationName(dto.publicationName())
                .title(dto.title())
                .isbn(dto.isbn())
                .createdBy(creator)
                .build();
        authorRepository.save(newAuthor);
        return new AuthorResponse(newAuthor);
    }

    public void deletePublication(Long userId, boolean isAdmin, Long publicationId) {
        Author author = authorRepository.findById(publicationId)
                .orElseThrow(() -> new ApiException(PublicationErrorCode.PUBLICATION_NOT_FOUND));
        validateAdminOrCreator(userId, isAdmin, author.getCreatedBy());
        authorRepository.deleteById(publicationId);
    }

    public AuthorResponse getPublication(Long publicationId) {
        Author author = authorRepository.findById(publicationId)
                .orElseThrow(() -> new ApiException(PublicationErrorCode.PUBLICATION_NOT_FOUND));
        return new AuthorResponse(author);
    }

    public AuthorResponse updatePublication(Long userId, boolean isAdmin, Long publicationId, UpdateAuthorRequest dto) {
        Author author = authorRepository.findById(publicationId)
                .orElseThrow(() -> new ApiException(PublicationErrorCode.PUBLICATION_NOT_FOUND));
        validateAdminOrCreator(userId, isAdmin, author.getCreatedBy());
        author.update(dto.authors(), dto.authorType(), dto.publicationDate(), dto.publicationHouse(), dto.publisher(), dto.publicationName(), dto.title(), dto.isbn());
        return new AuthorResponse(author);
    }

    @Transactional(readOnly = true)
    public AuthorFindAllResponse getPublications(String keyword, Pageable pageable) {
        Page<Author> authorPage = authorRepository.findAllBy(keyword, pageable);
        Page<AuthorSummaryResponse> responsePage = authorPage.map(AuthorSummaryResponse::from);
        return AuthorFindAllResponse.from(responsePage);
    }

    private void validateAdminOrCreator(Long userId, boolean isAdmin, User createdBy) {
        if (!isAdmin && (createdBy == null || !createdBy.getId().equals(userId))) {
            throw new ApiException(PublicationErrorCode.PUBLICATION_ACCESS_DENIED);
        }
    }
}
