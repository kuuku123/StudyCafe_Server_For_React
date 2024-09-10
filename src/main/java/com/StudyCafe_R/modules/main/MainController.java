package com.StudyCafe_R.modules.main;

import com.StudyCafe_R.modules.account.repository.AccountRepository;
import com.StudyCafe_R.modules.account.service.AccountService;
import com.StudyCafe_R.modules.study.StudyRepository;
import com.StudyCafe_R.modules.study.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final StudyRepository studyRepository;


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
