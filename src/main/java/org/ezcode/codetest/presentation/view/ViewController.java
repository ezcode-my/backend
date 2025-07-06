package org.ezcode.codetest.presentation.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/test/main")
    public String getMainView(){
        return "test-main";
    }

    @GetMapping("/test/signin")
    public String getSigninView(){
        return "test-login";
    }

    @GetMapping("/test/submit")
    public String getSubmitView() {
        return "test-submit";
    }

    @GetMapping("/test/problems")
    public String getProblemsView() {
        return "test-problems";
    }

    @GetMapping("/test/ranking")
    public String getRankingView() {
        return "test-ranking";
    }

    @GetMapping("/test/chatting")
    public String getChattingPage() {
        return "chat-page";
    }

    @GetMapping("/test/searching")
    public String getSearchingPage() {
        return "search-page";
    }
}
