<%-- 
    Document   : add_new_subject
    Created on : 10 Jul, 2020, 2:50:00 AM
    Author     : BHAVESH
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add subject</title>
        <style>
             body {font-family: Arial, Helvetica, sans-serif;}
* {box-sizing: border-box;}

input[type="text"], select{
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  margin-top: 6px;
  margin-bottom: 16px;
  resize: vertical;
}
.container {
  border-radius: 5px;
  background-color: #f2f2f2;
  padding: 20px;
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

a {
  border: none;
  outline: none;
  padding: 10px;
  height: 40px;
  background: #b80f22;
  color: #fff;
  font-size: 18px;
  border-radius: 20px;
}

a:hover {
  cursor: pointer;
  background: #ffc107;
  color: #000;
}

.link{
    margin: 0;
  position: absolute;
  left: 60%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}

.btn{
    margin: 0;
  position: absolute;
  left: 40%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}
            </style>
    </head>
    <body>
        <br><br>
        <form action="AddServlet" method="post">
        <div class="container">
            <label for="name">Subject name</label>
            <input type="text" name="sub_name" placeholder="Enter subject name" required>
    
    <br><br>
    
    <label for="code">Subject code</label>
    <input type="text" name="sub_code" placeholder="Enter subject code" required>
         
                <label for="type">Select subject type</label>
                <select name="sub_type">
                 <option value="th">Theory</option>
                 <option value="pr">Practical</option>
                 <option value="tw">Term work</option>
                 <option value="or">Oral</option>
            </select>
        
        <br>
        <br><br>
            <div class="btn">
        <input type="submit" value="Add Subject">
            </div>
        <div class="link">   
        <a href="ViewDelete">View Subjects</a>
        </div>
        </div>
        </form>
    </body>
</html>
