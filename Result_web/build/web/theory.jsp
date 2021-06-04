<%-- 
    Document   : theory
    Created on : 9 Jul, 2020, 2:12:00 AM
    Author     : BHAVESH
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Theory Subject</title>
        <style>
            body {font-family: Arial, Helvetica, sans-serif;}
            * {box-sizing: border-box;}

            input[type=text], select, textarea {
                width: 100%;
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
                margin-top: 6px;
                margin-bottom: 16px;
                resize: vertical;
            }

            table,th,td,tr{
                padding: 10px;
                border: none;
            }
            
            input[type="submit"] {
  border: none;
  outline: none;
  height: 40px;
  background: #b80f22;
  color: #fff;
  font-size: 18px;
  border-radius: 20px;
}

input[type="submit"]:hover {
  cursor: pointer;
  background: #ffc107;
  color: #000;
}

.btn{
    margin: 0;
  position: absolute;
  left: 50%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}


            .container {
                border-radius: 5px;
                background-color: #f2f2f2;
                padding: 20px;
            }
        </style>
    </head>
    <body>
        <%
            int th = (Integer)session.getAttribute("th");
        %>
        <form method="post" action="SubjectTH">
         <table>
            <% for(int i=1;i<=th;i++){%>
                <tr>
                    <td>Subject <%=i%>: <input type="text" name="subject<%=i%>" id="subject<%=i%>" required/></td>
                    <td>Subject Code <%=i%>: <input type="text" name="subcode<%=i%>" id="subcode<%=i%>" required/></td>
                </tr>
                <br/>
                <%}%>
        </table>
        <div class="btn">
  <input type="submit" value="Submit">
        </div>
  </form>
    </body>
</html>
