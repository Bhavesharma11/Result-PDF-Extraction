<%-- 
    Document   : fileupload1
    Created on : 10 Jul, 2020, 4:40:08 AM
    Author     : BHAVESH
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>FE Upload File</title>
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

<h2>Upload File</h2>

<div class="container">
    <form action="Result_FE" method="post">
    <label for="file">File Path</label>
    <input type="text" id="file" name="fileup" required>
    <h3>Enter Seat Number's Range:</h3>
    <label for="frm">From</label>
    <input type="text" id="frm" name="from" required>
    
    <label for="too">To</label>
    <input type="text" id="too" name="to" required>
    </div>
<br><br>
<div class="btn">
    <input type="submit" value="Submit">
           </div>
  </form>

</body>
</html>

