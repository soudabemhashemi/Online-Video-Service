package com.example.springboot.controllers;

import com.example.springboot.controllers.models.Login;
import com.example.springboot.controllers.models.SignUp;
import com.example.springboot.controllers.models.SignUpWithGit;
import com.example.springboot.services.AuthService;
import com.example.springboot.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@RestController



@RequestMapping("/auth")
public class AuthController extends HttpServlet {

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Login loginData, HttpServletRequest request){
        try {
            AuthService.authUser(loginData, request);
            String answer = JWTUtils.createJWT(loginData.getEmail(), 24);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Email or password is not correct.\"}");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout() {
        try {
            AuthService.logoutUser();
            return ResponseEntity.ok("{\"message\": \"User logged out successfully.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"user not found. invalid login.\"}");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignUp signUpData, HttpServletRequest request){
        try {
            AuthService.signupUser(signUpData, request);
            String answer = JWTUtils.createJWT(signUpData.getEmail(), 24);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Email already registered.\"}");
        }
    }

//    @PostMapping("/signupWithGit")
//    public ResponseEntity signup(){
//        try {
//            AuthService.signupUser(signUpData);
//            String answer = JWTUtils.createJWT(signUpData.getEmail(), 24);
//            return ResponseEntity.status(HttpStatus.OK).body(answer);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Email already registered.\"}");
//        }
//    }

    @GetMapping("/callback")
    public ResponseEntity callback(
            @RequestParam(required = true) String code,
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException,InterruptedException{
        String client_id = "e4b7b4d360ab731e3f58";
        String client_secret = "b629b2644bedc1795e6863f3514670ade7f8f571";
        String accessTokenUrl = String.format("https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s", client_id, client_secret, code);
        HttpClient client = HttpClient.newHttpClient();
        URI accessTokenUri = URI.create(accessTokenUrl);
        HttpRequest.Builder accessTokenBuilder = HttpRequest.newBuilder().uri(accessTokenUri);
        HttpRequest accessTokenRequest =
                accessTokenBuilder
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .header("accept", "application/json")
                        .build();
        HttpResponse <String> accessTokenResult = client.send(accessTokenRequest, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> resultBody = mapper.readValue(accessTokenResult.body(), HashMap.class);
        String accessToken =(String) resultBody.get("access_token");


        URI userDataUri  = URI.create("https://api.github.com/user");
        HttpRequest.Builder userDataBuilder = HttpRequest.newBuilder().uri(userDataUri);
        HttpRequest req =
                userDataBuilder.GET()
                        .header("Authorization", String.format("token %s", accessToken))
                        .build();
        HttpResponse <String> userDataResult = client.send (req, HttpResponse.BodyHandlers.ofString());
        SignUpWithGit signUpWithGitData = new Gson().fromJson(userDataResult.body(), SignUpWithGit.class);
        try {
            AuthService.signUpWithGithub(signUpWithGitData, request);
            String answer = JWTUtils.createJWT(signUpWithGitData.getEmail(), 24);
            return ResponseEntity.status(HttpStatus.OK).body(answer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Email already registered.\"}");
        }


//        return userDataResult.body();


    }


}
