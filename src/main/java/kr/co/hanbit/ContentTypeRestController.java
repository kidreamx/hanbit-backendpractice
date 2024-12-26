package kr.co.hanbit;

import kr.co.hanbit.Bookmark.Bookmark;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentTypeRestController {
    @RequestMapping(value = "/returnString", produces =  "text/plain") // produces를 지정함으로써 Content-Type을 강제 지정할수 있다.
    public String returnString(){
        return "<strong>문자열</strong>을 리턴";
    }
    @RequestMapping("/returnBookmark")
    public Bookmark returnBookmark(){
        return new Bookmark();
    }
}
