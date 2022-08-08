package com.example.zerobaseassignment.controller;

import com.example.zerobaseassignment.service.SeoulService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet2", urlPatterns = "/history")
public class SeoulController extends HttpServlet {
    SeoulService seoulService = new SeoulService();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("id parameter is: " + req.getParameter("id"));
        seoulService.deleteHistoryById(req.getParameter("id"));

        resp.sendRedirect("/history.jsp");


    }
}
