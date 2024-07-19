package net.internalerror.rest.controller;

import lombok.RequiredArgsConstructor;
import net.internalerror.repository.RecipeBookMemberRepository;
import net.internalerror.repository.RecipeBookRepository;
import net.internalerror.rest.endpoint.RecipeBookMemberEndpoint;
import net.internalerror.security.JwtService;
import net.internalerror.tables.records.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipe-book-member")
@RequiredArgsConstructor
public class RecipeBookMemberController implements RecipeBookMemberEndpoint {

    private final JwtService jwtService;

    private final RecipeBookRepository recipeBookRepository;

    private final RecipeBookMemberRepository recipeBookMemberRepository;

    @Override
    public void invite(String token, InviteRequest request) {
        User user = jwtService.extractUser(token);

    }

    @Override
    public void join(String token, JoinRequest request) {

    }


}
