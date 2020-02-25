package edu.eci.cvds.servlet;

import edu.eci.cvds.servlet.model.Todo;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        urlPatterns="/helloServlet"
)

public class newServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int prop =HttpServletResponse.SC_OK;
        Writer responseWriter=null;
        int validId=-1;
        try {
            responseWriter = resp.getWriter();
            Optional<String> optId = Optional.ofNullable(req.getParameter("id"));
            String id = optId.isPresent() && !optId.get().isEmpty() ? optId.get() : "-1";
            try {
                validId=Integer.parseInt(id);
            }catch(NumberFormatException e) {
                prop = HttpServletResponse.SC_BAD_REQUEST;
            }
            if (prop==200) prop = (validId < 1 || validId > 200) ? HttpServletResponse.SC_NOT_FOUND :HttpServletResponse.SC_OK;
            prop = (req.getParameter("id") == null)? HttpServletResponse.SC_BAD_REQUEST:prop;
            resp.setStatus(prop);
        }catch(MalformedURLException e) {
            prop=HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }catch(Exception e) {
            prop=HttpServletResponse.SC_BAD_REQUEST;
        }
        if(prop==200) {
            ArrayList<Todo> todos = new ArrayList();
            todos.add(Service.getTodo(validId));
            String htmlTable=Service.todosToHTMLTable(todos);
            responseWriter.write(htmlTable);
        }else {
            responseWriter.write("Error "+prop);
        }
        responseWriter.flush();
    }

}