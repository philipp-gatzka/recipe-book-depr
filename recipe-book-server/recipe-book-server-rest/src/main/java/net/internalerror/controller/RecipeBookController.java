package net.internalerror.controller;

import lombok.RequiredArgsConstructor;
import net.internalerror.endpoint.RecipeBookEndpoint;
import net.internalerror.service.RecipeBookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe-book")
public class RecipeBookController implements RecipeBookEndpoint {

    private final RecipeBookService service;

    @Override
    public ResponseEntity<CreateRecipeBookResponse> create(String token, CreateRecipeBookRequest request) {
        return service.create(token, request);
    }


}
