package com.StudyCafe_R.modules.main;

import com.StudyCafe_R.modules.account.AccountRepository;
import com.StudyCafe_R.modules.account.CurrentAccount;
import com.StudyCafe_R.modules.account.domain.Account;
import com.StudyCafe_R.modules.account.responseDto.ApiResponse;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.study.StudyRepository;
import com.StudyCafe_R.modules.study.domain.Study;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final StudyRepository studyRepository;

    @ResponseBody
    @GetMapping("/")
    public ResponseEntity<String> home(@CurrentAccount Account account, Model model, HttpServletRequest request, HttpServletResponse response) {
        if (account != null) {
            // login again to remove email validation message
            Account account1 = accountRepository.findById(account.getId()).get();

            accountService.login(account1,request, response);
            model.addAttribute(account1);
        }
        model.addAttribute("studyList", studyRepository.findFirst9ByPublishedAndClosedOrderByPublishedDateTimeDesc(true, false));

        ApiResponse<String> apiResponse = new ApiResponse<>("login succeed", HttpStatus.OK, null);

        return new ResponseEntity<>(new Gson().toJson(apiResponse), HttpStatus.OK);
    }


    @GetMapping("/search/study")
    public String searchStudy(@PageableDefault(size = 9,sort = "publishedDateTime",direction = Sort.Direction.ASC) Pageable pageable,
                              String keyword, Model model) {
        Page<Study> studyPage = studyRepository.findByKeyword(keyword,pageable);
        model.addAttribute("studyPage",studyPage);
        model.addAttribute("keyword",keyword);
        model.addAttribute("sortProperty",pageable.getSort().toString().contains("publishedDateTime") ? "publishedDateTime" : "memberCount");
        return "search";
    }
}
