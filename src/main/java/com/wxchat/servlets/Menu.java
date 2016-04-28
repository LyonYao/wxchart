package com.wxchat.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wxchat.cases.MenuPusher;

public class Menu  extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String isPush = req.getParameter("push");
		if("push".equals(isPush)){
			new MenuPusher().push();
		}
	}

}
