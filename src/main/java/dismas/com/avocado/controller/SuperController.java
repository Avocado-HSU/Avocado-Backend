package dismas.com.avocado.controller;

import dismas.com.avocado.domain.Member;
import dismas.com.avocado.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/super-user")

public class SuperController {

    @Autowired
    MemberRepository memberRepository;

    @DeleteMapping("/delete-user")
    public void deleteUser(@RequestParam Long id){
        Optional<Member> member=memberRepository.findById(id);
        memberRepository.delete(member.get());
    }
}
