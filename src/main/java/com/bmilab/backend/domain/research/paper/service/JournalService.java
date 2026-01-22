package com.bmilab.backend.domain.research.paper.service;

import com.bmilab.backend.domain.research.paper.dto.request.CreateJournalRequest;
import com.bmilab.backend.domain.research.paper.dto.request.UpdateJournalRequest;
import com.bmilab.backend.domain.research.paper.dto.response.JournalFindAllResponse;
import com.bmilab.backend.domain.research.paper.dto.response.JournalResponse;
import com.bmilab.backend.domain.research.paper.dto.response.JournalSummaryResponse;
import com.bmilab.backend.domain.research.paper.entity.Journal;
import com.bmilab.backend.domain.research.paper.exception.PaperErrorCode;
import com.bmilab.backend.domain.research.paper.repository.JournalRepository;
import com.bmilab.backend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;

    public JournalResponse createJournal(CreateJournalRequest dto) {
        Journal newJournal = Journal.builder()
                .journalName(dto.journalName())
                .category(dto.category())
                .publisher(dto.publisher())
                .publishCountry(dto.publishCountry())
                .isbn(dto.isbn())
                .issn(dto.issn())
                .eissn(dto.eissn())
                .jif(dto.jif())
                .jcrRank(dto.jcrRank())
                .issue(dto.issue())
                .build();
        journalRepository.save(newJournal);
        return JournalResponse.from(newJournal);
    }

    public void deleteJournal(Long userId, boolean isAdmin, Long journalId) {
        if (!isAdmin) {
            throw new ApiException(PaperErrorCode.PAPER_ACCESS_DENIED);
        }
        journalRepository.deleteById(journalId);
    }

    @Transactional(readOnly = true)
    public JournalResponse getJournal(Long journalId) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ApiException(PaperErrorCode.JOURNAL_NOT_FOUND));
        return JournalResponse.from(journal);
    }

    public JournalResponse updateJournal(Long journalId, UpdateJournalRequest dto) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ApiException(PaperErrorCode.JOURNAL_NOT_FOUND));
        journal.update(
                dto.journalName(),
                dto.category(),
                dto.publisher(),
                dto.publishCountry(),
                dto.isbn(),
                dto.issn(),
                dto.eissn(),
                dto.jif(),
                dto.jcrRank(),
                dto.issue()
        );
        return JournalResponse.from(journal);
    }

    @Transactional(readOnly = true)
    public JournalFindAllResponse getJournals(String keyword, Pageable pageable) {
        Page<Journal> journalPage = journalRepository.findAllBy(keyword, pageable);

        List<JournalSummaryResponse> journals = journalPage.getContent().stream()
                .map(JournalSummaryResponse::from)
                .toList();

        return JournalFindAllResponse.of(journals, journalPage.getTotalPages());
    }
}
