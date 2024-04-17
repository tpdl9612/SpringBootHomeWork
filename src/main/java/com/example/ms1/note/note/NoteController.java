package com.example.ms1.note.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @RequestMapping("/")
    public String main(Model model) {
        //1. DB에서 데이터 꺼내오기
        List<Note> noteList = noteRepository.findAll();

        if(noteList.isEmpty()) {
            saveDefault();
            return "redirect:/";
        }

        //2. 꺼내온 데이터를 템플릿으로 보내기
        model.addAttribute("noteList", noteList);
        model.addAttribute("targetNote", noteList.get(0));

        return "main";
    }

    @PostMapping("/write")
    public String write() {

        saveDefault();

        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        Note note = noteRepository.findById(id).get();
        model.addAttribute("targetNote", note);
        model.addAttribute("noteList", noteRepository.findAll());

        return "main";
    }
    @PostMapping("/update")
    public String update(Long id, String title, String content) {
        Note note = noteRepository.findById(id).get();

        if(title.trim().length() == 0) {
            title = "제목 없음";
        }

        note.setTitle(title);
        note.setContent(content);

        noteRepository.save(note);
        return "redirect:/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {

        noteRepository.deleteById(id);
        return "redirect:/";
    }

    private Note saveDefault() {
        Note note = new Note();
        note.setTitle("new title..");
        note.setContent("");
        note.setCreateDate(LocalDateTime.now());

        return noteRepository.save(note);
    }
}
