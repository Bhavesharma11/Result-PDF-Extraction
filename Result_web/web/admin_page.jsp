<%-- 
    Document   : admin_page
    Created on : 7 Jul, 2020, 8:07:11 PM
    Author     : BHAVESH
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Page</title>
        <style>
            body {font-family: Arial, Helvetica, sans-serif;}
* {box-sizing: border-box;}

input[type="number"], select{
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
  left: 95%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}

.btn{
    margin: 0;
  position: absolute;
  left: 50%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}
        </style>
    </head>
    <body>
        <br>
        <div class="link">
        <a href="LogoutServlet">Logout</a>    
        </div>
        <br><br>
        <form action="Admin" method="post">
            <div class="container">
         
                <label for="year">Select year</label>
                <select name="year">
                 <option value="FE">FE</option>
                 <option value="SE">SE</option>
                 <option value="TE">TE</option>
                 <option value="BE">BE</option>
            </select>
        
        <br>
        <br>
            <label for="sem">Select Semester</label>
            <select name="sem">
                 <option value="FirstSemester">First semester</option>
                 <option value="FullYear">Full year</option>
            </select>
            
            <label for="thry">No. of Theory Subjects</label>
    <input type="number" id="thry" name="theory" placeholder="Theory Subjects.." required>
    
    <br><br>
    
    <label for="tw">No. of Term Work Subjects</label>
    <input type="number" id="tw" name="termwork" placeholder="Term Work Subjects.." required>

    <br><br>
    
    <label for="prac">No. of Practical Subjects</label>
    <input type="number" id="prac" name="practical" placeholder="Practical Subjects.." required>
    
    <br><br>
    
    <label for="orl">No. of Oral Subjects</label>
    <input type="number" id="orl" name="oral" placeholder="Oral Subjects.." required>
            </div>
            <br><br>
            
            <div class="btn">
        <input type="submit" value="Submit">
            </div>
        </form>
    </body>
</html>
