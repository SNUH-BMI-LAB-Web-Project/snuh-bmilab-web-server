package com.bmilab.backend.domain.research.publication.controller;

import com.bmilab.backend.domain.research.publication.dto.request.CreateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.request.UpdateAuthorRequest;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorFindAllResponse;
import com.bmilab.backend.domain.research.publication.dto.response.AuthorResponse;
import com.bmilab.backend.domain.research.publication.service.PublicationService;
import com.bmilab.backend.domain.user.enums.Role;
import com.bmilab.backend.global.security.UserAuthInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/research/authors")
@RequiredArgsConstructor
public class PublicationController implements PublicationApi {

    private final PublicationService publicationService;

    @PostMapping
    public ResponseEntity<AuthorResponse> createPublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestBody @Valid CreateAuthorRequest request
    ) {
        AuthorResponse response = publicationService.createPublication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponse> updatePublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long authorId,
            @RequestBody @Valid UpdateAuthorRequest request
    ) {
        AuthorResponse response = publicationService.updatePublication(authorId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponse> getPublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long authorId
    ) {
        return ResponseEntity.ok(publicationService.getPublication(authorId));
    }

    @GetMapping
    public ResponseEntity<AuthorFindAllResponse> getPublications(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "publicationDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(publicationService.getPublications(keyword, pageable));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deletePublication(
            @AuthenticationPrincipal UserAuthInfo userAuthInfo,
            @PathVariable Long authorId
    ) {
        boolean isAdmin = userAuthInfo.getUser().getRole() == Role.ADMIN;
        publicationService.deletePublication(userAuthInfo.getUserId(), isAdmin, authorId);
        return ResponseEntity.ok().build();
    }
}
