<html>
 <head>
  <title> Calculating </title>
 </head>
<h> Scheduling teams... </h>
 <body>
<br><br>
<?php>
 $teams = isset($_REQUEST['schools']) ? json_decode($_REQUEST['schools']) : array();
 $fp = fopen('schools.txt','w') or die('fopen failed');
 fwrite($fp,print_r($teams,true)) or die('fwrite failed');
 fclose($fp);
 echo shell_exec('/usr/bin/java -jar /home/knoebber17/public_html/scheduling/GetSchools.jar')
 ?>
 </body>
</html>
