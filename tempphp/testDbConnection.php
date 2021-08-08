<?php

$endpoint="blackpointdatabase.cx7rbplauz8z.us-east-2.rds.amazonaws.com";
$username=$_GET["username"];
$password=$_GET["password"];
$dbname="BPTInventory";

//Establish Connection
$conn = mysqli_connect($endpoint, $username, $password, $dbname);

if (!$conn) {
    die("Failed" . mysqli_connect_error());
}

echo "Connection";
?>
