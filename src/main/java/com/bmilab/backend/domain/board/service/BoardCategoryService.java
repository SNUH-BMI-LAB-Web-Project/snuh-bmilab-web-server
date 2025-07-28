package com.bmilab.backend.domain.board.service;

import com.bmilab.backend.domain.board.dto.request.BoardCategoryReqeust;
import com.bmilab.backend.domain.board.dto.response.BoardCategoryFindAllResponse;
import com.bmilab.backend.domain.board.entity.BoardCategory;
import com.bmilab.backend.domain.board.exception.BoardErrorCode;
import com.bmilab.backend.domain.board.repository.BoardCategoryRepository;
import com.bmilab.backend.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    public BoardCategory getBoardCategoryById(Long categoryId) {
        return boardCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(BoardErrorCode.CATEGORY_NOT_FOUND));
    }

    public BoardCategoryFindAllResponse getAllBoardCategories() {
        List<BoardCategory> boardCategories = boardCategoryRepository.findAll();

        return BoardCategoryFindAllResponse.of(boardCategories);
    }

    @Transactional
    public void createBoardCategory(BoardCategoryReqeust request) {
        String categoryName = request.name();
        String categoryColor = request.color();

        validateBoardCategory(categoryName);

        BoardCategory boardCategory = BoardCategory.builder()
                .name(categoryName)
                .color(categoryColor)
                .build();

        boardCategoryRepository.save(boardCategory);
    }

    @Transactional
    public void updateById(Long categoryId, BoardCategoryReqeust request) {
        BoardCategory boardCategory = boardCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(BoardErrorCode.CATEGORY_NOT_FOUND));

        boardCategory.update(request.name(), request.color());
    }

    @Transactional
    public void deleteById(Long categoryId) {
        boardCategoryRepository.deleteById(categoryId);
    }

    private void validateBoardCategory(String name) {
        if(boardCategoryRepository.existsByName(name)){
            throw new ApiException(BoardErrorCode.CATEGORY_NAME_DUPLICATE);
        }
    }
}
