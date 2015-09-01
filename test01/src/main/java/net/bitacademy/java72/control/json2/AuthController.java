package net.bitacademy.java72.control.json2;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import net.bitacademy.java72.domain.Member;
import net.bitacademy.java72.service.MemberService;

@Controller
@RequestMapping("/auth")
public class AuthController {
  @Autowired MemberService memberService;
  

  
  @RequestMapping(value="/login", 
      method=RequestMethod.POST)
  @ResponseBody
  public String login(
      String email, 
      String password,
      String saveEmail,
      HttpServletResponse response,
      HttpSession session) throws Exception {
	  
	  System.out.println("로그인처리중");
	    HashMap<String,Object> resultMap = new HashMap<String,Object>();
    if (saveEmail != null) {
      Cookie cookie = new Cookie("email", email);
      cookie.setMaxAge(60 * 60 * 24);
      response.addCookie(cookie);
    } else { 
      // 湲곗뼲�븯湲곕�� 泥댄겕�븯吏� �븡�븯�떎硫�, 荑좏궎瑜� 臾댄슚�솕 �떆�궓�떎.
      Cookie cookie = new Cookie("email", "");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }

    Member member = memberService.getUser(email, password);
    


    if (member == null) {
    	
      session.invalidate();
      resultMap.put("result","failure");
      
      return new Gson().toJson(resultMap);
    } else {
      session.setAttribute("member", member);

      String refererUrl = 
          (String)session.getAttribute("refererUrl");
      if (refererUrl == null) {
    	  resultMap.put("refererUrl", refererUrl);
      }else{
    	  resultMap.put("result", "sucess");
      }
      
    }return new Gson().toJson(resultMap);
  }
  
  @RequestMapping("/logout.do")
  @ResponseBody
  public String logout(HttpSession session) {
	  HashMap<String,Object> resultMap = new HashMap<String,Object>();
    session.invalidate(); 
	  resultMap.put("result", "sucess");
	  //json문자열을 생성한다...
	  return new Gson().toJson(resultMap);
  }
}






