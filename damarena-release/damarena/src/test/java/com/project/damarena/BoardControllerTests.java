package com.project.damarena;

import com.project.damarena.controller.BoardController;
import com.project.damarena.model.Board;
import com.project.damarena.service.UserService;
import com.project.damarena.service.TournamentService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BoardControllerTests {

    @Autowired
    private MockHttpServletRequest request;

    @MockBean
    private UserService userService;

    @MockBean
    private TournamentService tournamentService;

    @Autowired
    private BoardController boardController;

    @MockBean
    private Model model;

    @MockBean
    private HttpSession session;

    @MockBean
    private Board board;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartSinglePlayerGame() {

        String difficulty = "easy";
        String color = "white";

        String result = boardController.startSinglePlayerGame(model, difficulty, color);

        assertEquals("game/board", result);
    }
}