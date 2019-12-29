package com.questnr.utils;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for turning all upper case or mixed case requests to lower case
 *
 * @author shilpa
 *
 */
public class CaseInsensitiveRequestFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    // request.getServletPath().toLowerCase();
    String requestURI = request.getServletPath();

    boolean redirect = false;
    boolean removeSlashes = false;
    boolean lowerCase = false;
    boolean removeExtension=false;
    if (requestURI.endsWith(".pdf")) {
      redirect = true;
      removeExtension=true;

    }
    else if(requestURI.contains("/documents/") || requestURI.contains("/pdfs/"))
    {
      redirect = false;
    }
    else {
      for (int i = 0; i < requestURI.length(); i++) {

        char ch = requestURI.charAt(i);
        if (Character.isUpperCase(ch)) {
          lowerCase = true;
          redirect = true;
          break;
        }
        if (requestURI.endsWith("/")) {
          removeSlashes = true;
          redirect = true;
        }


      }
    }
    String newURI = "";
    if (redirect) {
      if (removeExtension) {
        newURI = requestURI.substring(0, requestURI.length() - 4);
      } else {
        if (lowerCase) {
          newURI = requestURI.toLowerCase();
        }
        if (removeSlashes) {
          newURI = requestURI.replaceAll("/+$", "");
        }
      }
      response.sendRedirect(newURI);
    } else {
      filterChain.doFilter(request, response);
    }
  }

}
