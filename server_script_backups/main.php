<?

$databasehost = "agentyou.square7.ch";
$databasename = "agentyou";
$databaseusername ="agentyou";
$databasepassword = "N!221133ncb";


<?php
$con=mysqli_connect($databasehost,$databaseusername,$databasepassword,$databasename);
$sql="INSERT INTO basic_user_info (ASIH, NAME) VALUES ('admin', 'admin','adminstrator')";
if (mysqli_query($con,$sql))
{
   echo "Values have been inserted successfully";
}
?>