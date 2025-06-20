package org.ezcode.codetest.presentation.submission;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SubmitViewController {

    @GetMapping("/submit-test")
    public String getView() {
        return "submit-test";
    }
}
