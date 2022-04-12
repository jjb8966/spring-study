package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    // HTTP 메소드가 GET 일 때만 동작
    // = @GetMapping
    @RequestMapping(value = "/new-form", method = RequestMethod.GET)
    public String newForm() {
        return "new-form";
    }

    // HTTP 메소드가 POST 일 때만 동작
    @PostMapping("/save")
    public String save(
            // =request.getParameter("userName")
            // =Integer.parseInt(request.getParameter("age"))
            @RequestParam("userName") String userName,
            @RequestParam("age") int age,
            Model model
    ) {
        Member member = new Member(userName, age);
        memberRepository.save(member);

        model.addAttribute("member", member);

        return "save-result";
    }

    @GetMapping()
    public String list(Model model) {
        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);

        return "members";
    }
}
